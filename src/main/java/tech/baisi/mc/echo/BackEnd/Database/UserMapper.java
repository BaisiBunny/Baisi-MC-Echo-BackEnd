package tech.baisi.mc.echo.BackEnd.Database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    @Select("SELECT token FROM mc.`user` WHERE token = #{token};")
    //检查某个token的数量。
    public List<String> GetTokens(String token);

    @Select("SELECT name FROM mc.`user` WHERE name = #{name};")
    //检查某个token的数量。
    public List<String> GetNames(String name);

    @Select("SELECT name, password, token, money, game_key, status, reg_ip, reg_date FROM mc.`user` WHERE token = #{token};")
    public List<UserEntity> GetUserEntityByToken(String token);
}
