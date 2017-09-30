package com.mobile2016.backend.mapper;

import com.mobile2016.backend.model.Post;
import com.mobile2016.common.Constant;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/20.
 */
@Mapper
public interface PostMapper {


    @Select("SELECT * FROM target_post WHERE id = #{id};")
    Post findById(int id);


    @Select({
            "<script>",
            "SELECT N.*,C.name AS targetName FROM target_post N ",
            "LEFT JOIN target C ON N.target = C.id ",
            "WHERE N.valid=1",
            "<when test='title!=null'>",
            "AND N.TITLE LIKE CONCAT('%',#{title},'%')",
            "</when>",
            "<when test='target!=0'>",
            "AND N.target = #{target}",
            "</when>  ",
            "limit #{start},#{end}",
            "</script>"
    })
    List<Post> list(Post post);

    @Select({
            "<script>",
            "SELECT COUNT(1) FROM target_post N ",
            "LEFT JOIN target C ON N.target = C.id ",
            "WHERE N.valid =1",
            "<when test='title!=null'>",
            "AND N.TITLE LIKE CONCAT('%',#{title},'%')",
            "</when>",
            "<when test='target!=0'>",
            "AND N.target = #{target}",
            "</when>",
            "</script>"
    })
    int count(Post post);

    @Transactional
    @Insert("INSERT INTO target_post (title, description, target, content, addDate,browses,likes,score,enabled,valid) VALUES ( #{title}, #{description}, #{target},#{content}, now(),0,0,0,0,1);")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Post post);


    @Transactional
    @Update("UPDATE target_post  SET title = #{title} , content = #{content} , target = #{target}, updateDate=now() WHERE id = #{id};")
    int updateArticle(Post post);


    @Transactional
    @Update("UPDATE target_post  SET enabled = #{enabled} WHERE id = #{id};")
    int updateState(Post post);

}
