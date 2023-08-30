package tech.baisi.mc.echo.BackEnd.Functions;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.baisi.mc.echo.BackEnd.Database.ItemEntity;
import tech.baisi.mc.echo.BackEnd.Database.ItemMapper;
import tech.baisi.mc.echo.BackEnd.Database.UserEntity;
import tech.baisi.mc.echo.BackEnd.Database.UserMapper;
import tech.baisi.mc.echo.BackEnd.Entity.UserData;

import java.util.List;


@RestController
public class WebRequests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/register_account")
    //注册新账号
    public String RegisterAccount(@RequestBody JSONObject jsonObject){
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String reg_ip = jsonObject.getString("reg_ip");
        //检查合法性
        if(!MyFunctions.IsLegalNameOrPassword(name)){
            return "illegal_name";
        }
        if(!userMapper.GetNames(name).isEmpty()){
            return "name_exists";
        }
        if(!MyFunctions.IsLegalNameOrPassword(password)){
            return "illegal_password";
        }
        if(reg_ip.equals("#no#ip")){
            return "please_wait";
        }

        //插入新数据
        String token = MyFunctions.GenerateRandomString(32);
        for(;;){
            if(!userMapper.GetTokens(token).isEmpty()){
                token = MyFunctions.GenerateRandomString(32);
            }else {
                break;
            }
        }
        String game_key = MyFunctions.GenerateRandomString(8);
        String time = MyFunctions.GetTimeNow();
        userMapper.insert(new UserEntity(name,password,token,0,game_key,"empty",reg_ip,time));

        return "token:"+token;
    }

    @RequestMapping("/get_data")
    public UserData GetData(@RequestBody JSONObject jsonObject){
        String token = jsonObject.getString("token");
        if(!MyFunctions.IsLegalToken(token)){
            return new UserData("游客","xxxx",0,"empty");
        }
        if(userMapper.GetTokens(token).size() != 1){
            //不是有效token
            return new UserData("游客","xxxx",0,"empty");
        } else {
            //是有效token
            UserEntity userEntity = userMapper.GetUserEntityByToken(token).get(0);
            return new UserData(userEntity.getName(),userEntity.getGame_key(),userEntity.getMoney(),userEntity.getStatus());
        }
    }

    @PostMapping("/login")
    public String Login(@RequestBody JSONObject jsonObject){
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        List<UserEntity> userEntities = userMapper.GetUserEntityByName(name);
        if(!MyFunctions.IsLegalNameOrPassword(name)){
            return "illegal_name";
        }
        if(!MyFunctions.IsLegalNameOrPassword(password)){
            return "illegal_password";
        }
        if(userEntities.isEmpty()){
            return "user_not_exist";
        }
        if(!name.equals(userEntities.get(0).getName())){
            return "wrong_name_case";
        }
        if(!password.equals(userEntities.get(0).getPassword())){
            return "wrong_password";
        }
        return "token:"+userEntities.get(0).getToken();
    }

    @RequestMapping("/get_items")
    public JSONObject getItems(@RequestBody JSONObject input){
        String search = input.getString("searchContent");
        String sort = input.getString("sort");
        int page = input.getIntValue("currentPage");
        System.out.println(search+" "+sort+" "+page);

        List<ItemEntity> itemEntities;
        int totalAmount = 1;

        PageHelper.startPage(page,40);


        if(sort.equals("all")){
            itemEntities = itemMapper.getAllItems(search);
            totalAmount = itemMapper.getAllItemsCount(search).size();
        }
        else {
            itemEntities = itemMapper.getSortedItems(search,sort);
            totalAmount = itemMapper.getSortedItemsCount(search,sort).size();
        }

        JSONObject object = new JSONObject();

        object.put("currentPage",page);
        object.put("totalAmount",totalAmount);

        JSONArray array = new JSONArray();

        for(ItemEntity item : itemEntities){
            JSONObject tempObject = new JSONObject();
            tempObject.put("id",item.getId());
            tempObject.put("name",item.getName());
            tempObject.put("price",item.getPrice());
            tempObject.put("amount",item.getAmount());
            tempObject.put("picture",item.getPicture());
            array.add(tempObject);
        }

        object.put("items",array);

        object.put("isEmpty",array.isEmpty());

        return object;
    }

    @RequestMapping("/buy")
    public String buy(@RequestBody JSONObject input){
        String id = input.getString("id");
        String token = input.getString("token");
        ItemEntity item = itemMapper.selectById(id);
        UserEntity user;
        if(!MyFunctions.IsLegalToken(token)){
            return "token_error";
        }
        if(userMapper.GetTokens(token).size() != 1){
            //不是有效token
            return "token_error";
        } else {
            //是有效token
            user = userMapper.GetUserEntityByToken(token).get(0);
        }
        int new_money = user.getMoney() - item.getPrice();
        if(new_money < 0){
            return "not_enough_money";
        }
        userMapper.UpdateMoneyByToken(token,new_money);

        //发货
        String sendMsg = item.getCommand().replace("$player$",user.getName());

        //rabbitMQ
        rabbitTemplate.convertAndSend("mc_exchange","mc_shop_routing",sendMsg);

        return "success";
    }

    @RequestMapping("/change_password")
    public String changePassword(@RequestBody JSONObject input){
        String token = input.getString("token");
        String name = input.getString("name");
        String newPassword = input.getString("newPassword");

        if(!MyFunctions.IsLegalToken(token)){
            return "token_error";
        }
        if(userMapper.GetTokens(token).size() != 1){
            //不是有效token
            return "token_error";
        }
        if(!MyFunctions.IsLegalNameOrPassword(newPassword)){
            return "illegal_password";
        }
        UserEntity user = userMapper.GetUserEntityByToken(token).get(0);
        if(!user.getName().equals(name)){
            return "wrong_name";
        }

        String newToken = MyFunctions.GenerateRandomString(32);
        for(;;){
            if(!userMapper.GetTokens(newToken).isEmpty()){
                newToken = MyFunctions.GenerateRandomString(32);
            }else {
                break;
            }
        }

        String newGameKey = MyFunctions.GenerateRandomString(8);

        userMapper.ChangePassword(name,newPassword,newToken,newGameKey);

        return "token:"+newToken;
    }

}
