package com.niit.dao;

import java.util.List;

import com.niit.model.BlogComment;
import com.niit.model.BlogPost;
import com.niit.model.BlogPostLikes;

public interface BlogPostDao {
	
	public void saveBlogPost(BlogPost blogPost);
	public List<BlogPost> approvedBlogs();
	public List<BlogPost> blogsWaitingForApproval();
	public BlogPost getBlogPost(int id);
	public void updateApprovalStatus(BlogPost blogPost);
	
	public BlogPostLikes hasUserLikedBlogPost(int blogId, String email);
	public BlogPost updateBlogPostLikes(int blogPostId, String email);
	
	void addBlogComment(BlogComment blogComment);
	List<BlogComment> getAllBlogComment(int blogPostId);
}
