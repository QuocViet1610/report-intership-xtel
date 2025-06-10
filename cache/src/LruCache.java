import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private final int MAX_SIZE;
    private final Map<K, V> cache;

    // Constructor
    public LruCache(int maxSize) {
        this.MAX_SIZE = maxSize;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_SIZE;  // Xóa phần tử ít dùng nhất khi cache vượt quá kích thước
            }
        };
    }

    // Phương thức lấy giá trị từ cache
    public V get(K key) {
        return cache.get(key);
    }

    // Phương thức thêm dữ liệu vào cache
    public void put(K key, V value) {
        cache.put(key, value);
    }

    public static void main(String[] args) {
        LruCache<String, String> cache = new LruCache<>(3);

        cache.put("user1", "User One");
        cache.put("user2", "User Two");
        cache.put("user3", "User Three");

        System.out.println(cache.get("user1")); // Truy cập user1

        cache.put("user4", "User Four"); // Thêm vào, "user2" sẽ bị loại bỏ vì ít được truy cập

        System.out.println(cache.get("user2")); // null, đã bị loại bỏ
        System.out.println(cache.get("user3")); // User Three
        System.out.println(cache.get("user4")); // User Four
    }
}
