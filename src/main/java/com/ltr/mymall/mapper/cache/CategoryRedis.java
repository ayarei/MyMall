package com.ltr.mymall.mapper.cache;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.ltr.mymall.pojo.Category;
import com.ltr.mymall.pojo.SerializeDeserializeWrapper;
import com.ltr.mymall.util.ProtoStuffUtil;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class CategoryRedis {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private JedisPool jedisPool;
	
	private RuntimeSchema<Category> schema = RuntimeSchema.createFrom(Category.class);
	private RuntimeSchema<Integer> schemaTotal = RuntimeSchema.createFrom(Integer.class);

	public Category getCategory(int categoryId) {
		// 从redis获取一个分类信息
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryID:" + categoryId;
				// 使用protostuff实现序列化，不使用java内部序列化
				byte[] bytes = jedis.get(key.getBytes());
				// 获取到缓存
				if (bytes != null) {
					Category category = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, category, schema);
					// 反序列化Categoty
					return category;
				}
			} finally {
				jedis.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Page<Category> getCategoryList(int pageStart) {
		// 从redis获取分类列表
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryList:" + pageStart;
				// 使用protostuff实现序列化，不使用java内部序列化
				byte[] bytes = jedis.get(key.getBytes());
				// 获取到缓存
				if (bytes != null) {
					// 反序列化Categoty
					SerializeDeserializeWrapper<Page<Category>> result = ProtoStuffUtil.deserialize(bytes,
							SerializeDeserializeWrapper.class);
					return result.getData();
				}
			} finally {
				jedis.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 缓存一个Category对象
	 * @param category
	 * @return
	 */
	public String putCategory(Category category) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryID:" + category.getId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(category, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 缓存超时时间，分类不常变化，5小时
				int timeout = 60 * 60 * 5;
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 缓存查询的List
	 * 
	 * List不能直接序列化 
	 * 
	 * 使用包装类包装List
	 * 
	 * 注意：缓存的数据类型为Page<E>
	 */
	public String putCategory(Page<Category> list, int pageStart) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryList:" + pageStart;
				byte[] bytes = ProtoStuffUtil.serialize(list);
				// 缓存超时时间，分类不常变化，5小时
				int timeout = 60 * 60 * 5;
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 从Redi中删除对应元素 用于缓存更新
	 */
	public void clear(int categoryId) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key1 = "categoryID:" + categoryId;
				jedis.del(key1.getBytes());
				String total = "categoryTotal";
				jedis.del(total.getBytes());
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 从列表中删除所有分类列表 用于缓存更新
	 */
	public void clearAllList() {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				Set<String> keySet = jedis.keys("categoryList:*");
				if (keySet != null) {
					for (String e : keySet) {
						jedis.del(e.getBytes());
					}
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 由于使用了分页插件 需要缓存总数据条目数
	 * 
	 * @param total
	 * @return
	 */
	public String putTotal(int total) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryTotal";
				byte[] bytes = ProtostuffIOUtil.toByteArray(total, schemaTotal,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60 * 5;
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取总数据条目数
	 * @return
	 */
	public Integer getTotal() {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "categoryTotal";
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					Integer total = schemaTotal.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, total, schemaTotal);
					// 反序列化Categoty
					return total;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
