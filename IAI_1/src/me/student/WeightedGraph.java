package me.student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WeightedGraph {

    static class Edge {
        Coordinates3D source;
        Coordinates3D destination;
        Double weight;

        public Edge(Coordinates3D source, Coordinates3D destination, Double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Graph {
        public int vertices;
        public HashMap<Coordinates3D, LinkedList<Edge>> adjacencylist;
        public List<Coordinates3D> verticesList;

        Graph() {
            this.vertices = 0;
            adjacencylist = new HashMap<>();
            verticesList  = new ArrayList<>();
        }

        Graph(int vertices, Boolean isSymetrical, long seed) {
            this.vertices = vertices;
            adjacencylist = new HashMap<>();
            verticesList  = new ArrayList<>();
            //initialize adjacency lists for all the vertices

            for (int i = 0; i < vertices ; i++) {

                Generators.Interval x = new Generators.Interval(-100, 100);
                Generators.Interval y = new Generators.Interval(-100, 100);
                Generators.Interval z = new Generators.Interval(0, 50);

                Coordinates3D newC3d = Generators.getRandomCoordinates3d(x, y, z, isSymetrical, seed++);

                adjacencylist.put(
                    newC3d,
                    new LinkedList<>()
                );

                verticesList.add(newC3d);
            }

            for(Coordinates3D c3D : adjacencylist.keySet()) {
                for(Coordinates3D cord : adjacencylist.keySet()) {
                    if(c3D == cord) continue;
                    LinkedList<Edge> ll = adjacencylist.get(c3D);
                    ll.add(new Edge(c3D, cord, c3D.getDistanceTo(cord)));
                }
            }

        }

        public boolean addVertex(Coordinates3D c3d) {
            if(c3d == null) return false;

            boolean status = verticesList.add(c3d);
            if( status ) vertices++;
            return status;
        }

        public void addEdge(Coordinates3D source, Coordinates3D destination, Double weight) {
            Edge edge = new Edge(source, destination, weight);
            if(!adjacencylist.keySet().contains(source))
                adjacencylist.put(source, new LinkedList<>());

            adjacencylist.get(source).add(edge);
        }

        public void addEdge(Coordinates3D source, Coordinates3D destination) {
            Edge edge = new Edge(source, destination, source.getDistanceTo(destination));
            if(!adjacencylist.keySet().contains(source))
                adjacencylist.put(source, new LinkedList<>());
                
            adjacencylist.get(source).add(edge);
        }

        public Edge getEdge(Coordinates3D source, Coordinates3D destination) {
            List<Edge> edgesLinkedList = adjacencylist.get(source);
            for(Edge e : edgesLinkedList) {
                if(e.destination == destination) return e;
            }

            return null;
        }

        /**
         * Reduces the edges but always keeps at least one edge
         * for each node. Returns percent of all reduced eges.
         */
        public Double reduceEdges(Integer percent, long seed) {
            if(percent < 0 || percent > 100) return null;
            if(adjacencylist.isEmpty()) return null;
            Random rand = new Random(seed);

            Integer countEdges = 0;

            for(Coordinates3D c3D : adjacencylist.keySet()) {
                LinkedList<Edge> edgesList = adjacencylist.get(c3D);
                countEdges += edgesList.size();
            }

            final Integer triedToRemove = countEdges * percent / 100;
            Integer toBeRemoved = triedToRemove;

            List<LinkedList<Edge>> removingForbidden = new ArrayList<>();
            
            while (removingForbidden.size() != this.vertices && toBeRemoved != 0) {
                for(Coordinates3D c3D : adjacencylist.keySet()) {
                    if(toBeRemoved == 0) break;
                    LinkedList<Edge> edgesList = adjacencylist.get(c3D);
                    if(removingForbidden.contains(edgesList)) continue;
                    edgesList.remove(rand.nextInt(edgesList.size()));
                    toBeRemoved--;
                    if(edgesList.size() < 2) removingForbidden.add(edgesList);
                }
            }

            return (triedToRemove - toBeRemoved) / Double.valueOf(countEdges) * 100;
        }
    
        /**
         * Prints all conections existing betwen nodes.
         * Be careful when number of vertices is big :)
         */
        public void printGraph(){
            for (Coordinates3D c3D : adjacencylist.keySet()) {
                LinkedList<Edge> list = adjacencylist.get(c3D);
                for (int i = 0; i < list.size() ; i++) {
                    System.out.println("vertex -> " + list.get(i).source.toString() + " is connected to " +
                            list.get(i).destination.toString() + " with weight " +  list.get(i).weight);
                }
            }
        }

    }

}
