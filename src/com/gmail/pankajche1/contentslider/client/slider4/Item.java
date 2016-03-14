package com.gmail.pankajche1.contentslider.client.slider4;



public class Item{
	private int id;
	private String imageUrl="";
	private String linkUrl="";
	public Item(int id,String imageUrl,String linkUrl){
		this.id=id;
		this.imageUrl=imageUrl;
		this.linkUrl=linkUrl;
	}
	public int getId() {
		return id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}

	
}
