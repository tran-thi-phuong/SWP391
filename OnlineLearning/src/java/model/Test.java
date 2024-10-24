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
    private String mediaType;           // New field for media type (image/video)
    private String mediaURL;            // New field for media URL
    private String mediaDescription;     // New field for media description

    public Test() {
    }

    
    public Test(int testID, int subjectID, String title, String description, String type, String level, String mediaType, String mediaURL, int duration, double passCondition, String mediaDescription, int quantity) {
        this.testID = testID;
        this.subjectID = subjectID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.passCondition = passCondition;
        this.level = level;
        this.quantity = quantity;
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
        this.mediaDescription = mediaDescription;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    @Override
    public String toString() {
        return "Test{" + "testID=" + testID + ", subjectID=" + subjectID + ", title=" + title + ", description=" + description + ", type=" + type + ", duration=" + duration + ", passCondition=" + passCondition + ", level=" + level + ", quantity=" + quantity + ", mediaType=" + mediaType + ", mediaURL=" + mediaURL + ", mediaDescription=" + mediaDescription + '}';
    }
    
}
