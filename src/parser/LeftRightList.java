package parser;

import java.util.List;

public class LeftRightList<T> {
    List<T> left;
    List<T> right;

    public LeftRightList(List<T> left, List<T> right) {
        this.left = left;
        this.right = right;
    }

    public List<T> getLeft() {
        return left;
    }

    public List<T> getRight() {
        return right;
    }
}
