package com.gmail.pankajche1.contentslider.client.slider4;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
//import com.google.inject.Inject;

public class Model {
	private double width;//width of the display
	private double wDisplay;//width of the window through which items are shown
	private double height;
	private double wContent=0;
	private double hContent=0;
	private double xContent=0;
	private double xContent0;
	private double yContent=0;
	private double marginItemLeft=2;
	private double marginItemRight=2;
	private int wSidePanel=42;
	
	private double wItem;//the net width of the item on the stage wItem+margins
	private double hItem;
	private double scrollSpan;
	private int tScroll=0;//duration of scroll one unit
	private int tRest=0;//duration of rest after one unit scroll

	private int nItems=0;//total items at this time on the client
	private int nTotalItems=0;//total items on the server
	private String itemsInfo="";
	//these are the indexes for the next scroll:
	private int iFirst=0,iLast=-1;//pointers showing the items on the display stage
	private int iFirstPrev=0,iLastPrev=0;//previous indexes of the above
	private int nDisplay=0;//items to display on the stage
	public int iRemovedFromDisplay=0;
	public int iTargetItem=0;
	public int iInsertedToDisplay=0;
	public int iItemToDisplay=0;

	private List<Item> items;
	private List<Item> itemsOnDisplay;//items which are on the stage for display
	private List<Item> itemsLoaded;//items that are loaded from the server
	//managing the events:
	
	private HandlerManager eventHandler=new HandlerManager(null);
	//scroll movement data:
	public boolean isLeftFromRight=true;
	public boolean isNextLeftFromRight=false;
	public boolean isDirectionSwitch=false;
	public boolean isScrolling=false;
	public boolean isResting=false;
	public boolean isInterrupt=false;
	public boolean isToPause=false;
	public boolean isPause=false;
	//variables latest 131011 1918 hrs
	public boolean isResumeByMouseOut=false;
	public boolean isPausedByPauseButton=false;
	//variables on 140125 15:22 hrs:
	public boolean isNewData=false;
	//@Inject
	public Model(int width,int height,int wSidePanel,int nItemsDisplay,int leftMarginItem,int rightMarginItem,int tScroll,int tRest,int nTotalItems){

		this.width=width;
		this.height=height;
		marginItemLeft=leftMarginItem;
		marginItemRight=rightMarginItem;
		this.wSidePanel=wSidePanel;
		//this.wItem=wItem;
		//this.hItem=hItem;
		//this.hContent=hItem;
		this.tScroll=tScroll;
		this.tRest=tRest;
		this.nTotalItems=nTotalItems;
		//int nItemsDisplay=5;
		this.wItem=(width-2*wSidePanel)/nItemsDisplay-marginItemLeft-marginItemRight;
		wDisplay=width-2*wSidePanel;
		scrollSpan=this.wItem+marginItemLeft+marginItemRight;
		//scrollSpan=100;	
		items = new ArrayList<Item>();
		itemsOnDisplay = new ArrayList<Item>();

	}
	public void addItem(String imgUrl,String link){
		//the size of the List<>
		int size = items.size();
		//adding a new item with the id of its index in the List:
		items.add(new Item(size,imgUrl,link));
		//now the total number of items has increased by one:
		nItems++;
		itemsInfo="showing "+nItems+" of "+nTotalItems;
		//firing the event that an item has been added:
		SliderEvent event = new SliderEvent("add");
		event.intData=nItems-1;
		eventHandler.fireEvent(event);
		//putting the items on display:
		if(wContent-wItem-marginItemLeft-marginItemRight<wDisplay){
			itemsOnDisplay.add(items.get(items.size()-1));
			//set the position and size of the content:
			wContent+=wItem+marginItemLeft+marginItemRight;
			//xContent=width/2-wContent/2;
			//yContent=height/2-hContent/2;
			//the number of items displayed on the stage:

			nDisplay++;
			
			//System.out.println("Model::putting an item to display: wContent="+wContent+
					//", width:"+width+", nDisplay:"+nDisplay);
			iLast++;//index of the last item on the display
			//firing an event that an item is put on the stage to display:
			SliderEvent displayItemEvent = new SliderEvent("display");
			displayItemEvent.intData=nItems-1;
			eventHandler.fireEvent(displayItemEvent);
		}

	}
	public String getImageUrl(int index){
		return items.get(index).getImageUrl();
	}
	public String getLinkUrl(int index){
		return items.get(index).getLinkUrl();
	}
	public void resetDisplay(){
		//now we need to map the list displat to the view again:
		List<Integer> listIndexDisplay = new ArrayList<Integer>();
		for(Item item:itemsOnDisplay){
			listIndexDisplay.add(item.getId());
		}
		
	}
	public List<Integer> getIndexDisplay(){
		List<Integer> listIndexDisplay = new ArrayList<Integer>();
		for(Item item:itemsOnDisplay){
			listIndexDisplay.add(item.getId());
		}
		return listIndexDisplay;
	}
	public void setX(double progress) {


		if(isLeftFromRight){
			//shift to left:
			xContent=-scrollSpan*progress;//this is the change in x per frame

		}else{
			xContent=xContent0+scrollSpan*progress;
			

		}
		//fire the event:
		
		eventHandler.fireEvent(new SliderEvent("updatePosition"));
	}
	public HandlerManager getEventHandler() {
		return eventHandler;
	}
	public double getxContent() {
		return xContent;
	}
	public void onScrollComplete() {
		
		if(isLeftFromRight){
		//on LEFT <- RIGHT scroll complete:

		//2 remove the first item from the display list:
		//iRemovedFromDisplay=iFirst;
		iRemovedFromDisplay=0;
		iTargetItem=iFirst;//this is the index of the target item on the main list
		itemsOnDisplay.remove(0);
		//1 set the content at default (0,0) position (i.e. shift it to the right)
		xContent=0;
		

		//3 shift the items to compensate for the position change in step 1
		}else{
			//xContent=xContent-scrollSpan;
			//remove the last item from the display:
			//iRemovedFromDisplay=iLast;
			iTargetItem=iLast;
			iRemovedFromDisplay=itemsOnDisplay.size()-1;
			itemsOnDisplay.remove(itemsOnDisplay.size()-1);
			//intData=

		}
		//processDirectionChangeMsg();
		//fire the event:
		eventHandler.fireEvent(new SliderEvent("scrollComplete"));
		
	}
	public List<Integer> getScrollIndexes(int nItems,int iFirst,int iLast){
		List<Integer> list = new ArrayList<Integer>();
		int i=0;
			if(iFirst<iLast){//this is a normal situation:
				for(i=iFirst;i<=iLast;i++){
					list.add(i);
				}
				
			}else{
				//go till the end of the items:
				for(i=iFirst;i<nItems;i++){
					list.add(i);
				}
				//start from the zero till the iEnd index:
				for(i=0;i<=iLast;i++){
					list.add(i);
				}
			}
		return list;
	}
	public int[] getNextScrollRange(int nItems,int nDisplay,int iCurFirst,boolean isAntiClock){
		int iNextFirst=-1,iNextLast=-1;
		int[] range={iNextFirst,iNextLast};
		if(isAntiClock){
			//removing the item from the view:
			///view.removeItemFromStage(iFirst);
			//after a left scroll the first item will be shifted to the right
			iNextFirst=iCurFirst+1;
			
			if(iNextFirst>=nItems){
				iNextFirst=0;
			}
			iNextLast=iNextFirst+nDisplay-1;
			if(iNextLast>=nItems){
				iNextLast=nDisplay-nItems+iNextFirst-1;
			
			}
			//putting the last item on the view to display:
			///view.putItemAtLast(iLast);
			//and x will be shifted to right:
			///xContent=xContent+scrollSpan;
			//updating the view:
			///view.setPosition(xContent,yContent);
		}else{
			//x will be brought to its initial position:
			///xContent=xContent-scrollSpan;
			//removing the last item from the stage:
			///view.removeItemFromStage(iLast);
			//changing the start and end item indexes:
			iNextFirst=iCurFirst-1;
			if(iNextFirst<0){
				iNextFirst=nItems-1;
			}
			iNextLast=iNextFirst+nDisplay-1;
			if(iNextLast>=nItems){
				iNextLast=nDisplay-nItems+iNextFirst-1;
			
			}
			//put the first item on the stage:
			///view.putItemAtFirst(iFirst);
			///view.setPosition(xContent,yContent);
		}
		
		range[0]=iNextFirst;
		range[1]=iNextLast;
		return range;
	}
	public int gettRest() {
		return tRest;
	}
	public void onRestComplete() {

		boolean isSwitch=processDirectionChangeMsg();
		if(isSwitch){
			//put the items at the desired ends of the display list:
			if(isLeftFromRight){
				//put the iLast at the last
				
				itemsOnDisplay.add(items.get(iLast));
				iItemToDisplay=iLast;
				iInsertedToDisplay=itemsOnDisplay.size();
				xContent=0;
				
			}else{
				//put the iFirst at the start:
				itemsOnDisplay.add(0, items.get(iFirst));
				iItemToDisplay=iFirst;
				iInsertedToDisplay=0;
				//the above will shift the content one scroll span to the right
				//shift the content one scroll span to the nagative direction to hide the first item:
				xContent=-scrollSpan;
				xContent0=xContent;
			}			
		}else doPriming();

		//check if some pausing order has come:
		if(isInterrupt){
			//check if some new ajax data has come:
			boolean isNewDataAdd=false;
			if(isNewData){
				//put the new loaded items:
				int nItemsPrev=nItems;
				addNewItems();
				isNewDataAdd=true;
				//at last remove the message:
				isNewData=false;
				//isToPause=false;
				//and fire the event to notify the view to update the data on DOM

				if(isToPause){
					//if the slider is to be paused
					eventHandler.fireEvent(new SliderEvent("newDataAddedPause",nItemsPrev));
				}else{
					isScrolling=true;
					isResting=false;
					//if the slider not to be paused
					eventHandler.fireEvent(new SliderEvent("newDataAdded",nItemsPrev));
				}
			}
			if(isToPause){
				if(!isNewDataAdd){
					SliderEvent event=new SliderEvent("prime");
					event.isNextStart=false;
					eventHandler.fireEvent(event);	
				}
				isToPause=false;
				isPause=true;
				//now neither scrolling nor resting so:
				isScrolling=false;
				isResting=false;
			}
			isInterrupt=false;
		}else{
			isScrolling=true;
			isResting=false;
			SliderEvent event=new SliderEvent("prime");
			event.isNextStart=true;
			eventHandler.fireEvent(event);	
		}
		//xContent0=xContent;
		
	}
	public void resume(){
		isScrolling=true;
		isResting=false;
		isPause=false;
		isInterrupt=false;
		//this event will not be fired:
		eventHandler.fireEvent(new SliderEvent("startEnterFrame"));	
	}
	private boolean processDirectionChangeMsg(){

		if(isDirectionSwitch){
			if(isLeftFromRight==isNextLeftFromRight){
				isDirectionSwitch=false;
				return false;
			}else{
				isLeftFromRight=isNextLeftFromRight;
				//remove the direction change message: [destroy the message for next use]
				isDirectionSwitch=false;
				return true;
			}
		}else return false;		
	}
	private void doPriming(){
		//get the next scroll ranges:
		//System.out.println("doPriming(), iFrist="+iFirst+", isLeftFromRight="+isLeftFromRight);
		int[] range=getNextScrollRange(nItems,nDisplay,iFirst,isLeftFromRight);
		iFirstPrev=iFirst;
		iLastPrev=iLast;
		iFirst=range[0];
		iLast=range[1];
		//System.out.println("doPriming(), iFrist="+iFirst+", isLeftFromRight="+isLeftFromRight);
		if(isLeftFromRight){
			//put the iLast at the last
			
			itemsOnDisplay.add(items.get(iLast));
			iItemToDisplay=iLast;
			iInsertedToDisplay=itemsOnDisplay.size();
			xContent=0;
			
		}else{
			//put the iFirst at the start:
			itemsOnDisplay.add(0, items.get(iFirst));
			iItemToDisplay=iFirst;
			iInsertedToDisplay=0;
			//the above will shift the content one scroll span to the right
			//shift the content one scroll span to the nagative direction to hide the first item:
			xContent=-scrollSpan;
			xContent0=xContent;
		}
	}
	public List<Item> getItems() {
		return items;
	}
	public List<Item> getItemsOnDisplay() {
		return itemsOnDisplay;
	}
	public int getiFirst() {
		return iFirst;
	}
	public int getiLast() {
		return iLast;
	}
	
	public int getiFirstPrev() {
		return iFirstPrev;
	}
	public int getiLastPrev() {
		return iLastPrev;
	}
	public int getnDisplay() {
		return nDisplay;
	}
	public int getnItems() {
		return nItems;
	}
	public double getWidth() {

		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getwContent() {
		return wContent;
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
		//1 calculate the iFirst and iLast thing:
		//int[] range=getNextScrollRange(nItems,nDisplay,iFirst,isLeftFromRight);
		iFirstPrev=iFirst;
		iLastPrev=iLast;
		//iFirst=range[0];
		//iLast=range[1];
		//adding an item to the display stage:
		//itemsOnDisplay.add(items.get(iLast));
		//the above will increase the width of the display:
		//wContent+=wItem+marginItemLeft+marginItemRight;
		xContent=0;
		//isNextLeftFromRight=isLeftFromRight;
		//order the view to start its animation's run function:
		//view.startRun(tScroll);
		eventHandler.fireEvent(new SliderEvent("startLeftFromRight"));
		//then the view will call its onEnterFrame() function
	}
	public int gettScroll() {
		return tScroll;
	}
	/**
	 * this method is called first time when the slider has not started sliding yet
	 */
	public void startScrollRight() {
		//priming the content for display on the stage:
		//set the movement direction:
		isLeftFromRight=false;
		//removing an extra item at the right side:
		itemsOnDisplay.remove(itemsOnDisplay.size()-1);
		iRemovedFromDisplay=itemsOnDisplay.size()-1;
		//1 calculate the iFirst and iLast thing:
		int[] range=getNextScrollRange(nItems,nDisplay,iFirst,isLeftFromRight);
		iFirstPrev=iFirst;
		iLastPrev=iLast;
		iFirst=range[0];
		iLast=range[1];
		iItemToDisplay=iFirst;
		//now put the required item at the start:
		itemsOnDisplay.add(0, items.get(iFirst));
		//the above addition increases the width of the display:
		wContent+=wItem+marginItemLeft+marginItemRight;
		//shift the content to the left by one item unit width:
		xContent=-scrollSpan;
		//activity
		isScrolling=true;
		isResting=false;
		//getting the current position of the content:
		xContent0=xContent;

		//isNextLeftFromRight=isLeftFromRight;
		//fire event for the view to start run time:
		//view.startRun(tScroll);
		eventHandler.fireEvent(new SliderEvent("startLeftToRight"));
		
	}

	public void startScroll() {
		isScrolling=true;
		isResting=false;
		eventHandler.fireEvent(new SliderEvent("scrollStarted"));
		
	}
	public double getMarginItemLeft() {
		return marginItemLeft;
	}
	public double getMarginItemRight() {
		return marginItemRight;
	}
	public int getwSidePanel() {
		return wSidePanel;
	}
	public double getwItem() {
		return wItem;
	}
	public int getnTotalItems() {
		return nTotalItems;
	}
	public void setnTotalItems(int nTotalItems) {
		this.nTotalItems = nTotalItems;
	}
	public void setLoadedItems(List<String> imageUrls, List<String> linkUrls, int nTotalItems){
		itemsLoaded=new ArrayList<Item>();
		int size=items.size();
		for(int i=0;i<imageUrls.size();i++){
			itemsLoaded.add(new Item(size,imageUrls.get(i),linkUrls.get(i)));
			size++;
		}
		this.nTotalItems=nTotalItems;
	}
	public List<Item> getItemsLoaded() {
		return itemsLoaded;
	}
	public void updateDisplay(){
		int nItemsPrev=nItems;
		addNewItems();
		eventHandler.fireEvent(new SliderEvent("newDataAddedPause",nItemsPrev));
	}
	private void addNewItems(){
		if(itemsLoaded.size()>0){
			for(Item item:itemsLoaded){
				items.add(item);
				nItems++;
			}
		}
		//string for ui display:
		itemsInfo="showing "+nItems+" of "+nTotalItems;
		//creating a new display list:
		//calculate the new iStart and iEnd:
		//int[] range=getNextScrollRange(nItems,nDisplay,iFirst,isLeftFromRight);
		
		iLast=getLastIndex(nItems,nDisplay,iFirst);
		//get the index sequence in the display list:
		List<Integer> sequence = this.getScrollIndexes(nItems, iFirst, iLast);
		//put the items on the display list based on the above sequence:
		itemsOnDisplay.clear();
		for(int i:sequence){
			itemsOnDisplay.add(items.get(i));
		}
		//removing the items from the list:
		
		itemsLoaded.clear();
	}
	private int getLastIndex(int nItems,int nDisplayItems,int iFirst){
		int iLast=0;
		iLast=iFirst+nDisplayItems-1;
		if(iLast>=nItems){
			iLast=nDisplayItems-nItems+iFirst-1;
		
		}
		return iLast;
	}
	/*
	public void setNewItemsData(int iStart,List<String> imagesUrls,List<String> linkUrls){
		if(iStart>=items.size()) return;
		for(int i=iStart;i<items.size();i++){
			imagesUrls.add(items.get(i).getImageUrl());
			linkUrls.add(items.get(i).getLinkUrl());
		}
	}
	*/
	public String getItemsInfo() {
		return itemsInfo;
	}
	
}
