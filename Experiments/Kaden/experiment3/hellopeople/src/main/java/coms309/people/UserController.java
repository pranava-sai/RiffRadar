package coms309.people;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

@RestController
public class UserController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    ArrayList<User> userList = new ArrayList<User>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/users")
    public  String getAllUsers() {
        String userInfo = "";
        //Prints empty if no users in database, otherwise lists all users that have been created
        if(userList.isEmpty()){
            return "No users in database";
        }
        for(User u: userList) {
            userInfo += u.toString();
            userInfo += "\n\n";
        }

        return userInfo;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/user")
    public  @ResponseBody String createUser(@RequestBody User newUser) {
        System.out.println(newUser);
        userList.add(newUser);
        return "New "+ newUser.getUserType() + " " + newUser.getUserName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/users/{userName}")
    public User getUser(@PathVariable String userName) {
        //Finds and returns specific user with given userName
        for(User u: userList) {
            if(u.getUserName().contains(userName)){
                return u;
            }
        }
        return null;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/users/{userName}")
    public @ResponseBody String updateUser(@PathVariable String userName, @RequestBody User u) {
        //Finds and updates given user's info
        for(int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getUserName().contains(userName)){
                userList.set(i, u);
                break;
            }
        }


        return "User Updated, New Info: \n" + u.toString();
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/users/{name}")
    public String deletePerson(@PathVariable String name) {
        User removedUser = null;
        //finds and removes given user from the userList ArrayList
        for(User user: userList) {
            if(user.getUserName().contains(name)){
                userList.remove(user);
                break;
            }
        }
        return "User " + name + " deleted.";
    }
}

