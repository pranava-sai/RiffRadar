package coms309;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
class WelcomeController {

    ArrayList<userCreator> userList = new ArrayList<userCreator>();
    @GetMapping("/")
    public String welcome() {
        return "Welcome to riff radar";
    }

    @GetMapping("/users")
    public String getUsers() {
        String users = "";
        //Parses userList to add each user to a string for printing
        for(userCreator user: userList){
            users += user.printUserInfo() + "\n\n";
        }
        return users;
    }

        @PostMapping("/user")
    public String createDefaultUser(@RequestBody userCreator user) {
        //Creates new user based on request sent through request body
        System.out.println(user);
        userList.add(user);
        return "User " + user.getUserName() + " created";
    }




}
