
//https://ru.wikipedia.org/wiki/%D0%9A%D1%80%D0%B0%D1%81%D0%BD%D0%BE-%D1%87%D1%91%D1%80%D0%BD%D0%BE%D0%B5_%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE

package RBTree;

import org.jetbrains.annotations.NotNull;

public class RBTree {

    // Корень дерева
    RBTreeNode root;
    private final boolean RED = false;
    private final boolean BLACK = true;

    // Возвращает ноду с ключем key
    public RBTreeNode query(int key) {
        RBTreeNode tmp = root;
        while (tmp != null) {
            if (tmp.getKey() == key)
                return tmp;
            else if (tmp.getKey() > key)
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        return null;
    }

//    Новый узел в красно-чёрное дерево добавляется на место одного из листьев, окрашивается в красный цвет и
//    к нему прикрепляется два листа (так как листья являются абстракцией, не содержащей данных, их добавление
//    не требует дополнительной операции).
//    Что происходит дальше, зависит от цвета близлежащих узлов. Заметим, что:
//
//    Свойство 3 (Все листья чёрные) выполняется всегда.
//    Свойство 4 (Оба потомка любого красного узла — чёрные) может нарушиться только при добавлении красного узла,
//    при перекрашивании чёрного узла в красный или при повороте.
//    Свойство 5 (Все пути от любого узла до листовых узлов содержат одинаковое число чёрных узлов)
//    может нарушиться только при добавлении чёрного узла, перекрашивании красного узла в чёрный (или наоборот),
//    или при повороте.
//    Примечание: Буквой N будем обозначать текущий узел (окрашенный красным).
//    Сначала это новый узел, который вставляется, но эта процедура может применяться рекурсивно к другим узлам
//    (смотрите случай 3).
//    P будем обозначать предка N, через G обозначим дедушку N,
//    а U будем обозначать дядю (узел, имеющий общего родителя с узлом P).
//    Отметим, что в некоторых случаях роли узлов могут меняться, но, в любом случае, каждое обозначение будет
//    представлять тот же узел, что и в начале.


    // Вставка ноды с ключом key
    public void insert(int key) {
        RBTreeNode node = new RBTreeNode(key);
        if (root == null) {
            // Вставка первой ноды
            root = node;
            node.setColor(BLACK);
            return;
        }
        RBTreeNode parent = root;   // Родитель
        RBTreeNode son = null;      // Потомок
        if (key <= parent.getKey()) {   // Если ключ родителя меньше или равен ключу вставляемого
            son = parent.getLeft(); // Получить левого потомока родителя
        } else {
            son = parent.getRight();    // Получить правого потомока родителя
        }
        // Поиск позиции для вставки ноды
        while (son != null) {       // Найти последнего потомока (лист) в дереве
            parent = son;
            if (key <= parent.getKey()) {
                son = parent.getLeft();
            } else {
                son = parent.getRight();
            }
        }
        if (key <= parent.getKey()) { // Добавить ноду в конец дерева (присоединить к листу)
            parent.setLeft(node);
        } else {
            parent.setRight(node);
        }
        node.setParent(parent);

        // Сохранить ноду в дереве
        insertFix(node);
    }

    // Вставка подготовленной ноды
    private void insertFix(@NotNull RBTreeNode node) {
        RBTreeNode father, grandFather;
        while ((father = node.getParent()) != null && father.getColor() == RED) { // Пока предок - красный
            grandFather = father.getParent();
            if (grandFather.getLeft() == father) {  // F - ситуация с левым сыном G, как и в предыдущем анализе
                RBTreeNode uncle = grandFather.getRight(); // получить дядю (соседа) предка
                // Установить цвета нод. При необходимости, повернуть
                if (uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if (node == father.getRight()) {
                    leftRotate(father);
                    RBTreeNode tmp = node;
                    node = father;
                    father = tmp;
                }
                setBlack(father);
                setRed(grandFather);
                rightRotate(grandFather);
            } else {                               // F - случай правого сына G, симметричная операция
                RBTreeNode uncle = grandFather.getLeft();
                if (uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if (node == father.getLeft()) {
                    rightRotate(father);
                    RBTreeNode tmp = node;
                    node = father;
                    father = tmp;
                }
                setBlack(father);
                setRed(grandFather);
                leftRotate(grandFather);
            }
        }
        setBlack(root);
    }


    // Удаление ноды с ключом key
    public void delete(int key) {
        delete(query(key));
    }

    // Удаление ноды
    private void delete(RBTreeNode node) {
        if (node == null)
            return;
        if (node.getLeft() != null && node.getRight() != null) {
            RBTreeNode replaceNode = node;
            RBTreeNode tmp = node.getRight();
            while (tmp != null) {
                replaceNode = tmp;
                tmp = tmp.getLeft();
            }
            int t = replaceNode.getKey();
            replaceNode.setKey(node.getKey());
            node.setKey(t);
            delete(replaceNode);
            return;
        }
        RBTreeNode replaceNode = null;
        if (node.getLeft() != null)
            replaceNode = node.getLeft();
        else
            replaceNode = node.getRight();

        RBTreeNode parent = node.getParent();
        if (parent == null) {
            root = replaceNode;
            if (replaceNode != null)
                replaceNode.setParent(null);
        } else {
            if (replaceNode != null)
                replaceNode.setParent(parent);
            if (parent.getLeft() == node)
                parent.setLeft(replaceNode);
            else {
                parent.setRight(replaceNode);
            }
        }
        if (node.getColor() == BLACK)
            removeFix(parent, replaceNode);

    }

    // Дополнительный цвет находится в узле
    private void removeFix(RBTreeNode father, RBTreeNode node) {
        while ((node == null || node.getColor() == BLACK) && node != root) {
            if (father.getLeft() == node) {  // S - левый сын P, как и в предыдущем анализе
                RBTreeNode brother = father.getRight();
                if (brother != null && brother.getColor() == RED) {
                    setRed(father);
                    setBlack(brother);
                    leftRotate(father);
                    brother = father.getRight();
                }
                if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
                    setRed(brother);
                    node = father;
                    father = node.getParent();
                    continue;
                }
                if (isRed(brother.getLeft())) {
                    setBlack(brother.getLeft());
                    setRed(brother);
                    rightRotate(brother);
                    brother = brother.getParent();
                }

                brother.setColor(father.getColor());
                setBlack(father);
                setBlack(brother.getRight());
                leftRotate(father);
                node = root;// вне цикла
            } else {                         // S - случай правого сына P, симметричная операция
                RBTreeNode brother = father.getLeft();
                if (brother != null && brother.getColor() == RED) {
                    setRed(father);
                    setBlack(brother);
                    rightRotate(father);
                    brother = father.getLeft();
                }
                if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
                    setRed(brother);
                    node = father;
                    father = node.getParent();
                    continue;
                }
                if (isRed(brother.getRight())) {
                    setBlack(brother.getRight());
                    setRed(brother);
                    leftRotate(brother);
                    brother = brother.getParent();
                }

                brother.setColor(father.getColor());
                setBlack(father);
                setBlack(brother.getLeft());
                rightRotate(father);
                node = root;// вне цикла
            }
        }

        if (node != null)
            node.setColor(BLACK);
    }

    // Цвет ноды - черный?
    private boolean isBlack(RBTreeNode node) {
        if (node == null)
            return true;
        return node.getColor() == BLACK;
    }

    // Цвет ноды - красный?
    private boolean isRed(RBTreeNode node) {
        if (node == null)
            return false;
        return node.getColor() == RED;
    }

    // Левый поворот
    private void leftRotate(@NotNull RBTreeNode node) {
        RBTreeNode right = node.getRight();
        RBTreeNode parent = node.getParent();
        if (parent == null) {
            root = right;
            right.setParent(null);
        } else {
            if (parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            right.setParent(parent);
        }
        node.setParent(right);
        node.setRight(right.getLeft());
        if (right.getLeft() != null) {
            right.getLeft().setParent(node);
        }
        right.setLeft(node);
    }

    // Правый поворот
    private void rightRotate(@NotNull RBTreeNode node) {
        RBTreeNode left = node.getLeft();
        RBTreeNode parent = node.getParent();
        if (parent == null) {
            root = left;
            left.setParent(null);
        } else {
            if (parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            left.setParent(parent);
        }
        node.setParent(left);
        node.setLeft(left.getRight());
        if (left.getRight() != null) {
            left.getRight().setParent(node);
        }
        left.setRight(node);
    }

    // Установить цвет ноды в черный
    private void setBlack(RBTreeNode node) {
        node.setColor(BLACK);
    }

    // Установить цвет ноды в красный
    private void setRed(RBTreeNode node) {
        node.setColor(RED);
    }

    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(RBTreeNode node) {
        if (node == null)
            return;
        inOrder(node.getLeft());
        System.out.println(node);
        inOrder(node.getRight());
    }
}

