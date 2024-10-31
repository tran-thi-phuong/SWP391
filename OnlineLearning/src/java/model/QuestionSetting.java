/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class QuestionSetting {

    private int userID;
    private boolean showContent;
    private boolean showLessonID;
    private boolean showStatus;
    private boolean showLevel;
    private int numberOfItems;

    public QuestionSetting() {
        // Default constructor
    }

    public int getUserID() {
        return userID;
    }

    // Getters and Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isShowContent() {
        return showContent;
    }

    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
    }

    public boolean isShowLessonID() {
        return showLessonID;
    }

    public void setShowLessonID(boolean showLessonID) {
        this.showLessonID = showLessonID;
    }

    public boolean isShowStatus() {
        return showStatus;
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public boolean isShowLevel() {
        return showLevel;
    }

    public void setShowLevel(boolean showLevel) {
        this.showLevel = showLevel;
    }

    public boolean getShowContent() {
        return showContent;
    }

    public boolean getShowLessonID() {
        return showLessonID;
    }

    public boolean getShowStatus() {
        return showStatus;
    }

    public boolean getShowLevel() {
        return showLevel;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
