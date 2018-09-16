package com.zzh.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * redis集群客户端
 * @filename JedisClusterFactory.java
 * @author chenchaochao
 * @datetime 2017年3月31日 上午10:26:52
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);
    private String address;
    private JedisCluster jedisCluster;
    private Integer timeout;
    private Integer maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    public JedisClusterFactory() {
    }

    @Override
    public JedisCluster getObject() throws Exception {
        return this.jedisCluster;
    }

    @Override
    public Class<? extends JedisCluster> getObjectType() {
        return this.jedisCluster != null?this.jedisCluster.getClass():JedisCluster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
            HashSet<HostAndPort> haps = new HashSet<>();
            String [] hosts = this.address.split(",");
            int size = hosts.length;
            for(int i=0;i<size;i++){
            	String[] ipAndPort = hosts[i].split(":");
            	logger.info("初始化redis hostAndport："+hosts[i]);
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
			return haps;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        String password = PropertyPlaceholderConfigurer.getProperty("REDIS_PASSWORD");
        if(StringUtils.isNotBlank(password)){
            int soTimeOut = Integer.parseInt(PropertyPlaceholderConfigurer.getProperty("REDIS_SOCKETTIMEOUT","20000"));
            this.jedisCluster = new JedisCluster(haps, this.timeout.intValue(),soTimeOut,this.maxRedirections.intValue(),password, this.genericObjectPoolConfig);
        }else{
            this.jedisCluster = new JedisCluster(haps, this.timeout.intValue(),this.maxRedirections.intValue(), this.genericObjectPoolConfig);
        }
        logger.info("初始化jedisCluster："+this.jedisCluster);
    }

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTimeout(int timeout) {
        this.timeout = Integer.valueOf(timeout);
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = Integer.valueOf(maxRedirections);
    }
    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

	public String setex(final String key, final int seconds, final String value){
    	return this.jedisCluster.setex(key, seconds, value);
    }
 
	public String set(final String key, final String value){
    	return this.jedisCluster.set(key, value);
    }
    public String get(String key){
    	return this.jedisCluster.get(key);
    }
    
    public long del(String key){
    	return this.jedisCluster.del(key);
    }
    
    public void del(Set<String> set){
    	for(String s : set){
    		del(s);
    	}
    }
    
    /**
     * 是否存在某个hash field
     * @author chenchaochao
     * @datetime 2017年6月8日下午12:32:19
     * @param hname
     * @param field
     * @return
     * 
     */
    public boolean hexists(String hname,String field){

    	return jedisCluster.hexists(hname, field);
    }
    /**
     * 是否存在某个 key
     * @author chenchaochao
     * @datetime 2017年6月8日下午12:32:19
     * @param hname
     * @param field
     * @return
     * 
     */
    public boolean exists(String key){

    	return jedisCluster.exists(key);
    }  
    
    /**
     * 是否存在某个hash field
     * @author chenchaochao
     * @datetime 2017年6月8日下午12:32:19
     * @param hname
     * @param field
     * @return
     * 
     */
    public long expire(String key,int timeout){

    	return jedisCluster.expire(key,timeout);
    } 
    /**
     * 根据key保存一个map对象
     * @author chenchaochao
     * @datetime 2017年6月8日下午12:35:19
     * @param key
     * @param name
     * @param field
     * @return
     * 
     */
    public String hmset(String key,String name,String field){
    	Map<String,String> map = new HashMap<>();
    	map.put(name, field);
    	return jedisCluster.hmset(key,map);
    }
    
    /**
     * 删除hset中的值
     * @author chenchaochao
     * @datetime 2017年6月14日上午11:44:24
     * @param key
     * @param field
     * @return
     * 
     */
    public long hdel(String key, String field){
    	return jedisCluster.hdel(key, field);
    }
    
    /**
     * 模糊查询
     * @author chenchaochao
     * @datetime 2017年4月21日 下午3:38:30
     * @param key
     * @return
     */
    public TreeSet<String> startWidthHkeys(String key){
    	TreeSet<String> keys = new TreeSet<>();  
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();  
        for(String k : clusterNodes.keySet()){  
            logger.info("Getting keys from: {}", k);  
            JedisPool jp = clusterNodes.get(k);  
            Jedis connection = jp.getResource();  
            try {  
                keys.addAll(connection.keys(key.concat("*")));  
            } catch(Exception e){  
                logger.info("Getting keys error: {}", e);  
            } finally{
                logger.info("Connection closed.");  
                connection.close();
            }  
        }  
        logger.debug("Keys gotten!");  
        return keys;  
    }
    
    /**
     * 返回1 设置成功 0 设置失败
     * setnx
     * @author chenchaochao
     * @datetime 2017年6月14日上午11:44:24
     * @param key
     * @param field
     * @return
     * 
     */
    public long setnx(String key, String val,int timeout){
    	long result = jedisCluster.setnx(key, val);
    	if(timeout != 0)
    	{
    		jedisCluster.expire(key, timeout);
    	} 	
    	return result;
    }

    /**
     * 返回1 设置成功 0 设置失败
     * setnx
     * @author chenchaochao
     * @datetime 2017年6月14日上午11:44:24
     * @param key
     * @param field
     * @return
     *
     */
    public long setlnx(String key, String val,long timeout){
    	long result = jedisCluster.setnx(key, val);
    	if(timeout != 0)
    	{
    		jedisCluster.pexpire(key, timeout);
    	}
    	return result;
    }
    
    /**
     * setnx
     * @author chenchaochao
     * @datetime 2017年6月14日上午11:44:24
     * @param key
     * @param field
     * @return
     * 
     */
    public void setxTimeout(String key, String val,int timeout){
    	jedisCluster.set(key, val);
    	jedisCluster.expire(key, timeout);

    }
}
