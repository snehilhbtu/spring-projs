package com.springboot.blog.controller;


import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId")long postId,
                                                 @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}/")
    public CommentDto getCommentById(@PathVariable("postId")long postId,@PathVariable("commentId")long commentId){
        return commentService.getCommentById(postId,commentId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentByPostId(@PathVariable("postId")long postId){
        return commentService.getAllCommentByPost(postId);
    }

    @PutMapping("/{commentId}/")
    @PreAuthorize("hasRole('ADMIN')")
    public CommentDto updateComment(@PathVariable("postId")long postId,@PathVariable("commentId")long commentId,@Valid @RequestBody CommentDto commentDto){
        return commentService.updateComment(postId,commentId,commentDto);
    }

    @DeleteMapping("/{commentId}/")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteComment(@PathVariable("postId")long postId,@PathVariable("commentId")long commentId){
        return commentService.deleteComment(postId,commentId);
    }

}
