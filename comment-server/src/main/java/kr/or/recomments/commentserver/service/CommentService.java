package kr.or.recomments.commentserver.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.or.recomments.domain.Comment;
import kr.or.recomments.dto.CommentDTO;
import kr.or.recomments.persistence.CommentDao;

@Service
public class CommentService {
	private CommentDao dao;
	private final Logger log = LoggerFactory.getLogger(CommentService.class);
	private Set<Integer> reservedChar;

	public CommentService(CommentDao dao) {
		this.dao = dao;
		this.reservedChar = new HashSet<>();
		reservedChar.add((int) '/');
		reservedChar.add((int) '%');
		reservedChar.add((int) '_');
	}

	public CommentDTO findAll() {
		Collection<Comment> collection = dao.selectAll();

		for (Comment comment : collection) {
			comment.setCommentOrder(asciiToDigitOrder(comment.getCommentOrder()));
		}

		Comment[] commentArray = (Comment[]) collection.toArray(new Comment[collection.size()]);

		return makeNestedByLoop(commentArray);
	}

	private CommentDTO makeNestedByLoop(Comment[] list) {
		Stack<String> ancestorOrder = new Stack<>();

		String parentOrder = "";
		CommentDTO root = new CommentDTO(parentOrder);

		Map<String, CommentDTO> map = new HashMap<>();
		map.put(parentOrder, root);

		int len = list.length;
		Comment comment;
		CommentDTO parentDTO, childDTO;
		String childOrder;

		for (int idx = 0; idx < len; idx++) {
			comment = list[idx];

			childDTO = new CommentDTO(comment);
			childOrder = childDTO.comment.getCommentOrder();
			map.put(childOrder, childDTO);

			if (parentOrder.length() > childOrder.length()) {
				parentOrder = backTracking(ancestorOrder, parentOrder, childOrder, map);
			} else {
				String sib = childOrder.substring(0, parentOrder.length());
				if (parentOrder.equals(sib)) {
					parentDTO = map.get(parentOrder);
					childDTO = map.get(childOrder);
					parentDTO.recomments.add(childDTO);

					ancestorOrder.push(parentOrder);
					parentOrder = childOrder;
				} else {
					parentOrder = backTracking(ancestorOrder, parentOrder, childOrder, map);
				}
			}
		}

		return map.get("");
	}

	private String backTracking(Stack<String> ancestorOrder, String parentOrder, String childOrder,
			Map<String, CommentDTO> map) {
		while (!ancestorOrder.isEmpty()) {
			parentOrder = ancestorOrder.pop();
			if (parentOrder.length() > childOrder.length()) {
				continue;
			}
			String sib = childOrder.substring(0, parentOrder.length());
			if (parentOrder.equals(sib)) {
				break;
			}
		}

		CommentDTO parentDTO = map.get(parentOrder);
		CommentDTO childDTO = map.get(childOrder);
		parentDTO.recomments.add(childDTO);
		ancestorOrder.push(parentOrder);
		return childOrder;
	}

	private String asciiToDigitOrder(String commentOrder) {
		StringBuilder digitOrder = new StringBuilder();

		char[] order = commentOrder.toCharArray();
		for (char c : order) {
			if (c == '/') {
				digitOrder.append(c);
			} else {
				digitOrder.append((int) c);
			}
		}

		return digitOrder.toString();
	}

	public Comment create(Comment comment) {
		log.info("comment: {}", comment);

		if ("".equals(comment.getCommentOrder())) { // 원글 댓글.
			String sibling = dao.selectByParentOrder("");
			if (sibling == null) {
				sibling = "-1";
			} else {
				sibling = asciiToDigitOrder(sibling);
				log.info("root sibling: {}", sibling);
				int idx = sibling.indexOf("/");
				if (idx > -1) { // 대댓글 달린 원댓글.
					sibling = sibling.substring(0, idx);
				}
			}

			int digit = Integer.valueOf(sibling);
			digit++;
			if (reservedChar.contains(digit)) {
				digit++;
			}
			if (digit > 127) {
				throw new RuntimeException();
			}
			comment.setCommentOrder(digitToAsciiOrder(digit + ""));
		} else {
			String parentAsciiOrder = comment.getCommentOrder();
			String siblingAsciiOrder = asciiToDigitOrder(dao.selectByParentOrder(digitToAsciiOrder(parentAsciiOrder)));

			String sibling = siblingAsciiOrder.substring(parentAsciiOrder.length(), siblingAsciiOrder.length());

			log.info("parentAsciiOrder: {}", parentAsciiOrder);
			log.info("siblingAsciiOrder: {}", siblingAsciiOrder);
			log.info("sibling: {}", sibling);

			if ("".equals(sibling)) { // 첫 대댓글. 
				sibling = "-1";
			} else {
				int idx = sibling.indexOf("/", 1);
				if (idx > -1) {
					sibling = sibling.substring(1, idx);
				} else {
					sibling = sibling.substring(1, sibling.length());
				}
			}

			int digit = Integer.valueOf(sibling);
			digit++;

			if (reservedChar.contains(digit)) {
				digit++;
			}
			if (digit > 127) {
				throw new RuntimeException();
			}
			comment.setCommentOrder(digitToAsciiOrder(parentAsciiOrder + "/" + digit));
		}

		Integer id = dao.insert(comment);
		comment.setId(id);
		comment.setCommentOrder(asciiToDigitOrder(comment.getCommentOrder()));
		return comment;
	}

	private String digitToAsciiOrder(String digitOrder) {
		StringBuilder asciiOrder = new StringBuilder();

		String[] tokenized = digitOrder.split("/");
		int digit;
		for (String token : tokenized) {
			digit = Integer.valueOf(token);
			asciiOrder.append(((char) digit));
			asciiOrder.append("/");
		}
		asciiOrder.deleteCharAt(asciiOrder.length() - 1);

		return asciiOrder.toString();
	}

}
