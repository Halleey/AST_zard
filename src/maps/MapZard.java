package maps;

import java.util.Collection;
import java.util.Map;

public class MapZard {
    private final Map<Object, Object> objectMap;



    public MapZard(Map<Object, Object> objectMap) {
        this.objectMap = objectMap;
    }

    public Collection<Object> values() {
        return objectMap.values();
    }

    public void clear() {
        objectMap.clear();
    }

    public Object remove(Object key) {
        return objectMap.remove(key);
    }

    public Object get(Object key) {
        return objectMap.get(key);
    }

    public Object put(Object key, Object value) {
        return objectMap.put(key, value);
    }

    public int size() {
        return objectMap.size();
    }

    public boolean containsKey(Object key) {
        return objectMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return objectMap.containsValue(value);
    }
}
