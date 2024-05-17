package coms309.people;

public class Band {

    private String bandName;

    private String genre;

    private String location;

    private String bandMembers;

    public Band(){

    }

    public Band(String bandName, String genre, String location, String bandMembers){
        this.bandName = bandName;
        this.genre = genre;
        this.location = location;
        this.bandMembers = bandMembers;
    }

    public String getBandName(){return this.bandName;}

    public void setBandName(String bandName){this.bandName = bandName;}

    public String getGenre(){return this.genre;}

    public void setGenre(String genre){this.genre = genre;}

    public String getLocation(){return this.location;}

    public void setLocation(String location){this.location = location;}

    public String getBandMembers(){return this.bandMembers;}

    public void setBandMembers(String bandMembers){this.bandMembers = bandMembers;}

    @Override
    public String toString(){
        return bandName + " "
                + genre + " "
                + location + " "
                + bandMembers;
    }
}