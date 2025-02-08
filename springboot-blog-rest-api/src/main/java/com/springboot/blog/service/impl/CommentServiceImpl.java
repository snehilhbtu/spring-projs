package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.PostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository){
        this.commentRepository=commentRepository;
        this.postRepository=postRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));

        Comment comment= PostMapper.toComment(commentDto);
        comment.setPost(post);

        Comment newComment=commentRepository.save(comment);

        return PostMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentByPost(Long postId) {
        return null;
    }
}
