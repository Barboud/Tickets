package net.hackyourfuture.tickets.service;

import lombok.AllArgsConstructor;
import net.hackyourfuture.tickets.model.Project;
import net.hackyourfuture.tickets.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAllProjects();
    }
}