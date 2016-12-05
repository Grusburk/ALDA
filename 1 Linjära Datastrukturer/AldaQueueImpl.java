// Mattin Lotfi matt@lotfi.se
package com.company;

import org.w3c.dom.traversal.NodeIterator;

import java.util.*;

public class AldaQueueImpl implements ALDAQueue {
    private Node firstInList;
    private final int maximumCapacity;
    private int numberOfElementsInList = 0;
    private int nrOfMovedNodes;

    public AldaQueueImpl(int maximumCapacity) {
        if (maximumCapacity > 0){
            this.maximumCapacity = maximumCapacity;
        } else {
            throw new IllegalArgumentException("Capacity has to be bigger then zero");
        }
    }

    @Override
    public void add(Object element) {
            if (element == null) {
                throw new NullPointerException();
            }
            if (firstInList == null) {
                firstInList = new Node(element);
                numberOfElementsInList++;
            } else if (numberOfElementsInList < maximumCapacity){
                Node head = firstInList;
                while (firstInList.hasNextInList()) {
                    firstInList = firstInList.getNextInList();
                }
                firstInList.setNextInList(new Node(element));
                numberOfElementsInList++;
                firstInList = head;
            }else{
                throw new IllegalStateException("You have reached maxCapacity");
        }
    }

    @Override
    public void addAll(Collection c) {
        for (Object cs : c) {
            add(cs);
        }
    }

    @Override
    public Object remove() {
        Node removeFirstInList = firstInList;
        if (numberOfElementsInList <= 0 || firstInList == null){
            throw new NoSuchElementException("There aint no such element");
        }else {
            System.out.print("");
            firstInList = firstInList.getNextInList();
            numberOfElementsInList--;
            return removeFirstInList.getData();
        }
    }

    @Override
    public Object peek() {
        if (firstInList != null){
            return firstInList.getData();
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        numberOfElementsInList = 0;
        firstInList = null ;
    }

    @Override
    public int size() {
        return numberOfElementsInList;
    }

    @Override
    public boolean isEmpty() {
            return numberOfElementsInList == 0;
    }

    @Override
    public boolean isFull() {
        return numberOfElementsInList == maximumCapacity;
    }

    @Override
    public int totalCapacity() {
        return maximumCapacity;
    }

    @Override
    public int currentCapacity() {
        return maximumCapacity - numberOfElementsInList;
    }

    //Anledningen till att jag skapar upp lastNodeInList här är för att jag tidigare inte tänkt att den har behövde den för att underlätta något.
    @Override
    public int discriminate(Object o) {
        nrOfMovedNodes = 0;
        if (o == null){
            throw new NullPointerException();
        } else if (firstInList == null){
            return nrOfMovedNodes;
        }else {
            Node nodeToMove = firstInList;
            Node lastInList = firstInList;
            Node nextNode = null;
            Node formerNode = null;
            Node nodeToStopAt = null;

            while(lastInList.hasNextInList()){
                lastInList = lastInList.getNextInList();
            }
            do {
                if (nodeToMove.getData().equals(o)){
                    if (nodeToStopAt == null){
                        nodeToStopAt = nodeToMove;
                    }
                    if (nodeToMove.hasNextInList()){
                        if (formerNode != null){
                            formerNode.setNextInList(nodeToMove.getNextInList());
                        } else {
                            firstInList = nodeToMove.getNextInList();
                        }
                    }
                    nextNode = nodeToMove.getNextInList();
                    lastInList.setNextInList(nodeToMove);
                    lastInList = nodeToMove;
                    lastInList.setNextInList(null);
                    nodeToMove = nextNode;
                    nrOfMovedNodes ++;
                }else if (o == null){
                    throw new NullPointerException();
                }else {
                    formerNode = nodeToMove;
                    nodeToMove = nodeToMove.getNextInList();
                }
            } while(nodeToMove != null && nodeToMove != nodeToStopAt);
            return nrOfMovedNodes;
        }
    }

    @Override
    public Iterator iterator() {
        return new NodeIterator(firstInList);
    }

    @Override
    public String toString() {
        String temp = "";
        if (firstInList != null) {
            temp += firstInList.toString();
            Node temporaryNode = firstInList;
            while (firstInList.hasNextInList()) {
                firstInList = firstInList.getNextInList();
                temp += ", " + firstInList.toString();
            }
            firstInList = temporaryNode;
        }
        return "[" + temp + "]";
    }

    public class NodeIterator implements Iterator {
        private Node current;
        private Node it;

        public NodeIterator(Node current) {
            it = new Node();
            it.setNextInList(current);
        }

        @Override
        public boolean hasNext() {
            if (it != null) {
                return it.hasNextInList();
            }
            throw new NoSuchElementException("There aint no such element");
        }

        @Override
        public Object next() {
            if (it != null && hasNext()){
                Object data = it.getNextInList().getData();
                it = it.getNextInList();
                return data;
            }
            throw new NoSuchElementException("There aint no such element");
        }
    }
}
