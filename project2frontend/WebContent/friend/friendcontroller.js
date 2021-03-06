app.controller('FriendCtrl', function($scope, $location, $rootScope, FriendService) 
{
	$scope.suggestedUsers = [];

	$scope.addFriend = function(toId) 
	{
		console.log('Friend email ' + toId);
		FriendService.addFriend(toId).then(function(response)
		{
			alert('Friend request has been sent successfully')
			getAllSuggestedUsers()
		},
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}

	$scope.acceptRequest = function(request) 
	{
		FriendService.acceptRequest(request).then(function(response) 
		{
			getPendingRequests()
		}, 
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	$scope.deleteRequest = function(request)
	{
		FriendService.deleteRequest(request).then(function(response) 
		{
			getPendingRequests()
		}, 
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	function getAllSuggestedUsers() 
	{
		console.log("Suggested users fun in ctrler");
		FriendService.getAllSuggestedUsers().then(function(response) 
		{
			$scope.suggestedUsers = response.data
			console.log('All suggested users ' + $scope.suggestedUsers);
			for (var i = 0; i < $scope.suggestedUsers.length; i++) 
			{	console.log(JSON.stringify($scope.suggestedUsers[i]));	}
		}, 
		function(response)
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	function getPendingRequests()
	{
		console.log("Pending req fun in ctrler");
		FriendService.getPendingRequests().then(function(response) 
		{
			$scope.pendingRequests = response.data
		}, 
		function(response) 
		{
			$rootScope.error = response.data
			if (response.status == 401)
				$location.path('/login')
		})
	}
	
	FriendService.getAllFriends().then(function(response) 
	{
		$rootScope.friends = response.data
	},
	function(response) 
	{
		$rootScope.error = response.data
		if (response.status == 401)
			$location.path('/login')
	})

	getAllSuggestedUsers();
	getPendingRequests();
	
})
