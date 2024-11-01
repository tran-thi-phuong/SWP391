/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class TestMedia {
    private int mediaId;
    private String mediaLink;
    private int testId; // Foreign key reference to the Test
    private String description;

    // Constructors
    public TestMedia() {}

    public TestMedia(int mediaId, String mediaLink, int testId, String description) {
        this.mediaId = mediaId;
        this.mediaLink = mediaLink;
        this.testId = testId;
        this.description = description;
    }

    // Getters and Setters
    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

