/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author 84336
 */
public class PackagePrice {

    private int packageId;
    private int subjectId;
    private String name;
    private int durationTime;
    private double salePrice;
    private double price;

    // Default constructor
    public PackagePrice() {
    }

    // Parameterized constructor
    public PackagePrice(int packageId, int subjectId, String name, int durationTime, double salePrice, double price) {
        this.packageId = packageId;
        this.subjectId = subjectId;
        this.name = name;
        this.durationTime = durationTime;
        this.salePrice = salePrice;
        this.price = price;
    }

    // Getters and Setters

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PackagePrice{" +
                "packageId=" + packageId +
                ", subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", durationTime=" + durationTime +
                ", salePrice=" + salePrice +
                ", price=" + price +
                '}';
    }
}

