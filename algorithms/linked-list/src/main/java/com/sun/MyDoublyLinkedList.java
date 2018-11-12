package com.sun;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Description: 双向链表 <br/>
 * Date: 2018-11-03
 *
 * @author Sun
 */
@Data
public class MyDoublyLinkedList<E> {

    private Node<E> first;
    private Node<E> last;

    public E getFirstValue() {
        if (this.first != null) {
            return this.first.value;
        }
        return null;
    }

    public MyDoublyLinkedList add(E... es) {
        for (int i = 0; i < es.length; i++) {
            add(es[i]);
        }
        return this;
    }


    public MyDoublyLinkedList add(E e) {
        Node node = new Node(e);
        if (first == null) {
            first = node;
        } else if (last == null) {
            node.prev = first;
            first.next = node;
            last = node;
        } else {
            node.prev = last;
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
        private Node<E> prev;
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


    public MyDoublyLinkedList<E> reverse() {
        Node current = first;
        if (current.getNext() != null) {
            Node first = this.first;
            Node last = this.last;
            this.last = first;
            this.first = last;

            while (current != null) {
                Node currentNext = current.getNext();
                reverseNode(current);
                current = currentNext;
            }
        }
        return this;
    }

    private void reverseNode(Node node) {
        Node prev = node.prev;
        node.prev = node.next;
        node.next = prev;
    }


}
