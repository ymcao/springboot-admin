package com.mobile2016.backend.service;

import com.mobile2016.backend.model.AdminUser;
import com.mobile2016.backend.model.User;
import com.mobile2016.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
//@CacheConfig(cacheNames="user_list_keys")
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    //@Autowired
    //private RedisUtil redisClientUtil;

    ////-------------------------------->普通用户


    @Transactional(readOnly = true)
    public List<User> loadAllUsers(User user) {
        /*Set keys=redisClientUtil.keys("m_"+"*");
        if(keys.size()>0){
            List<User> users=new ArrayList<>();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                String key= (String) it.next();
                User u=(User)redisClientUtil.getCacheSimple(key);
                users.add(u);
            }
            LoggerUtil.W("------>loadAllUsers从Redis缓存");
            return users;
        }else {

            LoggerUtil.W("------>loadAllUsers从数据库");
            return userMapper.loadAllUsers(user);
        }*/
        return userMapper.loadAllUsers(user);
    }

    @Transactional(readOnly = true)
    public int count(){
        return userMapper.count();
    }



    //@CachePut(key="'m_'+#p0.mobile")
    public  User insertUser(User user) throws  Exception{


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);

        return  userMapper.findUserByMobile(user) ;
    }


    //@CachePut(key="'m_'+#p0.mobile")
    public  User updateUser(User user){
        userMapper.updateUser(user);
        return  userMapper.findUserByMobile(user) ;
    }


    //@CacheEvict(key="'m_'+#p0.mobile")
    public void delUserById(User user){

        userMapper.delUserById(user);
    }


    //@Cacheable(key="'m_'+#p0.mobile")
    @Transactional(readOnly = true)
    public  User findUserByMobile(User user) {
        return userMapper.findUserByMobile(user);
    }


    //-------------------------------->管理员用户

    /**
     * @param user
     */
    public AdminUser  insertAdminUser(AdminUser user) throws Exception {

        AdminUser u=userMapper.findAdminUserByName(user.getUsername());
        if(u!=null){
            throw new Exception("Username has  existed........");
        }
        //
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (user.getRole().equals("super")) {
            user.setRole("ROLE_SUPER");
        } else if (user.getRole().equals("admin")) {
            user.setRole("ROLE_ADMIN");
        }
        user.setAddate(new java.sql.Date(new java.util.Date().getTime()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertAdminUser(user);

       return   userMapper.findAdminUserByName(user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AdminUser user = getAdminUserByname(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return new UserDetails() {
            private static final long serialVersionUID = 3720901165271071386L;
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> auths = new ArrayList<>();
                auths.add(new SimpleGrantedAuthority(user.getRole()));
                return auths;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }


    public AdminUser getAdminUserByname(String username) {
        return userMapper.findAdminUserByName(username);
    }
}