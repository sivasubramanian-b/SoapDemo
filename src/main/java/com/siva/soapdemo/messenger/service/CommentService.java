package com.siva.soapdemo.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.siva.soapdemo.messenger.database.DatabaseClass;
import com.siva.soapdemo.messenger.exception.DataNotFoundException;
import com.siva.soapdemo.messenger.model.Comment;
import com.siva.soapdemo.messenger.model.Message;

@WebService
public class CommentService {
	
	private Map<Long, Message> messages = DatabaseClass.getMessages();
	
	public List<Comment> getAllComments(long messageId) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		return new ArrayList<Comment>(comments.values());
	}
	
	
	  public CommentService() { this.addComment(1L, new Comment(1,
	  "Hello from otherside", "team1")); this.addComment(2L, new Comment(2,
	  "Hello", "team2")); }
	 
	
	public Comment getComment(long messageId, long commentId) {	
		Message message = messages.get(messageId);
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		Comment comment = comments.get(commentId);
	if (comment == null) {
			throw new DataNotFoundException("invalid comment id");
		}
		return comment;
	}
	
	public Comment addComment(long messageId, Comment comment) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		comment.setId(comments.size() + 1);
		comments.put(comment.getId(), comment);
		return comment;
	}
	
	public Comment updateComment(long messageId, Comment comment) throws DataNotFoundException {
		Message message = messages.get(messageId);
		if (message==null) throw new DataNotFoundException("Invalid Message id");
	    Map<Long, Comment> comments=message.getComments();
		if (comments==null) throw new DataNotFoundException("Invalid comment id");
		if (comment.getId() <= 0) {
			return null;
		}
		comments.put(comment.getId(), comment);
		return comment;
	}
	
	public Comment removeComment(long messageId, long commentId) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		return comments.remove(commentId);
	}
		
}
