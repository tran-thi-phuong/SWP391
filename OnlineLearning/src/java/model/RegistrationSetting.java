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
public class RegistrationSetting {

    private int settingID;
    private int userID;
    private boolean registrationId;             // True if selected, otherwise false
    private boolean email;              // True if selected, otherwise false (renamed from 'name')
    private boolean subject;  
    private boolean campaign;  // True if selected, otherwise false
    private boolean packageId;         // True if selected, otherwise false
    private boolean totalCost;            // True if selected, otherwise false
    private boolean registrationTime;            // True if selected, otherwise false
    private boolean validFrom;       // True if selected, otherwise false
    private boolean validTo;               // True if selected, otherwise false
    private boolean status;            // True if selected, otherwise false
    private boolean staff;            // True if selected, otherwise false
    private boolean note;            // True if selected, otherwise false
    private int numberOfItems;           // Number of items per page
    private Date createdAt;              // Created timestamp
    private Date updatedAt;              // Updated timestamp

    public RegistrationSetting() {
    }

    public RegistrationSetting(int settingID, int userID, boolean registrationId, boolean email, boolean subject, boolean campaign, boolean packageId, boolean totalCost, boolean registrationTime, boolean validFrom, boolean validTo, boolean status, boolean staff, boolean note, int numberOfItems, Date createdAt, Date updatedAt) {
        this.settingID = settingID;
        this.userID = userID;
        this.registrationId = registrationId;
        this.email = email;
        this.subject = subject;
        this.campaign = campaign;
        this.packageId = packageId;
        this.totalCost = totalCost;
        this.registrationTime = registrationTime;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = status;
        this.staff = staff;
        this.note = note;
        this.numberOfItems = numberOfItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    
    
    public boolean isCampaign() {
        return campaign;
    }

    public void setCampaign(boolean campaign) {
        this.campaign = campaign;
    }

    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(boolean registrationId) {
        this.registrationId = registrationId;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

   
    public boolean isPackageId() {
        return packageId;
    }

    public void setPackageId(boolean packageId) {
        this.packageId = packageId;
    }

    public boolean isTotalCost() {
        return totalCost;
    }

    public void setTotalCost(boolean totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(boolean registrationTime) {
        this.registrationTime = registrationTime;
    }

    public boolean isValidFrom() {
        return validFrom;
    }

    public void setValidFrom(boolean validFrom) {
        this.validFrom = validFrom;
    }

    public boolean isValidTo() {
        return validTo;
    }

    public void setValidTo(boolean validTo) {
        this.validTo = validTo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isNote() {
        return note;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    
}
