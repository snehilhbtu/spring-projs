package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.blog.utils.AppConstants.COMMENT_AND_POST_DONT_RELATE;

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

        return PostMapper.toCommentDto(newComment);
    }

    @Override
    public List<CommentDto> getAllCommentByPost(Long postId) {

        List<Comment> commentList=commentRepository.findAllByPostId(postId);

        return commentList.stream().map(PostMapper::toCommentDto).toList();

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if(!doesCommentBelongsToPost(postId,commentId))
            throw new BlogApiException(HttpStatus.BAD_REQUEST,COMMENT_AND_POST_DONT_RELATE);

        return PostMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId,CommentDto updatedCommentDto) {

        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if(!doesCommentBelongsToPost(postId,commentId))
            throw new BlogApiException(HttpStatus.BAD_REQUEST,COMMENT_AND_POST_DONT_RELATE);

        comment.setName(updatedCommentDto.getName());
        comment.setEmail(updatedCommentDto.getEmail());
        comment.setBody(updatedCommentDto.getBody());

        return PostMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public String deleteComment(Long postId, Long commentId) {

        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if(!doesCommentBelongsToPost(postId,commentId))
            throw new BlogApiException(HttpStatus.BAD_REQUEST,COMMENT_AND_POST_DONT_RELATE);

        commentRepository.delete(comment);

        return "Comment Deleted SuccessFully";
    }

    //to check if comment and post relate
    private boolean doesCommentBelongsToPost(Long postId, Long commentId){

        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));

        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        return comment.getPost().getId().equals(post.getId());

    }


}
