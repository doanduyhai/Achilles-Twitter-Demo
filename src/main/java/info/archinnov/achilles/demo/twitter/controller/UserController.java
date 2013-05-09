package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.service.UserService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody(required = true) User lightUser) {
        return userService.createUser(lightUser);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@RequestParam(required = true) String userLogin) {
        return userService.getUser(userLogin);
    }

    @RequestMapping(value = "/friend", method = RequestMethod.PUT)
    @ResponseBody
    public User addFriend(@RequestParam(required = true) String userLogin,
            @RequestParam(required = true) String friendLogin) {
        return userService.addFriend(userLogin, friendLogin);
    }

    @RequestMapping(value = "/friend", method = RequestMethod.DELETE)
    @ResponseBody
    public User removeFriend(@RequestParam(required = true) String userLogin,
            @RequestParam(required = true) String friendLogin) {
        return userService.removeFriend(userLogin, friendLogin);
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    public List<User> friends(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return userService.getFriends(userLogin, length);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    @ResponseBody
    public List<User> followers(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return userService.getFollowers(userLogin, length);
    }
}
