package com.springboot.blog.dto;

import java.util.List;
/*
* this response is designed to have page related data, like numbber,size,etc
* including postsdata list
*
* */
public class PostResponse {

    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private Long totalElement;
    private int totalPages;
    private boolean last;

    public PostResponse() {
    }

    public PostResponse(List<PostDto> content, int pageNo, int pageSize, Long totalElement, int totalPages, boolean last) {
        this.content = content;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElement = totalElement;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<PostDto> getContent() {
        return content;
    }

    public void setContent(List<PostDto> content) {
        this.content = content;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }


}
