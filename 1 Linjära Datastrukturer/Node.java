// Mattin Lotfi matt@lotfi.se

package com.company;

/**
 * Created by Mattin on 2016-01-24.
 */
public class Node {
    private Object data;
    private Node nextInList;

    public Node() {

    }

    public Object getData() {
        return data;
    }

    public Node(Object data) {
        this.data = data;
    }

    public Node getNextInList() {
        return nextInList;
    }

    public void setNextInList(Node nextInList) {
        this.nextInList = nextInList;
    }

    public boolean hasNextInList () {
        return nextInList != null;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}