/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Date;
/**
 *
 * @author 84336
 */
public class Subject {

    private int subjectId;
    private String title;
    private String description;
    private int subjectCategoryId;
    private String status;
    private Date updateDate;
    // Default constructor
    public Subject() {
    }

    public Subject(int subjectId, String title, String description, int subjectCategoryId, String status, Date updateDate) {
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.subjectCategoryId = subjectCategoryId;
        this.status = status;
        this.updateDate = updateDate;
    }

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
                ", status='" + status + '\'' + ", date=" + updateDate +
                '}';
    }
}
