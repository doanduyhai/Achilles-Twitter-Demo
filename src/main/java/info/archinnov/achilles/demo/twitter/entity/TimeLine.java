package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.WideRow;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.type.WideMap;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "timeline")
@WideRow
public class TimeLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private WideMap<UUID, Tweet> timeline;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public WideMap<UUID, Tweet> getTimeline() {
        return timeline;
    }
}
