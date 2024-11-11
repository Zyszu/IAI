package me.student_258354.my_tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cities {
    
    public City startCity;
    public List<City> citiesList;

    private Integer numberOfCities;
    private Integer numberOfRoads;

    public Cities() {
        this.startCity = null;
        this.citiesList = new ArrayList<>();

        this.numberOfCities = 0;
        this.numberOfRoads = 0;
    }

    public City add(City c) {
        if(c == null) return c;
        if(startCity == null) {
            this.startCity = c;
            numberOfCities = 1;
            citiesList.add(c);
            return c;
        }

        numberOfCities++;
        numberOfRoads += c.roadsList.size();
        citiesList.add(c);
        return c;
    }

    // returns how many percent of roads were reduced
    public Double reduceConnectionsByPercent(Integer percent) {
        if(percent < 0 || percent > 100) return -1.0;
        Integer tr = this.numberOfRoads * percent / 100;
        if(this.numberOfRoads - tr < this.numberOfCities) return -1.0;

        Random rand = new Random();

        for(int i = 0; i < tr; i ++) {
            City c = this.citiesList.get(rand.nextInt(citiesList.size()));

            if(c.roadsList.size() <= 1) continue;
            c.roadsList.remove(rand.nextInt(c.roadsList.size()));
        }


        Double reducedBy = Double.valueOf(tr) / Double.valueOf(numberOfRoads);
        this.numberOfRoads = this.numberOfRoads - tr;
        return reducedBy;
    }

    public Integer getNumberOfCities()   {return this.numberOfCities;}

    public Integer getNumberOfRoads()    {return this.numberOfRoads;}

    public void setCitiesAndRoadsNumber() {
        this.numberOfCities = this.citiesList.size();

        for(City c : this.citiesList) {
            this.numberOfRoads += c.roadsList.size();
        }
    }

    public String stringSummary(){
        String s = "";

        s +=  "cities: " + Integer.toString(this.numberOfCities);
        s += " roads: " + Integer.toString(this.numberOfRoads);
        
        return s;
    }

}
