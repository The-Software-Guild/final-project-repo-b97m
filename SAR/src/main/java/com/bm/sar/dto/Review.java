package com.bm.sar.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transferrable data representing article reviews 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public class Review {
    private int id;
    private LocalDateTime creationTime;
    private LocalDateTime updateTime;
    private String text;
    private int articleId;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public LocalDateTime getCreationTime() {
	return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
	this.creationTime = creationTime;
    }

    public LocalDateTime getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
	this.updateTime = updateTime;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public int getArticleId() {
	return articleId;
    }

    public void setArticleId(int articleId) {
	this.articleId = articleId;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 53 * hash + this.id;
	hash = 53 * hash + Objects.hashCode(this.creationTime);
	hash = 53 * hash + Objects.hashCode(this.updateTime);
	hash = 53 * hash + Objects.hashCode(this.text);
	hash = 53 * hash + this.articleId;
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Review other = (Review) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (this.articleId != other.articleId) {
	    return false;
	}
	if (!Objects.equals(this.text, other.text)) {
	    return false;
	}
	if (!Objects.equals(this.creationTime, other.creationTime)) {
	    return false;
	}
	return Objects.equals(this.updateTime, other.updateTime);
    }
}