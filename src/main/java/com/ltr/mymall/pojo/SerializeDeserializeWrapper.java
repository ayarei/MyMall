package com.ltr.mymall.pojo;

/**
 * 用于缓存序列化List、Map等对象的包装类
 * @author 刘添瑞
 *
 * @param <T>
 */
public class SerializeDeserializeWrapper<T> {

	private T data;
	
	/**
	 * 返回一个包装了data的wrapper对象
	 * @param <T>
	 * @param data
	 * @return
	 */
	public static <T> SerializeDeserializeWrapper<T> builder(T data){
		 SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
		 wrapper.setData(data);
		 return wrapper;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
