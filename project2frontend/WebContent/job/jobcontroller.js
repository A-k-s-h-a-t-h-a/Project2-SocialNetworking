app.controller('JobCtrl',function($scope, JobService, $location, $rootScope)
{
	$scope.addJob= function(job)
	{
		console.log("Calling addjob function in controller");
		$scope.isClicked= false
		JobService.addJob(job).then(function(response)
		{
			alert('Job details inserted successfully')
			$location.path('/home')
		},
		function(response)
		{
			//3 return statement for error in middleware
			$scope.error= response.data
			if($scope.error.errorCode == 7)
			{//not logged in
				$rootScope.error= response.data
				$location.path('/login')
			}	
		})
	}
	
	function getActiveJobs()
	{//select * from job where active= true
		JobService.getActiveJobs().then(function(response)
		{
		//response.data is Array of Active jobs[{... active= true},{...,active= true}] for success
		$scope.activeJobs= response.data;
		},
		function(response)
		{
			//response.data is ErrorClazz object
			$scope.error= response.data
			if(response.status == 401)
				$location.path('/login')
		})
	}
	
	
	function getInActiveJobs()
	{
		JobService.getInActiveJobs().then(function(response)
		{
			//response.data is array of inactive jobs
			$scope.inActiveJobs= response.data
		},
		function(response)
		{
			$scope.error= response.data
			if(response.data.errorCode == 7)
				$location.path('/login')
		})
	}
	
	
	$scope.showJobDetails= function(id)
	{
		$scope.id= id
		$scope.isClicked= !$scope.isClicked;
	}
	
	
	$scope.deactivateJobPosition= function(job)
	{
		job.active= false //change the value of the property active from true to false
		JobService.updateActiveStatus(job).then(function(response)
		{
			getActiveJobs()
		},
		function(response)
		{
			$scope.error= response.data
			if(response.status == 401)
				$location.path('/login')
		})
	}
	
	
	$scope.activateJobPosition= function(job)
	{
		job.active= true
		JobService.updateActiveStatus(job).then(function(response)
		{
			getInActiveJobs()//$scope.inActiveJobs= select * from job where active= false
		},
		function(response)
		{
			$scope.error= response.data
			if(response.status == 401)
				$location.path('/login')
		})
	}
	
	
	getActiveJobs();
	
	if($rootScope.loggedInUser.role == 'ADMIN')
      getInActiveJobs()
      
})
