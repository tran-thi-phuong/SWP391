/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */

import java.util.Date;

public class Subject {
    private int subjectID;
    private String title;
    private String description;
    private int subjectCategoryID;
    private String status;
    private String thumbnail;
    private Date updateDate;

    public Subject() {
    }

    public Subject(int subjectID, String title, String description, int subjectCategoryID, String status, String thumbnail, Date updateDate) {
        this.subjectID = subjectID;
        this.title = title;
        this.description = description;
        this.subjectCategoryID = subjectCategoryID;
        this.status = status;
        this.thumbnail = thumbnail;
        this.updateDate = updateDate;
    }

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

    public int getSubjectCategoryID() {
        return subjectCategoryID;
    }

    public void setSubjectCategoryID(int subjectCategoryID) {
        this.subjectCategoryID = subjectCategoryID;
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

    
}
