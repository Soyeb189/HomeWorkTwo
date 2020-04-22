package com.example.homeworktwo.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user")
public class User implements Serializable {

    @ColumnInfo(name = "user_name")
    private String name;

    @ColumnInfo(name = "user_email")
    private String email;

    @ColumnInfo(name = "user_phone")
    private String phoneNumber;

    @ColumnInfo(name = "user_password")
    private String password;

    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    private long id;


    @ColumnInfo(name = "checked_val")
    private int checkValue;

    @ColumnInfo(name = "pro_image")
    private String image;

    @ColumnInfo(name = "device_uuid")
    private String uuid;



    public User(String name, String email, String phoneNumber, String password, long id, int checkValue, String image, String uuid) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.id = id;
        this.checkValue = checkValue;
        this.image = image;
        this.uuid = uuid;
    }

    @Ignore
    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(int checkValue) {
        this.checkValue = checkValue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}
