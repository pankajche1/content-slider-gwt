package com.gmail.pankajche1.contentslider.client.slider4;

import java.util.List;

import com.gmail.pankajche1.contentslider.client.slider4.ContentSlider4.IAjaxLoader;
//import com.google.gwt.user.client.Timer;
//import com.google.inject.Inject;

public class Controller4 {
	private final IView4 view;
	private final Model model;
	private boolean hasStarted=false;
	private IAjaxLoader loader;
	private long sliderFilmKey;
	
	private int nItemsPerAjax=20;
	private boolean isLoading=false;
	//@Inject
	public Controller4(IView4 view,Model model){
		this.view=view;
		this.model=model;
		this.view.setModel(model);
		this.view.setController(this);
	}
	public void setAjaxLoader(IAjaxLoader loader){
		this.loader=loader;
	}
	
	public void onEnterFrame(double progress){
		model.setX(progress);
	}
	public void onScrollComplete(){
		model.onScrollComplete();
	}
	public void onRestComplete(){
		model.onRestComplete();
	}
	public Model getModel() {
		return model;
	}
	public void addItem(String imgUrl,String link){
		model.addItem(imgUrl, link);
	}
	/**
	 * left <- right movement
	 */
	public void startLeftScroll(){
		if(model.isPausedByPauseButton&&model.isPause){
			
		}else{
		model.isDirectionSwitch=true;
		model.isNextLeftFromRight=true;
		if(model.isScrolling){
			
		}else if(model.isResting){
		
		}else{
			hasStarted=true;
			model.startScrollLeft();
		}	
		}
	}
	/**
	 * left -> right movement
	 */
	public void startRightScroll(){
		if(model.isPausedByPauseButton&&model.isPause){
			
		}else{	
		//here you need to find if the scrolling is on at the
		//current time:
		model.isDirectionSwitch=true;
		model.isNextLeftFromRight=false;
		if(model.isScrolling){
			//model.isLeftFromRight=false;
			
		}else if(model.isResting){
			
		}else{
			hasStarted=true;
			model.startScrollRight();
		}
		}
	}
	public void onPauseButtonClick(){
		model.isResumeByMouseOut=false;
		model.isPausedByPauseButton=true;
		//pause();
	}
	/*
	 * this is the start/resume button: it shows two icons[start or resume] depending
	 * upon the state of the button.
	 */
	public void onResumeButtonClick(){
		if(hasStarted){
			resume();
		}else{
			//run the slider:
			hasStarted=true;
			model.startScrollLeft();			
		}
	}
	public void pause(){
		model.isInterrupt=true;
		model.isToPause=true;
		
		
}
	private void resume(){
		model.isResumeByMouseOut=false;
		model.isToPause=false;
		model.isInterrupt=false;
		model.isPausedByPauseButton=false;
		if(model.isPause){
			model.resume();
		}
	}
	public void onStartButtonClick() {
		//set the model state to be 'started'
		if(!hasStarted){
			//now the scroll is started and the slider is active now
			hasStarted=true;
			model.startScroll();
		}
		
	}
	public void togglePauseResume(boolean isDown){
		if(isDown){//the button is down means it is showing the pause icon
			//and when it was clicked that time it was showing the start icon
			//start or resume actions will be taken here
			if(!hasStarted){
				//now the scroll is started and the slider is active now
				hasStarted=true;
				model.startScroll();
			}else{//the icon is already active but it is paused
				//so resume it.
				resume();
			}			
		}else{//the botton is showing the start icon
			//and when it was clicked it was showing the pause icon
			//PAUSE ACTIONS will be taken here
			model.isResumeByMouseOut=false;
			model.isPausedByPauseButton=true;
			model.isToPause=true;
			model.isInterrupt=true;
		}
		
	}
	public void pauseByMouseOver(){
		if(model.isPausedByPauseButton){
			//do nothing
		}else{
			model.isResumeByMouseOut=true;
			pause();
		}
	}
	public void resumeByMouseOut(){
		if(hasStarted){
			if(model.isResumeByMouseOut){
				resume();
			}
		}
	}
	public void load(){
		if(!isLoading){//if currently there is no ajax loading process going on
			//System.out.println("Controller4::load() called...");
			//show in the view the ajax loading notification
			if(loader!=null){
				//if some items are remaining then load them
				if(model.getnItems()<model.getnTotalItems()){
					view.showAjaxLoading();
					isLoading=true;
					//Timer t = new Timer(){
					//@Override
					//public void run() {
						//loader.load(sliderFilmKey,model.getnItems(),nItemsPerAjax);
					//}};
					//t.schedule(2000);
					loader.load(sliderFilmKey,model.getnItems(),nItemsPerAjax);
				}
			}//else System.out.println("Controller4::load() called...loader is null");
		}
	}
	/*
	 * to add the data that has been loaded by the ajax call
	 * this function is called from the outside to add the new data:
	 */
	public void addLoadedData(List<String> listImageUrls,List<String> linkUrls,int nTotalItems){
		//System.out.println("Controller4::addLoadedData() called...size loaded="+listImageUrls.size());
		model.setLoadedItems(listImageUrls, linkUrls, nTotalItems);
		//remove from the view the ajax loading notification
		//first stop whatever is going on on the display stage
		//the stage can be (1) scrolling, (2) resting, (3) scroll complete (4) rest complete
		//(5) paused
		//model is to be paused for the new data update

		if(model.isPause){
			//slider is paused at this time
			//it will never go to any where from this state now
			//you will have to put the new data and then do nothing
			model.updateDisplay();//this is the method to update while the silder is paused
		}else{
			model.isInterrupt=true;
			model.isNewData=true;
			//model is not paused at this time
			//see if the model has some message to pause:
			if(model.isToPause){
				//model is to be paused
				//it means it will get the message in the onRestComplete() method:
			}else{
				//it means model is sliding or resting but it has no order to pause
				//it will put the new data when the model reaches to the onRestComplete() method
			}			
		}
		//now i am free of loading:
		isLoading=false;
		view.removeAjaxLoading();
		/*
		if(model.isResting){
			// cancel the rest animator enter frame
			// take the values of iFirst and iLast and the direction
			// now add the new items in the items list
		}else if(model.isScrolling){
			// at the end of the scrolling cancel the next flow of program to the rest period
			// now add the new items in the item list
			// start the rest period
		}
		*/
	}
	public void setSliderFilmKey(long sliderFilmKey) {
		this.sliderFilmKey = sliderFilmKey;
	}
	public void setTotalItems(int nTotal){
		model.setnTotalItems(nTotal);
	}

	
}
