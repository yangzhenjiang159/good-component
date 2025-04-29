## 1. 基本用法（CacheLoader）
通过 ``CacheLoader`` 自动加载数据到缓存，适用于缓存未命中时需要自动加载数据的场景。
```java
import com.google.common.cache.*;

LoadingCache<String, String> cache = CacheBuilder.newBuilder()
    .build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) {
            // 缓存未命中时，调用此方法加载数据
            return fetchDataFromDB(key);
        }
    });

// 使用缓存
String value = cache.get("key1"); // 自动加载数据
```

## 2. 基于时间的过期策略
写入后过期：``expireAfterWrite``  
访问后过期：``expireAfterAccess``
```java
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES) // 写入后10分钟过期
    .expireAfterAccess(5, TimeUnit.MINUTES) // 5分钟内未被访问则过期
    .build(new CacheLoader<String, String>() {
        // ...
    });
```

## 3. 基于权重的容量限制
通过 ``maximumSize`` 或 ``maximumWeight`` 限制缓存条目数量或总权重。
```java
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
    .maximumSize(100) // 最多缓存100个条目
    .weigher((String key, String value) -> value.length()) // 权重计算（按值长度）
    .maximumWeight(1024) // 总权重不超过1024
    .build(new CacheLoader<String, String>() {
        // ...
    });
```

## 4. 显示清楚缓存
手动清除单个、多个或全部缓存条目
```java
// 清除单个条目
cache.invalidate("key1");

// 清除多个条目
cache.invalidateAll(Arrays.asList("key1", "key2"));

// 清除所有条目
cache.invalidateAll();
```

## 5. 弱引用键/值
使用弱引用（Weak Reference）允许缓存条目在内存不足时被垃圾回收。
```java
// 清除单个条目
Cache<String, String> cache = CacheBuilder.newBuilder()
                .weakKeys()      // 键使用弱引用
                .weakValues()    // 值使用弱引用
                .build();
```

## 6. 缓存移除监听器
通过 ``RemovalListener`` 监听缓存条目移除事件。
```java
Cache<String, String> cache = CacheBuilder.newBuilder()
        .removalListener((RemovalNotification<String, String> notification) -> {
            System.out.println("Key " + notification.getKey() + " removed, reason: " + notification.getCause());
        })
        .build();
```

## 7. 统计信息
启用统计功能以监控缓存命中率、加载时间等。
```java
Cache<String, String> cache = CacheBuilder.newBuilder()
        .recordStats() // 启用统计
        .build();

// 获取统计信息
CacheStats stats = cache.stats();
System.out.println("命中率: " + stats.hitRate());
```

## 8. 异步刷新
通过 ``refreshAfterWrite`` 定期异步刷新缓存，避免阻塞请求。
```java
LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .refreshAfterWrite(1, TimeUnit.MINUTES) // 写入1分钟后自动刷新
        .build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return fetchDataFromDB(key);
            }

            @Override
            public ListenableFuture<String> reload(String key, String oldValue) {
                // 异步刷新数据
                return executor.submit(() -> fetchDataFromDB(key));
            }
        });
```

## 9. 手动加载（Callable）
通过 ``Callable`` 在 ``get`` 时动态指定加载逻辑。
```java
Cache<String, String> cache = CacheBuilder.newBuilder().build();

String value = cache.get("key1", () -> fetchDataFromDB("key1"));

```