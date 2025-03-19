package lists;

import java.util.ArrayList;
import java.util.List;

public class ZardList {
    private final List<Object> elements;

    public ZardList() {
        this.elements = new ArrayList<>();
    }

    // 🔹 Novo construtor que aceita uma lista
    public ZardList(List<Object> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public void add(Object value) {
        elements.add(value);
    }

    public Object get(int index) {
        return elements.get(index);
    }

    public void remove(int index) {
        elements.remove(index);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public void clear() {
        elements.clear();
    }
}

