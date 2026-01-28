package com.rl.blogmanagementsystem.service;

import com.rl.blogmanagementsystem.dto.BlogRequest;
import com.rl.blogmanagementsystem.dto.BlogResponse;
import com.rl.blogmanagementsystem.entity.Blog;
import com.rl.blogmanagementsystem.entity.User;
import com.rl.blogmanagementsystem.mapper.BlogMapper;
import com.rl.blogmanagementsystem.repository.BlogRepository;
import com.rl.blogmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogMapper blogMapper;

    public BlogResponse createBlog(BlogRequest blogRequest, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = blogMapper.toEntity(blogRequest, author);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toDto(savedBlog);
    }


    public BlogResponse updateBlog(Long blogId, BlogRequest blogRequest, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog savedBlog = blogRepository.findByIdAndAuthor(blogId, author)
                .orElseThrow(() -> new RuntimeException("Blog not found or access denied"));

        savedBlog.setTitle(blogRequest.getTitle());
        savedBlog.setContent(blogRequest.getContent());

        Blog updatedBlog = blogRepository.save(savedBlog);
        return blogMapper.toDto(updatedBlog);
    }

    public void deleteBlog(Long blogId, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog savedBlog = blogRepository.findByIdAndAuthor(blogId, author)
                .orElseThrow(() -> new RuntimeException("Blog not found or access denied"));

        blogRepository.delete(savedBlog);
    }

    public List<BlogResponse> getBlogs(String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return blogRepository.findByAuthor(author)
                .stream()
                .map(blogMapper::toDto)
                .toList();
    }

    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(blogMapper::toDto)
                .toList();
    }
}
