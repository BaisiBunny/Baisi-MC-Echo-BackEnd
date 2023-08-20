package tech.baisi.mc.echo.BackEnd.Entity;

public class UserData {
    private String name;
    private String game_key;
    private int money;
    private String status;

    public String getName() {
        return name;
    }

    public String getGame_key() {
        return game_key;
    }

    public int getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }

    public UserData(String name, String game_key, int money, String status) {
        this.name = name;
        this.game_key = game_key;
        this.money = money;
        this.status = status;
    }
}
