package me.student_258354.my_tools;

public class Road {
    public City destination;
    public double weight;

    public Road(City destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public String toString() {
        return "dest: " + (this.destination == null ? "NULL" : this.destination.coordinates.toString()) + 
        " distance: " + Double.toString(weight);
    }
}
