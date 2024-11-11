package me.student;
import java.util.LinkedList;
import java.util.List;

public class Coordinates3D {
    private Integer x, y, z;
    private Boolean symetrical;

    public Coordinates3D(Integer x, Integer y, Integer z, Boolean symetrical) {
        this.x          = x;
        this.y          = y;
        this.z          = z;
        this.symetrical = symetrical;
    }

    public Coordinates3D(Coordinates3D coordinates3d) {
        Coordinates3D c3d = coordinates3d;
        if(c3d == null) 
            c3d = new Coordinates3D(null, null, null, false);

        this.x          = c3d.x;
        this.y          = c3d.y;
        this.z          = c3d.z;
        this.symetrical = coordinates3d.symetrical;
    }

    public Boolean isEqual(Coordinates3D coordinates) {
        if(
            this.x == coordinates.x &&
            this.y == coordinates.y &&
            this.z == coordinates.z
        ) return true;
        return false;
    }

    public Double getDistanceTo(Coordinates3D coordinates) {
        if (coordinates == null) return null;

        Double ans = Math.sqrt(
            Math.pow(this.x - coordinates.x, 2) +
            Math.pow(this.y - coordinates.y, 2) +
            Math.pow(this.z - coordinates.z, 2)
        );

        if(this.symetrical) return ans;
        if(this.z > coordinates.z) return ans * 0.9;
        if(this.z < coordinates.z) return ans * 1.1;
        return ans;
    }

    public static String toString(Coordinates3D c) {
        if(c == null) return null;
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

    // returns an array [x, y, z]
    public Integer[] get() {
        return new Integer[] {this.x, this.y, this.z};
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Integer getZ() {
        return this.z;
    }

    public static class Coordinates3DLinkedList {

        private List<Coordinates3D> linkedList;
        private Double pathDistance;

        public Coordinates3DLinkedList() {
            linkedList = new LinkedList<>();
            pathDistance = 0.0;
        }

        public Coordinates3DLinkedList(Coordinates3D coordinates3d) {
            linkedList = new LinkedList<>();
            pathDistance = 0.0;

            add(coordinates3d);
        }

        public Coordinates3DLinkedList(Coordinates3DLinkedList cd3dll) {
            linkedList = new LinkedList<>();
            pathDistance = 0.0;
            addAll(cd3dll);
        }

        public boolean add(Coordinates3D coordinates3d) {
            if(coordinates3d == null) return false;

            if(linkedList.size() == 0) {
                return linkedList.add(coordinates3d);
            }

            Coordinates3D last = linkedList.getLast();
            Coordinates3D newLast = coordinates3d;
            boolean status = linkedList.add(coordinates3d);

            if(!status) return status;
            pathDistance += last.getDistanceTo(newLast);

            return status;
        }

        public boolean addAll(Coordinates3DLinkedList cd3dll) {
            boolean status = false;
            if(cd3dll == null) return status;
            
            if(linkedList.size() == 0) {
                status = this.linkedList.addAll(cd3dll.linkedList);
                this.pathDistance = cd3dll.pathDistance;
                return status;
            }
            Coordinates3D prevLast = linkedList.getLast();
            status = this.linkedList.addAll(cd3dll.linkedList);
            if(!status) return status;

            this.pathDistance += prevLast.getDistanceTo(cd3dll.getFirst());
            this.pathDistance += cd3dll.pathDistance;
            return status;
        }

        public boolean addFirst(Coordinates3D coordinates3d) {
            if(coordinates3d == null) return false;

            if(linkedList.size() == 0) {
                linkedList.addFirst(coordinates3d);
                return true;
            }

            Coordinates3D first = linkedList.getFirst();
            Coordinates3D newFirst = coordinates3d;
            linkedList.addFirst(coordinates3d);

            pathDistance += newFirst.getDistanceTo(first);
            return true;
        }

        public boolean contains(Coordinates3D c3d) {
            return linkedList.contains(c3d);
        }

        public Coordinates3D getFirst() { return linkedList.getFirst(); }

        public Coordinates3D getLast() { return linkedList.getLast(); }


        public Double getPathDistance() { return pathDistance; }

        public Integer size() { return linkedList.size(); }

        public Coordinates3D get(int index) {
            return this.linkedList.get(index);
        }

        public String toString() {
            if(linkedList.size() == 0) return "empty";
            String ans = "";
            for(Coordinates3D c3d : linkedList) {
                ans += c3d.toString() + "->";
            }

            return ans.substring(0, ans.length() - 2);
        }

    }

}
