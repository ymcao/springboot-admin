package com.mobile2016.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by caoyamin on 2016/11/7.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    //@Autowired
    //private WebAnthencationProder provider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许访问静态资源
        http.authorizeRequests()
                .antMatchers("/upload", "/css/**", "/js/**", "/img/**",
                        "/LESS/**", "/font-awesome/**", "/fonts/**", "/templates/**", "/404", "/401","/403","/500")
                .permitAll();
        //所有的访问都需要权限验证
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin()
                .failureUrl("/admin/login?error=true")
                .successHandler(new AuthenticationSuccessHandler())
                //访问成功页url
                .defaultSuccessUrl("/admin/index")
                //默认访问页
                .loginPage("/admin/login")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/admin/login")
                .permitAll();

        // 允许iframe 嵌套
        http.headers().frameOptions().disable();
        //关闭csrf 防止循环定向
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).accessDeniedHandler(new MyAccessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/webjars/**");
        web.ignoring().antMatchers("/img/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        //采用自定义验证
		//auth.authenticationProvider(provider);
        //需要采用加密
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }



    private class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.setCharacterEncoding("utf-8");
            if (isAjax(request)) {
                response.getWriter().println("请登录");
            } else {
                response.sendRedirect("/admin/login");
            }

        }
    }


    private class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
                throws ServletException, IOException {

            clearAuthenticationAttributes(request);
            if (!isAjax(request)) {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
    }



    private class MyAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setCharacterEncoding("utf-8");
            if (isAjax(request)) {
                response.getWriter().println("无权限访问");
            } else {
                response.sendRedirect("/admin/403");
            }

        }
    }

    protected boolean isAjax(HttpServletRequest request) {
        if(request.getHeader("X-Requested-With")!=null
                &&request.getHeader("X-Requested-With")!=""){
            return true;
        }
        return false;
    }

}