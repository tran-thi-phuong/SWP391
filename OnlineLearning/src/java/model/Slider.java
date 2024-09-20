/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */

public class Slider {
    private int sliderID;
    private Blog blog;
    private Subject subject;
    private String title;
    private String image;
    private String content;

    public Slider() {
    }

    public Slider(int sliderID, Blog blog, Subject subject, String title, String image, String content) {
        this.sliderID = sliderID;
        this.blog = blog;
        this.subject = subject;
        this.title = title;
        this.image = image;
        this.content = content;
    }

    // Getter và Setter cho tất cả các thuộc tính
    public int getSliderID() {
        return sliderID;
    }

    public void setSliderID(int sliderID) {
        this.sliderID = sliderID;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
