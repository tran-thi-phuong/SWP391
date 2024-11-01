/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class CampaignMedia {

    private int mediaID;
    private int campaignID;
    private String mediaLink;
    private String description;

    // Constructors
    public CampaignMedia() {}

    public CampaignMedia(int mediaID, int campaignID, String mediaLink, String description) {
        this.mediaID = mediaID;
        this.campaignID = campaignID;
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

    public int getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(int campaignID) {
        this.campaignID = campaignID;
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
}
