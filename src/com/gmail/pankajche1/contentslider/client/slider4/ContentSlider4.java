package com.gmail.pankajche1.contentslider.client.slider4;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

public class ContentSlider4 {
	public interface IAjaxLoader{
		

		void load(long sliderKey, int iStart, int nItemsPerAjax);
	}
	private IView4 view;
	private final Controller4 controller;
	public ContentSlider4(int width,int height,int wSidePanel,int nItemsDisplay,int leftMarginItem,int rightMarginItem,
			int tScroll,int tRest,int nTotalItems){
		view = new ViewImpl();
		//parameters: width, height
		//int width,int height,int nItemsDisplay,int leftMarginItem,int rightMarginItem
		Model model=new Model(width,height,wSidePanel,nItemsDisplay,leftMarginItem,rightMarginItem,tScroll,tRest,nTotalItems);
		controller = new Controller4(view,model);
	}
	public void go(HasWidgets container){
		container.add(view.asWidget());
		
	}
	public void addItem(String imgUrl,String link){
		controller.addItem(imgUrl, link);
	}
	public void start(){
		controller.onStartButtonClick();
	}
	public void setAjaxLoader(IAjaxLoader loader){
		controller.setAjaxLoader(loader);
	}
	public void setAjaxData(List<String> imageUrls,List<String> linkUrls,int nTotalItems){
		controller.addLoadedData(imageUrls, linkUrls, nTotalItems);
	}
	public void setSliderFilmKey(long key){
		controller.setSliderFilmKey(key);
	}
	public void setTotalItems(int nTotal){
		controller.setTotalItems(nTotal);
	}
}
