package com.springboot.blog.dto;

/*
* i was using postDto here ,i guess due to which recursion was happening
* */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class CommentDto {

    private long id;

    @NotEmpty
    @Size(min=10,message = "body must be 10 in length")
    private String body;
    @NotEmpty
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotEmpty
    @Size(min=5,message = "name must be 5 in length")
    private String name;

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



}
