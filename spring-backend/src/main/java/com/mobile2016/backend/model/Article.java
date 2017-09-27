package com.mobile2016.backend.model;

import java.sql.Date;

public class Article extends TModel{

    private int id;
    private String title;
    private String description;
    private int category;
    private String categoryName;
    private String categoryImage;
    private String content;
    private Date addDate;
    private Date updateDate;
    private int commendState;
    private int state;
    private int browses;
    private int likes;
    private int comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBrowses() {
        return browses;
    }

    public void setBrowses(int browses) {
        this.browses = browses;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCommendState() {
        return commendState;
    }

    public void setCommendState(int commendState) {
        this.commendState = commendState;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}
