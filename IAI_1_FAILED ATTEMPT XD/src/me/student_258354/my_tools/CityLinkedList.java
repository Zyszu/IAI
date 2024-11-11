package me.student_258354.my_tools;

public class CityLinkedList extends MyLinkedList<City> {

    public CityLinkedList(MyLinkedList<City> myLinkedList) {
        this.head = myLinkedList.head;
        this.tail = myLinkedList.tail;
    }

    public static Double getTotalDistance(MyLinkedList<City> myLinkedList) {
        if(myLinkedList == null)        return 0.0;
        if(myLinkedList.getSize() <= 1) return 0.0;

        Double distance = 0.0;

        MyLinkedList.Node<City> at = myLinkedList.head;
        while (at.next != null) {
            distance += at.data.coordinates.getDistance(
                at.next.data.coordinates
            );
            at = at.next;
        }
        
        return distance;
    }

}
