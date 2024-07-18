package com.db.ppt.dementia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "date_time")
    private LocalDate dateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User addedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "auther", "comments" }, allowSetters = true)
    private Blog blog_comments;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Comment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public Comment commentText(String commentText) {
        this.setCommentText(commentText);
        return this;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDate getDateTime() {
        return this.dateTime;
    }

    public Comment dateTime(LocalDate dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public User getAddedBy() {
        return this.addedBy;
    }

    public void setAddedBy(User user) {
        this.addedBy = user;
    }

    public Comment addedBy(User user) {
        this.setAddedBy(user);
        return this;
    }

    public Blog getBlog_comments() {
        return this.blog_comments;
    }

    public void setBlog_comments(Blog blog) {
        this.blog_comments = blog;
    }

    public Comment blog_comments(Blog blog) {
        this.setBlog_comments(blog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return getId() != null && getId().equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", commentText='" + getCommentText() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            "}";
    }
}
