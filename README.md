# spring-boot-admin
### 手把手一步步搭建Admin后台管理系统

`1.spring-boot mybatis
 2.spring-boot scurity权限认证
 3.thymeleaf模板
 4.后台管理系统，界面比较比较漂亮
`

> build.gradle

```c
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    runtime('mysql:mysql-connector-java')
    compile("org.springframework.boot:spring-boot-starter-web")
    //compile ("org.springframework.boot:spring-boot-starter-data-rest")
    //compile "com.alibaba:druid:1.0.26"
    compile "org.aspectj:aspectjweaver:1.5.4"
    compile "org.mybatis.spring.boot:mybatis-spring-boot-starter:1.1.1"
    compile ("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile ("org.springframework.boot:spring-boot-starter-security")
    compile group: 'io.jsonwebtoken', name: 'jjwt', version: '0.7.0'
    compile 'org.springframework.mobile:spring-mobile-device:1.1.5.RELEASE'
    compile 'javax.servlet:javax.servlet-api:3.1.0'
    compile 'org.thymeleaf.extras:thymeleaf-extras-springsecurity4:2.1.2.RELEASE'
}

```
运行截图 

![](https://raw.githubusercontent.com/ymcao/springboot-admin/master/screenshot/admin_screenshot.png)



