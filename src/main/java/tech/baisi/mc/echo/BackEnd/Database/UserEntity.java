package tech.baisi.mc.echo.BackEnd.Database;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class UserEntity {
    @TableId
    private String name;
    private String password;
    private String token;
    private int money;
    private String game_key;
    private String status;
    private String reg_ip;
    private String reg_date;

    public UserEntity(String name, String password, String token, int money, String game_key, String status, String reg_ip, String reg_date) {
        this.name = name;
        this.password = password;
        this.token = token;
        this.money = money;
        this.game_key = game_key;
        this.status = status;
        this.reg_ip = reg_ip;
        this.reg_date = reg_date;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public int getMoney() {
        return money;
    }

    public String getGame_key() {
        return game_key;
    }

    public String getStatus() {
        return status;
    }

    public String getReg_ip() {
        return reg_ip;
    }

    public String getReg_date() {
        return reg_date;
    }
}
