package com.example.seconddatastructureproject;

public class CursorArray<Hind extends Comparable<Hind>> {
    Node<Hind>[] cursorArray;

    public Node<Hind>[] getCursorArray() {
        return cursorArray;
    }

    public CursorArray(int capacity) {
        cursorArray=new Node[capacity];
        initialization();

    }


    public int initialization() {
        for (int i = 0; i < cursorArray.length - 1; i++)
            cursorArray[i] = new Node<>(null, i + 1);
        cursorArray[cursorArray.length - 1] = new Node<>(null, 0);
        return 0;
    }


    public int malloc() {
        int p = cursorArray[0].next;
        cursorArray[0].next = cursorArray[p].next;
        return p;
    }


    public void free(int p) {
        cursorArray[p] = new Node(null, cursorArray[0].next);
        cursorArray[0].next = p;
    }


    public boolean isNull(int l) {
        return cursorArray[l] == null;
    }


    public boolean isEmpty(int l) {
        return cursorArray[l].next == 0;
    }


    public boolean isLast(int p) {
        return cursorArray[p].next == 0;
    }


    public int createList() {
        int l = malloc();
        if (l == 0)
            System.out.println("Error: Out of space!!!");
        else
            cursorArray[l] = new Node("-", 0);
        return l;
    }


    public void insertAtHead(Hind data, int l) {
        if (isNull(l)) // list not created
            return;
        int p = malloc();
        if (p != 0) {
            cursorArray[p] = new Node(data, cursorArray[l].next);
            cursorArray[l].next = p;
        } else
            System.out.println("Error: Out of space!!!");
    }




    @Override
    public String toString() {
        String s=" ";
        for(int i=0; i<cursorArray.length; i++){
            s+=i+":" + cursorArray[i] + "\n";
        }

        return s;
    }


    public void traversList(int l) {
        System.out.print("list_"+l+"-->");
        while(!isNull(l) && !isEmpty(l)){
            l=cursorArray[l].next;
            System.out.print(cursorArray[l]+"-->");
        }
        System.out.println("null");
    }

    public void insertAtLast(Hind data, int l){
        if (isNull(l)) // list not created
            return;
        int p = malloc();
        if (p != 0) {
            int index=l;
            while(!isNull(index)&& !isLast(index)){
                index=cursorArray[index].getNext();
            }
            cursorArray[index].setNext(p);
            cursorArray[p]=new Node(data, 0);


        } else
            System.out.println("Error: Out of space!!!");

    }


    public int find(Hind data, int l){
        if(!isNull(l) && !isEmpty(l)){
            l=cursorArray[l].getNext();
            if(cursorArray[l].data.equals(data))
                return l;

            return find(data, l);
        }
        return -1; // not found
    }

    public Hind deleteFirst(int L){
        if(isNull(L))
            return null;
        if(!isEmpty(L)){
            int next = cursorArray[L].getNext();
            Hind t = cursorArray[next].getData();
            cursorArray[L].setNext(cursorArray[next].getNext());
            free(next);
            return t;

        }
        return null;

    }

    public Hind seeFirst(int L){
        if(isNull(L))
            return null;
        if(!isEmpty(L)){
            int next = cursorArray[L].getNext();
            Hind t = cursorArray[next].getData();
            return t;

        }
        return null;

    }

    public void deleteAll(int L){
        while(!isEmpty(L)){
            deleteFirst(L);

        }
    }

    public boolean hasFree(){
        return cursorArray[0].getNext()!=0;
    }

}