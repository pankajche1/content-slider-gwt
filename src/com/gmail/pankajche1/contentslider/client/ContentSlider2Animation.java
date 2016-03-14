package com.gmail.pankajche1.contentslider.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

public class ContentSlider2Animation extends Animation{
	private ContentSlider2 view;
	private ItemsContainer target;
	private Element elem;
	
	private int duration=500;//0.5 seconds
	private int stay=1000;//1 second stay
	private int slideMode=0;//for right to left default mode
	private int curDuration=0;
	private double left2=225;
	private double leftOffset;
	private double left1;
	private double finalLeft;
	private double totalChange;
	private double p1;
	private double p2;
	private double pTemp1,pTemp2;
	private boolean isLeftToRightTemp;
	private final static int PRIME_STATE=0;
	//scroll modes:
	private final static int LEFT_TO_RIGHT=1;
	private final static int RIGHT_TO_LEFT=2;
	private final static int LEFT_TO_RIGHT_REST=3;
	private final static int RIGHT_TO_LEFT_REST=4;
	//states:
	private final static int RIGHT_LEFT_STATE=3;
	private final static int LEFT_RIGHT_STATE=6;
	private final static int REST_STATE=4;
	private final static int NO_WORK_STATE=5;
	//layout of the slider:
	//private final static int ITEM_AT_RIGHT=0;
	//private final static int ITEM_AT_LEFT=0;
	//state mode of the animation:
	//1: doing forced left scroll
	//2: doing forced right scroll
	//3: left <- right
	//4: doing rest after a scroll
	//5: no work standing still
	//6: left -> right
	private int stateMode=PRIME_STATE;//no work standing still
	private int activityMode=0;//0: activity not started yer, 1: working, 2: not working (paused)
	//toggle pause button:
	private boolean isPauseToggle=false;
	private int scrollingMode;//0 for right to left, 1 for left to right;
	//variable scrollMode:
		//0 no setting yet, 1 left to right scroll 2 right to left scroll
	private int scrollMode=0;//0
	private int scrollModeNext;
	private boolean isScrollingMode=false;
	
	private boolean isSwitchDirection=false;
	private boolean isPause=false;
	private boolean isToBePaused=false;
	private boolean isPauseMode1=false;
	private boolean isMousePause=false;
	private boolean isStay=false;
	
	private boolean isAnimOn=false;
	private boolean isAnimStarted=false;
	private boolean isForceScroll=false;
	private List<Integer> listScrollCommands;
	private int scrollDirection=-1;
	private boolean isLeftToRight=false;
	public ContentSlider2Animation(ContentSlider2 view, ItemsContainer target,double leftOffset) {
		
		this.view = view;
		this.target = target;
		this.leftOffset=leftOffset;
		elem=this.target.getElement();
		listScrollCommands=new ArrayList<Integer>();
	}	
	public void togglePause(boolean isDown){
		if(!isDown){//the pause/play button is down: pause is showing
			//show the system is to be paused
			pause();
		}else{//the button is up: play icon is showing
			//show the system is to start playing
			resume();
		}
		/*
		if(isPauseToggle){
			resume();
			isPauseToggle=false;
		}else{
			pause();
			isPauseToggle=true;
		}
		*/
	}
	public void pauseByMouseIn(){
		if(isPauseMode1){
			//do nothing if the system has been paused by
			//the play/pause button
		}else{
			//tell that the system is paused by mouse in
			isMousePause=true;
			//now if the system is in some activity
			//issue the pause orders to pause when activity is over:
			isToBePaused=true;
			
		}
	}
	public void resumeByMouseOut(){
		if(!isPauseMode1){
			isMousePause=false;
			//system has not been paused by the pause button:
			//so resume by mouse out
			isToBePaused=false;
			if(isPause){
				isPause=false;
				//System.out.println("resumeByMouseOut(): stateMode:"+stateMode
						//+", scrollMode:"+scrollMode);
				//the system was paused at the start of the rest:
				//so the state mode is NO_WORK_STATE
				//we need to reset that:
				if(scrollMode==RIGHT_TO_LEFT_REST){
					stateMode=REST_STATE;
				}else if(scrollMode==LEFT_TO_RIGHT_REST){
					stateMode=REST_STATE;
				}else if(scrollMode==LEFT_TO_RIGHT){
					stateMode=LEFT_RIGHT_STATE;
				}else if(scrollMode==RIGHT_TO_LEFT){
					stateMode=RIGHT_LEFT_STATE;
				}
				this.run(curDuration);
			}
			
		}
	}
	//this is pause by the pause/play button:
	public void pause(){
		//System.out.println("pause() called...");
		//place an order that it is to be paused:
		isToBePaused=true;
		isPauseMode1=true;//the system will by paused by the button
		switch(stateMode){
		case 1:
		break;
		case 2:
			break;
		case RIGHT_LEFT_STATE:// scrolling from right to left is going on
			//isPause=true;
			break;
		case REST_STATE://rest after scroll
			//isPause=true;
			//cancel the current rest animation
			//activityMode=2;
			//this.cancel();

			break;
		case NO_WORK_STATE://doing no work at this time

			
			
			break;
		case LEFT_RIGHT_STATE://scrolling from left to right is on:
			//isPause=true;
			break;
		}
	}
	//resume by the button:
	public void resume(){
		//System.out.println("resume() called...");
		isToBePaused=false;
		isPauseMode1=false;//system paused by play/pause button
		if(isPause){
				isPause=false;
				//System.out.println("resumeByMouseOut(): stateMode:"+stateMode
						//+", scrollMode:"+scrollMode);
				//the system was paused at the start of the rest:
				//so the state mode is NO_WORK_STATE
				//we need to reset that:
				if(scrollMode==RIGHT_TO_LEFT_REST){
					stateMode=REST_STATE;
				}else if(scrollMode==LEFT_TO_RIGHT_REST){
					stateMode=REST_STATE;
				}else if(scrollMode==LEFT_TO_RIGHT){
					stateMode=LEFT_RIGHT_STATE;
				}else if(scrollMode==RIGHT_TO_LEFT){
					stateMode=RIGHT_LEFT_STATE;
				}
				this.run(curDuration);
		}
		
		/*
		if(activityMode==2){
			activityMode=1;//system has started working
			this.run(curDuration);
		}
	*/

		
	}
	public void startRightToLeft(int duration,int tStay,int left2){
		//System.out.println("startRightToLeft() called...");
		activityMode=1;
		//System.out.println("scroll mode:"+scrollMode+", stateMode:"+stateMode+
				//", isPause:"+isPause+", isToBePaused:"+isToBePaused);
		//the animation has started so the pause/play button should
		//show the pause face:
		view.showPause();
		//isPauseToggle=false;
		//system data:
		isPause=false;
		isPauseMode1=false;//system paused by play/pause button
		switch(stateMode){
		case PRIME_STATE:
			//the user may have pressed the pause button:
			//so remove these settings:
			isPause=false;
			isToBePaused=false;
			//now set the scrolling parameters:
			p1=-leftOffset;
			p2=0;
			//set the scroll/rest activity:
			scrollingMode=0;//0 for right to left TODO remove this variable
			//the system is scrolling
			isScrollingMode=true;//TODO remove this variable
			//parameters to be set:
			//1 state mode:
			stateMode=3;//right to left scroll at the present moment
			//2 scrollMode:
			scrollMode=RIGHT_TO_LEFT;//system is set for right to left scrolling
			//animation data:
			this.duration=duration;
			this.stay=tStay;
			this.curDuration=this.duration;
			//start animating:
			this.run(duration);
			
			break;
		case 1:
		break;
		case 2:
			break;
		case RIGHT_LEFT_STATE:// scrolling from right to left is going on
			//the user may have pressed the pause button so reset it:
			isToBePaused=false;
			//and do nothing
			break;
		case REST_STATE://rest after scroll
			//during rest scroll from right to left is pressed
			//during the rest process the user may have pressed the pause button
			//so release that state:
			isPause=false;
			isToBePaused=false;
			//here we want to know the scroll mode
			if(scrollMode==LEFT_TO_RIGHT_REST){
				isSwitchDirection=true;
				pTemp1=-leftOffset;
				pTemp2=0;
				scrollModeNext=RIGHT_TO_LEFT;//next mode of scrolling is from left to right	
				/*
				//first cancel the current rest animation
				this.cancel();
				//set the updating parameters:
				p1=-220;
				p2=0;
				//set the scroll/rest activity:
				scrollMode=RIGHT_TO_LEFT;//0 for right to left
				//the system is scrolling
				isScrollingMode=true;
				//state mode:
				stateMode=RIGHT_LEFT_STATE;//right to left scroll at the present moment
				//animation data:
				this.duration=duration;
				this.stay=tStay;
				this.curDuration=this.duration;
				target.shiftItemToBack();
				elem.getStyle().setLeft(0, Style.Unit.PX);
				//start animating:
				this.run(duration);
				*/
			}
			/* commented on 131002 2012
			if(scrollingMode!=0){//switch the direction only if the scroll in not from 
				//right to left
				//first cancel the current rest animation
				this.cancel();
				//set the updating parameters:
				p1=-220;
				p2=0;
				//set the scroll/rest activity:
				scrollingMode=0;//0 for right to left
				//the system is scrolling
				isScrollingMode=true;
				//state mode:
				stateMode=3;//right to left scroll at the present moment
				//animation data:
				this.duration=duration;
				this.stay=tStay;
				this.curDuration=this.duration;
				//start animating:
				this.run(duration);
			}
			*/
			break;
		case NO_WORK_STATE://doing no work at this time
			//clear all the pausing paramters:
			isPause=false;
			isToBePaused=false;		
			//see what is the scroll mode at the presente paused state:
			//if it is from left to right then just start the scroll
			if(scrollMode==RIGHT_TO_LEFT_REST){
				//start the scrolling again:
				stateMode=RIGHT_LEFT_STATE;
				this.run(tStay);
			}else if(scrollMode==RIGHT_TO_LEFT){
				stateMode=RIGHT_LEFT_STATE;
				this.run(duration);
				
			}else if(scrollMode==LEFT_TO_RIGHT_REST||scrollMode==LEFT_TO_RIGHT){
				//change the paramters of scrolling:
				stateMode=RIGHT_LEFT_STATE;
				scrollMode=RIGHT_TO_LEFT;
				//now set the scrolling parameters:
				p1=-leftOffset;
				p2=0;	
				//the last picture is to be shifted to the first:
				this.duration=duration;
				this.stay=tStay;
				this.curDuration=this.duration;
				//the the last item is shifted to the first position
				target.shiftItemToBack();
				elem.getStyle().setLeft(0, Style.Unit.PX);
				//start animating:
				this.run(duration);				
			}			
			
			
			break;
		case LEFT_RIGHT_STATE://left -> right is ON
			//so when the system will come to the rest
			isToBePaused=false;
			//these paramters will be set:
			
			isSwitchDirection=true;
			pTemp1=-leftOffset;
			pTemp2=0;
			scrollModeNext=RIGHT_TO_LEFT;//next mode of scrolling is from left to right	
				
			break;
		}
		
/*
		
		if(stateMode==6){//it is doing left to right scroll
			
		}
		//if not then wait for this rest mode:
		if(stateMode==4){//means animation is resting
			//so new parameters can be set
			p1=225;
			p2=-225;
			//change the arrangements here:
			if(isLeftToRight){
				//before the rest it was scrolling left to right
				//put the first item to the back:
				target.shiftItemToBack();
				elem.getStyle().setLeft(0, Style.Unit.PX);
			}else{
				//before the rest it was scrolling right to left
				target.shiftLastToFirst();
				elem.getStyle().setLeft(-225, Style.Unit.PX);//for left to right scroll
			}
			
		}else{//the animation is not free so new parameters can
			//the switch direction of scroll command is issued:
			isSwitchDirection=true;
			//not be set
			pTemp1=225;
			pTemp2=225;
			isLeftToRightTemp=false;
			
		}
		slideMode=0;
		//System.out.println(target.getStyle().getLeft());
		//target.getStyle().setLeft(300, Style.Unit.PX);
		//System.out.println(target.getStyle().getLeft());
		//this.left1=elem.getAbsoluteLeft();
		//System.out.println("left1:"+left1);
		this.left2=225;
		totalChange=225;
		finalLeft=-225;
		scrollDirection=-1;
		isLeftToRight=false;
		stateMode=3;
		//p1=-225;
		//p2=0;
		//System.out.println("left2:"+left2);
		//target.getAbsoluteLeft();
		//target.getOffsetLeft();
		this.duration=duration;
		this.stay=tStay;
		this.curDuration=this.duration;
		isAnimStarted=true;
		isAnimOn=true;
		//this.run(duration);
		*/
		
	}
	public void startLeftToRight(int duration,int tStay){
		//System.out.println("startLeftToRight() called...");
		//the animation has started so the pause/play button should
		//show the pause face:
		view.showPause();
		//isPauseToggle=false;
		//system data:
		isPause=false;
		isPauseMode1=false;//system paused by play/pause button
		activityMode=1;
		//this.duration=duration;
		//this.stay=tStay;
		switch(stateMode){
		case PRIME_STATE:
			//the user may have pressed the pause button:
			//so remove these settings:
			isPause=false;
			isToBePaused=false;
			//now set the scrolling parameters:
			p1=leftOffset;
			p2=-leftOffset;
			//parameters to be set:
			//1 state mode:
			stateMode=LEFT_RIGHT_STATE;//left -> right scroll mode
			//2 scrollMode:
			scrollMode=LEFT_TO_RIGHT;//left -> right scroll
			//animation data:
			this.duration=duration;
			this.stay=tStay;
			this.curDuration=this.duration;
			//the the last item is shifted to the first position
			target.shiftLastToFirst();
			//the first item is hide:
			elem.getStyle().setLeft(-leftOffset, Style.Unit.PX);
			//start animating:
			this.run(duration);
			break;
		case 1:
		break;
		case 2:
			break;
		case RIGHT_LEFT_STATE:// scrolling from right to left is going on
			//tell the system that an order has come to 
			//switch the mode from left to right
			isSwitchDirection=true;
			pTemp1=leftOffset;
			pTemp2=-leftOffset;
			scrollModeNext=LEFT_TO_RIGHT;//next mode of scrolling is from left to right
			break;
		case REST_STATE://rest after scroll
			//the user may have pressed the pause button:
			//so remove these settings:
			isPause=false;
			isToBePaused=false;
			//now if the scrollMode is opposite to that we want:
			if(scrollMode==RIGHT_TO_LEFT_REST){//act only when current scroll is not from left to right
				isSwitchDirection=true;
				pTemp1=leftOffset;
				pTemp2=-leftOffset;
				scrollModeNext=LEFT_TO_RIGHT;//next mode of scrolling is from left to right
				/*
				//cancel the current animation:
				this.cancel();
				//set the updating parameters:
				p1=220;
				p2=-220;
				//set the scroll/rest activity:
				scrollMode=LEFT_TO_RIGHT;//0 for right to left and 1 from left to right
				//the system is scrolling
				isScrollingMode=true;
				//state mode:
				stateMode=LEFT_RIGHT_STATE;//left to right scroll at the present moment
				//animation data:
				this.duration=duration;
				this.stay=tStay;
				this.curDuration=this.duration;
				//start animating:
				//the the last item is shifted to the first position
				target.shiftLastToFirst();
				//the first item is hide:
				elem.getStyle().setLeft(-220, Style.Unit.PX);
				this.run(duration);
				*/
			}
			break;
		case NO_WORK_STATE://doing no work at this time
			//clear all the pausing paramters:
			isPause=false;
			isToBePaused=false;		
			//see what is the scroll mode at the presente paused state:
			//if it is from left to right then just start the scroll
			if(scrollMode==LEFT_TO_RIGHT_REST){
				//start the scrolling again:
				stateMode=LEFT_RIGHT_STATE;
				this.run(tStay);
			}else if(scrollMode==LEFT_TO_RIGHT){
				stateMode=LEFT_RIGHT_STATE;
				this.run(duration);
				
			}else if(scrollMode==RIGHT_TO_LEFT_REST||scrollMode==RIGHT_TO_LEFT){
				//change the paramters of scrolling:
				stateMode=LEFT_RIGHT_STATE;
				scrollMode=LEFT_TO_RIGHT;
				//now set the scrolling parameters:
				p1=leftOffset;
				p2=-leftOffset;		
				//the last picture is to be shifted to the first:
				this.duration=duration;
				this.stay=tStay;
				this.curDuration=this.duration;
				//the the last item is shifted to the first position
				target.shiftLastToFirst();
				//the first item is hide:
				elem.getStyle().setLeft(-leftOffset, Style.Unit.PX);
				//start animating:
				this.run(duration);				
			}
			/*
			 * 


			//set the scroll/rest activity:
			scrollingMode=0;//0 for right to left TODO remove this variable
			//the system is scrolling
			isScrollingMode=true;//TODO remove this variable
			//parameters to be set:
			//1 state mode:
			stateMode=3;//right to left scroll at the present moment
			//2 scrollMode:
			scrollMode=2;//system is set for right to left scrolling
			//animation data:
			this.duration=duration;
			this.stay=tStay;
			this.curDuration=this.duration;
			//start animating:
			this.run(duration);
			 * 
			 */
			
			/*
			//set the scroll/rest activity:
			scrollingMode=1;//0 for right to left and 1 from left to right
			//the system is scrolling
			isScrollingMode=true;
			//state mode:
			stateMode=6;//left to right scroll at the present moment
			//animation data:
			this.duration=duration;
			this.stay=tStay;
			this.curDuration=this.duration;
			//start animating:
			//the the last item is shifted to the first position
			target.shiftLastToFirst();
			//the first item is hide:
			elem.getStyle().setLeft(-220, Style.Unit.PX);
			this.run(duration);
			*/
			
			break;
		case LEFT_RIGHT_STATE://doing left to right scroll so no need
			//to do any thing else
			//but the user may have pressed the pause button:
			//so release that constraint:
			isPause=false;
			isToBePaused=false;			
			break;
		}
	
		/*
		slideMode=1;
		this.left2=225;
		this.duration=duration;
		this.stay=tStay;
		isAnimStarted=true;
		isAnimOn=true;
		totalChange=225;
		finalLeft=-225;		
		p1=225;
		p2=-225;
		isLeftToRight=true;
		stateMode=6;
		//the the last item is shifted to the first position
		target.shiftLastToFirst();
		//the first item is hide:
		elem.getStyle().setLeft(-225, Style.Unit.PX);
		scrollDirection=1;
		this.run(duration);
		*/
		
	}
	//force scroll left<-right
	public void scrollLeft(){
		isForceScroll=true;
		
		listScrollCommands.add(RIGHT_TO_LEFT);//code for scrolling left
		//System.out.println("isStay:"+isStay);
		if(isStay){//if the slider is at rest 
			//the slider will be foreced to slide
			isStay=false;
			curDuration=duration;
			//see if there is some force scroll:
			if(listScrollCommands.size()>0){
				scrollDirection=listScrollCommands.remove(0);
				
			}else{
				scrollDirection=-1;//the default scroll direction
			}	
			run(curDuration);
		}else{
			//it means scrolling is going on alreay
		}
		
		
		
	}
	public void scrollRight(){
		isForceScroll=true;
		listScrollCommands.add(1);//code for scrolling right
		
			
	}
	//the sliding will pause only after completing the current slide animation
	public void pauseAfterComplete(){
		pause();
	}
	public void restart(){
		resume();
		/*
		isPause=false;
		
		if(isAnimStarted){
			if(!isAnimOn){
				isAnimOn=true;
				if(isStay){
					isStay=false;
				}else{
					isStay=true;
				}
				this.run(curDuration);
			}
		}
		*/
	}
	@Override
	protected void onUpdate(double progress) {
		
		if(stateMode!=4){//the slider is sliding
			//for right to left
			///elem.getStyle().setLeft(scrollDirection*left2*progress, Style.Unit.PX);
			//for left to right:
			//elem.getStyle().setLeft((-225+scrollDirection*left2*progress), Style.Unit.PX);
			elem.getStyle().setLeft(p1*progress+p2, Style.Unit.PX);
			//scrollDirection*225*progress-225;//right to left
			//(p)
			//scrollDirection*totalChange*progress+finalLeft;//left to right
			//System.out.println("left:"+elem.getStyle().getLeft());
		}else{//the slider is staying still
			//do nothing;
		}
		
		
	}
	@Override
	protected void onComplete(){
		//target.getStyle().setLeft(0, Style.Unit.PX);	
		switch(stateMode){
		case 1:
			break;
		case RIGHT_LEFT_STATE://one unit of scroll from right to left complete:
			//if an order has come that the  scroll is to be from
			// left left -> right
			
			//isStay=true;//now it will stay still
			stateMode=REST_STATE;//doing rest mode code
			scrollMode=RIGHT_TO_LEFT_REST;
			//changing the arrangement of the items on the slider:
			target.shiftItemToBack();
			elem.getStyle().setLeft(0, Style.Unit.PX);
			
			//right to left
			
			//left to right
			///elem.getStyle().setLeft(0, Style.Unit.PX);//for left to right scroll
			//elem.getStyle().setLeft(0, Style.Unit.PX);//for Right to left scroll
			curDuration=stay;
			break;
		case REST_STATE:// 4 rest state after scrolls
			//System.out.println("REST complete() called...");
			curDuration=duration;
			//here the system can come from two sources:
			//1 from scrolling right to left rest complete
			//2 from scrolling left to right rest complete
				//so how to know the source
				//by the var 'scrollMode'
			if(scrollMode==RIGHT_TO_LEFT_REST){//right to left mode
				stateMode=RIGHT_LEFT_STATE;//left <- right
				scrollMode=RIGHT_TO_LEFT;
			}else if(scrollMode==LEFT_TO_RIGHT_REST){//left to right mode
				stateMode=LEFT_RIGHT_STATE;//left -> right
				scrollMode=LEFT_TO_RIGHT;
			}
			//if scroll direction is to be switched:
			if(isSwitchDirection){
				isSwitchDirection=false;
				//change the arrangement of the items on the slider
				//setting these three parameters;
					p1=pTemp1;
					p2=pTemp2;
					scrollMode=scrollModeNext;
					if(scrollMode==RIGHT_TO_LEFT){//left <- right
						//before the rest it was scrolling left to right
						//put the first item to the back:
						target.shiftItemToBack();
						elem.getStyle().setLeft(0, Style.Unit.PX);
						stateMode=RIGHT_LEFT_STATE;
					}else if(scrollMode==LEFT_TO_RIGHT){//left -> right
						//before the rest it was scrolling right to left
						target.shiftLastToFirst();
						elem.getStyle().setLeft(-leftOffset, Style.Unit.PX);//for left to right scroll
						stateMode=LEFT_RIGHT_STATE;
					}
					
				
			}
			break;
		case LEFT_RIGHT_STATE://completing the left to right scroll mode
			if(listScrollCommands.size()>0){
				//isForceScroll=true;
				//there are some force scrolling commands:
				scrollDirection=listScrollCommands.remove(0);
				if(scrollDirection==RIGHT_TO_LEFT){
					//force scroll left<-right:
					//change the scrollling parameters:
					p1=-leftOffset;
					p2=0;
					run(duration);
					
				}
				
			}else{
				stateMode=REST_STATE;//doing rest mode code
				scrollMode=LEFT_TO_RIGHT_REST;
				target.shiftLastToFirst();
				elem.getStyle().setLeft(-leftOffset, Style.Unit.PX);//for left to right scroll
				curDuration=stay;
			}
			break;
		}
		//System.out.println("In complete(): isPause:"+isPause+", isToBePaused:"+isToBePaused);
		if(!isToBePaused){
			this.run(curDuration);
		}else{
			isToBePaused=false;
			isPause=true;
			stateMode=NO_WORK_STATE;
			activityMode=2;//the system is paused
		}
		/*
		if(isStay){
			isStay=false;
			curDuration=duration;
			//see if there is some force scroll:
			if(listScrollCommands.size()>0){
				scrollDirection=listScrollCommands.remove(0);
				
			}else{
				//if right to left scroll:
				///scrollDirection=-1;//the default scroll direction
				//if left to right scroll:
				scrollDirection=1;
			}

			
		}else{//coming after completing the sliding operation
			isStay=true;//now it will stay still
			stateMode=4;//doing rest
			//changing the arrangement of the items on the slider:
			if(!isLeftToRight){
				target.shiftItemToBack();
				elem.getStyle().setLeft(0, Style.Unit.PX);
			}
			else{
				target.shiftLastToFirst();
				elem.getStyle().setLeft(-225, Style.Unit.PX);//for left to right scroll
			}
			//right to left
			
			//left to right
			///elem.getStyle().setLeft(0, Style.Unit.PX);//for left to right scroll
			//elem.getStyle().setLeft(0, Style.Unit.PX);//for Right to left scroll
			curDuration=stay;

		}
		//if the user has chosen to pause or not:
		if(isPause){
			isAnimOn=false;
		}else{
			isAnimOn=true;
			this.run(curDuration);
		}
		*/
		
	}


}
