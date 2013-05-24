package info.archinnov.achilles.demo.twitter.entity.widerow;

import info.archinnov.achilles.annotations.WideRow;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.type.WideMap;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tag
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tagline")
@WideRow
public class TagLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String tag;

    @Column
    private WideMap<UUID, Tweet> tagline;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public WideMap<UUID, Tweet> getTagline() {
        return tagline;
    }

    public void setTagline(WideMap<UUID, Tweet> tagline) {
        this.tagline = tagline;
    }
}
