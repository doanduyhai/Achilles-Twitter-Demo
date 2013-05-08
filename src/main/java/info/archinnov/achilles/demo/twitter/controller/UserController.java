package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.service.UserService;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public User createUser(@RequestBody User lightUser) {
        return userService.createUser(lightUser);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public User getUser(@RequestParam String userLogin) {
        return userService.getUser(userLogin);
    }

    @RequestMapping(value = "/friend", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public User addFriend(@RequestParam String userLogin, @RequestParam String friendLogin) {
        return userService.addFriend(userLogin, friendLogin);
    }

    @RequestMapping(value = "/friend", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public User removeFriend(@RequestParam String userLogin, @RequestParam String friendLogin) {
        return userService.removeFriend(userLogin, friendLogin);
    }
}
