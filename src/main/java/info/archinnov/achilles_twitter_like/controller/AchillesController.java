package info.archinnov.achilles_twitter_like.controller;

import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles_twitter_like.entity.User;
import info.archinnov.achilles_twitter_like.model.Tweet;
import info.archinnov.achilles_twitter_like.service.AchillesTweetService;
import info.archinnov.achilles_twitter_like.service.AchillesUserService;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AchillesController
 * 
 * @author DuyHai DOAN
 * 
 */
@Controller
public class AchillesController
{
	@Inject
	private AchillesUserService userService;

	@Inject
	private AchillesTweetService tweetService;

	@Inject
	private ThriftEntityManager em;

	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public User createUser(@RequestBody User lightUser)
	{
		return userService.createUser(lightUser);
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public User getUser(@RequestParam String userLogin)
	{
		return userService.getUser(userLogin);
	}

	@RequestMapping(value = "/user/friend", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public User addFriend(@RequestParam String userLogin, @RequestParam String friendLogin)
	{
		return userService.addFriend(userLogin, friendLogin);
	}

	@RequestMapping(value = "/user/friend", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public User removeFriend(@RequestParam String userLogin, @RequestParam String friendLogin)
	{
		return userService.removeFriend(userLogin, friendLogin);
	}

	@RequestMapping(value = "/tweet", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public UUID postTweet(@RequestParam String authorLogin, @RequestBody String content)
	{
		return tweetService.createTweet(authorLogin, content);
	}

	@RequestMapping(value = "/tweet", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Tweet getTweet(@RequestParam String userLogin, @RequestParam String tweetId)
	{
		return tweetService.getTweet(userLogin, tweetId);
	}

	@RequestMapping(value = "/tweet", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public Tweet removeTweet(@RequestParam String userLogin, @RequestParam String tweetId)
	{
		return tweetService.removeTweet(userLogin, tweetId);
	}

	@RequestMapping(value = "/timeline", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Tweet> getTimeline(@RequestParam String userLogin, @RequestParam int length)
	{
		return userService.getTimeline(userLogin, length);
	}

	@RequestMapping(value = "/userline", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Tweet> getUserline(@RequestParam String userLogin, @RequestParam int length)
	{
		return userService.getUserline(userLogin, length);
	}

	@RequestMapping(value = "/tagline", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Tweet> getTagline(@RequestParam String tag, @RequestParam int length)
	{
		return tweetService.getTagLine(tag, length);
	}

}
