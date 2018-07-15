app.controller('BlogCtrl',function($scope, $location, $rootScope, BlogService,$routeParams,$sce) 
{
	$scope.addBlog = function(blog) 
	{
		BlogService.addBlog(blog).then(function(response) 
		{
			alert('BlogPost is added successfully and it is waiting for approval');
			$location.path('/home')
		},
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	function blogsApproved()
	{
		BlogService.blogsApproved().then(function(response)
		{
			$scope.blogsApproved = response.data
		},
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	function blogsWaitingForApproval()
	{
		BlogService.blogsWaitingForApproval().then(function(response)
		{
			$scope.waitingblogs = response.data
		},
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	blogsApproved();
	
	if($rootScope.loggedInUser.role == 'ADMIN')
		blogsWaitingForApproval();
		

	$scope.approve = function(blog)
	{
		blog.approved = true
		BlogService.updateApprovalStatus(blog).then(function(response)
		{
			alert('BlogPost is approved successfully');
			BlogService.blogsWaitingForApproval().then(function(response)
			{
				$scope.waitingblogs = response.data;
				if($scope.waitingblogs.length != 0)
					$location.path('/blogswaitingforapproval')
				else
					$location.path('/blogsapproved')
			})
		},
		function(response)
		{
			$scope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	};
	
	$scope.reject = function(blog)
	{
		blog.approved = false
		BlogService.updateApprovalStatus(blog).then(function(response)
		{
			alert('BlogPost is deleted successfully');
			BlogService.blogsWaitingForApproval().then(function(response)
			{
				$scope.waitingblogs = response.data;
				if($scope.waitingblogs.length != 0)
					$location.path('/blogswaitingforapproval')
				else
					$location.path('/blogsapproved')
			})
		},
		function(response)
		{
			$scope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
		
		

	var id = $routeParams.id;
			
	BlogService.getBlogPost(id).then(function(response)
	{
		$scope.blog = response.data
		console.log('selected blog '+$scope.blog);
		$scope.content = $sce.trustAsHtml($scope.blog.blogContent)
	},
	function(response)
	{
		$rootScope.error  =  response.data
		if (response.status == 401)
			$location.path('/login')
	})
		
	$scope.updateLikes = function(id)
	{
		BlogService.updateBlogPostLikes(id).then(function(response)
		{
			$scope.blog = response.data
			$scope.isLiked = !$scope.isLiked;
		},
		function(response)
		{
			$scope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	$scope.addcomment = function(bcomment, id) 
	{
		BlogService.addComment(bcomment, id).then(function(response) 
		{
			alert('Your comment has been added');
			$location.path('/getblogpost/'+ id)
		},
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	function BlogComments()
	{
		BlogService.getBlogComments(id).then(function(response)
		{
			$scope.blogcomments = response.data
			if($scope.blogcomments.length != 0)
				$location.path('/getblogpost/'+ id)
			else
				$location.path('/blogsapproved')
		},
		function(response)
		{
			$rootScope.error  =  response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	BlogComments();
			
})
