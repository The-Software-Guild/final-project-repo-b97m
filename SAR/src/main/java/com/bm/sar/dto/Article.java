package com.bm.sar.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transferrable data representing (saved) articles 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public class Article {
    private int id;
    private String author;
    private String title;
    private String sourceName;
    private String url;
    private LocalDateTime publicationTime;
    private LocalDateTime savingTime;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public LocalDateTime getPublicationTime() {
	return publicationTime;
    }

    public void setPublicationTime(LocalDateTime publicationTime) {
	this.publicationTime = publicationTime;
    }

    public LocalDateTime getSavingTime() {
	return savingTime;
    }

    public void setSavingTime(LocalDateTime savingTime) {
	this.savingTime = savingTime;
    }

    public String getSourceName() {
	return sourceName;
    }

    public void setSourceName(String sourceName) {
	this.sourceName = sourceName;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 73 * hash + this.id;
	hash = 73 * hash + Objects.hashCode(this.author);
	hash = 73 * hash + Objects.hashCode(this.title);
	hash = 73 * hash + Objects.hashCode(this.sourceName);
	hash = 73 * hash + Objects.hashCode(this.url);
	hash = 73 * hash + Objects.hashCode(this.publicationTime);
	hash = 73 * hash + Objects.hashCode(this.savingTime);
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
	final Article other = (Article) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (!Objects.equals(this.author, other.author)) {
	    return false;
	}
	if (!Objects.equals(this.title, other.title)) {
	    return false;
	}
	if (!Objects.equals(this.sourceName, other.sourceName)) {
	    return false;
	}
	if (!Objects.equals(this.url, other.url)) {
	    return false;
	}
	if (!Objects.equals(this.publicationTime, other.publicationTime)) {
	    return false;
	}
	return Objects.equals(this.savingTime, other.savingTime);
    }
}
