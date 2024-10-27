/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sonna
 */

public class BlogMedia {
    private int mediaId;         // ID của phương tiện
    private int blogId;          // ID của bài blog mà phương tiện thuộc về
    private String mediaType;    // Loại phương tiện (ví dụ: "image", "video")
    private String mediaLink;     // Liên kết đến phương tiện
    private String description;   // Mô tả về phương tiện

    public BlogMedia() {
    }

    
    // Constructor
    public BlogMedia(int mediaId, int blogId, String mediaType, String mediaLink, String description) {
        this.mediaId = mediaId;
        this.blogId = blogId;
        this.mediaType = mediaType;
        this.mediaLink = mediaLink;
        this.description = description;
    }

    // Getter and Setter methods
    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

