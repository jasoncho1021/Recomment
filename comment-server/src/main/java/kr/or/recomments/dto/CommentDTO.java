package kr.or.recomments.dto;

import java.util.LinkedList;
import java.util.List;

import kr.or.recomments.domain.Comment;

public class CommentDTO {
	public Comment comment;
	public List<CommentDTO> recomments = new LinkedList<>();

	public CommentDTO(String commentOrder) {
		this.comment = new Comment(commentOrder);
	}

	public CommentDTO(Comment comment) {
		this.comment = comment;
	}
}
