package tech.baisi.mc.echo.BackEnd.Database;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("items")
public class ItemEntity {
    @TableId
    private String id;
    private String name;

    public String getCommand() {
        return command;
    }

    private int price;
    private int amount;
    private String picture;
    private String command;
    private String tag;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }
}
