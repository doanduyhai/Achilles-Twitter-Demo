 Below is the procedure to install and use the Chrome Postman REST Client extension to interact with the demo

### Installing the client
 
 First you need to have a recent Google Chrome browser. Then install the **[Postman REST Client]** add-on, it's free.

### Importing scripts

 All REST requests have been prepared before hand and saved as scripts. They are saved under the **Postman_Client_Scripts** folder in the demo source:

* **AchillesDemo-environment.json**: script to load environment and variable. It define the variable **{{url}}** which default to _http://localhost:8080_
* **AchillesDemo_Users.json**: predefined collection of requests to interact with users, namely:
    * create user
    * follow/stop following an user
    * get information on an user
    * list user friends
    * list user followers
* **AchillesDemo_Tweets.json**: predefined collection of requests to interact with tweets, namely:
    * post a tweet
    * display a tweet
    * add/remove a tweet to/from favorite list
    * delete a tweet
* **AchillesDemo_Lines.json**: predefined collection of requests to interact with lines, namely:
    * list an user timeline
    * list all user tweets
    * list an user mentionline
    * list all tweets for a given tag


### Playing scenarios

 The scenarios are there to illustrate the features of the twitter demo back-end and also serves as functional validation tests.

Scenario 1: 
 * create 3 users, **John Doe**, **Helen Sue** and **Richard Smith**
 * **Helen** and **Richard** follows **John**
 * When getting info on **John**, his followers count should be 2

Scenario 2:
 * **John** posts 4 tweets. Some tweets contain the _#Achilles_, _#Cassandra_ and _#Java_ hashtags
 * **John** tweets count should be updated to 4
 * Those 4 tweets should be visible in **John** userline
 * Those 4 tweets should be visible in **Helen** and **Richard** timeline since they follow **John**
 * Some of these tweets are visible in the _#Achilles_, _#Cassandra_ and _#Java_ taglines

Scenario 3:
 * **Richard** starts following **Helen**
 * **Helen** posts a tweet ( _tweet5_ ) with the hashtag _#Hibernate_
 * **Richard** should see _tweet5_ in his timeline
 * _tweet5_ should be visible in the _#Hibernate_ tagline
 * **Helen** tweets count and followers count should be 1

Scenario 4: 
 * **Richard** stop following **Helen**
 * **Helen** posts a tweet ( _tweet6_ ) with the _#DevoxxFR_ hashtag
 * _tweet6_ should NOT be visible in **Richard** timeline
 * _tweet6_ should be visible in the _#DevoxxFR_ tagline
 * **Helen** tweets count should be incremented to 2 and followers count decremented to 0
 
Scenario 5:
 * **Helen** adds **John** second tweet (_tweet2_) to her favorite list
 * **Richard** adds **John** third tweet (_tweet3_) to his favorite list
 * _tweet2_ should appear in **Helen** favorite line
 * _tweet3_ should appear in **Richard** favorite line
 * _tweet2_ and _tweet3_ should have 1 favorite count

Scenario 6:
 * **John** posts a tweet ( _tweet7_ ) mentioning **Helen** (@hsue) and **Richard** (@rsmith) and **Batman** (@batman), an inexisting user
 * _tweet7_ should appear in **Helen** and **Richard** mention line
 * **Helen** and **Richard** mention count should be incremented to 1
 * **Batman** mention line should be empty since the user does not exists

Scenario 7:
 * **John** removes his first tweet ( _tweet1_ )
 * _tweet1_ should no longer exist in **Helen** and **Richard** timeline
 * _tweet1_ should be removed from some tagline ( _#Achilles_ and _#Cassandra_ namely)
 * **John** tweets count should be decremented

Scenario 8:
 * **John** removes _tweet2_ and _tweet3_. These tweets were favorited by **Helen** and **Richard**
 * **Helen** and **Richard** favorite line should be now empty
 * **John** tweets count should be decremented by 2

Scenario 9:
 * **John** removes _tweet7_. This tweet mentioned **Helen** and **Richard**
 * **Helen** and **Richard** mention line should be now empty
 * **John** tweets count should be decremented 
 


 



[Postman REST Client]: https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm