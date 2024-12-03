package parser;

import java.util.Map;

public class LeftRightFrequencyMap<T> {
    private final Map<T, Integer> left;
    private final Map<T, Integer> right;

    public LeftRightFrequencyMap(Map<T, Integer> left, Map<T, Integer> right) {
        this.left = left;
        this.right = right;
    }

    public Map<T, Integer> getLeft() {
        return left;
    }

    public Map<T, Integer> getRight() {
        return right;
    }

    public Integer getLeftFrequency(T key) {
        Integer value = left.get(key);
        return value == null ? 0 : value;
    }

    public Integer getRightFrequency(T key) {
        Integer value = right.get(key);
        return value == null ? 0 : value;
    }
}
