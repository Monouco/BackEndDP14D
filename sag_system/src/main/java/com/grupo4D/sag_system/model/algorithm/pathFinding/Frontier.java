package com.grupo4D.sag_system.model.algorithm.pathFinding;

import java.util.ArrayList;

public class Frontier {
    private ArrayList<Node> cola;
    private ArrayList<int []> states;


    public Frontier(Node n) {
        cola = new ArrayList<>();
        states = new ArrayList<>();
        addNode(n);
    }

    public ArrayList<Node> getCola() {
        return cola;
    }

    public ArrayList<int[]> getStates() {
        return states;
    }

    public void addNode(Node n){
        int cost = n.getPath_cost();
        int i;

        //Encuentra la posicion
        for(i=0; i < cola.size(); i++){
            if(cost < cola.get(i).getPath_cost())
                break;
        }
        cola.add(i,n);
        states.add(i,n.getState());
    }

    public Node popNode(){
        Node n = cola.get(0);
        cola.remove(0);
        states.remove(0);
        return n;
    }

    public void replace(Node n, int index){
        //Remueve el nodo y lo vuelve a insertar, de manera ordenada
        this.cola.remove(index);
        this.states.remove(index);
        this.addNode(n);
    }

}
