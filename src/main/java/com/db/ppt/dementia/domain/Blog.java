package com.db.ppt.dementia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Blog.
 */
@Entity
@Table(name = "blog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 200)
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDate dateTime;

    @Column(name = "vote")
    private Integer vote;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User auther;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog_comments")
    @JsonIgnoreProperties(value = { "addedBy", "blog_comments" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Blog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Blog content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDateTime() {
        return this.dateTime;
    }

    public Blog dateTime(LocalDate dateTime) {
        this.setDateTime(dateTime);
        return this;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getVote() {
        return this.vote;
    }

    public Blog vote(Integer vote) {
        this.setVote(vote);
        return this;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public User getAuther() {
        return this.auther;
    }

    public void setAuther(User user) {
        this.auther = user;
    }

    public Blog auther(User user) {
        this.setAuther(user);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setBlog_comments(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setBlog_comments(this));
        }
        this.comments = comments;
    }

    public Blog comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Blog addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBlog_comments(this);
        return this;
    }

    public Blog removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setBlog_comments(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blog)) {
            return false;
        }
        return getId() != null && getId().equals(((Blog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Blog{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", vote=" + getVote() +
            "}";
    }
}
