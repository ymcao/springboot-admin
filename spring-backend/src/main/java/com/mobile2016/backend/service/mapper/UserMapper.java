package com.mobile2016.backend.service.mapper;

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


    @Transactional
    @Insert("INSERT INTO admin_user(username,password,role,mobile,state,avatar,addate)" +
            "values(#{username},#{password},#{role},#{mobile},1,#{avatar},now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    long insertAdminUser(AdminUser admin_user);



    @Select("SELECT * FROM admin_user where username=#{username}")
    AdminUser findAdminUserByName(@Param("username") String username);




    @Select("SELECT * FROM  user where id=#{id}")
    User findUserById(User user);

    @Select({
            "<script>",
            "SELECT *  FROM  user ",
            "where enabled=1",
            "<when test='username!=null'>",
            "AND username LIKE CONCAT('%',#{username},'%')",
            "</when>",
            "limit #{start},#{end}",
            "</script>"
    })
    List<User> loadAllUsers(User user);



    @Transactional
    @Update("UPDATE  user set username=#{username},password=#{password},phone=#{phone} where id=#{id}")
    long updateUser(User user);


    @Transactional
    @Update("UPDATE  user set avatar=#{avatar} where id=#{id}")
    long updateHead(User user);


    @Select({
            "<script>",
            "SELECT COUNT(*) FROM user ",
            "WHERE enabled = 1 ",
            "<when test='username!=null'>",
            "AND username LIKE CONCAT('%',#{username},'%')",
            "</when>",
            "</script>"
    })
    int count();

    @Update("UPDATE user  SET enabled=#{enabled} where id=#{id}")
    void delUserById(User user);

}
