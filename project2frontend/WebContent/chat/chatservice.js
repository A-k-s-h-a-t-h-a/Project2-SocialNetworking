app.filter('reverse', function() 
	{
	  return function(items) {
	    return items.slice().reverse();
	  };
	});

app.directive('ngFocus', function() 
	{
		return function(scope, element, attrs) {
			element.bind('click', function() {
				$('.' + attrs.ngFocus)[0].focus();
    });
  };
});

app.factory('ChatService', function($rootScope,$http)
	{
		var chatService={}
		chatService.getAllFriends=function(){
			return $http.get("http://localhost:8088/project2middleware/friends");
		}
	
	var ulist=$http.get("http://localhost:8088/project2middleware/friends");
	var socket = new SockJS('/project2middleware/chatmodule');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '', function(frame) {
      $rootScope.$broadcast('sockConnected', frame);
    });
    
    return {
      stompClient: stompClient,
      users:chatService.getAllFriends()
    };
});