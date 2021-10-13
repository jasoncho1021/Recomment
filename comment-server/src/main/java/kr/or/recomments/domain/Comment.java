package kr.or.recomments.domain;

public class Comment {
	private Integer id;
	private String text;
	private String commentOrder;

	public Comment() {
	}

	public Comment(String commentOrder) {
		this.commentOrder = commentOrder;
	}

	public Comment(String text, String commentOrder) {
		this.text = text;
		this.commentOrder = commentOrder;
	}

	public String getCommentOrder() {
		return commentOrder;
	}

	public void setCommentOrder(String commentOrder) {
		this.commentOrder = commentOrder;
	}

	public Comment(Integer id, String text, String commentOrder) {
		this(text, commentOrder);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", commentOrder=" + commentOrder + "]";
	}

}
