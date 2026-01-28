package com.rl.blogmanagementsystem.service;

import com.rl.blogmanagementsystem.dto.BlogRequest;
import com.rl.blogmanagementsystem.dto.BlogResponse;
import com.rl.blogmanagementsystem.entity.Blog;
import com.rl.blogmanagementsystem.entity.User;
import com.rl.blogmanagementsystem.mapper.BlogMapper;
import com.rl.blogmanagementsystem.repository.BlogRepository;
import com.rl.blogmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogService blogService;

    private User author;
    private Blog blog;
    private BlogResponse blogResponse;

    @BeforeEach
    void setup() {
        author = new User();
        author.setEmail("test@gmail.com");

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("title");
        blog.setContent("content");
        blog.setAuthor(author);

        blogResponse = new BlogResponse();
        blogResponse.setId(1L);
        blogResponse.setTitle("title");
    }

    @Test
    void createBlogShouldCreateBlog() {
        BlogRequest blogRequest = new BlogRequest();
        blogRequest.setTitle("Title");
        blogRequest.setContent("Content");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(author));

        when(blogRepository.save(any(Blog.class)))
                .thenReturn(blog);

        when(blogMapper.toEntity(blogRequest, author))
                .thenReturn(blog);

        when(blogMapper.toDto(blog))
                .thenReturn(blogResponse);

        BlogResponse res = blogService.createBlog(blogRequest, "test@gmail.com");

        Assertions.assertNotNull(res);
        Assertions.assertEquals("title", res.getTitle());

        verify(blogRepository).save(any(Blog.class));
    }

    @Test
    void updateBlogShouldUpdateOwnBlog() {
        BlogRequest blogRequest = new BlogRequest();
        blogRequest.setTitle("Title");
        blogRequest.setContent("Content");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(author));

        when(blogRepository.findByIdAndAuthor(1L, author))
                .thenReturn(Optional.of(blog));

        when(blogRepository.save(any(Blog.class)))
                .thenReturn(blog);


        when(blogMapper.toDto(blog))
                .thenReturn(blogResponse);

        BlogResponse updatedBlog = blogService.updateBlog(1L, blogRequest, "test@gmail.com");

        Assertions.assertNotNull(updatedBlog);
        verify(blogRepository).save(blog);
        verify(blogMapper).toDto(blog);
    }


    @Test
    void deleteBlogShouldDeleteBlog() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(author));

        when(blogRepository.findByIdAndAuthor(1L, author))
                .thenReturn(Optional.of(blog));

        doNothing().when(blogRepository).delete(blog);

        blogService.deleteBlog(1L, "test@gmail.com");

        verify(blogRepository).delete(blog);
    }

    @Test
    void getBlogsShouldReturnOwnerBlogs() {
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(author));

        when(blogRepository.findByAuthor(author))
                .thenReturn(List.of(blog));

        when(blogMapper.toDto(blog))
                .thenReturn(blogResponse);

        List<BlogResponse> res = blogService.getBlogs("test@gmail.com");

        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals("title", res.getFirst().getTitle());
    }

    @Test
    void getAllBlogsShouldReturnAllBlogs() {
        when(blogRepository.findAll())
                .thenReturn(List.of(blog));

        when(blogMapper.toDto(blog))
                .thenReturn(blogResponse);

        List<BlogResponse> res = blogService.getAllBlogs();

        Assertions.assertEquals(1, res.size());
    }
}
