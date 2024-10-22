/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class QuestionMedia {
    private int mediaID;
    private int questionID;
    private String mediaLink;
    private String description;

    // Constructor
    public QuestionMedia(int questionID, String mediaLink, String description) {
        this.questionID = questionID;
        this.mediaLink = mediaLink;
        this.description = description;
    }

    // Getters and Setters
    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "QuestionMedia{" +
                "mediaID=" + mediaID +
                ", questionID=" + questionID +
                ", mediaLink='" + mediaLink + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

