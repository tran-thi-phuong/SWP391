/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class Question {
    private int questionID;
    private int lessonID;
    private String status;
    private String content;
    private String level;

    public Question() {
    }

    
    // Constructor
    public Question(int lessonID, String status, String content, String level) {
        this.lessonID = lessonID;
        this.status = status;
        this.content = content;
        this.level = level;
    }

    // Getters and Setters
    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", lessonID=" + lessonID +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}

