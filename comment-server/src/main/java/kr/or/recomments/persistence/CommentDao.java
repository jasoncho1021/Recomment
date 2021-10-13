package kr.or.recomments.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.recomments.domain.Comment;

@Repository
public class CommentDao {

	private static final String SELECT_BY_ORDER = "SELECT MAX(COMMENT_ORDER) from COMMENT where COMMENT_ORDER like :corder and length(COMMENT_ORDER) = :clen";
	private static final String SELECT_BY_PARENT_ORDER = "SELECT MAX(COMMENT_ORDER) from COMMENT where COMMENT_ORDER like :corder";
	private static final String SELECT_ALL = "SELECT * FROM COMMENT ORDER BY COMMENT_order";

	private NamedParameterJdbcTemplate jdbc;
	private RowMapper<Comment> rowMapper = BeanPropertyRowMapper.newInstance(Comment.class);
	private SimpleJdbcInsert insertAction;

	public CommentDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("comment").usingGeneratedKeyColumns("id");
	}

	public List<Comment> selectAll() {
		Map<String, Object> params = Collections.emptyMap();
		return jdbc.query(SELECT_ALL, params, rowMapper);
	}

	public String selectByParentOrder(String parentOrder) {
		Map<String, Object> params = new HashMap<>();
		params.put("corder", parentOrder + "%");
		return (String) jdbc.queryForObject(SELECT_BY_PARENT_ORDER, params, String.class);
	}

	public String selectByOrder(String corder) {

		int clen = corder.length();

		Map<String, Object> params = new HashMap<>();
		params.put("corder", corder + "%");
		params.put("clen", clen + 2);

		String ReplyOrder = (String) jdbc.queryForObject(SELECT_BY_ORDER, params, String.class);
		return ReplyOrder;
	}

	public Integer insert(Comment comment) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(comment);
		return insertAction.executeAndReturnKey(params).intValue();
	}
}
