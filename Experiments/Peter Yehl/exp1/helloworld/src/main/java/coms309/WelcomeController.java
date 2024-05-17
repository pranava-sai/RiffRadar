package coms309;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/welcome")
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    @PostMapping("/greet")
    public String greetRequestBody(@RequestBody String name){
        return "Hello and welcome to COMS 309: " + name + "!";
    }
}