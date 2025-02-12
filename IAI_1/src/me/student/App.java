package me.student;

import java.io.BufferedWriter;
import java.io.FileWriter;

import me.student.WeightedGraph.Graph;

public class App {
    public static void main(String[] args) throws Exception {
        final Boolean isSymetrical = true;
        final long seed            = 2115;
        final Boolean saveToFile   = false;
        String fileName            = isSymetrical ? "data_symetrical.txt" : "data_asymetrical.txt";
        BufferedWriter writer      = new BufferedWriter(new FileWriter(fileName));
        
        for(int i = 5; i < 21; i++) {
            String msg = "number_of_vertices = " + String.valueOf(i) + " | seed = " + String.valueOf(seed) + '\n';
            System.out.print(msg);
            if(saveToFile) writer.append(msg);

            Graph g = new Graph(i, isSymetrical, seed);
            Coordinates3D startNode = SalesmanProblemAlgorithms.getStartNode(g);

            if(!isSymetrical)
                g.reduceEdges(20, seed) ;
                // System.out.println("graph edges reduced by: " + g.reduceEdges(20) + "%");
            // g.printGraph();

            // Because it is always taking the shortest path it
            // not always retruns all the nodes. The last node 
            // it's visiting might not be connected to some
            // remaning nodes.
            Infos nn = SalesmanProblemAlgorithms.tryNN(g, startNode);
            msg = " NN -> " + nn.toString() + '\n';
            System.out.print(msg);
            if(saveToFile) writer.append(msg);

            Infos aStar = SalesmanProblemAlgorithms.tryAStar(g, startNode);
            msg = " A* -> " + aStar.toString() + '\n';
            System.out.print(msg);
            if(saveToFile) writer.append(msg);

            // Infos aco = SalesmanProblemAlgorithms.tryACO(g, startNode);
            // msg = "ACO -> " + aco.toString() + '\n';
            // System.out.print(msg);
            // if(saveToFile) writer.append(msg);

            // if(
            //     (g.vertices > 13 && !isSymetrical) ||
            //     (g.vertices > 12 && isSymetrical)
            // ) {
            //     msg = "DFS -> no data (too big memory consumption or processing power is needed)\n";
            //     System.out.print(msg);
            //     if(saveToFile) writer.append(msg);
            // } else {
            //     // [ToDo]
            //     // Something is wrong with DFS method. Sometime it
            //     // retruns path that is longer than BFS method
            //     Infos dfs = SalesmanProblemAlgorithms.tryDFS(g, startNode);
            //     msg = "DFS -> " + dfs.toString() + '\n';
            //     System.out.print(msg);
            //     if(saveToFile) writer.append(msg);
            // }

            // if(
            //     (g.vertices > 12 && !isSymetrical) ||
            //     (g.vertices > 11 && isSymetrical)
            // ) {
            //     msg = "BFS -> no data (too big memory consumption or processing power is needed)\n";
            //     System.out.print(msg);
            //     if(saveToFile) writer.append(msg);
            // } else {
            //     // just works :)
            //     Infos bfs = SalesmanProblemAlgorithms.tryBFS(g, startNode);
            //     msg = "BFS -> " + bfs.toString() + '\n';
            //     System.out.print(msg);
            //     if(saveToFile) writer.append(msg);
            // }
        }

        writer.close();
    }
}
