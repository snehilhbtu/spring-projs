package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getAllCommentByPost(Long postId);

    CommentDto getCommentById(Long postId,Long commentId);

    CommentDto updateComment(Long postId,Long commentId,CommentDto commentDto);

    String deleteComment(Long postId,Long commentId);

}
