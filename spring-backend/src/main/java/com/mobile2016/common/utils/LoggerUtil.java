package com.mobile2016.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static Logger logger= LoggerFactory.getLogger(LoggerUtil.class);

    public  static  void I(String msg){
        logger.info(msg);
    }

    public  static  void D(String msg){
        logger.debug( msg);
    }

    public  static  void E(String msg){
        logger.error( msg);
    }

    public  static  void W(String msg){
        logger.warn( msg);
    }

}
