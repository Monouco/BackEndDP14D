package com.grupo4D.sag_system.model.algorithm.pathFinding;

import com.grupo4D.sag_system.model.algorithm.Mapa;

import java.util.ArrayList;

public class Node {
    private int[] state; //Pos X Y del nodo
    private Node parent;
    private int path_cost;

    public Node(int x, int y, Node parent, int p_cost) {
        this.state = new int[2];
        this.state[0] = x;
        this.state[1] = y;
        this.parent = parent;
        this.path_cost = p_cost;
    }

    public int getPath_cost() {
        return path_cost;
    }

    public int[] getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public ArrayList<Node> expand(AstarSearch astar){
        Mapa mapa = astar.getMapa();
        ArrayList<Node> children = new ArrayList<>();
        int x_pos = state[0];
        int y_pos = state[1];
        //Izquierda
        if(x_pos - 1 >= 0){
            children.add(new Node(x_pos-1,y_pos,this,
                    astar.cost_function(new int[]{x_pos-1,y_pos}, path_cost)));
        }
        //Derecha
        if(x_pos + 1 < mapa.getWidth()){
            children.add(new Node(x_pos+1,y_pos,this,
                    astar.cost_function(new int[]{x_pos+1,y_pos}, path_cost)));
        }
        //Arriba
        if(y_pos - 1 >= 0){
            children.add(new Node(x_pos,y_pos-1,this,
                    astar.cost_function(new int[]{x_pos,y_pos-1}, path_cost)));
        }
        //Abajo
        if(y_pos + 1 < mapa.getHeight()){
            children.add(new Node(x_pos,y_pos+1,this,
                    astar.cost_function(new int[]{x_pos,y_pos+1}, path_cost)));
        }

        return children;
    }
}
