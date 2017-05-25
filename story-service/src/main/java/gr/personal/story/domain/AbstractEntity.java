package gr.personal.story.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by nkanakis on 5/25/2017.
 */
public abstract class AbstractEntity {

    @Id
    private String id;
    private String userId;
    private Date postDate;


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Date getPostDate() {
        return postDate;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setUserId(String userId) {
        this.userId = userId;
    }

    protected void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
