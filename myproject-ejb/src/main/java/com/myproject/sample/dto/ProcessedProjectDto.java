package com.myproject.sample.dto;

import com.myproject.sample.model.Project;

public class ProcessedProjectDto {
    private String id;

    private String name;

    private String thumbnail;

    public ProcessedProjectDto(Project project){
        this.id = project.getId();
        this.name = project.getName();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
