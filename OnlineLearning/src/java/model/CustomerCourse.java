package model;

/**
 * Represents a customer's course progress along with user details.
 */
public class CustomerCourse {
    private int courseID;
    private int userID;
    private String userName; // Add user name field
    private String email;    // Add email field
    private String phone;    // Add phone field
    private double progress;

    // Default constructor
    public CustomerCourse() {
    }

    // Constructor with courseID, userID, progress, and user details
    public CustomerCourse(int courseID, int userID, String userName, String email, String phone, double progress) {
        this.courseID = courseID;
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.progress = progress;
    }

    // Getters and setters
    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
