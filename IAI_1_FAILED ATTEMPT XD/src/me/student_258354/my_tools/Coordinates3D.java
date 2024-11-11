package me.student_258354.my_tools;

public class Coordinates3D {
    public Integer x, y, z;

    public Coordinates3D(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Boolean equals(Coordinates3D coordinates) {
        if(
            this.x == coordinates.x &&
            this.y == coordinates.y &&
            this.z == coordinates.z
        ) return true;
        return false;
    }

    public Boolean isEqual(Coordinates3D coordinates) {
        if(
            this.x == coordinates.x &&
            this.y == coordinates.y &&
            this.z == coordinates.z
        ) return true;
        return false;
    }

    public Double getDistance(Coordinates3D coordinates) {
        return Math.sqrt(
            Math.pow(x - coordinates.x, 2) +
            Math.pow(y - coordinates.y, 2) +
            Math.pow(z - coordinates.z, 2)
        );
    }

    public static String toString(Coordinates3D c) {
        return c.toString();
    }

    public String toString() {
        String s = "[";
        s += "X: "  + (x != null ? String.valueOf(x) : "null");
        s += " Y: " + (y != null ? String.valueOf(y) : "null");
        s += " Z: " + (z != null ? String.valueOf(z) : "null");
        s += "]";

        return s;
    }
}
