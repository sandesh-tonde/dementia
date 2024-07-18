package com.db.ppt.dementia.domain;

import static com.db.ppt.dementia.domain.BlogTestSamples.*;
import static com.db.ppt.dementia.domain.CommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.ppt.dementia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BlogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Blog.class);
        Blog blog1 = getBlogSample1();
        Blog blog2 = new Blog();
        assertThat(blog1).isNotEqualTo(blog2);

        blog2.setId(blog1.getId());
        assertThat(blog1).isEqualTo(blog2);

        blog2 = getBlogSample2();
        assertThat(blog1).isNotEqualTo(blog2);
    }

    @Test
    void commentTest() {
        Blog blog = getBlogRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        blog.addComment(commentBack);
        assertThat(blog.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getBlog_comments()).isEqualTo(blog);

        blog.removeComment(commentBack);
        assertThat(blog.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getBlog_comments()).isNull();

        blog.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(blog.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getBlog_comments()).isEqualTo(blog);

        blog.setComments(new HashSet<>());
        assertThat(blog.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getBlog_comments()).isNull();
    }
}
