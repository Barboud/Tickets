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
                "SELECT \n" +
                        "    projects.id, \n" +
                        "    projects.name,\n" +
                        "    COUNT(*) FILTER (WHERE tickets.status = 'OPEN') AS open,\n" +
                        "    COUNT(*) FILTER (WHERE tickets.status = 'IN_PROGRESS') AS in_progress,\n" +
                        "    COUNT(*) FILTER (WHERE tickets.status = 'CLOSED') AS closed\n" +
                        "FROM projects\n" +
                        "LEFT JOIN tickets ON tickets.project_id = projects.id\n" +
                        "GROUP BY projects.id, projects.name;",
                (rs, rowNum) -> new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("open"),
                        rs.getInt("in_progress"),
                        rs.getInt("closed")
                )
        );
    }

}