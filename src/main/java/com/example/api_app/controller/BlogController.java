package com.example.api_app.controller;

import com.example.api_app.model.Blog;
import com.example.api_app.model.Comment;
import com.example.api_app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    // Insert blog
    @PostMapping
    public ResponseEntity<Blog> insertBlog(@RequestBody Blog blog) {
        Blog createdBlog = blogService.insertBlog(blog);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    // Insert comment
    @PostMapping("/{blogId}/comments")
    public ResponseEntity<Blog> insertComment(@PathVariable Long blogId, @RequestBody Comment comment) {
        Comment savedComment = blogService.insertComment(blogId, comment);
        if (savedComment != null) {
            return new ResponseEntity<>(savedComment.getBlog(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Filter blogs
    @GetMapping("/filter")
    public ResponseEntity<List<Blog>> filterBlogs(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String title,
                                                  @RequestParam(required = false) boolean published,
                                                  @RequestParam(required = false) LocalDateTime publishedDate) {
        List<Blog> filteredBlogs = blogService.filterBlogs(name, title, published, publishedDate);
        return new ResponseEntity<>(filteredBlogs, HttpStatus.OK);
    }

    // Get blog full info
    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getBlogFullInfo(@PathVariable Long blogId) {
        Blog blog = blogService.getBlogFullInfo(blogId);
        if (blog != null) {
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
