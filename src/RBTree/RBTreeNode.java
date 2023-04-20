package RBTree;

import java.util.Objects;

// Нода КЧ дерева
public class RBTreeNode {
    private final boolean RED = false;
    private final boolean BLACK = true;
    private int key;
    private boolean color;
    private RBTreeNode left;
    private RBTreeNode right;
    private RBTreeNode parent;

    // Конструктор класса
    public RBTreeNode(int key) {
        this.key = key;
        this.color = RED;
    }

    // Гетттеры + сеттеры полей класса
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public RBTreeNode getLeft() {
        return left;
    }

    public void setLeft(RBTreeNode left) {
        this.left = left;
    }

    public RBTreeNode getRight() {
        return right;
    }

    public void setRight(RBTreeNode right) {
        this.right = right;
    }

    public RBTreeNode getParent() {
        return parent;
    }

    public void setParent(RBTreeNode parent) {
        this.parent = parent;
    }
// Вывод ноды дерева
    @Override
    public String toString() {
        return  (char)27 + "[37m" +
                "RBTreeNode{" +
                (color ? (char)27 + "[37m": (char)27 + "[31m") +
                "key=" + key +
                ", color=" + (color ? "Black": "Red") +
                ", parent key=" + (parent != null ? parent.key: "null") +
                ", left key=" + (left != null ? left.key: "null") +
                ", right key=" + (right != null ? right.key: "null") +
                (char)27 + "[37m" +
                '}';
    }
}
