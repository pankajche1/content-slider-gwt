package com.gmail.pankajche1.contentslider.client.slider4;


import com.google.gwt.user.client.ui.Widget;

public interface IView4 {
	void setModel(Model model);
	Widget asWidget();
	void setController(Controller4 controller);
	void startRun(int duration);
	void rest(int duration);
	void showAjaxLoading();
	void removeAjaxLoading();
	
}
