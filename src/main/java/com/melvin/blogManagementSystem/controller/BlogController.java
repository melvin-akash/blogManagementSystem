package com.melvin.blogManagementSystem.controller;

import com.melvin.blogManagementSystem.dto.BlogDto;
import com.melvin.blogManagementSystem.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @PostMapping("/savePost")
    public ResponseEntity<?> saveBlog(@RequestBody BlogDto blogDto){
        try {
            blogService.saveBlogPost(blogDto);
            Map<String , Object> map = new HashMap<>();
            map.put("Post created" , blogDto);

            return ResponseEntity.ok(map);

        }catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Post cannot be created");
            map.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);

        }
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<?> getAllPosts() {
        try {

            return ResponseEntity.ok(blogService.getAllBlogs());

        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Posts cannot be fetched");
            map.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

}
