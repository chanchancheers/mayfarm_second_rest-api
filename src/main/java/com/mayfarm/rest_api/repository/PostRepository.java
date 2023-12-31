package com.mayfarm.rest_api.repository;

import com.mayfarm.rest_api.dto.reponse.PostResponse;
import com.mayfarm.rest_api.entity.Pagination;
import com.mayfarm.rest_api.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;
    public List<Post> findById(int id) {
        List<Post> results = jdbcTemplate.query("SELECT * FROM post p WHERE id = ?",
                this.postRowMapper(), id);
        return results.isEmpty() ? null : results;
    }

    public List<Post> findAll() {
        List<Post> results = jdbcTemplate.query("SELECT * FROM post",
                this.postRowMapper());
        return results.isEmpty() ? null : results;
    }

    public int countAll(String kw){
        String query = ("SELECT COUNT(DISTINCT p.*) FROM post p " +
                "WHERE subject LIKE '%" + kw + "%' " +
                "or content LIKE '%" + kw + "%'");
        Integer totalPost = this.jdbcTemplate.queryForObject(query, Integer.class);
        return (int) totalPost;
    }

    public List<Post> findAllWithPaging(String orderKey, Pagination pagination, String kw){
        String query = "SELECT DISTINCT p.* FROM post p " +
                "WHERE p.subject LIKE '%" + kw + "%' " +
                "or p.content LIKE '%" + kw + "%' "+ orderKey +
                " LIMIT " + pagination.getEndIdx() + " OFFSET " + (pagination.getStartIdx()-1);

        List<Post> posts = jdbcTemplate.query(query, this.postRowMapper());
        return posts;
    }

    public List<Post> findAllBy(String orderKey, String kw){
        String query = "SELECT DISTINCT p.* FROM post p " +
                "WHERE p.subject LIKE '%" + kw + "%' " +
                "or p.content LIKE '%" + kw + "%' "+ orderKey;
        List<Post> results = jdbcTemplate.query(query, this.postRowMapper());
        return results.isEmpty() ? null : results;
    }

    public Post save(Post post){
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO post (subject, content) VALUES (?, ?)";
        jdbcTemplate.update (conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, post.getSubject());
            preparedStatement.setString(2, post.getContent());
            return preparedStatement;
        }, generatedKeyHolder);
        //bring auto_incremented 'id'
        Integer id = (int) generatedKeyHolder.getKeys().get("id");

        //bring auto_generated 'Date'
        Timestamp createdDate_ = (Timestamp) generatedKeyHolder.getKeys().get("created_date");
        LocalDateTime createdDate = createdDate_.toLocalDateTime();
        Timestamp modifiedDate_ = (Timestamp) generatedKeyHolder.getKeys().get("modified_date");
        LocalDateTime modifiedDate = modifiedDate_.toLocalDateTime();

        //setting
        post.setId(id);
        post.setCreatedDate(createdDate);
        post.setModifiedDate(modifiedDate);

        log.info("POST CREATION SUCCESS : POST ID {}", id);
        return post;
    }



    public void modify(int id, Post post) {

        this.jdbcTemplate.update("UPDATE post SET subject = ?, content = ?, modified_date = ? WHERE id = ?",
                post.getSubject(), post.getContent(), post.getModifiedDate(), id);
    }
    public void delete(int id){
        this.jdbcTemplate.update("DELETE FROM post WHERE id = ?", id);
    }

    public RowMapper<Post> postRowMapper() {
        RowMapper<Post> rowMapper = new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                Post post = Post.builder()
                        .subject(rs.getString("subject"))
                        .content(rs.getString("content"))
                        .build();
                post.setId(rs.getInt("id"));
                post.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                post.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());

                return post;
            }
        };
        return rowMapper;
    }

}
