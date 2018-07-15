package com.niit.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.BlogPostDao;
import com.niit.dao.UserDao;
import com.niit.model.BlogComment;
import com.niit.model.BlogPost;
import com.niit.model.BlogPostLikes;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@RestController
public class BlogPostController {

	@Autowired
	private BlogPostDao blogPostDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	BlogComment blogComment;
	
	@RequestMapping(value = "/addblogpost", method = RequestMethod.POST)
	public ResponseEntity<?> saveBlogPost(@RequestBody BlogPost blogPost, HttpSession session) 
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		blogPost.setPostedOn(new Date());
		blogPost.setPostedBy(userDao.getUser(email));
		blogPostDao.saveBlogPost(blogPost);
		return new ResponseEntity<BlogPost>(blogPost, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/blogsapproved", method = RequestMethod.GET)
	public ResponseEntity<?> getBlogsApproved( HttpSession session) 
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		List<BlogPost> blogsApprovedList = blogPostDao.approvedBlogs();
		return new ResponseEntity<List<BlogPost>>(blogsApprovedList, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/blogswaitingforapproval", method = RequestMethod.GET)
	public ResponseEntity<?> getBlogsWaitingForApproval( HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		User user=userDao.getUser(email);
		if (!user.getRole().equals("ADMIN")) {
			ErrorClazz errorClazz = new ErrorClazz(8, "Access denied");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		List<BlogPost> blogsWaitingForApproval = blogPostDao.blogsWaitingForApproval();
		return new ResponseEntity<List<BlogPost>>(blogsWaitingForApproval, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getblogpost/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBlogPost(@PathVariable int id, HttpSession session) 
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		BlogPost blogPost=blogPostDao.getBlogPost(id);
		return new ResponseEntity<BlogPost>(blogPost, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/updateapprovalstatus", method = RequestMethod.PUT)
	public ResponseEntity<?> updateApprovalStatus(@RequestBody BlogPost blogPost, HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		User user=userDao.getUser(email);
		if (!user.getRole().equals("ADMIN")) {
			ErrorClazz errorClazz = new ErrorClazz(8, "Access denied");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		
		try{
			blogPostDao.updateApprovalStatus(blogPost);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			ErrorClazz errorClazz=new ErrorClazz(10,"Unable to Approve/reject the blogpost.."+e.getMessage());
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@RequestMapping(value="/hasuserlikedblog/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> hasUserLikedBlogPost(@PathVariable int id,HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		BlogPostLikes blogPostLikes = blogPostDao.hasUserLikedBlogPost(id, email);
		return new ResponseEntity<BlogPostLikes>(blogPostLikes, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/updateblogpostlikes/{id}",method=RequestMethod.PUT)
	public ResponseEntity<?> updateBlogPostLikes(@PathVariable int id,HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz errorClazz = new ErrorClazz(7, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(errorClazz, HttpStatus.UNAUTHORIZED);
		}
		BlogPost blogPost=blogPostDao.updateBlogPostLikes(id, email);
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@RequestMapping(value = "/addcomment/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> addBlogComment(@PathVariable int id, @RequestBody String comment, HttpSession session) 
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}

		User commentedBy = userDao.getUser(email);
		blogComment.setCommentedon(new Date());
		blogComment.setCommentText(comment);
		blogComment.setCommentedBy(commentedBy);
		BlogPost bp = blogPostDao.getBlogPost(id);
		blogComment.setBlogPost(bp);
		
		try {
			blogPostDao.addBlogComment(blogComment);
		} 
		catch (Exception e) {
			ErrorClazz error = new ErrorClazz(6, "Unable to post the comment" + e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BlogComment>(blogComment, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/getblogcomments/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllComments(@PathVariable int id, HttpSession session) 
	{
		String email = (String) session.getAttribute("email");
		if (email == null) {
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access...");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		List<BlogComment> blogComments = blogPostDao.getAllBlogComment(id);
		return new ResponseEntity<List<BlogComment>>(blogComments, HttpStatus.OK);
	}
}
