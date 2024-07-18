package com.db.ppt.dementia.domain;

import static com.db.ppt.dementia.domain.BlogTestSamples.*;
import static com.db.ppt.dementia.domain.CommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.ppt.dementia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void blog_commentsTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Blog blogBack = getBlogRandomSampleGenerator();

        comment.setBlog_comments(blogBack);
        assertThat(comment.getBlog_comments()).isEqualTo(blogBack);

        comment.blog_comments(null);
        assertThat(comment.getBlog_comments()).isNull();
    }
}
