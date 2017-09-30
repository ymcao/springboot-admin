package com.mobile2016.backend.mapper;

import com.mobile2016.backend.model.Target;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/30.
 */
@Mapper
public interface TargetMapper {


    @Select("SELECT * FROM Target  WHERE id = #{id};")
    Target findById(int id);


    @Select({
            "<script>",
            "SELECT * FROM Target",
            "WHERE enabled = 1",
            "<when test='name!=null'>",
            "AND name LIKE CONCAT('%',#{name},'%')",
            "</when>",
            "order by addDate desc limit #{start},#{end}",
            "</script>"
    })
    List<Target> list(Target target);

    @Select({
            "SELECT count(*) FROM Target  WHERE enabled = 1"
    })
    int count();

    @Transactional
    @Insert("INSERT INTO Target (name, addDate, enabled) VALUES ( #{name}, now(), 1);")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Target target);



    @Transactional
    @Update("UPDATE Target SET name = #{name}  WHERE id = #{id};")
    int updateCategory(Target target);


    @Transactional
    @Update("UPDATE Target SET enabled = #{enabled} WHERE id = #{id};")
    int updateState(Target target);

}
