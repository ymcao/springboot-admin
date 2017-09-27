package com.mobile2016.backend.service.mapper;

import com.mobile2016.backend.model.Article;
import com.mobile2016.backend.model.Category;
import com.mobile2016.backend.model.Constant;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/20.
 */
@Mapper
public interface ArticleMapper {


    @Select("SELECT * FROM admin_article  WHERE id = #{id};")
    Article findById(int id);


    @Select({
            "<script>",
            "SELECT N.*,C.name AS categoryName,C.image AS categoryImage FROM admin_article N ",
            "LEFT JOIN admin_category C ON N.category = C.id ",
            "<when test='title!=null'>",
            "AND N.TITLE LIKE CONCAT('%',#{title},'%')",
            "</when>",
            "<when test='category!=0'>",
            "AND category = #{category}",
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByAddDateAsc+"\"'>",
            "order by "+Constant.OrderByAddDateAsc+",addDate desc",
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByAddDateDesc+"\"'>",
            "order by "+Constant.OrderByAddDateDesc,
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByBrowsesDesc+"\"'>",
            "order by "+Constant.OrderByBrowsesDesc+",addDate desc",
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByCommentsDesc+"\"'>",
            "order by "+ Constant.OrderByCommentsDesc+",addDate desc",
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByLikesDesc+"\"'>",
            "order by "+Constant.OrderByLikesDesc+",addDate desc",
            "</when>",
            "<when test='orderBy==\""+Constant.OrderByScoreDesc+"\"'>",
            "order by "+Constant.OrderByScoreDesc+",addDate desc",
            "</when>",
            "limit #{start},#{end}",
            "</script>"
    })
    List<Article> list(Article article);

    @Select({
            "<script>",
            "SELECT COUNT(*) FROM admin_article N ",
            "LEFT JOIN admin_category C ON N.category = C.id ",
            "WHERE N.state = 1 ",
            "<when test='title!=null'>",
            "AND N.TITLE LIKE CONCAT('%',#{title},'%')",
            "</when>",
            "<when test='category!=0'>",
            "AND category = #{category}",
            "</when>",
            "<when test='commendState!=0'>",
            "AND commendState = #{commendState}",
            "</when>",
            "</script>"
    })
    int count(Article article);

    @Transactional
    @Insert("INSERT INTO admin_article (title, description, category, content, addDate,commendState,browses,likes,score,state) VALUES ( #{title}, #{description}, #{category},#{content}, now(),1,0,0,0,0);")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Article article);


    @Transactional
    @Update("UPDATE admin_article  SET title = #{title} , content = #{content} , category = #{category}, updateDate=now() WHERE id = #{id};")
    int updateArticle(Article article);


    @Transactional
    @Update("UPDATE admin_article  SET state = #{state} WHERE id = #{id};")
    int updateState(Article article);

}
