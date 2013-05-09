 The entities has been designed to favor the reading tweets use case over deletion. Indeed, most of the time people create and read tweets, the tweet removal scenario is quite rare. This emphasises one of the most important requirement when working with **Cassandra**: **_you must adapt the entity modeling and design to your business use cases_** 


 With this in mind, we duplicate as much as we can data to avoid multiple reads while fetching tweets in different line.

 Of course the trade-off is to keep an inverted index to know in which lines a tweet has been spread for deletion scenario. Consequently the tweet deletion scenario is slower since we need to read from the inverted index before removing data. It's the _read-before-write anti-pattern_ and is quite expensive but still acceptable because the removal use case is rare.

 

## User

```java
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column
    private Date accountCreationDate;

    @Column
    private Counter tweetsCounter;

    private long tweetsCount;

    @Column
    private Counter friendsCounter;

    private long friendsCount;

    @Column
    private Counter followersCounter;

    private long followersCount;

    @Column
    private Counter mentionsCounter;

    private long mentionsCount;

    @Column(table = "timeline")
    private WideMap<UUID, Tweet> timeline;

    @Column(table = "userline")
    private WideMap<UUID, Tweet> userline;

    @Column(table = "favoriteline")
    private WideMap<UUID, Tweet> favoriteline;

    @Column(table = "mentionline")
    private WideMap<UUID, Tweet> mentionline;

    @JoinColumn(table = "friends")
    @ManyToMany
    private WideMap<String, User> friends;

    @JoinColumn(table = "followers")
    @ManyToMany
    private WideMap<String, User> followers;
    ...
}
```

 The user entity exposes 3 counters:

 * **tweetsCounter**: number of tweets created by the user
 * **friendsCounter**: number of people the user is following
 * **followersCounter**: number of people following the user
 * **mentionsCounter**: number of tweet in which the user is mentioned (good popularity indicator)

And several wide maps:

 * **timeline**: user timeline
 * **userline**: list of tweets created by the user
 * **favoriteline**: list of user' favorite tweets
 * **mentionline**: list of tweets in which the user has been mentioned
 * **friends**: list of people the user is following
 * **followers**: list of people following the user

> The wide maps for lines has the **Tweet** POJO as value. As you can notice, the **Tweet** object is duplicated in several lines. Indeed, the choice of denormalizing and duplicating data has a big impact on read performance.


There are also 4 primitive long fields to carry the counter values for serialization back to the front-end because the **Counter** type is a proxy.
 
 
## Tweet Index
```java
@Entity
@Table(name = "tweet_index")
public class TweetIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column
    private Counter favoritesCount;

    @Column
    private Tweet tweet;

    @ManyToMany
    @Column(table = "tweet_user_timeline_index")
    private WideMap<String, String> timelineUsers;

    @ManyToMany
    @Column(table = "tweet_user_favoriteline_index")
    private WideMap<String, String> favoritelineUsers;

    @Column(table = "tweet_user_mentionline_index")
    private WideMap<String, String> mentionlineUsers;

    @Column(table = "tweet_tag_index")
    private WideMap<String, String> tags;
    ...
}
```

 The **TweetIndex**, as mentioned by its name, keeps an index on which line the tweet has been duplicated to remove when the original tweet is deleted.

 
## WideRows

 The _timeline_ widemap field in the **User** entity is writing to the underlying **Cassandra** column family named _"timeline"_. The same applies to the other widemap fields.

 The problem is that if we want to fetch tweets directly from these widemaps, we must load the **User** entity before hand, which causes an unnecessary read.

 The solution would be to map these column families with **WideRow** entities:

```java
@Entity
@Table(name = "userline")
@WideRow
public class UserLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private WideMap<UUID, Tweet> userline;
}

@Entity
@Table(name = "timeline")
@WideRow
public class TimeLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private WideMap<UUID, Tweet> timeline;
}

@Entity
@Table(name = "favoriteline")
@WideRow
public class FavoriteLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private WideMap<UUID, Tweet> favoriteline;
}

@Entity
@Table(name = "mentionline")
@WideRow
public class MentionLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private WideMap<UUID, Tweet> mentionline;
}

@Entity
@Table(name = "tagline")
@WideRow
public class TagLine implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	private String tag;

	@Column
	private WideMap<UUID, Tweet> tagline;
}

@Entity
@Table(name = "friends")
@WideRow
public class FriendLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @JoinColumn
    private WideMap<String, User> friends;
}

@Entity
@Table(name = "followers")
@WideRow
public class FollowerLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @JoinColumn
    private WideMap<String, User> followers;
}
```

 With these **WideRow** entities, fetching tweets from a line result in only one unique slice query. For **friends** and **followers** line, there is _one extra multiget slice query_ to fetch join entities.

 In the end, for each line (such as _"timeline"_ ) we have 2 distincts mapping in **Achilles**:

 * **WideRow** mapping to fetch data directly from the column family
 * **WideMap** field mapping inside the **User** entity
