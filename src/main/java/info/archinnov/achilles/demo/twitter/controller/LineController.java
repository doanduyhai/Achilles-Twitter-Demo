package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.demo.twitter.service.LineService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(method = RequestMethod.GET, produces = "application/json")
public class LineController {

    @Inject
    private LineService lineService;

    @RequestMapping(value = "/timeline")
    @ResponseBody
    public List<TweetModel> getTimeline(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return lineService.getTimeline(userLogin, length);
    }

    @RequestMapping(value = "/userline")
    @ResponseBody
    public List<TweetModel> getUserline(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return lineService.getUserline(userLogin, length);
    }

    @RequestMapping(value = "/favoriteline")
    @ResponseBody
    public List<TweetModel> getFavoriteline(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return lineService.getFavoriteLine(userLogin, length);
    }

    @RequestMapping(value = "/mentionline")
    @ResponseBody
    public List<TweetModel> getMentionline(@RequestParam(required = true) String userLogin,
            @RequestParam(defaultValue = "10") int length) {
        return lineService.getMentionLine(userLogin, length);
    }

    @RequestMapping(value = "/tagline")
    @ResponseBody
    public List<TweetModel> getTagline(@RequestParam(required = true) String tag,
            @RequestParam(defaultValue = "10") int length) {
        return lineService.getTagLine(tag, length);
    }
}
