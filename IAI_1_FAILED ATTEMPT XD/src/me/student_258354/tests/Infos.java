package me.student_258354.tests;

public class Infos {
    public Double  foundPathCost;
    public long     time;
    public long     memmoryConsumption;
    public long     visitedCities;

    public Infos(Double foundPathCost, long time, long memmoryConsumption, long visitedCities) {
        this.foundPathCost      = foundPathCost;
        this.time               = time;
        this.memmoryConsumption = memmoryConsumption;
        this.visitedCities      = visitedCities;
    }

    public static String toString(Infos i) {
        return i.toString();
    }

    public String toString() {
        String s = "";
        s +=  "fpc = " + String.valueOf(foundPathCost);
        s += "; time = " + String.valueOf(time) + "ms";
        s += "; mc = " + String.valueOf(memmoryConsumption) + "bits";
        s += "; ct = " + String.valueOf(visitedCities) + ";";

        return s;
    }
}
