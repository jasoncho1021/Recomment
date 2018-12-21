package kr.or.recomments.domain;

public class Comment {
	private Integer id;
	private String text;
	private String comment_order;
	public Comment() {		
	}
	public Comment(String text, String comment_order) {
		this.text = text;
		this.comment_order = comment_order;
	}
	public Comment(Integer id, String text, String comment_order) {
		this(text, comment_order);
		this.id = id;		
	}	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", comment_order=" + comment_order + "]";
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getComment_order() {
		return comment_order;
	}
	public void setComment_order(String comment_order) {
		this.comment_order = comment_order;
	}
	
}
