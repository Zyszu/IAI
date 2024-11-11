package me.student;
import java.util.Random;

public class Generators {

    public static class Interval {
    
        private Integer start, end, length;

        public Interval(Integer start, Integer end) {
            this.start  = start;
            this.end    = end;

            if(this.start == null || this.end == null) 
                this.length = 0;
            this.length = Math.abs(this.end - this.start);
        }
        
        public Interval(Integer n) {
            this.start  = n;
            this.end    = n;

            if(this.start == null || this.end == null) 
                this.length = 0;
            this.length = Math.abs(this.end - this.start);
        }

        public Integer getStart() { return this.start; }

        public Integer getEnd() { return this.end; }

        public Integer length() { return this.length; }

        public Integer[] get() { return new Integer[] {start, end, length}; }


    }

    public static Coordinates3D getRandomCoordinates3d(Interval x, Interval y, Interval z, Boolean isSimetrical, long seed) {
        Random rand = new Random(seed);

        Integer newX = rand.nextInt(x.length) + x.getStart();
        Integer newY = rand.nextInt(y.length) + y.getStart();
        Integer newZ = rand.nextInt(z.length) + z.getStart();

        return new Coordinates3D(newX, newY, newZ, isSimetrical);
    }
}
