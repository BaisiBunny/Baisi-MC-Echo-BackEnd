package tech.baisi.mc.echo.BackEnd.Database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<ItemEntity> {
    @Select("SELECT id, name, price, amount, picture, command, tag FROM mc.items WHERE name LIKE \"%${name}%\" ORDER BY priority ASC")
    List<ItemEntity> getAllItems(String name);

    @Select("SELECT id FROM mc.items WHERE name LIKE \"%${name}%\" ORDER BY priority ASC")
    List<ItemEntity> getAllItemsCount(String name);

    @Select("SELECT id, name, price, amount, picture, command, tag FROM mc.items WHERE name LIKE \"%${name}%\" AND tag LIKE \"%${tag}%\" ORDER BY priority ASC")
    List<ItemEntity> getSortedItems(String name, String tag);

    @Select("SELECT id FROM mc.items WHERE name LIKE \"%${name}%\" AND tag LIKE \"%${tag}%\" ORDER BY priority ASC")
    List<ItemEntity> getSortedItemsCount(String name, String tag);
}
