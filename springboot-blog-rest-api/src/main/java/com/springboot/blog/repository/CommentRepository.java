package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    /*
    * Spring Data JPA does not include a default method to fetch comments by post_id, so we define findByPostId(Long postId), leveraging Derived Query Methods.
    * Spring parses the method name, generating a JPQL query like SELECT c FROM Comment c WHERE c.post.id = :postId,
    * which Hibernate translates into SQL (SELECT * FROM comment WHERE post_id = ?).
    * At runtime, Spring creates a proxy implementation of CommentRepository,
    * executes the query, and maps the results to Comment objects.
    * This approach eliminates boilerplate code while allowing customization via @Query for complex queries.
    *
    * */

    List<Comment> findAllByPostId(Long postId);

}
