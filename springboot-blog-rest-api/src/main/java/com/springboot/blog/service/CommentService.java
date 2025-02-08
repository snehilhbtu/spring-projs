package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    public CommentDto createComment(Long postId,CommentDto commentDto);

    public List<CommentDto> getAllCommentByPost(Long postId);

}
