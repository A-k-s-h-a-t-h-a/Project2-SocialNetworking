package com.niit.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.model.Chat;

@RestController
public class ChatController 
{
	private static final Log logger = LogFactory.getLog(ChatController.class);
	private final SimpMessagingTemplate messagingTemplate;
	private List<String> users = new ArrayList<String>();

	@Autowired

	public ChatController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@SubscribeMapping("/join/{username}")
	public List<String> join(@DestinationVariable("username") String username) 
	{
		System.out.println("username in sockcontroller" + username);
		if (!users.contains(username)) {
			users.add(username);
		}

		System.out.println("====JOIN==== " + username);
		// notify all subscribers of new user
		messagingTemplate.convertAndSend("/topic/join", username);
		return users;
	}

	@MessageMapping(value = "/chat")
	public void chatReceived(Chat chat) 
	{
		if ("all".equals(chat.getTo())) {
			System.out.println("IN CHAT RECEIVED " + chat.getMessage() + " " + chat.getFrom() + " to " + chat.getTo());
			messagingTemplate.convertAndSend("/queue/chats", chat);
		}

		else {
			System.out.println("CHAT TO " + chat.getTo() + " From " + chat.getFrom() + " Message " + chat.getMessage());
			messagingTemplate.convertAndSend("/queue/chats/" + chat.getTo(), chat);
			messagingTemplate.convertAndSend("/queue/chats/" + chat.getFrom(), chat);
		}
	}

}
