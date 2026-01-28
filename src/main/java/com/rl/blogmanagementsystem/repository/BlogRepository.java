package com.rl.blogmanagementsystem.repository;

import com.rl.blogmanagementsystem.entity.Blog;
import com.rl.blogmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByIdAndAuthor(Long blogId, User author);
    List<Blog> findByAuthor(User author);
}
