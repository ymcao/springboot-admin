package com.mobile2016.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class RedisUtil {

    private  Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *
     * <Description> 根据前缀进行清除缓存<br>
     * @param prefix
     * <br>
     */
    public  void cleanRedis(String prefix) {
        logger.info("cleanRedis prefix: {}", prefix);
        try {
            if (null != prefix) {
                if (null != redisTemplate) {
                    Set<String> keys = redisTemplate.keys(prefix + "*");
                    for (String key : keys) {
                        redisTemplate.delete(key);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("cleanRedis error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 根据KEY进行清除缓存<br>
     * @param key
     * <br>
     */
    public  void cleanRedisByKey(String key) {


        logger.info("cleanRedisByKey key: {}", key);
        try {
            if (null != key) {
                if (null != redisTemplate) {
                    redisTemplate.delete(key);
                }
            }

        } catch (Exception e) {
            logger.error("cleanRedisByKey error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 缓存字符串<br>
     * @param key
     * @param data
     * @param minus
     * <br>
     */
    public  void putCacheStr(String key, String data, Long minus) {
        logger.info("putCacheStr : {}, {}, {} minute", key, data, minus);
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    opsValue.set(key, data);
                }
            }
        } catch (Exception e) {
            logger.error("putCacheStr error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 获取缓存字符串<br>
     * @param key
     * @return <br>
     */
    public  String getCacheStr(String key) {
        logger.info("getCacheStr : {}", key);

        String retStr = null;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    retStr = String.valueOf(opsValue.get(key));
                }
            }
        } catch (Exception e) {
            logger.error("getCacheStr error : {} ", e.getMessage(), e);
        }
        return retStr;
    }

    /**
     *
     * <Description> 缓存简单对象<br>
     * 基本数据类型和简单对象
     * @param key
     * @param data
     * @param minus
     * <br>
     */
    public  void putCacheSimple(String key, Object data, Long minus) {
        logger.info("putCacheSimple : {}, {}, {} minute", key, data, minus);
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    opsValue.set(key, data);

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheSimple error : {} ", e.getMessage(), e);
        }
    }

    /**
     * @param key
     * @return <br>
     */
    public  Object getCacheSimple(String key) {
        logger.info("getCacheSimple : {}", key);

        Object object = null;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    object = (Object) opsValue.get(key);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheSimple error : {} ", e.getMessage(), e);
        }
        return object;
    }

    /**
     *
     * <Description> 缓存List数据<br>
     * @param key
     * @param datas
     * @param minus
     * <br>
     */
    public  void putCacheList(String key, List<?> datas, Long minus) {
        logger.info("putCacheList : {}, {}, {} minute", key, datas, minus);
        try {
            ListOperations<String, Object> opsList = null;
            if (null != redisTemplate) {
                opsList = redisTemplate.opsForList();
                if (null != opsList) {
                    int size = datas.size();
                    for (int i = 0; i < size; i++) {
                        opsList.leftPush(key, datas.get(i));
                    }

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheList error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 获取缓存的List对象<br>
     * @param key
     * @return <br>
     */

    public  List<Object> getCacheList(String key,long start,long end) {
        logger.info("getCacheList : {}", key);

        List<Object> dataList = new ArrayList<Object>();
        try {
            ListOperations<String, Object> opsList = null;
            if (null != redisTemplate) {
                opsList = redisTemplate.opsForList();
                if (null != opsList) {
                    return  opsList.range(key,start,end);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheList error : {} ", e.getMessage(), e);
        }
        return dataList;
    }


    public  List<Object> getCacheList(String key) {
        logger.info("getCacheList : {}", key);

        List<Object> dataList = new ArrayList<Object>();
        try {
            ListOperations<String, Object> opsList = null;
            if (null != redisTemplate) {
                opsList = redisTemplate.opsForList();
                if (null != opsList) {
                    Long size = opsList.size(key);
                    for (int i = 0; i < size; i++) {
                        dataList.add(opsList.index(key, i));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getCacheList error : {} ", e.getMessage(), e);
        }
        return dataList;
    }

    /**
     *
     * <Description> 缓存SET数据<br>
     * @param key
     * @param data
     * @param minus
     * <br>
     */
    public  void putCacheSet(String key, Set<?> data, Long minus) {
        logger.info("putCacheSet : {}, {}, {} minute", key, data, minus);
        try {
            //SetOperations<String, Object> opsSet = null;
            ZSetOperations<String,Object> zsSet=null;
            if (null != redisTemplate) {
                zsSet=redisTemplate.opsForZSet();
                //opsSet = redisTemplate.opsForSet();
                if (null != zsSet) {

                    zsSet.add(key,data,System.currentTimeMillis());

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheSet error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 获取缓存的SET对象<br>
     * @param key
     * @return <br>
     */
    public  Set<?> getCacheSet(String key,long start,long end) {
        logger.info("getCacheSet : {}", key);

        Set<?> dataSet = new HashSet<>();
        try {
            ZSetOperations<String, ?> zsSet ;
            if (null != redisTemplate) {
                zsSet = redisTemplate.opsForZSet();
                if (null != zsSet) {
                    dataSet = zsSet.range(key,start,end);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheSet error : {} ", e.getMessage(), e);
        }
        return dataSet;
    }


    public long getCacheSetSize(String key) {
        logger.info("getCacheSet : {}", key);

        Set<?> dataSet = new HashSet<>();
        try {
            ZSetOperations<String, ?> zsSet;
            if (null != redisTemplate) {
                zsSet = redisTemplate.opsForZSet();
                return zsSet.size(key);
            }
        } catch (Exception e) {
            logger.error("getCacheSet error : {} ", e.getMessage(), e);
        }
        return  0;
    }



    /**
     *
     * <Description> 缓存MAP数据<br>
     * @param key
     * @param datas
     * @param minus
     * <br>
     */
    public  void putCacheMap(String key, Map<Object, Object> datas, Long minus) {
        logger.info("putCacheMap : {}, {}, {} minute", key, datas, minus);
        try {
            HashOperations<String, Object, Object> opsHash ;
            if (null != redisTemplate) {
                opsHash = redisTemplate.opsForHash();
                if (null != opsHash) {
                    opsHash.putAll(key, datas);

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheMap error : {} ", e.getMessage(), e);
        }
    }

    /**
     *
     * <Description> 获取缓存的MAP对象<br>
     * @param key
     * @return <br>
     */
    public  Map<Object, Object> getCacheMap(String key) {
        logger.info("getCacheMap : {}", key);

        Map<Object, Object> dataMap=null;
        try {
            HashOperations<String, Object, Object> opsHash = null;
            if (null != redisTemplate) {
                opsHash = redisTemplate.opsForHash();
                if (null != opsHash) {
                    dataMap = opsHash.entries(key);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheMap error : {} ", e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
