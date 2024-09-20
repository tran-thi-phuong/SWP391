/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */
public class BlogCategory {
    private int blogCategoryId;
    private String title;

    // Default constructor
    public BlogCategory() {}

    public BlogCategory(int blogCategoryId, String title) {
        this.blogCategoryId = blogCategoryId;
        this.title = title;
    }

    public int getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(int blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }  
}
