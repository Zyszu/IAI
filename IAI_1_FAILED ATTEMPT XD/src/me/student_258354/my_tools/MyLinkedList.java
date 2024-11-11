package me.student_258354.my_tools;

public class MyLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    private Integer size;

    public static class Node<T> {
        public T data;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public Node() {
            this.data = null;
            this.next = null;
        }
    }

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public MyLinkedList(Node<T> node) {
        this.head = null;
        this.tail = null;
        this.size = 0;

        add(node);
    }

    public MyLinkedList(MyLinkedList<T> mll) {
        this.head = null;
        this.tail = null;
        this.size = 0;

        Node<T> at = mll.head;
        while (at != null) {
            this.add(new Node<T>(at.data));
            at = at.next;
        }
    }

    public Node<T> add(Node<T> obj) {
        if(obj == null) return null;
        if(head == null || tail == null) {
            this.head = obj;
            this.tail = head;
            this.size++;
            return obj;
        }

        tail.next = obj;
        tail = tail.next;
        this.size++;
        return obj;
    }

    public Node<T> addFirst(Node<T> obj) {
        if(obj == null) return null;
        if(head == null || tail == null) {
            head = obj;
            tail = head;
            this.size++;
            return obj;
        }

        obj.next = this.head;
        this.head = obj;
        this.size++;
        return obj;
    }

    public Node<T> add(T objT) {
        if(objT == null) return null;
        Node<T> obj = new Node<>(objT);
        if(head == null || tail == null) {
            this.head = obj;
            this.tail = head;
            this.size++;
            return obj;
        }

        tail.next = obj;
        tail = tail.next;
        this.size++;
        return obj;
    }

    public Node<T> addFirst(T objT) {
        if(objT == null) return null;
        Node<T> obj = new Node<>(objT);
        if(head == null || tail == null) {
            head = obj;
            tail = head;
            this.size++;
            return obj;
        }

        obj.next = this.head;
        this.head = obj;
        this.size++;
        return obj;
    }

    public Integer getSize() {
        return this.size;
    }

    public Node<T> getNode(Integer index) {
        if(index < 0) return null;
        Node<T> at = head;

        for(int i=0; i < index; i++) {
            if(at == null) return null;
            at = at.next;
        }

        return at;
    }

    public void reverseMyLinkedList() {
        Node<T> prev = null;
        Node<T> at = head;
        Node<T> next = at.next;
        this.tail = at;

        while (next != null) {
            at.next = prev;
            prev = at;
            at = next;
            next = at.next;
        }

        at.next = prev;
        this.head = at;
    }

    public boolean contains(Node<T> n) {
        if(n == null) return false;
        if(this.size == 0) return false;
        
        Node<T> at = this.head;
        while (at.next != null) {
            if(at.data.equals(n.data)) return true;
            at = at.next;
        }

        return false;
    }

    public boolean contains(T nT) {
        if(nT == null) return false;
        if(this.size == 0) return false;
        
        Node<T> at = this.head;
        while (at.next != null) {
            if(at.data.equals(nT)) return true;
            at = at.next;
        }

        return false;
    }
}
