package com.gmail.pankajche1.contentslider.client.slider3;

import com.google.gwt.user.client.ui.HasWidgets;

public class SliderController3 {
	private Model3 model;
	private View3 view;
	//time duration of the animations:
	private int tSlide;//time to slide
	private int tStay;//time of rest afte a slide
	private boolean hasStarted=false;
	//items data:
	private int nItems=0;
	private int iFirst=0,iLast=0;//pointers showing the items on the display stage
	private int nDisplay=0;//items to display on the stage
	public SliderController3(View3 view,int width,int height,int wItem,int hItem,
			int tScroll,int tRest){
		this.view=view;
		this.view.setController(this);
		model = new Model3(view,width,height,wItem,hItem,tScroll,tRest);
		
	}
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	public void addItem(String imgUrl,String link){
		model.addItem(imgUrl, link);
	}
	public Model3 getModel() {
		return model;
	}
	/**
	 * left <- right movement
	 */
	public void startLeftScroll(){
		if(model.getWidth()>model.getwContent()){
			System.out.println("scrolling not possible");
		}else{
			//System.out.println("left <- right: scrolling starting...");
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
		if(model.getWidth()>model.getwContent()){
			System.out.println("scrolling not possible");
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
	public void onEnterFrame(double progress){
		
		//set the model x parameter:
		model.setX(progress);
	}
	public void onComplete(){
		//now change the data at the end of the slide:
		model.onCompleteScroll();
	}
	public void onRestComplete(){
		model.onRestComplete();
	}
	public void pause(){
		
			model.isToPause=true;
			
			
	}
	public void onResumeButtonClick(){
		if(hasStarted){
			//run the slider:
			hasStarted=true;
			model.startScrollLeft();
		}else{
			resume();
		}
	}
	private void resume(){
		model.isResumeByMouseOut=false;
		model.isToPause=false;
		model.isPausedByPauseButton=false;
		if(model.isPause){
			model.resume();
		}
	}
	public void onPauseButtonClick(){
		model.isResumeByMouseOut=false;
		model.isPausedByPauseButton=true;
		pause();
	}
	public void onMouseOver(){
		if(model.isPausedByPauseButton){
			//do nothing
		}else{
			model.isResumeByMouseOut=true;
			pause();
		}
		
	}
	public void onMouseOut(){
		if(hasStarted){
			if(model.isResumeByMouseOut){
				resume();
			}
		}
	}	
}
