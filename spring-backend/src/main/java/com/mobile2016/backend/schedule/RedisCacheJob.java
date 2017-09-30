package com.mobile2016.backend.schedule;

import com.mobile2016.common.utils.LoggerUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class RedisCacheJob {

    private final static long THREE_Minute =  60 * 1000*15;


    //@Autowired
    //private RedisClientUtil redisClientUtil;


    @Scheduled(fixedDelay=THREE_Minute)
    public void redisCacheJob(){

        long current=System.currentTimeMillis();
        LoggerUtil.W("------>"+dateToString(new Date())+"----->任务执行了...");

        long cost=(System.currentTimeMillis()-current);
        if(cost>=1000){
            cost/=1000;
            LoggerUtil.W("------>方法耗费了"+cost+"秒");
        }else{
            LoggerUtil.W("------>方法耗费了"+cost+"毫秒");
        }


    }


    private  String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

}
