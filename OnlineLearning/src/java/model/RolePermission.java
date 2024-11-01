/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class RolePermission {
     private int rolePermissionId;
    private String role;      
    private int pageId;
     private Date updateAt;

    public RolePermission() {
    }

    public RolePermission(int rolePermissionId, String role, int pageId,  Date updateAt) {
        this.rolePermissionId = rolePermissionId;
        this.role = role;
        this.pageId = pageId;
        this.updateAt = updateAt;
    }

    public int getRolePermissionId() {
        return rolePermissionId;
    }

    public String getRole() {
        return role;
    }

    public int getPageId() {
        return pageId;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setRolePermissionId(int rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
     
    
}
