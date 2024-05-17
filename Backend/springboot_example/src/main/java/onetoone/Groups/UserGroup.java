package onetoone.Groups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import onetoone.Users.Attendee;
import onetoone.Users.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "user_attendee_groups",
            joinColumns = @JoinColumn(name = "usergroup_id"),
            inverseJoinColumns = @JoinColumn(name = "attendee_id")
    )
    Set<Attendee> groupAttendees = new HashSet<>();

    private String name;
    private String groupBio;
    private int groupPassword;

    public UserGroup(String name, String groupBio, int groupPassword) {
        this.name = name;
        this.groupBio = groupBio;
        this.groupPassword = groupPassword;
    }

    public UserGroup(){

    }

    // =============================== Getters and Setters for each field ================================== //


    public void setId(int id) {
        this.id = id;
    }

    public void addAttendee(Attendee attendee){
        this.groupAttendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee){
        this.groupAttendees.remove(attendee);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Set<Attendee> getGroupAttendees() {
        return groupAttendees;
    }

    public void setGroupAttendees(Set<Attendee> groupAttendees) {
        this.groupAttendees = groupAttendees;
     }

    public String getGroupBio() {
        return groupBio;
    }

    public void setGroupBio(String groupBio) {
        this.groupBio = groupBio;
    }

    public int getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(int groupPassword) {
        this.groupPassword = groupPassword;
    }
}
