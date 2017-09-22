package com.mobile2016.backend.service;

import com.mobile2016.backend.model.User;
import com.mobile2016.backend.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    public List<User> loadAllUsers(User user) {
        return userMapper.loadAllUsers(user);
    }

    public Integer count(){
        return userMapper.count();
    }
    /**
     * 保存用户
     *
     * @param user
     */
    public void save(User user) {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (user.getRole().equals("super")) {
            user.setRole("ROLE_SUPER");
        } else if (user.getRole().equals("admin")) {
            user.setRole("ROLE_ADMIN");
        } else if(user.getRole().equals("user")){
            user.setRole("ROLE_USER");
        }
        user.setAddate(new java.sql.Date(new java.util.Date().getTime()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.insert(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userMapper.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        //System.err.println(user.getRole() + "正在执行查询角色名称");
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

    public User getUserByname(String username) {
        return userMapper.findUserByName(username);
    }
}