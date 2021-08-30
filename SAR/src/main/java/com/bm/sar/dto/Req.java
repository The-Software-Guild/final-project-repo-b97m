package com.bm.sar.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transferrable data representing requests to the NewsAPI  
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public class Req {
    private int id;
    private String text;
    private LocalDateTime time;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public LocalDateTime getTime() {
	return time;
    }

    public void setTime(LocalDateTime time) {
	this.time = time;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + this.id;
	hash = 37 * hash + Objects.hashCode(this.text);
	hash = 37 * hash + Objects.hashCode(this.time);
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
	final Req other = (Req) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (!Objects.equals(this.text, other.text)) {
	    return false;
	}
	return Objects.equals(this.time, other.time);
    }
}