package com.grupo4D.sag_system.model.algorithm.pathFinding;

import com.grupo4D.sag_system.model.algorithm.Mapa;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Node {
    private int[] state; //Pos X Y del nodo
    private Node parent;
    private int path_cost;
    private int timeStep;

    public Node(int x, int y, Node parent, int p_cost, int t) {
        this.state = new int[2];
        this.state[0] = x;
        this.state[1] = y;
        this.timeStep = t;
        this.parent = parent;
        this.path_cost = p_cost;
    }

    public int getPath_cost() {
        return path_cost;
    }

    public int[] getState() {
        return state;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public Node getParent() {
        return parent;
    }

    public ArrayList<Node> expand(AstarSearch astar, double nuVelocity, LocalDateTime startDate){
        Mapa mapa = astar.getMapa();
        ArrayList<Node> children = new ArrayList<>();
        int x_pos = state[0];
        int y_pos = state[1];
        long nanos = 1000000000;
        long spentTime;
        spentTime = (long)(((this.timeStep + 1) * 1000 / nuVelocity) * nanos);
        LocalDateTime dateTemp = startDate.plusNanos(spentTime);
        //Izquierda
        if(x_pos - 1 >= 0){
            children.add(new Node(x_pos-1,y_pos,this,
                    astar.cost_function(new int[]{x_pos-1,y_pos}, path_cost, dateTemp),
                    this.timeStep+1));
        }
        //Derecha
        if(x_pos + 1 < mapa.getWidth()){
            children.add(new Node(x_pos+1,y_pos,this,
                    astar.cost_function(new int[]{x_pos+1,y_pos}, path_cost, dateTemp),
                    this.timeStep+1));
        }
        //Arriba
        if(y_pos - 1 >= 0){
            children.add(new Node(x_pos,y_pos-1,this,
                    astar.cost_function(new int[]{x_pos,y_pos-1}, path_cost, dateTemp),
                    this.timeStep+1));
        }
        //Abajo
        if(y_pos + 1 < mapa.getHeight()){
            children.add(new Node(x_pos,y_pos+1,this,
                    astar.cost_function(new int[]{x_pos,y_pos+1}, path_cost, dateTemp),
                    this.timeStep+1));
        }

        return children;
    }
}
