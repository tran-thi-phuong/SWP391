/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class SystemSetting {
    private int settingID;
    private int userID;
    private boolean quizID;             // True if selected, otherwise false
    private boolean name;                // True if selected, otherwise false
    private boolean subject;             // True if selected, otherwise false
    private boolean level;               // True if selected, otherwise false
    private boolean numberOfQuestions;   // True if selected, otherwise false
    private boolean duration;            // True if selected, otherwise false
    private boolean passRate;            // True if selected, otherwise false
    private boolean quizType;            // True if selected, otherwise false
    private int numberOfItems;           // Number of items per page
    private Date createdAt;              // Created timestamp
    private Date updatedAt;              // Updated timestamp

    // Getters and Setters

    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isQuizID() {
        return quizID;
    }

    public void setQuizID(boolean quizID) {
        this.quizID = quizID;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

    public boolean isLevel() {
        return level;
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public boolean isNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(boolean numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public boolean isDuration() {
        return duration;
    }

    public void setDuration(boolean duration) {
        this.duration = duration;
    }

    public boolean isPassRate() {
        return passRate;
    }

    public void setPassRate(boolean passRate) {
        this.passRate = passRate;
    }

    public boolean isQuizType() {
        return quizType;
    }

    public void setQuizType(boolean quizType) {
        this.quizType = quizType;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
