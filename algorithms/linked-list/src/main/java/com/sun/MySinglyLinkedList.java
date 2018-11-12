package com.sun;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Description: 单链表<br/>
 * Date: 2018-11-03
 *
 * @author Sun
 */
@Data
public class MySinglyLinkedList<E> {

    private Node<E> first;
    private Node<E> last;


    public MySinglyLinkedList add(E... es) {
        for (int i = 0; i < es.length; i++) {
            add(es[i]);
        }
        return this;
    }


    public MySinglyLinkedList add(E e) {
        Node node = new Node(e);
        if (first == null) {
            first = node;
        } else if (last == null) {
            first.next = node;
            last = node;
        } else {
            last.next = node;
            last = node;
        }
        return this;
    }

    public List<E> values() {
        List<E> list = Lists.newArrayList();
        Node current = first;
        while (current != null) {
            list.add((E) current.value);
            current = current.next;
        }
        return list;
    }


    public void print() {
        List<E> values = this.values();

        System.out.println(values);
    }

    @Override
    public String toString() {
        return values().toString();
    }

    /**
     * @param <E>
     */
    @Data
    private class Node<E> {
        private Node<E> next;
        private E value;

        public Node(E e) {
            value = e;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    '}';
        }
    }


    public MySinglyLinkedList<E> reverse() {
        Node pre, current, next;
        if (first != null && first.next != null) {
            Node first = this.first;
            Node last = this.last;
            this.last = first;
            this.first = last;

            // 从第二个开始
            current = first.next;
            pre = first;

            // 第一个下一个节点指向null
            first.next = null;

            while (current != null) {
                // 暂存下一个节点
                next = current.next;
                // 反向
                current.next = pre;

                // 指针指向下一个节点
                pre = current;
                current = next;
            }
        }
        return this;
    }


}
