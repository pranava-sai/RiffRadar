package onetoone.Groups;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Users.Attendee;
import onetoone.Users.AttendeeRepository;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "UserGroup", description = "UserGroup API")
@RestController
public class UserGroupController {

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    AttendeeRepository attendeeRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

//    @Operation(summary = "Get all UserGroup")
//    @GetMapping(path = "/usergroups")
//    List<UserGroup> getAllUserGroups(){
//        return userGroupRepository.findAll();
//    }

        @Operation(summary = "Get all UserGroup")
    @GetMapping(path = "/usergroups")
    List<UserGroup> getAllUserGroups(){
        return userGroupRepository.findAll();
    }

    @Operation(summary = "Get a specific UserGroup")
    @GetMapping(path = "/usergroups/{id}")
    UserGroup getUserGroupByLoginId(@PathVariable int id){

        return userGroupRepository.findById(id);
    }

    @Operation(summary = "Get group id with group name")
    @GetMapping(path = "/getusergroupid/{groupName}")
    String getUserGroupId(@PathVariable String groupName){
            UserGroup userGroup = userGroupRepository.findByName(groupName);
            String message = "{\"groupId\":\"" + userGroup.getId() + "\"}";
        return message;
    }

    @Operation(summary = "Get a specific Groups Attendees")
    @GetMapping(path = "/usergroups/{userGroupId}/attendees")
     Set<Attendee> getUserGroupAttendees(@PathVariable int userGroupId){
        UserGroup userGroup = userGroupRepository.findById(userGroupId);
        return userGroup.getGroupAttendees();
    }

    @Operation(summary = "Get a specific Groups Attendees")
    @GetMapping(path = "/attendees/{attendeeId}/usergroups")
    Set<UserGroup> getAttendeesUserGroups(@PathVariable int attendeeId){
        Attendee attendee = attendeeRepository.findById(attendeeId);
        return attendee.getUserGroups();
    }

    @Operation(summary = "Create a new UserGroup")
    @PostMapping(path = "/usergroups")
    String createUserGroup(@RequestBody UserGroup userGroup){
        if (userGroup == null)
            return failure;

        if(userGroupRepository.findByName(userGroup.getName()) != null) {
            return "{Failed: Group Name Already Exists}";
        }
        userGroupRepository.save(userGroup);
        return success;
    }

    @Operation(summary = "Add an attendee to a usergroup")
    @PutMapping(path = "/usergroups/{userGroupName}/attendees/{attendeeId}/password/{password}")
    String addAttendeeToUserGroup(@PathVariable("userGroupName") String userGroupName, @PathVariable("attendeeId") int attendeeId, @PathVariable("password") int password){
        UserGroup userGroup = userGroupRepository.findByName(userGroupName);
        Attendee attendee = attendeeRepository.findById(attendeeId);

        if(attendee == null || userGroup == null){
            return failure;
        }

        if(!userGroup.getName().equals(userGroupName)){
            return "{Message: Group does not exists or group name is incorrect}";
        }

        if(userGroup.getGroupPassword() != password){
            return "{Message: Password Incorrect}";
        }


        userGroup.addAttendee(attendee);
        attendee.setUserGroup(userGroup);

        userGroupRepository.save(userGroup);
        attendeeRepository.save(attendee);
        return success;

    }

    @Operation(summary = "Change a specific UserGroup")
    @PutMapping("/usergroups/{id}")
    UserGroup updateUserGroup(@PathVariable int id, @RequestBody UserGroup request){
        UserGroup user = userGroupRepository.findById(id);
        if(user == null)
            return null;
        userGroupRepository.save(request);
        return userGroupRepository.findById(id);
    }

        @Operation(summary = "Delete a UserGroup")
    @DeleteMapping(path = "/usergroups/{groupName}")
    String deleteUserGroup(@PathVariable String groupName){
        UserGroup userGroup = userGroupRepository.findByName(groupName);

        if(userGroup.getGroupAttendees() != null){
            userGroup.setGroupAttendees(null);
            userGroupRepository.save(userGroup);
        }

        userGroupRepository.deleteById(userGroup.getId());
        return success;
    }

    @Operation(summary = "Delete a Specific user from a UserGroup")
    @DeleteMapping(path = "/usergroups/{userGroupId}/attendees/{attendeeId}")
    String deleteAttendeeFromUserGroup(@PathVariable("userGroupId") int userGroupId, @PathVariable("attendeeId") int attendeeId){
        UserGroup userGroup = userGroupRepository.findById(userGroupId);
        Attendee attendee = attendeeRepository.findById(attendeeId);

        if(attendee == null || userGroup == null){
            return "{\"message\":\"Error: UserGroup or Attendee does not exist\"}";
        }

        userGroup.removeAttendee(attendee);
        attendee.removeFromUserGroup(userGroup);
        attendeeRepository.save(attendee);
        userGroupRepository.save(userGroup);
        return success;
    }


}
