package com.example.testaepmedia.entities;

///Base entity
public class ImageEntity {

	
	private String link; //Url link to image
	private byte[] imageBytes; //Optionally, not used for storing image byte array
	

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
}
