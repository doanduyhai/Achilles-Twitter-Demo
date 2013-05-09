### Posting a tweet

 When an user posts a tweet, it is added to this user list of tweets ( **userline** ) but also spread to all his followers timeline. Look at the method `spreadTweetCreation` of the **TweetService** class in the demo for more details on how it is achieved.


### Spreading Tags and Mentions

When a tweet contains hashtags ( **#tag** ) or mentions users ( **@login** ), we must add a copy of this tweet:

* to the **#tag** list of tweets, called **tagline**
* to the user **mentionline**


Look at the methods `spreadTags` and `spreadUserMention` of the **TweetService** class in the demo for more details on how it is achieved

### Favoriting a tweet

 An user can add a tweet to its favorite list. In this case the tweet will be copied to its **favoriteline**. 

 A favorite tweet can be also removed from the favorite list. In this case the copy of the tweet is removed, not the original one. 

 Look at the methods `addTweetToFavorite` and `removeTweetFromFavorite` of the **FavoriteService** class in the demo for more details on how it is achieved

### Managing tweet removal

 Since a tweet can be spread and duplicated in many lines, on its removal we must also remove all his copies. It is the purpose of the **TweetIndex** entity. Each of its **WideMap** field keeps a reference on the line in which the tweet has been spread. Upon removal we iterate on the widemap and remove each copy.

 Implementation details can be found in method `spreadTweetRemoval` of the **TweetService** class in the demo.