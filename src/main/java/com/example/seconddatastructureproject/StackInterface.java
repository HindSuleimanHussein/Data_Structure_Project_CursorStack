package com.example.seconddatastructureproject;

public interface StackInterface<Hind> {
    void push(Hind data);
    Hind pop();
    Hind peek();
    boolean isEmpty();
    void clear();

}