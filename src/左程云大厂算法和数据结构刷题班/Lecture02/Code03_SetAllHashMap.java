package 左程云大厂算法和数据结构刷题班.Lecture02;

import java.util.HashMap;

public class Code03_SetAllHashMap {

  /*
   * 设计有setAll功能的哈希表，put、get、setAll方法，时间复杂度O(1)
   * */

  public static class MyValue<V> {
    public V value;
    public long time;

    public MyValue(V v, long t) {
      this.value = v;
      this.time = t;
    }
  }

  public static class MyHashMap<K, V> {

    private HashMap<K, MyValue<V>> map;
    private long time;
    private MyValue<V> setAll;

    public MyHashMap() {
      this.map = new HashMap<>();
      this.time = 0;
      this.setAll = new MyValue<V>(null, -1);
    }

    public void put(K key, V value) {
      map.put(key, new MyValue<>(value, time++));
    }

    public void setAll(V value) {
      setAll = new MyValue<>(value, time++);
    }

    public V get(K key) {
      if (!map.containsValue(key)) {
        return null;
      }
      if (map.get(key).time > setAll.time) {
        return map.get(key).value;
      } else {
        return setAll.value;
      }
    }
  }

}
