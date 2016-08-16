package com.myproject.sample.rest;

import com.myproject.sample.config.AppProperty;
import com.myproject.sample.config.ApplicationConfigurator;
import com.myproject.sample.dto.ProcessedProjectDto;
import com.myproject.sample.locator.UserStorageResourceLocator;
import com.myproject.sample.model.Project;
import com.myproject.sample.model.User;
import com.myproject.sample.service.ProjectService;
import com.myproject.sample.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Path("/download")
public class ProjectDownloader {
    @Inject private UserService userService;

    @Inject private ProjectService projectService;

    @Inject private UserStorageResourceLocator locator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessedProjectDto> getFileList(@Context SecurityContext context){
        User user = userService.findByUsername(context.getUserPrincipal().getName());
        List<ProcessedProjectDto> result = new ArrayList<>();
        List<Project> projects;
        if(user.getRole().equals("ADMIN"))
            projects = projectService.findAll();
        else
            projects = user.getProjects();
        for(Project p : projects){
            result.add(new ProcessedProjectDto(p));
        }
        return result;
    }

    @Path("/{id}/{ext}")
    @GET
    public Response downloadPng(@PathParam("id") String id, @PathParam("ext") String extension){
        Project project = projectService.findById(id);
        File file = locator.locate(project, "processed" + File.separator + "processed." + extension);
        if(!file.exists())
            return Response.status(404).entity("File not found").build();

        Response.ResponseBuilder responseBuilder = Response.ok(file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        return responseBuilder.build();
    }
}
