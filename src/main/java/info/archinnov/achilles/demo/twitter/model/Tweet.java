package info.archinnov.achilles.demo.twitter.model;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.json.JsonDateDeserializer;
import info.archinnov.achilles.demo.twitter.json.JsonDateSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Tweet
 * 
 * @author DuyHai DOAN
 * 
 */
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String author;

    private Long favoritesCount;

    private String content;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date creationDate;

    public Tweet() {
    }

    public Tweet(UUID id, User creator, String content) {
        this.id = id;
        if (id != null) {
            this.creationDate = new Date(TimeUUIDUtils.getTimeFromUUID(id));
        }
        this.content = content;
        if (creator != null) {
            this.author = creator.getLogin();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Long favoritesCount) {
        this.favoritesCount = favoritesCount;
    }
}
