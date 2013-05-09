package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.demo.twitter.service.FavoriteService;
import info.archinnov.achilles.demo.twitter.service.TweetService;
import java.util.UUID;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/tweet", produces = "application/json")
public class TweetController {

    @Inject
    private TweetService tweetService;

    @Inject
    private FavoriteService favoriteService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public UUID postTweet(@RequestParam(required = true) String authorLogin,
            @RequestBody(required = true) String content) {
        return tweetService.createTweet(authorLogin, content);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Tweet getTweet(@RequestParam(required = true) String tweetId) {
        return tweetService.getTweet(tweetId);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Tweet removeTweet(@RequestParam(required = true) String tweetId) {
        return tweetService.removeTweet(tweetId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/favorite", method = RequestMethod.PUT)
    public void addTweetToFavorite(@RequestParam(required = true) String userLogin,
            @RequestParam(required = true) String tweetId) {

        favoriteService.addTweetToFavorite(userLogin, tweetId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/favorite", method = RequestMethod.DELETE)
    public void removeTweetFromFavorite(@RequestParam(required = true) String userLogin,
            @RequestParam(required = true) String tweetId) {

        favoriteService.removeTweetFromFavorite(userLogin, tweetId);
    }
}
