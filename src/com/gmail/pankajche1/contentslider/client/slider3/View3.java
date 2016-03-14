package com.gmail.pankajche1.contentslider.client.slider3;


import com.google.gwt.user.client.ui.Widget;

public interface View3 {
	void setModel(Model3 model);
	void setController(SliderController3 controller);
	void addItem(String imgUrl,String link,double wItem,double hItem,double marginItemLeft,
			double marginItemRight);
	void addItemToStage(int index,double width);
	void removeItemFromStage(int index);
	void putItemAtLast(int index);
	void putItemAtFirst(int index);
	Widget asWidget();
	void setDisplayHeight(int width);
	void setDisplayWidth(int height);
	void setPosition(double x, double y);
	void startRun(int duration);
	void rest(int duration);
}
