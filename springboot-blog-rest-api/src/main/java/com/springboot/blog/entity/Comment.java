package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    //@JsonBackReference // Marks this as the "child" side
    //@JsonIgnore // Ignores the post field during serialization
    private Post post;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return id == comment.id; // Use only the ID for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use only the ID for hashing
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", post=" + post +
                '}';
    }

    /*
    @OneToMany	One parent entity is related to multiple child entities.
    @ManyToOne	Multiple child entities relate to one parent entity.
    mappedBy	Used in @OneToMany to indicate the inverse side of the relationship.
    @JoinColumn	Used in @ManyToOne to define the foreign key column in the child table.
    */


    public Comment() {}

    public Comment(long id, String body, String email, String name, Post post) {
        this.id = id;
        this.body = body;
        this.email = email;
        this.name = name;
        this.post = post;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


}

/*

Immutable objects, with unchangeable states after creation, ensure thread safety and stable hash codes,
crucial for hash-based collections like HashMap. Avoid mutable fields in hashCode and equals to prevent inconsistent behavior.
Use immutable fields, synchronize access, or opt for thread-safe collections in multi-threaded environments.
Properly implement hashCode, equals, and toString for predictable object behavior.

* @Data gives getter and setter, toString ,equals, hashCode object, Constructor for @NotNull fields
* */