package com.example.api_app.service;

import com.example.api_app.model.Blog;
import com.example.api_app.model.Comment;
import com.example.api_app.repository.BlogRepository;
import com.example.api_app.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Blog insertBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Comment insertComment(Long blogId, Comment comment) {
        Blog blog = blogRepository.findById(blogId).orElse(null);
        if (blog != null) {
            comment.setBlog(blog);
            return commentRepository.save(comment);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Blog> filterBlogs(String name, String title, boolean published, LocalDateTime publishedDate) {
        if (name != null && title != null && publishedDate != null) {
            // Filter by name, title, published status, and published date
            return blogRepository.findByNameAndTitleAndPublishedAndPublishedDate(name, title, published, publishedDate);
        } else if (name != null && title != null) {
            // Filter by name and title
            return blogRepository.findByNameAndTitle(name, title);
        } else if (name != null) {
            // Filter by name
            return blogRepository.findByName(name);
        } else if (title != null) {
            // Filter by title
            return blogRepository.findByTitle(title);
        } else if (publishedDate != null) {
            // Filter by published date
            return blogRepository.findByPublishedDate(publishedDate);
        } else {
            // No filters applied, return all blogs
            return blogRepository.findAll();
        }
    }
    @Transactional(readOnly = true)
    public Blog getBlogFullInfo(Long blogId) {
        return blogRepository.findBlogWithImagesAndCommentsById(blogId);
    }
}
