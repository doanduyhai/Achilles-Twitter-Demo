package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.Key;
import info.archinnov.achilles.annotations.WideRow;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.type.MultiKey;
import info.archinnov.achilles.entity.type.WideMap;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tweetline")
@WideRow
public class TweetLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String partitionKey;

    @Column
    private WideMap<TweetKey, Tweet> tweets;

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public WideMap<TweetKey, Tweet> getTweets() {
        return tweets;
    }

    public static class TweetKey implements MultiKey {

        @Key(order = 1)
        private String digest;

        @Key(order = 2)
        private String author;

        @Key(order = 3)
        private UUID id;

        public TweetKey() {
        }

        public TweetKey(String digest) {
            this.digest = digest;
            this.author = null;
            this.id = null;
        }

        public TweetKey(String digest, String author, UUID id) {
            this.digest = digest;
            this.author = author;
            this.id = id;
        }

        public String getDigest() {
            return digest;
        }

        public String getAuthor() {
            return author;
        }

        public UUID getId() {
            return id;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }
}
