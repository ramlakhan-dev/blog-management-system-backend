package com.rl.blogmanagementsystem.controller;

import com.rl.blogmanagementsystem.dto.BlogRequest;
import com.rl.blogmanagementsystem.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public ResponseEntity<?> createBlog(
            @RequestBody BlogRequest blogRequest,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return new ResponseEntity<>(blogService.createBlog(blogRequest, email), HttpStatus.CREATED);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<?> updateBlog(
            @PathVariable Long blogId,
            @RequestBody BlogRequest blogRequest,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return new ResponseEntity<>(blogService.updateBlog(blogId, blogRequest, email), HttpStatus.OK);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> deleteBlog(
            @PathVariable Long blogId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        blogService.deleteBlog(blogId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getBlogs(Authentication authentication) {
        String email = authentication.getName();
        return new ResponseEntity<>(blogService.getBlogs(email), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        return new ResponseEntity<>(blogService.getAllBlogs(), HttpStatus.OK);
    }
}
