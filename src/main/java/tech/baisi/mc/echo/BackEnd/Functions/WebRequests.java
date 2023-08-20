package tech.baisi.mc.echo.BackEnd.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.baisi.mc.echo.BackEnd.Database.UserEntity;
import tech.baisi.mc.echo.BackEnd.Database.UserMapper;
import tech.baisi.mc.echo.BackEnd.Entity.UserData;

import java.util.List;


@RestController
public class WebRequests {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/register_account")
    //注册新账号
    public String RegisterAccount(
        @RequestParam(name = "name", defaultValue = "#no#name") String name,
        @RequestParam(name = "password", defaultValue = "#no#password") String password,
        @RequestParam(name = "reg_ip", defaultValue = "#no#ip") String reg_ip
    ){
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
        String game_key = MyFunctions.GenerateRandomString(4);
        String time = MyFunctions.GetTimeNow();
        userMapper.insert(new UserEntity(name,password,token,0,game_key,"empty",reg_ip,time));

        return "token:"+token;
    }

    @RequestMapping("/get_data")
    public UserData GetData(@RequestParam(name = "token", defaultValue = "#no#token") String token){
        if(userMapper.GetTokens(token).size() != 1){
            //不是有效token
            return new UserData("游客","xxxx",0,"empty");
        } else {
            //是有效token
            UserEntity userEntity = userMapper.GetUserEntityByToken(token).get(0);
            return new UserData(userEntity.getName(),userEntity.getGame_key(),userEntity.getMoney(),userEntity.getStatus());
        }
    }

    @RequestMapping("/login")
    public String Login(
        @RequestParam(name = "name", defaultValue = "#no#name") String name,
        @RequestParam(name = "password", defaultValue = "#no#password") String password
    ){
        List<UserEntity> userEntities = userMapper.GetUserEntityByName(name);
        if(userEntities.isEmpty()){
            return "user_not_exist";
        }
        if(!password.equals(userEntities.get(0).getPassword())){
            return "wrong_password";
        }
        return "token:"+userEntities.get(0).getToken();
    }
}
