package com.springboot.blog.service.impl;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.BlogMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository,CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository=commentRepository;
    }


    @Override
    public PostDto createPost(PostDto postDto) {
        Post newPost = postRepository.save(BlogMapper.toPost(postDto));

        PostDto responsePost = BlogMapper.toPostDto(newPost);

        return responsePost;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String orderBy) {

        //making a sort object based on sortBy and orderBy to pass in pageable

        Sort sort = orderBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //feature function
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        //getting pages of post based on pageable
        Page<Post> pages = postRepository.findAll(pageable);

        List<Post> postList = pages.getContent();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postList.stream().map(BlogMapper::toPostDto).toList());
        postResponse.setPageNo(pages.getNumber());
        postResponse.setPageSize(pages.getSize());
        postResponse.setTotalPages(pages.getTotalPages());
        postResponse.setTotalElement(pages.getTotalElements());
        postResponse.setLast(pages.isLast());


        return postResponse;
    }


    @Override
    public PostDto getPostById(long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

        return BlogMapper.toPostDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post updatedPost = postRepository.save(post);

        return BlogMapper.toPostDto(updatedPost);

    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

        postRepository.delete(post);

    }

}
