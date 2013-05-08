package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.demo.twitter.service.TweetService;
import java.util.UUID;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tweet")
public class TweetController {

    @Inject
    private TweetService tweetService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public UUID postTweet(@RequestParam String authorLogin, @RequestBody String content) {
        return tweetService.createTweet(authorLogin, content);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Tweet getTweet(@RequestParam String userLogin, @RequestParam String tweetId) {
        return tweetService.getTweet(userLogin, tweetId);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public Tweet removeTweet(@RequestParam String userLogin, @RequestParam String tweetId) {
        return tweetService.removeTweet(userLogin, tweetId);
    }
}
