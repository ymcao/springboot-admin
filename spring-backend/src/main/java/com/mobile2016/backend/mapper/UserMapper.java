package com.mobile2016.backend.mapper;

import com.mobile2016.backend.model.AdminUser;
import com.mobile2016.backend.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/20.
 */
@Mapper
public interface UserMapper {

    /////////////////////基础注解SQL

    @Insert("INSERT INTO admin_user(username,password,role,mobile,state,avatar,addate)" +
            "values(#{username},#{password},#{role},#{mobile},1,#{avatar},now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    AdminUser insertAdminUser(AdminUser admin_user);



    @Select("SELECT * FROM admin_user where username=#{username}")
    AdminUser findAdminUserByName(@Param("username") String username);

    //////////////////////////////////////

    @Insert("INSERT INTO user(username,password,email,mobile,enabled,avatar,registerDate)" +
            "values(#{username},#{password},#{email},#{mobile},1,#{avatar},now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long insertUser(User user);


    @Select("SELECT * FROM  user where mobile=#{mobile}")
    User findUserByMobile(User user);

    @Select({
            "<script>",
            "SELECT *  FROM  user ",
            "where enabled=1",
            "<when test='mobile!=null'>",
            "AND mobile LIKE CONCAT('%',#{mobile},'%')",
            "</when>",
            "limit #{start},#{end}",
            "</script>"
    })
    List<User> loadAllUsers(User user);



    @Update("UPDATE  user set username=#{username},password=#{password},mobile=#{mobile} where id=#{id}")
    long updateUser(User user);


    @Update("UPDATE  user set avatar=#{avatar} where id=#{id}")
    long updateHead(User user);


    @Select({
            "<script>",
            "SELECT COUNT(1) FROM user ",
            "WHERE enabled = 1 ",
            "<when test='mobile!=null'>",
            "AND mobile LIKE CONCAT('%',#{mobile},'%')",
            "</when>",
            "</script>"
    })
    int count();

    @Update("UPDATE user  SET enabled=#{enabled} where id=#{id}")
    void delUserById(User user);

}
