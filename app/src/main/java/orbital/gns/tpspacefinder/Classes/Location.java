package orbital.gns.tpspacefinder.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class Location implements Serializable {
    private String name;
    private HashMap<String, Boolean> seats;
    private int seatsAvailable;
    private int seatsTaken;
    private double seatsOccupancy;

    public Location() {

    }

    public Location(String name, int seatsAvailable) {
        this.name = name;
        seats = new HashMap<>();
        this.seatsAvailable = seatsAvailable;
        this.seatsOccupancy = 0.0;
        this.seatsTaken = 0;
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
