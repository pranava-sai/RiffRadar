package coms309.people;

public class Venue {

    private String venueName;
    private String type;

    private String location;

    private String capacity;

    public Venue(){

    }

    public Venue(String venueName, String type, String location, String capacity){
        this.venueName = venueName;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
    }

    public String getVenueName(){return this.venueName;}

    public void setVenueName(String name){this.venueName = venueName;}

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {return this.location;}

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCapacity() { return this.capacity; }

    public void setCapacity(String capacity) {this.capacity = capacity;}

    @Override
    public String toString() {
        return venueName + " "
                + type + " "
                + location + " "
                + capacity;
    }


}
