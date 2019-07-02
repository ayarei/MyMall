package com.ltr.mymall.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.github.pagehelper.Page;
import com.ltr.mymall.pojo.SerializeDeserializeWrapper;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtoStuffUtil {
	
	private static final Logger logger = Logger.getLogger(ProtoStuffUtil.class);
	
	/**
     * 需要使用包装类进行序列化/反序列化的class集合
     */
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<>();
    /**
     * 序列化/反序列化包装类 Class 对象
     */
    private static final Class<SerializeDeserializeWrapper> WRAPPER_CLASS = SerializeDeserializeWrapper.class;

    /**
     * 序列化/反序列化包装类 Schema 对象
     */
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(WRAPPER_CLASS);

    /**
     * 预定义一些Protostuff无法直接序列化/反序列化的对象
     */
    static {
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);

        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(Page.class);

        WRAPPER_SET.add(Object.class);
    }
    
    /**
    * 注册需要使用包装类进行序列化/反序列化的 Class 对象
    *
    * @param clazz 需要包装的类型 Class 对象
    */
   public static void registerWrapperClass(Class clazz) {
       WRAPPER_SET.add(clazz);
   }
   
   /**
    * 获取序列化对象类型的schema
    *
    * @param cls 序列化对象的class
    * @param <T> 序列化对象的类型
    * @return 序列化对象类型的schema
    */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> Schema<T> getSchema(Class<T> cls) {
		Schema<T> schema = RuntimeSchema.createFrom(cls);
		return schema;
	}

   /**
    * 序列化对象
    *
    * @param obj 需要序列化的对象
    * @param <T> 序列化对象的类型
    * @return 序列化后的二进制数组
    */
   @SuppressWarnings("unchecked")
   public static <T> byte[] serialize(T obj) {
       Class<T> clazz = (Class<T>) obj.getClass();
       LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
       try {
           Object serializeObject = obj;
           Schema schema = WRAPPER_SCHEMA;
           // 已经包含定义的schema
           if (!WRAPPER_SET.contains(clazz)) {
               schema = getSchema(clazz);
           } else {
               serializeObject = SerializeDeserializeWrapper.builder(obj);
           }
           return ProtostuffIOUtil.toByteArray(serializeObject, schema, buffer);
       } catch (Exception e) {
           logger.error("序列化对象异常 [" + obj + "]", e);
           throw new IllegalStateException(e.getMessage(), e);
       } finally {
           buffer.clear();
       }
   }

   /**
    * 反序列化对象
    *
    * @param data  需要反序列化的二进制数组
    * @param clazz 反序列化后的对象class
    * @param <T>   反序列化后的对象类型
    * @return 反序列化后的对象集合
    */
   public static <T> T deserialize(byte[] data, Class<T> clazz) {
       try {
           if (!WRAPPER_SET.contains(clazz)) {
               T message = clazz.newInstance();
               Schema<T> schema = getSchema(clazz);
               ProtostuffIOUtil.mergeFrom(data, message, schema);
               return message;
           } else {
               SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
               ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
               return wrapper.getData();
           }
       } catch (Exception e) {
           logger.error("反序列化对象异常 [" + clazz.getName() + "]", e);
           throw new IllegalStateException(e.getMessage(), e);
       }
	}
}
