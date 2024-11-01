package model;

public class Test {
    private int testID;
    private int subjectID;
    private String title;
    private String description;
    private String type;
    private int duration;
    private double passCondition;
    private String level;
    private int quantity;

    public Test() {
    }

    
    public Test(int testID, int subjectID, String title, String description, String type, String level, int duration, double passCondition, int quantity) {
        this.testID = testID;
        this.subjectID = subjectID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.passCondition = passCondition;
        this.level = level;
        this.quantity = quantity;
    }

    
    // Getters and Setters

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPassCondition() {
        return passCondition;
    }

    public void setPassCondition(double passCondition) {
        this.passCondition = passCondition;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Test{" + "testID=" + testID + ", subjectID=" + subjectID + ", title=" + title + ", description=" + description + ", type=" + type + ", duration=" + duration + ", passCondition=" + passCondition + ", level=" + level + ", quantity=" + quantity + '}';
    }
    
}
