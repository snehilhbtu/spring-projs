package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Comments;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(
        name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {

    @Id
    //use @GeneratedValue to let the database handle primary key generation, incrementaly using GenerationType.IDENTITY
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    //omitting this will still connect title to title column but not null wont be applicable
    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "content",nullable = false)
    private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    //@JsonManagedReference //Marks this as the "parent" side
    Set<Comment> comments=new HashSet<Comment>();

    @ManyToOne(fetch = FetchType.LAZY)
    //foreign key in post table for ref category
    @JoinColumn(name="category_id")
    private Category category;

    public Post(){}


    public Post(Long id, String title, String description, String content,Set<Comment> comments,Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.comments = comments;
        this.category=category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id); // Use only the ID for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use only the ID for hashing
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}

/*

Immutable objects, with unchangeable states after creation, ensure thread safety and stable hash codes,
crucial for hash-based collections like HashMap. Avoid mutable fields in hashCode and equals to prevent inconsistent behavior.
Use immutable fields, synchronize access, or opt for thread-safe collections in multi-threaded environments.
Properly implement hashCode, equals, and toString for predictable object behavior.

* @Data gives getter and setter, toString ,equals, hashCode object, Constructor for @NotNull fields
* */
