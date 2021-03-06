package com.grupo4D.sag_system.model.algorithm.pathFinding;

import com.grupo4D.sag_system.model.algorithm.Mapa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class AstarSearch {
    private Mapa mapa;
    private int [] destination; //coordenadas x y del destino del pedido
    private double velocity;

    public AstarSearch(Mapa map) {
        this.mapa = map;
    }

    public AstarSearch(Mapa map, int [] dest) {
        this.mapa = map;
        destination = dest;

    }

    public Mapa getMapa() {
        return mapa;
    }

    public ArrayList<int []> astar_search(int[] start, int[]goal, LocalDateTime startDate, int inc){
        this.destination = goal;
        Frontier frontier = new Frontier(new Node(start[0], start[1], null, 0, 0));
        ArrayList<int []> explored = new ArrayList<>();
        ArrayList<Node> children;
        Node n = null;
        Node incumbent = null;
        int index;
        ArrayList<int []> solutionPath = new ArrayList<>();
        int [] auxChildState;

        while (!frontier.getCola().isEmpty()){
            n = frontier.popNode();
            if (goalTest(n.getState(), goal))
                break;
            //El nodo ya fue explorado
            explored.add(new int[] {n.getState()[0],n.getState()[1],n.getTimeStep()});
            //explored.add(new int[] {n.getState()[0],n.getState()[1]});
            children = n.expand(this, this.velocity, startDate, inc);
            for(Node child: children){
                auxChildState =new int[] {child.getState()[0], child.getState()[1],child.getTimeStep()};
                //auxChildState =new int[] {child.getState()[0], child.getState()[1]};
                if(!isExplored(explored,auxChildState) && !isInFrontier(frontier, auxChildState)){
                    frontier.addNode(child);
                }
                //Si ya esta en la frontera, me quedo con el camino mas corto a ese nodo
                else if(isInFrontier(frontier, auxChildState)){
                    index = findIndex(frontier, auxChildState);
                    incumbent = frontier.getCola().get(index);
                    if(child.getPath_cost() < incumbent.getPath_cost()){
                        frontier.replace(child, index);
                    }
                }
            }

        }
        solutionPath = tracePath(n);
        //System.out.println("COSTO TOTAL: " + n.getPath_cost());
        //printSolution(solutionPath);
        return solutionPath;
    }

    private static int findIndex(Frontier frontier, int [] state){
        int i;
        for(i =0; i<frontier.getCola().size(); i++){
            int [] temp = frontier.getStates().get(i);
            //if(Arrays.equals(frontier.getCola().get(i).getState(),state))
            if(temp[0] == state[0] && temp[1]== state[1] && temp[2]==state[2])
                return i;
        }
        return -1;
    }

    private static void printSolution(ArrayList<int[]> solutionPath){
        System.out.print("S -> ");
        for(int[] state: solutionPath){
            System.out.print("("+state[0] + " " + state[1] + ")" + " -> ");
        }
        System.out.println("G");
    }

    private static ArrayList<int[]> tracePath(Node n){
        ArrayList<int []> states = new ArrayList<>();
        Node aux  = n;
        while (aux.getParent()!=null){
            states.add(0,aux.getState());
            aux = aux.getParent();
        }
        return states;
    }

    private boolean isInFrontier(Frontier frontier, int [] state){

        for(int i=0; i< frontier.getStates().size(); i++){
            int [] temp = frontier.getStates().get(i);
            /*if(Arrays.equals(frontier.getStates().get(i),state))
                return true;*/
            if(temp[0] == state[0] && temp[1]== state[1] && temp[2]==state[2])
                return true;
        }
        return false;
    }

    private boolean isExplored(ArrayList<int []> explored, int [] state){
        for(int i=0; i< explored.size(); i++){
            int [] temp = explored.get(i);
            /*if(Arrays.equals(explored.get(i),state))
                return true;*/
            if(temp[0] == state[0] && temp[1]== state[1] && temp[2]==state[2])
                return true;
        }
        return false;
    }

    private boolean goalTest(int [] curr_pos, int [] goal){
        if(curr_pos[0] == goal[0] && curr_pos[1] == goal[1])
            return true;
        return false;
    }

    public int cost_function(int [] coord, int parent_cost, LocalDateTime curTime){
        char[][]m = this.mapa.getMap();
        int total_cost=parent_cost;
        int node_x = coord[0];
        int node_y = coord[1];
        double manhattan_heuristic = Math.abs(coord[0] - this.destination[0]) + Math.abs(coord[1] - this.destination[1]);
        long nanos = 1000000000;

        //Nodo bloqueado
        //if (m[node_y][node_x] == '#')
        if(mapa.isBlocked(curTime, node_x, node_y))
            total_cost+=1000000;
        else
            total_cost+=1; //Consideramos como 1 el costo por defecto, sin bloqueos
        //heuristica
        total_cost+= 2 * manhattan_heuristic;

        return total_cost;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
