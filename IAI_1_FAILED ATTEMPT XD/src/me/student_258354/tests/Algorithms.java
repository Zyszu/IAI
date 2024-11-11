package me.student_258354.tests;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import me.student_258354.my_tools.Cities;
import me.student_258354.my_tools.City;
import me.student_258354.my_tools.CityLinkedList;
import me.student_258354.my_tools.MyLinkedList;
import me.student_258354.my_tools.Road;
import me.student_258354.my_tools.MyLinkedList.Node;

public class Algorithms {

    private MemoryMXBean    mbean;
    private MemoryUsage     beforeHeapMemoryUsage;
    private MemoryUsage     maxHeapMemoryUsage;

    public  Algorithms() {
        this.mbean = ManagementFactory.getMemoryMXBean();
        statsReset();
    }

    private void statsReset() {
        this.beforeHeapMemoryUsage = mbean.getHeapMemoryUsage();
        this.maxHeapMemoryUsage = mbean.getHeapMemoryUsage();
    }

    public Infos salemanProblemDFS(Cities cities) {
        statsReset();
        long timeStart = System.currentTimeMillis();

        HashMap<City, Boolean> visited = new HashMap<>();

        MyLinkedList<City> mll = DFS(cities.startCity, visited, cities);
        long timeEnd = System.currentTimeMillis();

        if(mll == null) {
            return new Infos(0.0, timeEnd-timeStart, 0, 0);
        }

        long maxMemUsage = maxHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed();
        return new Infos(CityLinkedList.getTotalDistance(mll), timeEnd - timeStart, maxMemUsage, mll.getSize());
    }

    public MyLinkedList<City> DFS(City at, HashMap<City, Boolean> visited, Cities c) {
        if(visited.containsKey(at) || at == null) return null;
        if(visited.size() == c.getNumberOfCities() - 1) {
            MyLinkedList<City> p = new MyLinkedList<City>();
            p.add(new MyLinkedList.Node<>(c.startCity));
            p.add(new MyLinkedList.Node<>(at));

            MemoryUsage currentMemUsage = mbean.getHeapMemoryUsage();
            if(this.maxHeapMemoryUsage.getUsed() < currentMemUsage.getUsed())
            maxHeapMemoryUsage = currentMemUsage;

            return p; 
        }
        visited.put(at, true);
        List<MyLinkedList<City>> linkedLists = new ArrayList<>();

        for(Road r : at.roadsList) {
            MyLinkedList<City> temp = DFS(r.destination, visited, c);
            if(temp != null) {
                linkedLists.add(temp);
            }
        }
        visited.remove(at);
        
        if(linkedLists.size() == 0) return null;

        MyLinkedList<City> minRoute = linkedLists.get(0);
        Double min = CityLinkedList.getTotalDistance(minRoute);

        for(int i = 1; i < linkedLists.size(); i++) {
            Double tempMin = CityLinkedList.getTotalDistance(linkedLists.get(i));
            
            if(tempMin < min) {
                min = tempMin;
                minRoute = linkedLists.get(i);
            }
        }

        minRoute.addFirst(new MyLinkedList.Node<City>(at));

        return minRoute;
    }

    public Infos salemanProblemBFS(Cities cities) {
        statsReset();
        long timeStart = System.currentTimeMillis();

        Queue<City> queue = new LinkedList<>();
        List<City> visitedList = new ArrayList<>();
        
        MyLinkedList<City> mll = BFS(queue, visitedList, cities);
        long timeEnd = System.currentTimeMillis();

        if(mll == null) {
            return new Infos(0.0, timeEnd-timeStart, 0, 0);
        }

        long maxMemUsage = maxHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed();
        return new Infos(CityLinkedList.getTotalDistance(mll), timeEnd - timeStart, maxMemUsage, mll.getSize());
    }

    public MyLinkedList<City> BFS(Queue<City> q, List<City> vl, Cities c) {
        List<MyLinkedList<City>> paths = new ArrayList<>();
        Queue<MyLinkedList<City>> queue = new LinkedList<>();

        MyLinkedList<City> path = new MyLinkedList<>();
        path.add(c.startCity);
        queue.add(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            Node<City> lastCity = path.getNode(path.getSize() - 1);

            if(path.getSize() == c.citiesList.size()) {
                path.add(c.startCity);
                paths.add(path);
            }

            for(Road r : lastCity.data.roadsList) {
                City neighbor = r.destination;
                
                Boolean doIt = true;
                Node<City> att = path.head;

                while (att != null) {
                    if(att.data.coordinates.isEqual(neighbor.coordinates)) {
                        doIt = false;
                        break;
                    }
                    att = att.next;
                }

                if(doIt) {
                    MyLinkedList<City> newPath = new MyLinkedList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        maxHeapMemoryUsage = mbean.getHeapMemoryUsage();

        MyLinkedList<City> shortestPath = null;

        for(MyLinkedList<City> mll : paths) {
            if(shortestPath == null) shortestPath = mll;
            else {
                if(CityLinkedList.getTotalDistance(shortestPath) > CityLinkedList.getTotalDistance(mll)) 
                    shortestPath = mll;
            }
        }

        return shortestPath;
    }

    public Infos salemanProblemGreedyNN(Cities cities) {
        statsReset();
        List<City> visited = new ArrayList<>();
        MyLinkedList<City> ans = new MyLinkedList<>();

        City at = cities.startCity;
        if(at == null) return null;

        long tStart = System.currentTimeMillis();
        while (at != null) {
            City nearest = null;
            for(Road r : at.roadsList) {
                if(visited.contains(r.destination)) continue;
                if(nearest == null) nearest = r.destination;
                if(
                    at.coordinates.getDistance(r.destination.coordinates) <
                    at.coordinates.getDistance(nearest.coordinates)
                ) nearest = r.destination;

            }

            ans.add(new MyLinkedList.Node<City>(at));
            visited.add(at);
            at = nearest;
        }

        ans.add(new MyLinkedList.Node<City>(cities.startCity));

        long tEnd = System.currentTimeMillis();
        this.maxHeapMemoryUsage = mbean.getHeapMemoryUsage();
        long memUse = this.maxHeapMemoryUsage.getUsed() - this.beforeHeapMemoryUsage.getUsed();

        return new Infos(CityLinkedList.getTotalDistance(ans), tEnd - tStart, memUse, ans.getSize());
    }

    private City getLowestDistanceCity(Set<City> unsetteledCities, HashMap<City, Double> values) {
        City lowestDistanceCity = null;
        Double lowestDistance = Double.MAX_VALUE;

        for(City c : unsetteledCities) {
            Double cityDistance = values.get(c);
            if(cityDistance < lowestDistance)
                lowestDistanceCity = c;
            lowestDistance = cityDistance;
        }

        return lowestDistanceCity;
    }

    private void calculateMinimumDistance(City sourceNode, City checkedNode, Double distanceToCheckedNode, HashMap<City, Double> values) {
        Double sourceDistance = values.get(sourceNode);
        if(sourceDistance + distanceToCheckedNode < values.get(checkedNode))
            values.put(checkedNode, sourceDistance + distanceToCheckedNode);
    }

    public Infos salemanProblemGreedyDijkstra(Cities cities) {

        HashMap<City, Double> values = new HashMap<>();
        for(City c : cities.citiesList) values.put(c, Double.MAX_VALUE);
        City startCity = new City(cities.startCity);
        values.put(startCity, Double.MAX_VALUE);
        
        Set<City> settledNodes = new HashSet<>();
        Set<City> unsettledNodes = new HashSet<>();
        
        unsettledNodes.add(startCity);
        values.put(startCity, 0.0);

        while (!unsettledNodes.isEmpty()) {
            City currentNode = getLowestDistanceCity(unsettledNodes, values);
            unsettledNodes.remove(currentNode);
            
            for(Road r : currentNode.roadsList) {
                City checkNode = r.destination;
                Double distanceToCheckedNode = r.weight;
                if(!settledNodes.contains(checkNode)) {
                    calculateMinimumDistance(currentNode, checkNode, distanceToCheckedNode, values);
                    unsettledNodes.add(checkNode);
                }
            }

            settledNodes.add(currentNode);
        }

        MyLinkedList<City> shortestPath = new MyLinkedList<>();

        while (!values.isEmpty()) {
            City min = null;
            Double minDist = Double.MAX_VALUE;
            for(City c : values.keySet()) {
                if(values.get(c) < minDist)
                    min = c;
            }

            shortestPath.addFirst(min);
            values.remove(min);
        }

        return new Infos(CityLinkedList.getTotalDistance(shortestPath), 0, 0, 0);
    }
    
    public static Infos salemanProblemAproxIn(Cities cities)         {return null;}
    
    public static Infos salemanProblemAproxAd(Cities cities)         {return null;}
    
    public static Infos salemanProblemAproxACO(Cities cities)        {return null;}
}