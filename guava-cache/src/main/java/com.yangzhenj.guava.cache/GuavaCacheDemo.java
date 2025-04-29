package com.yangzhenj.guava.cache;
import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;

public class GuavaCacheDemo {
	//1. 基本用法（CacheLoader）
	//通过 CacheLoader 自动加载数据到缓存，适用于缓存未命中时需要自动加载数据的场景。
	static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
			.build(new CacheLoader<String, String>() {
				@Override
				public String load(String key) {
					// 缓存未命中时，调用此方法加载数据
					return fetchDataFromDB(key);
				}
			});
	public static String fetchDataFromDB(String key) {
		// 模拟从数据库获取数据
		return "Data for " + key;
	}

	public static void main(String[] args) throws ExecutionException {
		String key1 = cache.get("key1");// 自动加载数据
		System.out.println("key1: " + key1);
	}
}
