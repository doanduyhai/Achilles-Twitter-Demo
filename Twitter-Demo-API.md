### Users

 * **POST** **_/user_**: create a new user. 
    * RequestBody: user info in JSON format
  
     Example:

     ```json
{
     	"login": "jdoe",
	"firstname": "John",
	"lastname": "DOE"
}
     ```
 * **GET** **_/user_**: get information on an user
    * RequestParams:
        * **userLogin** (required): login of the user to be retrieved


 * **PUT** **_/user_/friend**: add friend to user
    * RequestParams:
        * **userLogin** (required): login of the user
        * **friendLogin** (required): login of the friend to follow

 * **DELETE** **_/user_/friend**: remove a friend
    * RequestParams:
        * **userLogin** (required): login of the user
        * **friendLogin** (required): login of the friend to stop following

 * **GET** **_/user/friends_**: list all friends of the user
    * RequestParams:
        * **userLogin** (required): login of the user to be retrieved

 * **GET** **_/user/followers_**: list all followers of the user
    * RequestParams:
        * **userLogin** (required): login of the user to be retrieved

### Tweets

 * **POST** **_/tweet_**: create a new tweet 
    * RequestBody: content of the tweet in plain text
  
     Example:

     ```text
This is a tweet message
     ```
 * **GET** **_/tweet_**: get information on a tweet
    * RequestParams:
        * **tweetId** (required): tweet id

 * **DELETE** **_/tweet_**: Remove a tweet
    * RequestParams:
        * **tweetId** (required): tweet id

 * **PUT** **_/tweet/favorite_**: add a tweet in favorite list
    * RequestParams:
        * **userLogin** (required): login of the user
        * **tweetId** (required): tweet id

 * **DELETE** **_/tweet/favorite_**: remove a tweet from favorite list
    * RequestParams:
        * **userLogin** (required): login of the user
        * **tweetId** (required): tweet id

### Lines

* **GET** **_/timeline_**: display the timeline of an user
    * RequestParams:
        * **userLogin** (required): login of the user
        * **length** (optional): number of tweets to fetch

* **GET** **_/userline_**: display all tweets of an user
    * RequestParams:
        * **userLogin** (required): login of the user
        * **length** (optional): number of tweets to fetch

* **GET** **_/favoriteline_**: display the favorite list of an user
    * RequestParams:
        * **userLogin** (required): login of the user
        * **length** (optional): number of tweets to fetch

* **GET** **_/mentionline_**: display all tweets mentioning an user
    * RequestParams:
        * **userLogin** (required): login of the user
        * **length** (optional): number of tweets to fetch

* **GET** **_/tagline_**: display all tweets having tag
    * RequestParams:
        * **tag** (required): the hashtag
        * **length** (optional): number of tweets to fetch