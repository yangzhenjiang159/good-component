

阅读 Guava Cache 源码时，可以按照以下步骤和策略快速理清脉络并领会设计精髓：

---

### **1. 明确核心目标与功能**
- **核心功能**：本地缓存、线程安全、淘汰策略（LRU/LFU）、过期机制、自动加载、统计监控。
- **关键特性**：并发控制、惰性清理、权重支持、弱/软引用键值。

---

### **2. 从入口类切入**
- **`CacheBuilder`**：配置缓存的入口，链式调用设置参数（如 `maximumSize`, `expireAfterWrite`）。
    - 重点看 `build()` 方法，理解如何根据配置生成 `LocalCache` 实例。
- **`LoadingCache` 接口**：定义 `get(key)` 等核心方法，实际实现是 `LocalCache`。

---

### **3. 核心类与结构**
- **`LocalCache`**：缓存的核心实现类，数据结构类似分段锁的 `ConcurrentHashMap`。
    - **`Segment` 数组**：每个 Segment 管理一个哈希表，继承自 `ReentrantLock`，降低锁粒度。
    - **`ReferenceEntry`**：缓存条目的接口，支持不同引用类型（强/弱/软）。
    - **`ValueReference`**：值的引用，区分加载状态（如 `LoadingValueReference`）。
- **`CacheLoader`**：数据加载逻辑的抽象，用户需实现 `load(key)` 方法。

---

### **4. 关键流程分析**
- **缓存获取（`get(key)`）**：
    1. 根据 `key` 哈希定位到对应的 `Segment`。
    2. 在 `Segment` 内查找 `Entry`，若存在且有效（未过期），更新访问时间戳，返回结果。
    3. 若未命中，使用 `CacheLoader` 加载数据，通过 `loadAsync` 处理并发加载（避免重复加载同一 key）。
- **缓存淘汰（Eviction）**：
    - **基于容量**：`LocalCache` 维护权重总和，插入时检查是否触发淘汰（LRU 队列维护访问顺序）。
    - **基于时间**：通过 `accessTime`/`writeTime` 时间戳，惰性清理（在写操作或读操作时检查过期）。
- **并发控制**：
    - `Segment` 继承 `ReentrantLock`，写操作加锁，读操作使用 `volatile` 变量 + 无锁读。

---

### **5. 设计精髓提炼**
- **分段锁机制**：通过 `Segment` 拆分锁，提升高并发下的吞吐量（类似 `ConcurrentHashMap`）。
- **惰性清理优化**：不依赖后台线程，而是结合读写操作清理过期条目，减少开销。
- **高效的 LRU 近似算法**：通过维护访问队列（`accessQueue`/`writeQueue`），模拟 LRU 行为，而非严格排序。
- **引用类型支持**：利用弱引用（`WeakReference`）和软引用（`SoftReference`）自动回收键值，防止内存泄漏。
- **加载原子性**：通过 `LoadingValueReference` 标记加载状态，确保并发下同一 key 仅加载一次。

---

### **6. 调试与验证**
- **阅读单元测试**：查看 `guava-tests` 中 `com.google.common.cache` 的测试类（如 `LocalCacheTest`），通过测试用例理解模块行为。
- **调试关键场景**：例如缓存未命中时的加载过程、并发更新时的锁竞争、淘汰策略的触发条件。

---

### **7. 辅助工具与技巧**
- **绘制类图**：用工具（如 IntelliJ IDEA 的 Diagram）生成 `LocalCache` 及其内部类的关系图。
- **日志与日志**：在代码中添加临时日志，观察缓存操作时的内部状态变化。
- **参考文档**：结合 [Guava 官方文档](https://github.com/google/guava/wiki/CachesExplained) 和源码注释理解设计意图。

---

### **8. 重点代码片段**
- **`LocalCache.Segment#getEntry`**：处理哈希冲突和链表/红黑树查找。
- **`LocalCache.Segment#scheduleRefresh`**：刷新过期值的逻辑。
- **`LocalCache#removeEntry`**：淘汰条目的触发条件（如权重超限）。

---

通过以上步骤，逐步深入源码细节，重点关注并发控制、数据结构和清理策略的实现，最终掌握 Guava Cache 的设计精髓。