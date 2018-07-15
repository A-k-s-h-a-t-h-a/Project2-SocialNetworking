app.factory('BlogService',function($http)
{
	var blogService = {}
	
	blogService.addBlog = function(blog){
		return $http.post("http://localhost:8088/project2middleware/addblogpost",blog);
	}
	
	blogService.blogsApproved = function(){
		return $http.get("http://localhost:8088/project2middleware/blogsapproved")
	}
	
	blogService.blogsWaitingForApproval = function(){
		return $http.get("http://localhost:8088/project2middleware/blogswaitingforapproval")
	}
	
	blogService.getBlogPost = function(id){
		return $http.get("http://localhost:8088/project2middleware/getblogpost/"+id)
	}
	
	blogService.updateApprovalStatus = function(blog){
		return $http.put("http://localhost:8088/project2middleware/updateapprovalstatus",blog)
	}
	
	blogService.updateBlogPostLikes = function(id){
		return $http.put("http://localhost:8088/project2middleware/updateblogpostlikes/"+id)
	}

	blogService.addComment = function(bcomment,id){
		return $http.post("http://localhost:8088/project2middleware/addcomment/"+id,bcomment);
	}

	blogService.getBlogComments = function(id){
		return $http.get("http://localhost:8088/project2middleware/getblogcomments/"+id)
	}
	
	return blogService;
	
})
