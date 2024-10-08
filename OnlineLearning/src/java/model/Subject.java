package model;

import java.util.Date;

public class Subject {
    private int subjectID;
    private int userID;
    private String title;
    private String description;
    private int subjectCategoryId;
    private String status;
    private String thumbnail;
    private Date updateDate;
    private String userName;

    // Default constructor
    public Subject() {}

    // Parameterized constructor
    public Subject(int subjectID,int userID, String title, String description, int subjectCategoryId,
                   String status, String thumbnail, Date updateDate, String userName) {
        this.subjectID = subjectID;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.subjectCategoryId = subjectCategoryId;
        this.status = status;
        this.thumbnail = thumbnail;
        this.updateDate = updateDate;
        this.userName = userName;
    }
    public Subject(int subjectID, String title, String description, int subjectCategoryId,
                   String status, String thumbnail, Date updateDate) {
        this.subjectID = subjectID;
        this.title = title;
        this.description = description;
        this.subjectCategoryId = subjectCategoryId;
        this.status = status;
        this.thumbnail = thumbnail;
        this.updateDate = updateDate;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Getters and Setters
    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
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
                "subjectId=" + subjectID +
                ", userID='" + userID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", subjectCategoryId=" + subjectCategoryId +
                ", status='" + status + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
