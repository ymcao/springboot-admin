package com.mobile2016.backend.model;

import java.sql.Date;

public class Category extends TModel {

	private int id;
	private String name;
	private String description;
	private String image;
	private Date adddate;
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getAdddate() {
		return adddate;
	}

	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
