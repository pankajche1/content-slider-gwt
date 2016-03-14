package com.gmail.pankajche1.contentslider.client.slider3;

import java.util.ArrayList;
import java.util.List;





public class Model3 {
	private double width;//width of the display
	private double height;
	private double wContent=0;
	private double hContent=0;
	private double xContent=0;
	private double xContent0;
	private double yContent=0;
	private double marginItemLeft=2;
	private double marginItemRight=2;
	private double wItem;//the net width of the item on the stage wItem+margins
	private double hItem;
	private double scrollSpan;
	private int tScroll=0;//duration of scroll one unit
	private int tRest=0;//duration of rest after one unit scroll
	//items data:
	private int nItems=0;
	private int iFirst=0,iLast=-1;//pointers showing the items on the display stage
	private int nDisplay=0;//items to display on the stage
	private List<String> imgUrls;
	private List<String> links;
	private View3 view;
	//scroll movement data:
	public boolean isLeftFromRight=true;
	public boolean isNextLeftFromRight=false;
	public boolean isDirectionSwitch=false;
	public boolean isScrolling=false;
	public boolean isResting=false;
	public boolean isToPause=false;
	public boolean isPause=false;
	//variables latest 131011 1918 hrs
	public boolean isResumeByMouseOut=false;
	public boolean isPausedByPauseButton=false;
	public Model3(View3 view,int width,int height,int wItem,int hItem,int tScroll,int tRest) {
		this.view=view;
		this.view.setModel(this);
		this.width=width;
		this.height=height;
		this.wItem=wItem;
		this.hItem=hItem;
		this.hContent=hItem;
		this.tScroll=tScroll;
		this.tRest=tRest;
		scrollSpan=wItem+marginItemLeft+marginItemRight;
		imgUrls=new ArrayList<String>();
		links=new ArrayList<String>();
		this.view.setDisplayWidth(width);
		this.view.setDisplayHeight(hItem);
		
	}
	public void addItem(String imgUrl,String link){
		imgUrls.add(imgUrl);
		links.add(link);

		//now the total number of items has increased by one:
		nItems++;
		//adding the item to the view cache:
		view.addItem(imgUrl,link,wItem,hItem,marginItemLeft,marginItemRight);
		//diciding the number of items displayed on the stage:
		if(wContent-2*wItem-marginItemLeft-marginItemRight<width){
			//set the position and size of the content:
			wContent+=wItem+marginItemLeft+marginItemRight;
			xContent=width/2-wContent/2;
			yContent=height/2-hContent/2;
			//the number of items displayed on the stage:
			nDisplay++;
			iLast++;//index of the last item on the display
			//put the items on the display
			//view.addItem(imgUrl,link,wContent,wItem,hItem,marginItemLeft,marginItemRight);
			view.addItemToStage(nItems-1, wContent);
			view.setPosition(xContent,yContent);
		}
		
	}
	public void setX(double progress){
		
		if(isLeftFromRight){
			xContent=xContent0-scrollSpan*progress;//this is the change in x per frame
			//reflect the change in the view:
			view.setPosition(xContent,yContent);
		}else{
			xContent=xContent0+scrollSpan*progress;
			//update the view:
			view.setPosition(xContent,yContent);
		}

		
	}
	/**
	 * left <- right movement
	 */
	public void startScrollLeft(){
		isScrolling=true;
		isResting=false;
		//getting the current position of the content:
		xContent0=xContent;
		//set the direction:
		isLeftFromRight=true;
		//isNextLeftFromRight=isLeftFromRight;
		//order the view to start its animation's run function:
		view.startRun(tScroll);
		//then the view will call its onEnterFrame() function
	}
	/**
	 * left -> right movement
	 */
	public void startScrollRight(){
		isScrolling=true;
		isResting=false;
		//getting the current position of the content:
		xContent0=xContent;
		//set the movement direction:
		isLeftFromRight=false;
		//isNextLeftFromRight=isLeftFromRight;
		//fire event for the view to start run time:
		view.startRun(tScroll);
	}
	public void resume(){
		isScrolling=true;
		isResting=false;
		xContent0=xContent;
		isPause=false;
		view.startRun(tScroll);
	}
	public void onRestComplete(){
			isScrolling=true;
			isResting=false;
			if(isDirectionSwitch){
				isLeftFromRight=isNextLeftFromRight;
				isDirectionSwitch=false;
			}
			
			xContent0=xContent;
			//order the view to start its animation's run function:
			if(isToPause){
				isToPause=false;
				isPause=true;
				isScrolling=false;
				isResting=false;
			}else{
				view.startRun(tScroll);	
			}
	}
	public void onCompleteScroll(){
		if(isLeftFromRight){
			//removing the item from the view:
			view.removeItemFromStage(iFirst);
			//after a left scroll the first item will be shifted to the right
			iFirst++;
			if(iFirst>=nItems){
				iFirst=0;
			}
			iLast=iFirst+nDisplay-1;
			if(iLast>=nItems){
				iLast=nDisplay-nItems+iFirst-1;
			
			}
			//putting the last item on the view to display:
			view.putItemAtLast(iLast);
			//and x will be shifted to right:
			xContent=xContent+scrollSpan;
			//updating the view:
			view.setPosition(xContent,yContent);
		}else{
			//x will be brought to its initial position:
			xContent=xContent-scrollSpan;
			//removing the last item from the stage:
			view.removeItemFromStage(iLast);
			//changing the start and end item indexes:
			iFirst--;
			if(iFirst<0){
				iFirst=nItems-1;
			}
			iLast=iFirst+nDisplay-1;
			if(iLast>=nItems){
				iLast=nDisplay-nItems+iFirst-1;
			
			}
			//put the first item on the stage:
			view.putItemAtFirst(iFirst);
			view.setPosition(xContent,yContent);
		}
		isScrolling=false;
		isResting=true;
		if(isDirectionSwitch){
			isLeftFromRight=isNextLeftFromRight;
			isDirectionSwitch=false;
		}
		if(isToPause){
			isToPause=false;
			isPause=true;
			//now neither scrolling nor resting so:
			isScrolling=false;
			isResting=false;
		}else{
			view.rest(tRest);
		}
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}
	public List<String> getLinks() {
		return links;
	}
	public double getxContent() {
		return xContent;
	}
	public double getyContent() {
		return yContent;
	}
	public double getwContent() {
		return wContent;
	}
	public double gethContent() {
		return hContent;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public int getnDisplay() {
		return nDisplay;
	}
	public int getnItems() {
		return nItems;
	}
	public int getiFirst() {
		return iFirst;
	}
	public int getiLast() {
		return iLast;
	}
	/*
	public HandlerRegistration addSizeRequestHandler(UpdatePositionEventHandler handler){
		return addHandler(handler,UpdatePositionEvent.TYPE);
	}
	*/
	
}
