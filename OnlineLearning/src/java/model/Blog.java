/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blog {
    private int blogId;
    private Users userId;
    private String title;
    private String content;
    private Date createAt;
    private String status;
    private BlogCategory blogCategoryId; 
    private List<BlogMedia> mediaLinks;

    public Blog() {
        this.mediaLinks = new ArrayList<>(); // Khởi tạo danh sách
    }

    public Blog(int blogId, Users userId, String title, String content, Date createAt, BlogCategory blogCategoryId) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.blogCategoryId = blogCategoryId;
        this.mediaLinks = new ArrayList<>(); // Khởi tạo danh sách
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BlogCategory getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(BlogCategory blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public List<BlogMedia> getMediaLinks() {
        return mediaLinks;
    }

    public void setMediaLinks(List<BlogMedia> mediaLinks) {
        this.mediaLinks = mediaLinks;
    }

    public void addMedia(BlogMedia media) {
        this.mediaLinks.add(media); // Thêm phương thức để thêm media
    }
}
