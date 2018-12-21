package kr.or.recomments.commentserver.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.or.recomments.domain.Comment;
import kr.or.recomments.persistence.CommentDao;

@Service
public class CommentService {
	private CommentDao dao;
	private final Logger log = LoggerFactory.getLogger(CommentService.class);
	
	public CommentService(CommentDao dao) {
		this.dao = dao;
	}
	
	public Collection<Comment> findAll() {
		return dao.selectAll();
	}
	
	public Comment create(Comment comment) {

		String parentOrder = comment.getComment_order();
		String SiblingOrder = dao.selectByOrder(parentOrder);
		// .js에서 순회하는 편이 더 빠른가?
		// checkNull
		// 99 처리
		// delete처리 --> text만 지운다.// 노드를 지우는 것은 대댓글의 문맥을 헤치기 때문
		/*
		 * 부모 댓글을 지우면 어떻게 되나ㅏㅏㅏㅏㅏㅏ 
		 *	"지워진 댓글입니다."
		 */
		if( SiblingOrder != null ) {
			int IntSiblingOrder = Integer.parseInt(SiblingOrder.substring(parentOrder.length(), SiblingOrder.length()));
			IntSiblingOrder++;
			StringBuilder MyOrder = new StringBuilder(Integer.toString(IntSiblingOrder));
			log.info("MyOrder: {}", MyOrder);

			if(MyOrder.length() < 2) // 01 -> 1 -> 2 -> 02
				MyOrder.insert(0, "0");
			
			MyOrder.insert(0, parentOrder);
			comment.setComment_order(MyOrder.toString());
		}else {
			comment.setComment_order(parentOrder + "00");
		}
		
		Integer id = dao.insert(comment);
		comment.setId(id);
		return comment;
	}
}
