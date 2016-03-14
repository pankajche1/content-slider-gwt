package com.gmail.pankajche1.contentslider.client;


import com.gmail.pankajche1.contentslider.client.slider3.SliderController3;
import com.gmail.pankajche1.contentslider.client.slider3.View3;
import com.gmail.pankajche1.contentslider.client.slider3.View3Impl;

import com.google.gwt.user.client.ui.HasWidgets;

public class ContentSlider3{
	SliderController3 controller;
	public ContentSlider3(HasWidgets container,int width,int height,int wItem,int hItem,
			int tScroll,int tRest) {
		
		View3 view = new View3Impl();
		controller = new SliderController3(view,width,height,wItem,hItem,tScroll,tRest);
		controller.go(container);
	}

	public void addItem(String imgUrl,String link){
		controller.addItem(imgUrl, link);
	}
    public void startSlideShow(){
    	controller.startLeftScroll();
    }

}
