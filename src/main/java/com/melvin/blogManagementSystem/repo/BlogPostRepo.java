package com.melvin.blogManagementSystem.repo;

import com.melvin.blogManagementSystem.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepo extends JpaRepository<BlogPost , Integer> {

}
