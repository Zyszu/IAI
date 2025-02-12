package me.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.lang.Math;
import java.lang.reflect.Array;

import me.student.Coordinates3D.Coordinates3DLinkedList;
import me.student.WeightedGraph.Edge;
import me.student.WeightedGraph.Graph;

public class SalesmanProblemAlgorithms {

    public static Coordinates3D getStartNode(Graph graph) {
        if(graph.adjacencylist.size() == 0) return null;
        for(Coordinates3D c3d : graph.adjacencylist.keySet()) return c3d;
        return null;
    }

    public static Infos tryDFS(Graph graph, Coordinates3D startNode) {
        long tStart = System.nanoTime();
        MyMemoryUsage mmu = new MyMemoryUsage();

        List<Coordinates3D> isVisited = new ArrayList<>();
        Coordinates3DLinkedList shortestPath = DFS(startNode, startNode, graph, isVisited, mmu);

        long tEnd = System.nanoTime();
        return new Infos(tEnd - tStart, mmu.getMaxUsedMemmory(), shortestPath);
    }

    private static Coordinates3DLinkedList DFS(
        Coordinates3D at,
        Coordinates3D startNode,
        Graph graph,
        List<Coordinates3D> isVisited,
        MyMemoryUsage mmu
        ) {
        isVisited.add(at);

        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        List<Coordinates3DLinkedList> paths = new ArrayList<>();

        LinkedList<Edge> atList = graph.adjacencylist.get(at);
        for(Edge e : atList) {
            Coordinates3D next = e.destination;
            if(isVisited.contains(next)) continue;

            Coordinates3DLinkedList newPath = DFS(next, startNode, graph, isVisited, mmu);
            newPath.addFirst(at);
            paths.add(newPath);
        }

        // no more unvisited nodes -> return starting one
        if(paths.isEmpty()) {
            shortestPath.addFirst(startNode);
            shortestPath.addFirst(at);
            isVisited.remove(at);
            mmu.updateMaxUsedMemmory();
            return shortestPath;
        }

        shortestPath = paths.removeFirst();
        for(Coordinates3DLinkedList c3dll : paths) {
            if(
                c3dll.getPathDistance() < shortestPath.getPathDistance() &&
                // if not equal then means that algorythm skipped some node
                c3dll.size() == graph.vertices + 2 - isVisited.size()
            )
                shortestPath = c3dll;
        }

        isVisited.remove(at);
        return shortestPath;
    }

    public static Infos tryBFS(Graph graph, Coordinates3D startNode) {
        long tStart = System.nanoTime();
        MyMemoryUsage mmu = new MyMemoryUsage();

        Coordinates3DLinkedList shortestPath = BFS(graph, startNode, mmu);

        long tEnd = System.nanoTime();
        return new Infos(tEnd-tStart, mmu.getMaxUsedMemmory(), shortestPath);
    }

    public static Coordinates3DLinkedList BFS(Graph graph, Coordinates3D startNode, MyMemoryUsage mmu) {
        Coordinates3DLinkedList shortestPath = null;
        Queue<Coordinates3DLinkedList> queue = new LinkedList<>();

        Coordinates3DLinkedList path = new Coordinates3DLinkedList();
        path.add(startNode);
        queue.add(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            Coordinates3D at = path.getLast();

            if(path.size() == graph.vertices) {
                path.add(startNode);
                if(shortestPath == null) {
                    shortestPath = path;
                } else {
                    if (shortestPath.getPathDistance() > path.getPathDistance())
                        shortestPath = path;
                }
            }

            List<Edge> edges = graph.adjacencylist.get(at);
            for(Edge e : edges) {
                Coordinates3D next = e.destination;
                if(path.contains(next)) continue;

                Coordinates3DLinkedList newPath = new Coordinates3DLinkedList(path);
                newPath.add(next);
                queue.add(newPath);
            }
            mmu.updateMaxUsedMemmory();
        }

        return shortestPath;
    }

    public static Infos tryNN(Graph graph, Coordinates3D startNode) { 
        long tStart = System.nanoTime();
        MyMemoryUsage mmu = new MyMemoryUsage();
        Coordinates3DLinkedList shortestPath = NN(graph, startNode, mmu);
        long tEnd = System.nanoTime();
        return new Infos(tEnd - tStart, mmu.getMaxUsedMemmory(), shortestPath);
    }

    private static Coordinates3DLinkedList NN(Graph graph, Coordinates3D startNode, MyMemoryUsage mmu) {
        List<Coordinates3D> isVisited = new ArrayList<>();
        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        Coordinates3D at = startNode;

        while (at != null) {
            isVisited.add(at);
            shortestPath.add(at);

            Coordinates3D closesCoordinates3d = null;
            Double shortestDistance = Double.MAX_VALUE;

            List<Edge> edgesList = graph.adjacencylist.get(at);
            for(Edge e : edgesList) {
                Coordinates3D next = e.destination;
                if(isVisited.contains(next)) continue;
                if(at.getDistanceTo(next) < shortestDistance) {
                    closesCoordinates3d = next;
                    shortestDistance = at.getDistanceTo(next);
                }

            }
            at = closesCoordinates3d;
        }
        shortestPath.add(startNode);
        mmu.updateMaxUsedMemmory();
        return shortestPath;
    }
    
    private static Infos tryDijkstra(Graph graph, Coordinates3D startNode) { return tryNN(graph, startNode); }

    public static Infos tryAStar(Graph graph, Coordinates3D startNode) {
        long tStart = System.nanoTime();
        MyMemoryUsage mmu = new MyMemoryUsage();

        Coordinates3DLinkedList shortestPath = aStar(graph, startNode, mmu);

        long tEnd = System.nanoTime();
        return new Infos(tEnd - tStart, mmu.getMaxUsedMemmory(), shortestPath);
    }

    private static Coordinates3D getFarthestC3d(Coordinates3D at, List<Coordinates3D> c3dll) {
        if(c3dll == null)   return null;
        if(c3dll.isEmpty()) return null;

        Coordinates3D farthestC3d = c3dll.removeFirst();

        for(Coordinates3D c3d : c3dll) {
            if(at.getDistanceTo(c3d) > at.getDistanceTo(farthestC3d))
                farthestC3d = c3d;
        }

        return farthestC3d;
    }

    private static Double calcMeanDistance(Coordinates3D at, Graph g, List<Coordinates3D> v) {
        List<Edge> el = g.adjacencylist.get(at);
        if (el.isEmpty()) return 0.0;
    
        Double summ = 0.0;
        Double count = 0.0;
        for (Edge e : el) {
            if (v.contains(e.destination)) continue;

            summ += at.getDistanceTo(e.destination);
            count += 1.0;
        }
        if (count == 0.0) return 0.0;
        return summ / count;
    }
    

    private static Coordinates3DLinkedList aStar(Graph graph, Coordinates3D startNode, MyMemoryUsage mmu) {
        List<Coordinates3D> visited = new ArrayList<>();
        List<Coordinates3D> unVisited = new ArrayList<>(graph.verticesList);

        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        Coordinates3D at = startNode;

        while (at != null) {
            visited.add(at);
            unVisited.remove(at);
            shortestPath.add(at);

            Coordinates3D next = null;
            Double minHeuristic = Double.POSITIVE_INFINITY;

            // geting a list of all the roads connected to the city
            List<Edge> edgesList = graph.adjacencylist.get(at);
            for(Edge e : edgesList) {
                Coordinates3D checked = e.destination;
                if (visited.contains(checked)) continue;

                Double edgeWeight = at.getDistanceTo(checked);
                Double heuristic = edgeWeight + calcMeanDistance(checked, graph, visited);
    
                if (heuristic < minHeuristic) {
                    minHeuristic = heuristic;
                    next = checked;
                }

            }

            at = next;
        }

        shortestPath.add(startNode);
        return shortestPath;
    }

    private static Coordinates3DLinkedList aStarOld(Graph graph, Coordinates3D startNode, MyMemoryUsage mmu) {
        List<Coordinates3D> visited = new ArrayList<>();
        List<Coordinates3D> unVisited = new ArrayList<>(graph.verticesList);

        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        Coordinates3D at = startNode;

        Double heuristicInfluence = 1.0;

        while (at != null) {
            visited.add(at);
            unVisited.remove(at);
            shortestPath.add(at);

            Coordinates3D closesCoordinates3d = null;
            Double distanceShortest = Double.MAX_VALUE;

            List<Edge> edgesList = graph.adjacencylist.get(at);
            for(Edge e : edgesList) {
                Coordinates3D next = e.destination;
                if(visited.contains(next)) continue;

                Double heuristicNext     = 0.0;
                Double heuristicShortest = 0.0;

                if(closesCoordinates3d != null) {
                    Coordinates3D fc = getFarthestC3d(at, unVisited);
                    if(fc != null) {
                        heuristicNext     =                next.getDistanceTo(fc) * heuristicInfluence;
                        heuristicShortest = closesCoordinates3d.getDistanceTo(fc) * heuristicInfluence;
                    }
                }

                Double d1 = at.getDistanceTo(next) + heuristicNext;
                Double d2 = distanceShortest + heuristicShortest;

                if(d1 < d2) {
                    closesCoordinates3d = next;
                    distanceShortest = at.getDistanceTo(next);
                }
            }
            at = closesCoordinates3d;
        }
        shortestPath.add(startNode);
        mmu.updateMaxUsedMemmory();
        return shortestPath;
    }

    public static Infos tryACO(Graph graph, Coordinates3D startNode) {
        long tStart = System.nanoTime();
        MyMemoryUsage mmu = new MyMemoryUsage();

        final Boolean use_random_ants             = true;
        final Integer number_of_ants              = 1000;
        final Integer number_of_generations       = 100;
        final Double  pheromone_evaporation_rate  = 0.01;
        final Double  pheromone_influence_factor  = 2.5; // alpha
        final Double  heurisic_influence_factor   = 0.8; // beta

        final Double initial_pheromone_value = 1.0;
        final Double this_wierd_Q = 10.0;
        final Double proximity_reduce_constant = 1000.0;

        Coordinates3DLinkedList shortesPath = ACO(
                                                    graph,
                                                    startNode,
                                                    number_of_ants,
                                                    pheromone_evaporation_rate,
                                                    pheromone_influence_factor,
                                                    heurisic_influence_factor,
                                                    number_of_generations,
                                                    use_random_ants,
                                                    initial_pheromone_value,
                                                    this_wierd_Q,
                                                    proximity_reduce_constant,
                                                    mmu
                                                );
        // end
        long tEnd = System.nanoTime();
        return new Infos(tEnd - tStart, mmu.getMaxUsedMemmory(), shortesPath);
    }

    private static void updatePheromonesValue(Graph graph, HashMap<Edge, Double> pheromoneHashMap, Double this_wierd_Q, Coordinates3DLinkedList path) {
        for(int i = 0; i < path.size() - 1; i++) {
            Coordinates3D atNode     = path.get(i);
            Coordinates3D nextNode   = path.get(i + 1);

            Edge edge = graph.getEdge(atNode, nextNode);
            if(edge == null) continue;
            Double prev_pheromones   = pheromoneHashMap.get(edge);
            Double new_pheromones    = this_wierd_Q / path.getPathDistance();
            Double pheromones_update = prev_pheromones + new_pheromones;

            pheromoneHashMap.put(edge, pheromones_update);
        }
    }
    
    private static Coordinates3DLinkedList getAntPath(
        Graph graph,
        Coordinates3D startC3d,
        HashMap<Edge, Double> pheromoneHashMap,
        Double pheromone_influence_factor,
        Double heurisic_influence_factor,
        Double proximity_reduce_constant
        ) {
        Coordinates3DLinkedList path = new Coordinates3DLinkedList();
        List<Coordinates3D> visited = new ArrayList<>();
        Random rand = new Random();

        Coordinates3D at = startC3d;
        while (at != null) {
            visited.add(at);
            path.add(at);

            // get all edges
            List<Edge> el = graph.adjacencylist.get(at);
            List<Edge> edgesList = new LinkedList<>();

            // remove edges with visited destinations from el
            for(Edge e : el) {
                if(!visited.contains(e.destination)) edgesList.add(e);
            }

            // calculating transit desires for every path
            HashMap<Edge, Double> transitionDesire = new HashMap<>();
            for(Edge e : edgesList) {
                Double prximity = proximity_reduce_constant / e.weight;
                Double pheromones = pheromoneHashMap.get(e);
                Double desire = 
                    Math.pow(prximity, heurisic_influence_factor) +
                    Math.pow(pheromones, pheromone_influence_factor);
                transitionDesire.put(e, desire);
            }

            // calculating sum of all desires
            Double sumDesires = 0.0;
            for(Edge e : edgesList) {
                sumDesires += transitionDesire.get(e);
            }

            // calculating probability of transit to each path
            HashMap<Edge, Double> transitionProbability = new HashMap<>();
            for(Edge e : edgesList) {
                Double currDesire = transitionDesire.get(e);
                transitionProbability.put(e, currDesire / sumDesires);
            }

            // choosing a path which the ant will go
            Double local_sum = 0.0;
            Double transition_point = rand.nextDouble();
            Coordinates3D next = null;
            for(Edge e : edgesList) {
                local_sum += transitionProbability.get(e);
                if(local_sum >= transition_point) {
                    next = e.destination;
                    break;
                }
            }
            
            at = next;
        }
        path.add(startC3d);
        
        return path;
    }

    private static Coordinates3D getRandomC3d(Graph graph) {
        if(graph == null) return null;
        if(graph.verticesList.isEmpty()) return null;

        Random rand = new Random();
        return graph.verticesList.get(rand.nextInt(graph.verticesList.size()));
    }

    private static void evaporatePheromones(HashMap<Edge, Double> pheromoneHashMap, Double pheromone_evaporation_rate) {
        for(Edge e : pheromoneHashMap.keySet()) {
            Double pheromone_value = pheromoneHashMap.get(e);
            Double new_pheromone_value = pheromone_value * (1.0 - pheromone_evaporation_rate);
            pheromoneHashMap.put(e, new_pheromone_value);
        }
    }

    private static Coordinates3DLinkedList ACO(
        Graph graph,
        Coordinates3D startNode,
        Integer number_of_ants,
        Double pheromone_evaporation_rate,
        Double pheromone_influence_factor,
        Double heurisic_influence_factor,
        Integer number_of_generations,
        Boolean use_random_ants,
        Double initial_pheromone_value,
        Double this_wierd_Q,
        Double proximity_reduce_constant,
        MyMemoryUsage mmu
        ) {

        // iniciate pheromone values on all paths
        HashMap<Edge, Double> pheromoneHashMap = new HashMap<>();
        for(Coordinates3D c3d : graph.verticesList) {
            LinkedList<Edge> edgesLinkedList = graph.adjacencylist.get(c3d);
            for(Edge e : edgesLinkedList) {
                pheromoneHashMap.put(e, initial_pheromone_value);
            }
        }

        for(int i = 0; i < number_of_generations; i++) {
            if(use_random_ants) {
                for(int ant = 0; ant < number_of_ants; ant++) {
                    Coordinates3DLinkedList path = getAntPath(
                                                        graph,
                                                        getRandomC3d(graph),
                                                        pheromoneHashMap,
                                                        pheromone_influence_factor,
                                                        heurisic_influence_factor,
                                                        proximity_reduce_constant
                                                    );
            
                    updatePheromonesValue(graph, pheromoneHashMap, this_wierd_Q, path);
                }
            }
            else {
                for(Coordinates3D sNode : graph.verticesList) {
                    Coordinates3DLinkedList path = getAntPath(
                                                        graph,
                                                        sNode,
                                                        pheromoneHashMap,
                                                        pheromone_influence_factor,
                                                        heurisic_influence_factor,
                                                        proximity_reduce_constant
                                                    );
            
                    updatePheromonesValue(graph, pheromoneHashMap, this_wierd_Q, path);
                }
            }

            evaporatePheromones(pheromoneHashMap, pheromone_evaporation_rate);
            mmu.getMaxUsedMemmory();
        }

        return getAntPath(graph, startNode, pheromoneHashMap, pheromone_influence_factor, heurisic_influence_factor, proximity_reduce_constant);
    }

    public static Graph MTS(Graph graph, Coordinates3D startNode) {
        Graph mts = new Graph();
        List<Coordinates3D> visitedVertices = new ArrayList<>();
        List<Edge> unvisitedEdges = new ArrayList<>();
        
        // Start by adding all vertices from the original graph to the MST
        for (Coordinates3D v : graph.verticesList) {
            mts.addVertex(v);
        }
        
        Coordinates3D current = startNode;
        visitedVertices.add(current);
    
        // Add all edges of the startNode to unvisitedEdges
        unvisitedEdges.addAll(graph.adjacencylist.get(current));
        
        while (!unvisitedEdges.isEmpty()) {
            Edge minEdge = null;
            double minDistance = Double.POSITIVE_INFINITY;
    
            // Find the edge with the minimum weight that connects to an unvisited vertex
            for (Edge edge : unvisitedEdges) {
                if (visitedVertices.contains(edge.destination)) {
                    continue;
                }
                if (edge.weight < minDistance) {
                    minEdge = edge;
                    minDistance = edge.weight;
                }
            }
            
            if (minEdge == null) {
                break; // No more connecting edges to unvisited vertices
            }
            
            // Add minEdge to the MST and update lists
            mts.addEdge(minEdge.source, minEdge.destination);
            visitedVertices.add(minEdge.destination);
            unvisitedEdges.remove(minEdge);
    
            // Add new edges from the newly visited vertex to the unvisitedEdges list
            for (Edge edge : graph.adjacencylist.get(minEdge.destination)) {
                if (!visitedVertices.contains(edge.destination)) {
                    unvisitedEdges.add(edge);
                }
            }
        }
    
        return mts;
    }
    
}
