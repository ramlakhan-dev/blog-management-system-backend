package com.rl.blogmanagementsystem.mapper;

import com.rl.blogmanagementsystem.dto.BlogRequest;
import com.rl.blogmanagementsystem.dto.BlogResponse;
import com.rl.blogmanagementsystem.entity.Blog;
import com.rl.blogmanagementsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public BlogResponse toDto(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getAuthor().getName()
        );
    }

    public Blog toEntity(BlogRequest blogRequest, User author) {
        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(author);
        return blog;
    }
}
