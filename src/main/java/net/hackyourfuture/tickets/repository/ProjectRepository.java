package net.hackyourfuture.tickets.repository;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Project> findAllProjects() {
        return jdbcTemplate.query(
                "SELECT * FROM projects",
                (rs, rowNum) -> new Project(rs.getInt("id"), rs.getString("name"))
        );
    }
    
}