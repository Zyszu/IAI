package me.student_258354.my_tools;

import java.util.ArrayList;
import java.util.List;

public class City {

    public Coordinates3D   coordinates;
    public List<Road>      roadsList;

    public City(Integer x, Integer y, Integer z) {
        this.coordinates = new Coordinates3D(x, y, z);
        this.roadsList = new ArrayList<>();
    }
    
    public City(Integer x, Integer y, Integer z, List<Road> roadList) {
        this.coordinates = new Coordinates3D(x, y, z);
        this.roadsList  = roadList;
    }

    public City(Coordinates3D coordinates) {
        this.coordinates = coordinates;
        this.roadsList = new ArrayList<>();
    }
    
    public City(Coordinates3D coordinates, List<Road> roadList) {
        this.coordinates = coordinates;
        this.roadsList  = roadList;
    }

    public City(City city) {
        this.coordinates = city.coordinates;
        this.roadsList  = city.roadsList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final City other = (City) obj;
        if(!isEqual(other)) return false;

        return true;
    }

    public Boolean isEqual(City city) {
        return this.coordinates.isEqual(city.coordinates);
    }

}
