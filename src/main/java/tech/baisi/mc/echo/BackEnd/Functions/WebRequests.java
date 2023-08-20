package tech.baisi.mc.echo.BackEnd.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.baisi.mc.echo.BackEnd.Database.UserEntity;
import tech.baisi.mc.echo.BackEnd.Database.UserMapper;

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
            return "你的玩家ID不符合标准。";
        }
        if(!MyFunctions.IsLegalNameOrPassword(password)){
            return "你的密码不符合标准。";
        }
        if(reg_ip.equals("#no#ip")){
            return "请稍等几秒再尝试。";
        }

        //插入新数据
        String token = MyFunctions.GenerateRandomString(32);
        String game_key = MyFunctions.GenerateRandomString(4);
        String time = MyFunctions.GetTimeNow();
        userMapper.insert(new UserEntity(name,password,token,0,game_key,"empty",reg_ip,time));

        return token;
    }
}