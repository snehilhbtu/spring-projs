package com.springboot.blog.utils;

import com.springboot.blog.dto.CategoryDto;
import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.dto.PostDto;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;

import java.util.HashSet;

public class BlogMapper {

    //post --> postDto
    //while calling post to postDto we have to include comments inside posts
    public static PostDto toPostDto(Post post){
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());
        postDto.setComments(new HashSet<>(post.getComments().stream().map(BlogMapper::toCommentDto).toList()));
        postDto.setCategoryId(post.getCategory().getId());
        return postDto;

    }


    //postDto --> post


    /*You should not include setComments in toPost(PostDto postDto)
    since comments are managed separately via dedicated APIs and are not part of the PostDto request body.
    Setting post.setComments(postDto.getComments())
    risks unintentionally removing existing comments, especially with cascade = CascadeType.ALL, orphanRemoval = true.
    To prevent data loss, toPost(PostDto postDto) should exclude comments,
    while toPostDto(Post post) may include them for read operations, ensuring comments are handled independently.*/
    public static Post toPost(PostDto postDto){
        Post post=new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        return post;
    }

    //comment --> commentDto
    public static CommentDto toCommentDto(Comment comment){
        CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
        commentDto.setName(comment.getName());

        return commentDto;
    }

    //commentDto --> comment
    public static Comment toComment(CommentDto commentDto){
        Comment comment=new Comment();
        comment.setId(commentDto.getId());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());

        return comment;
    }

    public static Category toCategory(CategoryDto categoryDto){
        Category category=new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return category;

    }

    public static CategoryDto toCategoryDto(Category category){
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;

    }


}
