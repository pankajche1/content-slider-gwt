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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Style;

public class ContentSlider2 extends Composite  implements MouseOverHandler,MouseOutHandler{

	private static ContentSlider2UiBinder uiBinder = GWT
			.create(ContentSlider2UiBinder.class);

	interface ContentSlider2UiBinder extends UiBinder<Widget, ContentSlider2> {
	}
	@UiField
	HTMLPanel rootPanel;
	@UiField
	HTMLPanel sliderPanel;//this contains the slider and side bars on each sides
	//the content is the mask which shows only through the width and height
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
	ContentSlider2Style style;
	private ClientBundle1 bundle;
	private ContentSlider2Animation anim;
	private List<String> imageUrls;
	private List<String> linkedItemsUrls;
	public ContentSlider2(int width,int height,int widthItem,int heightItem,int spacing,List<String> imageUrls,
			List<String> linkedItemsUrls) {
		
		initWidget(uiBinder.createAndBindUi(this));
		bundle=ClientBundle1.INSTANCE;
		//setting the width of the rootPane:
		//rootPanel.setWidth(String.valueOf(width));
		//setting the customized size:
		//1 root panel:
		rootPanel.setHeight(String.valueOf(height));
		rootPanel.getElement().getStyle().setWidth(width, Style.Unit.PX);
		rootPanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//slider panel which contains the middle slider and the side bars on each side:
		sliderPanel.getElement().getStyle().setWidth(width, Style.Unit.PX);
		sliderPanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//2 content panel which is the mask:
		int wSidePanel=42;//42 px side panels on each side:
		content.getElement().getStyle().setWidth(width-2*42, Style.Unit.PX);
		content.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//4 the style of the side bars which contain the left right scroll direction change butttons:
		leftScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		leftScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//leftScroll.getElement().getStyle().setLineHeight(height, Style.Unit.PX);
		rightScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		rightScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);	
		//rightScroll.getElement().getStyle().setLineHeight(height, Style.Unit.PX);
		//5 item object's style:
		//int wItem=198;
		//int hItem=198;
		//adding the left and right buttons:
		Image imgBtnLeft=new Image(bundle.control_left());
		Image imgBtnRight=new Image(bundle.control_right());
		btnLeft=new PushButton(imgBtnLeft);
		btnLeft.getUpHoveringFace().setImage(new Image(bundle.leftButtonBlue()));
		btnLeft.setStyleName(style.scrollButton());
		//24px/2=12px is the height of the image inside
		btnLeft.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnLeft.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);
		btnRight=new PushButton(imgBtnRight);
		btnRight.getUpHoveringFace().setImage(new Image(bundle.rightButtonBlue()));
		btnRight.setStyleName(style.scrollButton());
		btnRight.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnRight.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);
		leftScroll.add(btnLeft);
		rightScroll.add(btnRight);
		btnLeft.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//anim.scrollRight();
				anim.startLeftToRight(800, 2000);
				
			}});
		btnRight.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//anim.scrollLeft();
				anim.startRightToLeft(800,2000, 200);
				
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
		/*
		//scroll left->right button:
		btnLeftToRight=new PushButton(new Image(bundle.control_fastforward_blue()));

		controlBase.add(btnLeftToRight);
		btnLeftToRight.setStyleName(style.toggleButton());
		btnLeftToRight.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				//
				anim.startLeftToRight(800, 2000);
				//anim.startLeftToRight(5000, 5000);
				
			}});
		//scroll left<-right button:
		btnRightToLeft=new PushButton(new Image(bundle.control_rewind_blue()));
		controlBase.add(btnRightToLeft);
		btnRightToLeft.setStyleName(style.toggleButton());
		btnRightToLeft.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				anim.startRightToLeft(800,2000, 200);
				//anim.startRightToLeft(5000,5000, 200);
				
			}});
		
		*/
		//controlPanel.add(new HTML("<div style='clear:both;'></div>"));


		//now design the slider:
		this.imageUrls=imageUrls;
		this.linkedItemsUrls=linkedItemsUrls;

		//create 10 items on the target (this is the element which slides)
		int i=0;
		ItemsContainer cont = new ItemsContainer("");
		cont.setStyleName(style.container());
		content.add(cont);
		HTMLPanel item;
		int wTotal=0;
		//int marginRight=5;
		//int spacing=10;
		for(i=0;i<this.imageUrls.size();i++){
			item=new HTMLPanel("<a href='"+linkedItemsUrls.get(i)+"'><img src='"+this.imageUrls.get(i)+"' width='"+widthItem+"px' height='"+heightItem+"px'></img></a>");
			item.setStyleName(style.item());
			item.getElement().getStyle().setWidth(widthItem, Style.Unit.PX);
			item.getElement().getStyle().setHeight(heightItem, Style.Unit.PX);
			item.getElement().getStyle().setMarginLeft(spacing/2, Style.Unit.PX);
			item.getElement().getStyle().setMarginRight(spacing/2, Style.Unit.PX);
			//width:
			wTotal+=widthItem+spacing;
			//target.add(item);
			cont.addItem(item);
		}
		cont.getElement().getStyle().setWidth(wTotal, Style.Unit.PX);
		cont.getElement().getStyle().setTop(height/2-heightItem/2, Style.Unit.PX);
		basePanel.addMouseOutHandler(this);
		basePanel.addMouseOverHandler(this);
		//leftScrollBtnPanel.addMouseOutHandler(this);
		//leftScrollBtnPanel.addMouseOverHandler(this);
		//rightScrollBtnPanel.addMouseOutHandler(this);
		//rightScrollBtnPanel.addMouseOverHandler(this);
		anim=new ContentSlider2Animation(this,cont,widthItem+spacing);
	}
	public void showPause(){
		btnPausePlay.setDown(true);//pause is the up face
	}
	public void showPlay(){
		btnPausePlay.setDown(false);//play is the down state
	}
	public void start(){
		anim.startRightToLeft(800,2000, 200);
		//anim.startRightToLeft(5000,5000, 200);
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
	interface ContentSlider2Style extends CssResource{
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
