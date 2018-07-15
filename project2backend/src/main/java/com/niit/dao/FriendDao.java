package com.niit.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.niit.model.Friend;
import com.niit.model.User;

@Repository
public interface FriendDao {

	void addFriend(Friend friend);

	List<Friend> listOfFriends(String email);

	void acceptRequest(Friend request);

	void deleteRequest(Friend request);

	List<User> suggestedUsers(String email);

	List<Friend> pendingRequests(String toEmailId);

}
