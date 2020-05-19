package orbital.gns.tpspacefinder.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class Location implements Serializable {
    private String name = "";
    public HashMap<String, Boolean> seats = new HashMap<>();
    public int seatsAvailable = 0;
    public int seatsTaken = 0;
    public double seatsOccupancy = 0;

    public Location() {

    }

    public Location(String name, int seatsAvailable) {
        this.name = name;
        seats = new HashMap<>();
        this.seatsAvailable = seatsAvailable;
        this.seatsTaken = 0;
        this.seatsOccupancy = 0.0;

    }

    public int locationPopularity() {
        return (int) (100.1/seatsOccupancy);
    }

    public boolean getSeats(String seat) {
        if (seats.containsKey(seat) && seats.get(seat)) {
            return false;
        } else {
           seats.put(seat, true);
           seatsAvailable -= 1;
           seatsTaken += 1;
           seatsOccupancy = (double) seatsTaken / (double) seats.size();
           return true;
        }
    }

    public boolean returnSeats(String seat) {
        if (this.seats.containsKey(seat) && seats.get(seat)) {
            seats.put(seat, false);
            seatsTaken -= 1;
            seatsAvailable += 1;
            seatsOccupancy = (double) seatsTaken / (double) seats.size();
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
