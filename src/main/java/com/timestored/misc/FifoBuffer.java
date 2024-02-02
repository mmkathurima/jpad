package com.timestored.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class FifoBuffer<E> {
    private final LinkedList<E> linkList;
    private final int limit;

    public FifoBuffer(int limit) {
        this.limit = limit;
        this.linkList = new LinkedList<E>();
    }

    public void addAll(Collection<E> items) {
        for (E e : items) {
            add(e);
        }
    }


    public void add(E e) {
        int p = this.linkList.lastIndexOf(e);

        if (p == -1) {
            if (this.linkList.size() == this.limit) {
                this.linkList.removeLast();
            }
        } else {

            this.linkList.remove(p);
        }
        this.linkList.addFirst(e);
    }

    public List<E> getAll() {
        return new ArrayList<E>(this.linkList);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\FifoBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */