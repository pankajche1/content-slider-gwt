package com.gmail.pankajche1.contentslider.client;

import java.util.List;

import com.gmail.pankajche1.contentslider.client.resources.ClientBundle1;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class ContentSlider1 extends Composite implements MouseOverHandler,MouseOutHandler{

	private static ContentSlider1UiBinder uiBinder = GWT
			.create(ContentSlider1UiBinder.class);

	interface ContentSlider1UiBinder extends UiBinder<Widget, ContentSlider1>{
	}
	//@UiField
	//Button btnPause,btnResume;
	//@UiField
	//Button btnSlideToLeft,btnSlideToRight;
	//@UiField
	//HTMLPanel target;
	@UiField
	HTMLPanel content,controlPanel,controlBase;
	//left and right scrolls:
	@UiField
	HTMLPanel leftScroll,rightScroll;
	private PushButton btnLeft,btnRight;
	private PushButton btnLeftToRight,btnRightToLeft;
	private ToggleButton btnPausePlay;
	@UiField
	FocusPanel basePanel;//,leftScrollBtnPanel,rightScrollBtnPanel;
	@UiField
	Style style;
	private ClientBundle1 bundle;
	private ContentSlider1Animation anim;
	private List<String> imageUrls;
	public ContentSlider1(List<String> imageUrls) {
		initWidget(uiBinder.createAndBindUi(this));
		bundle=ClientBundle1.INSTANCE;
		//adding the left and right buttons:
		Image imgBtnLeft=new Image(bundle.control_left());
		Image imgBtnRight=new Image(bundle.control_right());
		btnLeft=new PushButton(imgBtnLeft);
		btnLeft.setStyleName(style.scrollButton());
		btnRight=new PushButton(imgBtnRight);
		btnRight.setStyleName(style.scrollButton());
		leftScroll.add(btnLeft);
		rightScroll.add(btnRight);
		btnLeft.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				anim.scrollRight();
				
			}});
		btnRight.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				anim.scrollLeft();
				
			}});
		//pause/play toggle button:
		btnPausePlay = new ToggleButton(new Image(bundle.control_play_blue()),
				new Image(bundle.control_pause_blue())
				);
		btnPausePlay.setStyleName(style.toggleButton());
		controlBase.add(btnPausePlay);
		//btnPausePlay.
		btnPausePlay.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				anim.togglePause(btnPausePlay.isDown());
				
				
			}});
		//scroll left->right button:
		btnLeftToRight=new PushButton(new Image(bundle.control_fastforward_blue()));

		controlBase.add(btnLeftToRight);
		btnLeftToRight.setStyleName(style.toggleButton());
		btnLeftToRight.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//
				//anim.startLeftToRight(800, 2000);
				anim.startLeftToRight(5000, 5000);
				
			}});
		//scroll left<-right button:
		btnRightToLeft=new PushButton(new Image(bundle.control_rewind_blue()));
		controlBase.add(btnRightToLeft);
		btnRightToLeft.setStyleName(style.toggleButton());
		btnRightToLeft.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//anim.startRightToLeft(800,2000, 200);
				anim.startRightToLeft(5000,5000, 200);
				
			}});
		
		
		controlPanel.add(new HTML("<div style='clear:both;'></div>"));


		//now design the slider:
		this.imageUrls=imageUrls;
		//create 10 items on the target (this is the element which slides)
		int i=0;
		ItemsContainer cont = new ItemsContainer("");
		cont.setStyleName(style.container());
		content.add(cont);
		HTMLPanel item;
		for(i=0;i<this.imageUrls.size();i++){
			item=new HTMLPanel("<img src='"+this.imageUrls.get(i)+"'></img>");
			item.setStyleName(style.item());
			//target.add(item);
			cont.addItem(item);
		}
		basePanel.addMouseOutHandler(this);
		basePanel.addMouseOverHandler(this);
		//leftScrollBtnPanel.addMouseOutHandler(this);
		//leftScrollBtnPanel.addMouseOverHandler(this);
		//rightScrollBtnPanel.addMouseOutHandler(this);
		//rightScrollBtnPanel.addMouseOverHandler(this);
		anim=new ContentSlider1Animation(this,cont);
		
		
	}
	public void showPause(){
		btnPausePlay.setDown(true);//pause is the up face
	}
	public void showPlay(){
		btnPausePlay.setDown(false);//play is the down state
	}
	public void start(){
		//anim.startRightToLeft(800,2000, 200);
		anim.startRightToLeft(5000,5000, 200);
	}
	//@UiHandler("btnPause")
	//void onPauseClick(ClickEvent event){
		//anim.start(800,1000, 200);
		//anim.pause();
	//}
	//@UiHandler("btnResume")
	//void onResumeClick(ClickEvent event){
		//anim.start(800,1000, 200);
		//anim.resume();
	//}
	//@UiHandler("btnSlideToRight")
	//void startLeftToRightHandler(ClickEvent event){
		//anim.start(800,1000, 200);
		//anim.startLeftToRight(800, 1000);
	//}
	//@UiHandler("btnSlideToLeft")
	//void startRightToLeftHandler(ClickEvent event){
		//anim.startRightToLeft(800,1000, 200);
	
	//}
	interface Style extends CssResource{
		String rootPanel();
		String content();
		String container();
		String item();
		//toggle button style:
		String toggleButton();
		String controlPanel();
		String scrollButton();
	}
	@Override
	public void onMouseOut(MouseOutEvent event) {
		//System.out.println("mouse out");
		anim.resumeByMouseOut();
	}
	@Override
	public void onMouseOver(MouseOverEvent event) {
		//System.out.println("mouse in");
		anim.pauseByMouseIn();
		
	}

}
