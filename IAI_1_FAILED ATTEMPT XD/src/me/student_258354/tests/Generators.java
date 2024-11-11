package me.student_258354.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.student_258354.my_tools.Cities;
import me.student_258354.my_tools.City;
import me.student_258354.my_tools.Coordinates3D;
import me.student_258354.my_tools.Road;

public class Generators {

    public static Cities generateRandomCities(Interval n, Interval x, Interval y, Interval z) {
        if(n == null)
            return null;

        Random rand = new Random();
        List<City> citiesList = new ArrayList<>();

        for(Integer i = 0; i < rand.nextInt(n.getAbsDifference() + 1) + n.start; i++) {
            citiesList.add(generateRandomCity(x, y, z));
        }

        if(citiesList.size() == 0) return null;

        Cities cities = new Cities();
        cities.startCity = citiesList.get(0);
        cities.citiesList = citiesList;

        for(City cti1 : citiesList) {
            for(City cti2 : citiesList) {
                if(cti1.equals(cti2)) continue;

                Double weight = cti1.coordinates.getDistance(cti2.coordinates);

                if(cti1.coordinates.z > cti2.coordinates.z) weight *= 0.9;
                if(cti1.coordinates.z < cti2.coordinates.z) weight *= 1.1;

                Road rd = new Road(cti2, weight);
                cti1.roadsList.add(rd);
            }
        }

        return cities;
    }

    public static City generateRandomCity(Interval x, Interval y, Interval z) {
        if(x == null || y == null || z == null)
            return null;

        Random rand = new Random();
        Coordinates3D cord = new Coordinates3D(
            rand.nextInt(x.getAbsDifference() + 1) + x.start,
            rand.nextInt(y.getAbsDifference() + 1) + y.start,
            rand.nextInt(z.getAbsDifference() + 1) + z.start
        );

        return new City(cord);
    }

    public static class Interval {
        private Integer start, end;

        public Interval(Integer start, Integer end) {
            if(start == null || end == null) {
                start   = 0;
                end     = 0;
            }
            if(end > start) {
                Integer temp = start;
                this.start   = end;
                this.end     = temp;
            }

            this.start       = start;
            this.end         = end;
        }

        public Interval(Integer n) {
            if(n == null) {
                this.start  = 0;
                this.end    = 0;
            }

            this.start      = n;
            this.end        = n;
        }

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }

        public Integer getDifference() {
            return end - start;
        }

        public Integer getAbsDifference() {
            return Math.abs(end - start);
        }

    }

}
