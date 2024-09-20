/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */
import java.util.Date;

public class Blog {
    private int blogId;
    private User userId;
    private String title;
    private String content;
    private Date createAt;
    private BlogCategory blogCategoryId;

    public Blog() {
    }

    public Blog(int blogId, User userId, String title, String content, Date createAt, BlogCategory blogCategoryId) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.blogCategoryId = blogCategoryId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public BlogCategory getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(BlogCategory blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }
    
    
}