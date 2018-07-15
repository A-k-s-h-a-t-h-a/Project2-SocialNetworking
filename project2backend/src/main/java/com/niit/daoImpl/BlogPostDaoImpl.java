package com.niit.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.BlogPostDao;
import com.niit.model.BlogComment;
import com.niit.model.BlogPost;
import com.niit.model.BlogPostLikes;
import com.niit.model.User;

@Repository
@Transactional
public class BlogPostDaoImpl implements BlogPostDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public BlogPostDaoImpl() {
		System.out.println("BlogPostDaoImpl bean is created");
	}

	public void saveBlogPost(BlogPost blogPost) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(blogPost);
	}

	public List<BlogPost> approvedBlogs() 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BlogPost where approved=true");
		return query.list();
	}

	public List<BlogPost> blogsWaitingForApproval() 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BlogPost where approved=false");
		return query.list();
	}

	public BlogPost getBlogPost(int id) 
	{
		Session session = sessionFactory.getCurrentSession();
		BlogPost blogPost = (BlogPost)session.get(BlogPost.class,id);
		return blogPost;
	}

	public void updateApprovalStatus(BlogPost blogPost) 
	{
		Session session = sessionFactory.getCurrentSession();
		if(blogPost.isApproved()){
			session.update(blogPost);
		}
		else{
			session.delete(blogPost);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	public BlogPostLikes hasUserLikedBlogPost(int blogpostId, String email) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BlogPostLikes where blogPost.id=" + blogpostId + " and user.email='" + email + "'");
//	    query.setInteger(0,blogpostId);
//		query.setString(1,email);
		BlogPostLikes blogPostLikes = (BlogPostLikes) query.uniqueResult();
		return blogPostLikes;
	}


	public BlogPost updateBlogPostLikes(int blogPostId, String email)
	{
		Session session = sessionFactory.getCurrentSession();
		BlogPostLikes blogPostLikes = hasUserLikedBlogPost(blogPostId, email);
		BlogPost blogPost = (BlogPost) session.get(BlogPost.class, blogPostId);
		
		if (blogPostLikes == null) {
			blogPostLikes = new BlogPostLikes();
			User user = (User) session.get(User.class, email);

			blogPostLikes.setBlogPost(blogPost);
			blogPostLikes.setUser(user);
			session.save(blogPostLikes);
			
			blogPost.setLikes(blogPost.getLikes() + 1);
			session.update(blogPost);
		} 
		else 
		{
			session.delete(blogPostLikes);
			blogPost.setLikes(blogPost.getLikes() - 1);
			session.update(blogPost);
		}
		return blogPost;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void addBlogComment(BlogComment blogComment) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(blogComment);
	}

	public List<BlogComment> getAllBlogComment(int blogPostId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BlogComment where blogPost.id=?");
		query.setInteger(0, blogPostId);
		List<BlogComment> blogComments = query.list();
		return blogComments;
	}
}
