package com.mobile2016.backend.service.mapper;

import com.mobile2016.backend.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caoyamin on 2017/9/20.
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM admin_user where username=#{username}")
    User findUserByName(@Param("username") String username);


    @Select("SELECT * FROM admin_user limit #{start},#{end}")
    List<User> loadAllUsers(User user);

    @Transactional
    @Insert("INSERT INTO admin_user(username,password,role,mobile,state,avatar,addate)" +
            "values(#{username},#{password},#{role},#{mobile},#{state},#{avatar},#{addate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    long insert(User admin_user);


    @Select("SELECT COUNT(*) FROM admin_user")
    int count();

}
