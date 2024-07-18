package com.db.ppt.dementia.repository;

import com.db.ppt.dementia.domain.Blog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Blog entity.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    default Optional<Blog> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Blog> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Blog> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select blog from Blog blog left join fetch blog.auther", countQuery = "select count(blog) from Blog blog")
    Page<Blog> findAllWithToOneRelationships(Pageable pageable);

    @Query("select blog from Blog blog left join fetch blog.auther")
    List<Blog> findAllWithToOneRelationships();

    @Query("select blog from Blog blog left join fetch blog.auther where blog.id =:id")
    Optional<Blog> findOneWithToOneRelationships(@Param("id") Long id);
}
