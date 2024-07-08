package com.example.collegemanager.Student;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "student")
public class student implements Serializable {
    private String hoTen;
    @PrimaryKey
    @NonNull
    private String maSV;
    private float diemToan;
    private float diemVan;
    private float diemAnh;

    public student() {}
    public student(String hoTen, String maSV, float diemToan, float diemVan, float diemAnh) {
        this.hoTen = hoTen;
        this.maSV = maSV;
        this.diemToan = diemToan;
        this.diemVan = diemVan;
        this.diemAnh = diemAnh;
    }

    public student(String hoTen, String maSV) {
        this.hoTen = hoTen;
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public float getDiemToan() {
        return diemToan;
    }

    public void setDiemToan(float diemToan) {
        this.diemToan = diemToan;
    }

    public float getDiemVan() {
        return diemVan;
    }

    public void setDiemVan(float diemVan) {
        this.diemVan = diemVan;
    }

    public float getDiemAnh() {
        return diemAnh;
    }

    public void setDiemAnh(float diemAnh) {
        this.diemAnh = diemAnh;
    }
}
