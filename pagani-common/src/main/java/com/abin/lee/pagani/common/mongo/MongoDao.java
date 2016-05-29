package com.abin.lee.pagani.common.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */

public class MongoDao<T> {

    @Autowired
    public MongoTemplate mongoTemplate;

    /**
     * 插入一条记录
     * @param clazz
     */
    public void insert(T clazz) {
        mongoTemplate.save(clazz);
    }

    /**
     * 插入一个集合
     * @param params
     */
    public void insertAll(List<T> params) {
        mongoTemplate.insertAll(params);
    }

    /**
     * 查找多条记录通过条件
     * @param query
     * @param clazz
     * @return
     */
    public List<T> findList(Query query, Class<T> clazz) {
        return (List<T>) mongoTemplate.find(query, clazz);
    }

    /**
     * 查找多条记录通过条件
     * @param query
     * @param clazz
     * @param collectionName
     * @return
     */
    public List<T> findList(Query query, Class<T> clazz, String collectionName) {
        return (List<T>) mongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 查找一条记录通过条件
     * @param query
     * @param clazz
     * @return
     */
    public T findOne(Query query, Class<T> clazz) {
        return (T) mongoTemplate.findOne(query, clazz);
    }

    /**
     * 查找一条记录通过条件
     * @param query
     * @param clazz
     * @param collectionName
     * @return
     */
    public T findOne(Query query, Class<T> clazz, String collectionName) {
        return (T) mongoTemplate.findOne(query, clazz, collectionName);
    }

    /**
     * 只能更新一个字段
     *
     * @param clazz
     * @param id
     * @param key
     * @param value
     */
    public void updateById(Class<T> clazz, String id, String key, Object value) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        Update update = Update.update(key, value);
        mongoTemplate.updateFirst(query, update, clazz);
    }

    /**
     * 更新一条记录的所有字段
     *
     * @param clazz
     * @param id
     * @param request
     */
    public void updateById(Class<T> clazz, String id, Map<String, Object> request) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        Update update = Update.update("_id", id);
        for (Iterator<Map.Entry<String, Object>> iterator = request.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            update.set(entry.getKey(), entry.getValue());
            mongoTemplate.updateFirst(query, update, clazz);
        }
    }

    /**
     * 通过_id来删除一条记录
     * @param id  mongo默认生成的_id
     * @param clazz
     * @return
     */
    public T findById(String id, Class<T> clazz) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        return this.mongoTemplate.findOne(query, clazz);
    }

    /**
     * 删除一个Bean，删除的Collection是在Bean的注解里面指定
     * @param t
     */
    public void delete(T t) {
        this.mongoTemplate.remove(t);
    }

    /**
     * 删除一个Bean，通过beanName和collectionName
     * @param t
     * @param collectionName
     */
    public void delete(T t, String collectionName) {
        this.mongoTemplate.remove(t, collectionName);
    }


}
