package kr.or.recomments.commentserver.presentation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.or.recomments.commentserver.service.CommentService;
import kr.or.recomments.domain.Comment;
import kr.or.recomments.dto.CommentDTO;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	private final CommentService service;
	private final Logger log = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	public CommentController(CommentService service) {
		this.service = service;
	}

	@GetMapping
	List<CommentDTO> readList() {
		log.info("comments searched");
		CommentDTO root = service.findAll();
		return root.recomments;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Comment create(@RequestBody Comment comment) {
		log.info("comment creation request: {}", comment);

		Comment newComment = service.create(comment);
		log.info("comment created: {}", newComment);
		return newComment;

	}

}
