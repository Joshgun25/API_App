package com.example.api_app.repository;

import com.example.api_app.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByNameAndTitleAndPublishedAndPublishedDate(String name, String title, boolean published, LocalDateTime publishedDate);

    List<Blog> findByNameAndTitle(String name, String title);

    List<Blog> findByName(String name);

    List<Blog> findByTitle(String title);

    List<Blog> findByPublishedDate(LocalDateTime publishedDate);

    // Custom query to fetch blog with images and comments by ID
    @Query("SELECT b, i, c FROM Blog b LEFT JOIN BlogImage i ON b.id = i.blogId LEFT JOIN Comment c ON b.id = c.blog.id WHERE b.id = :id")
    Blog findBlogWithImagesAndCommentsById(Long id);
}