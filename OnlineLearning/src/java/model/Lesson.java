/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tuant
 */
public class Lesson {
    private int lessonID, subjectID, topicID, order;
    private String title, content, description, status,mediaLink;

    public Lesson() {
    }

    public Lesson(int lessonID, int subjectID, int topicID, int order, String title, String content, String description, String status, String mediaLink) {
        this.lessonID = lessonID;
        this.subjectID = subjectID;
        this.topicID = topicID;
        this.order = order;
        this.title = title;
        this.content = content;
        this.description = description;
        this.status = status;
        this.mediaLink=mediaLink;
    }

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }
    
    

    @Override
    public String toString() {
        return "Lesson{" + "lessonID=" + lessonID + ", subjectID=" + subjectID + ", topicID=" + topicID + ", order=" + order + ", title=" + title + ", content=" + content + ", description=" + description + ", status=" + status + '}';
    }
    
}
