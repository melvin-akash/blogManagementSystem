package com.melvin.blogManagementSystem.service;

import com.melvin.blogManagementSystem.dto.BlogDto;
import com.melvin.blogManagementSystem.entity.BlogPost;
import com.melvin.blogManagementSystem.jwt.JwtUtils;
import com.melvin.blogManagementSystem.repo.BlogPostRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    BlogPostRepo repo;

    @Autowired
    JwtUtils utils;

    @Autowired
    HttpServletRequest request;

    public void saveBlogPost(BlogDto blogDto){

        String username = utils.getUserNameFromJwtToken(utils.getJwtFromHeader(request));

        BlogPost blogPost = new BlogPost();
        blogPost.setUsername(username);
        blogPost.setTitle(blogDto.getTitle());
        blogPost.setContent(blogDto.getContent());
        blogPost.setTags(blogDto.getTags());

        repo.save(blogPost);
    }

    public List<BlogPost> getAllBlogs(){
        return repo.findAll();
    }
}
