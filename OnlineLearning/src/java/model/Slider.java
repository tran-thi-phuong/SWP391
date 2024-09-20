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
    private int SliderId;
    private int BlogId;
    private int SubjectId;
    private String title;
    private String image;
    private String content;

    public Slider() {
    }

    public Slider(int SliderId, int BlogId, int SubjectId, String title, String image, String content) {
        this.SliderId = SliderId;
        this.BlogId = BlogId;
        this.SubjectId = SubjectId;
        this.title = title;
        this.image = image;
        this.content = content;
    }

    public int getSliderId() {
        return SliderId;
    }

    public void setSliderId(int SliderId) {
        this.SliderId = SliderId;
    }

    public int getBlogId() {
        return BlogId;
    }

    public void setBlogId(int BlogId) {
        this.BlogId = BlogId;
    }

    public int getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(int SubjectId) {
        this.SubjectId = SubjectId;
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


