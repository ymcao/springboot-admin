package com.mobile2016.backend.service.mapper;

import com.mobile2016.backend.model.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/20.
 */
@Mapper
public interface CategoryMapper {


    @Select("SELECT * FROM admin_category  WHERE id = #{id};")
    Category findById(int id);


    @Select({
            "<script>",
            "SELECT * FROM admin_category",
            "WHERE state = 1",
            "<when test='name!=null'>",
            "AND name LIKE CONCAT('%',#{name},'%')",
            "</when>",
            "order by addDate desc limit #{start},#{end}",
            "</script>"
    })
    List<Category> list(Category category);

    @Select({
            "SELECT count(*) FROM admin_category  WHERE state = 1"
    })
    int count();

    @Transactional
    @Insert("INSERT INTO admin_category (name, description, image, adddate, state) VALUES ( #{name}, #{description}, #{image}, now(), 1);")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Category category);



    @Transactional
    @Update("UPDATE admin_category SET name = #{name} , description = #{description} , image = #{image} WHERE id = #{id};")
    int updateCategory(Category category);


    @Transactional
    @Update("UPDATE admin_category SET state = #{state} WHERE id = #{id};")
    int updateState(Category category);

}
