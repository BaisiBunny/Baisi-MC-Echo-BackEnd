package tech.baisi.mc.echo.BackEnd.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.baisi.mc.echo.BackEnd.Database.UserMapper;

@RestController
public class test {
    public static void main(String[] args) {
//        System.out.println(MyFunctions.GenerateRandomString(32));
//        System.out.println(MyFunctions.GenerateRandomString(32));
//        System.out.println(MyFunctions.GenerateRandomString(32));
//        System.out.println(MyFunctions.GenerateRandomString(32));
//        System.out.println(MyFunctions.GenerateRandomString(32));
        for(int i = 1; i < 100; i++){
            System.out.println("INSERT INTO mc.items (id, name, price, amount, picture, command, tag) VALUES('test"+i+"', '测试"+i+"', "+i+", "+i+", 'stone.webp', 'cmd', 'colorful');");
        }

    }

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/test")
    public String forTest(){
//        userMapper.selectById("baisi");
        return userMapper.selectById("baisi").getGame_key();
    }
}
