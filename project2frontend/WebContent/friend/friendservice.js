app.factory('FriendService',function($http)
{
	var friendService = {}

	friendService.addFriend = function(toId){
		return $http.post("http://localhost:8088/project2middleware/addfriend",toId)
	}

	friendService.acceptRequest = function(request){
		return $http.post("http://localhost:8088/project2middleware/acceptrequest",request);
	}
	
	friendService.deleteRequest = function(request){
		return $http.post("http://localhost:8088/project2middleware/deleterequest",request);
	}
	
	friendService.getAllSuggestedUsers = function(){
		return $http.get("http://localhost:8088/project2middleware/suggestedUsers")
	}
	
	friendService.getPendingRequests = function(){
		return $http.get("http://localhost:8088/project2middleware/pendingRequests")
	}
	
	friendService.getAllFriends = function(){
		return $http.get("http://localhost:8088/project2middleware/friends");
	}
	
	return friendService;
})