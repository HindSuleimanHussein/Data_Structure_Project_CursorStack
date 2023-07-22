package com.example.seconddatastructureproject;

public class Node<Hind extends Comparable<Hind>> {
    Hind data;
    int next;

    public Node(Hind data, int next) {
        this.data = data;
        this.next = next;
    }

    public void setData(Hind data) {
        this.data = data;
    }

    public Hind getData() {
        return data;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String toString() {
        return "["+ data+","+next+"]";
    }
}