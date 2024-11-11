package me.student;

import me.student.Coordinates3D.Coordinates3DLinkedList;

public class Infos {
    private long                    time;
    private long                    memmoryConsumption;
    private Integer                 visitedCities;
    private Double                  foundPathCost;
    private Coordinates3DLinkedList path;

    public Infos(
        long time,
        long memmoryConsumption,
        Coordinates3DLinkedList path
        ) {
        this.time               = time;
        this.memmoryConsumption = memmoryConsumption;
        if(path == null) {
            this.foundPathCost      = null;
            this.visitedCities      = null;
            this.path               = null;
        }
        else {
            this.foundPathCost      = path.getPathDistance();
            this.visitedCities      = path.size();
            this.path               = path;
        }
    }

    public static String toString(Infos i) {
        return i.toString();
    }

    public String toString() {
        String s = "";
        if (path == null) {
            s +=  "found_path_cost " + String.valueOf(foundPathCost);
            s += ";finding_path_time " + String.valueOf(time);
            s += ";memmory_consumption " + String.valueOf(memmoryConsumption);
            s += ";visited_cities " + String.valueOf(visitedCities);
            s += ";FAILED_TO_FIND_A_PATH";
            return s;
        }

        s +=  "found_path_cost " + String.valueOf(foundPathCost);
        s += ";finding_path_time " + String.valueOf(time);
        s += ";memmory_consumption " + String.valueOf(memmoryConsumption);
        s += ";visited_cities " + String.valueOf(visitedCities);

        return s;
    }

    public long                     getTime() { return this.time; }
    
    public long                     getMemmoryConsumption() { return this.memmoryConsumption; }

    public Integer                  getVisitedCities() { return this.visitedCities; }

    public Double                   getFoundPathCost() { return this.foundPathCost; }

    public Coordinates3DLinkedList  getPath() { return this.path; }

}
