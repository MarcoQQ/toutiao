package com.marco.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Select;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.velocity.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool jedisPool = null;
    public static Log logger = LogFactory.getLog(JedisAdapter.class);

    public static void print(int index, Object o){
        System.out.println(String.format("%d %s", index, o.toString()));
    }

//    public static void main(String argv[]){
//        Jedis jedis = new Jedis();
//        jedis.flushAll();
//
//
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("***", 6379);
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.sadd(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.srem(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.sismember(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public Long scard(String key){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0L;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

//    public void setObject(String key, Object obj){
//        set(key, JSON.toJSONString(obj));
//    }
//
//    public <T> T getObject(String key, Class<T> clazz){
//        String value = get(key);
//        if(value != null){
//            return JSON.parseObject(value, clazz);
//        }
//        return null;
//    }



}
