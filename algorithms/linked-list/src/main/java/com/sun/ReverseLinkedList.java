package com.sun;

/**
 * Description: <br/>
 * Date: 2018-11-03
 *
 * @author Sun
 */
public class ReverseLinkedList {


    public static void main(String[] args) {
        MyDoublyLinkedList<Integer> list = new MyDoublyLinkedList<>();
        list.add(1, 2, 3, 7, 9, 4, 5);
        list.print();

        list.reverse();
        list.print();

        MySinglyLinkedList<Integer> list2 = new MySinglyLinkedList<>();
        list2.add(11, 21, 13, 17, 19, 14, 15);
        list2.print();

        list2.reverse();
        list2.print();


        MySinglyLinkedList<Integer> list3 = new MySinglyLinkedList<>();
        list3.add(31);
        list3.print();

        MySinglyLinkedList<Integer> reverse3 = list3.reverse();
        reverse3.print();
    }


}
