package me.student_258354;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import me.student_258354.my_tools.Cities;
import me.student_258354.my_tools.City;
import me.student_258354.my_tools.MyLinkedList;
import me.student_258354.my_tools.Road;
import me.student_258354.tests.Algorithms;
import me.student_258354.tests.Generators;
import me.student_258354.tests.Generators.Interval;

public class App {

    public static void main(String[] args) throws Exception {

        // Interval n = new Interval(5, 20);
        Interval n = new Interval(10);
        Interval x = new Interval(-100, 100);
        Interval y = new Interval(-100, 100);
        Interval z = new Interval(0, 50);
        Cities cities = Generators.generateRandomCities(n, x, y, z);
        cities.setCitiesAndRoadsNumber();
        cities.reduceConnectionsByPercent(20);

        Algorithms algs = new Algorithms();
        System.out.println("DFS -> " + algs.salemanProblemDFS(cities).toString());
        System.out.println("BFS -> " + algs.salemanProblemBFS(cities).toString());
        System.out.println(" NN -> " + algs.salemanProblemGreedyNN(cities).toString());
        System.out.println("DIJ -> " + algs.salemanProblemGreedyDijkstra(cities).toString());

        // MemoryMXBean mmxb = ManagementFactory.getMemoryMXBean();
        // MemoryUsage beforeHeapMemoryUsage = mmxb.getHeapMemoryUsage();
        // Integer xx[] = new Integer[10000];
        // MemoryUsage afterHeapMemoryUsage = mmxb.getHeapMemoryUsage();
        // System.out.println(afterHeapMemoryUsage.getUsed() - beforeHeapMemoryUsage.getUsed());
        


        // cities.setCitiesAndRoadsNumber();
        // System.out.println(cities.stringSummary());
        // System.out.println(
        //     "Cities reduced by: " + 
        //     cities.reduceConnectionsByPercent(20) * 100 +
        //     "%"
        // );

        // System.out.println(cities.stringSummary());


        // for(City c : cities.citiesList) {
        //     System.out.println(
        //         "City with coordinets: " +
        //         c.coordinates.toString() +
        //         " is connected with roads: "
        //     );
            
        //     for(Road r : c.roadsList) {
        //         System.out.println(
        //             "--> " +
        //             r.destination.coordinates.toString() +
        //             " --> distance: " +
        //             r.weight
        //         );
        //     }
        //     System.out.println("=========== msg end ===========");
        // }

        // System.out.println(Algorithms.salemanProblemDFS(cities).toString());

        // MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        // for(int i = 0; i < 10; i++) {
        //     linkedList.add(new MyLinkedList.Node<Integer>());
        //     linkedList.tail.data = i;
        // }

        // for(int i = 0; i < linkedList.getSize(); i++) {
        //     System.out.print(linkedList.getNode(i).data + " --> ");
        // }
        // System.out.println("");

        // linkedList.reverseMyLinkedList();

        // for(int i = 0; i < linkedList.getSize(); i++) {
        //     String s = linkedList.getNode(i) == null ? "NULL" : Integer.toString(linkedList.getNode(i).data);
        //     System.out.print(s + " --> ");
        // }
        // System.out.println("");
    }
}
