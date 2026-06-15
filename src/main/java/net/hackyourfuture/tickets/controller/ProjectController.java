package net.hackyourfuture.tickets.controller;

import lombok.RequiredArgsConstructor;
import net.hackyourfuture.tickets.model.Project;
import net.hackyourfuture.tickets.service.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}