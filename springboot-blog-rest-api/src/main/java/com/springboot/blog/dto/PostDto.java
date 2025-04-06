package com.springboot.blog.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min=2,message = "title must be more than 2 in length")
    private String title;

    @NotEmpty
    @Size(min=5,message = "description must be more than 5 in length")
    private String description;

    @NotEmpty
    @Size(min=10,message = "content must be more than 10 in length")
    private String content;

    public Set<CommentDto> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDto> comments) {
        this.comments = comments;
    }

    private Set<CommentDto> comments;


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


}
