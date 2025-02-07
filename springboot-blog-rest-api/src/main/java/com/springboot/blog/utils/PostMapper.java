package com.springboot.blog.utils;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.entity.Post;

public class PostMapper {

    //post --> postDto

    public static PostDto toPostDto(Post post){
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());

        return postDto;

    }


    //postDto --> post
    public static Post toPost(PostDto postDto){
        Post post=new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        return post;
    }

}
