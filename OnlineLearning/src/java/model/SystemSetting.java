package model;

import java.util.Date;

public class SystemSetting {
    private int settingID;
    private int userID;
    private boolean quizID;             // True if selected, otherwise false
    private boolean title;              // True if selected, otherwise false (renamed from 'name')
    private boolean subject;             // True if selected, otherwise false
    private boolean description;         // True if selected, otherwise false
    private boolean quizType;            // True if selected, otherwise false
    private boolean duration;            // True if selected, otherwise false
    private boolean passCondition;       // True if selected, otherwise false
    private boolean level;               // True if selected, otherwise false
    private boolean quantity;            // True if selected, otherwise false
    private boolean passRate;            // True if selected, otherwise false
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

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public boolean isQuizType() {
        return quizType;
    }

    public void setQuizType(boolean quizType) {
        this.quizType = quizType;
    }

    public boolean isDuration() {
        return duration;
    }

    public void setDuration(boolean duration) {
        this.duration = duration;
    }

    public boolean isPassCondition() {
        return passCondition;
    }

    public void setPassCondition(boolean passCondition) {
        this.passCondition = passCondition;
    }

    public boolean isLevel() {
        return level;
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public boolean isQuantity() {
        return quantity;
    }

    public void setQuantity(boolean quantity) {
        this.quantity = quantity;
    }

    public boolean isPassRate() {
        return passRate;
    }

    public void setPassRate(boolean passRate) {
        this.passRate = passRate;
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
