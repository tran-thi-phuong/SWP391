package model;

import java.util.Date;

public class Subject {
    private int subjectId;
    private String title;
    private String description;
    private int subjectCategoryId;
    private String status;
    private String thumbnail;
    private Date updateDate;

    // Default constructor
    public Subject() {}

    // Parameterized constructor
    public Subject(int subjectId, String title, String description, int subjectCategoryId,
                   String status, String thumbnail, Date updateDate) {
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.subjectCategoryId = subjectCategoryId;
        this.status = status;
        this.thumbnail = thumbnail;
        this.updateDate = updateDate;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSubjectCategoryId() {
        return subjectCategoryId;
    }

    public void setSubjectCategoryId(int subjectCategoryId) {
        this.subjectCategoryId = subjectCategoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", subjectCategoryId=" + subjectCategoryId +
                ", status='" + status + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}