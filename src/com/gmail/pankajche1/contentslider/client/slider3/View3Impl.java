package com.gmail.pankajche1.contentslider.client.slider3;

import java.util.ArrayList;
import java.util.List;


import com.gmail.pankajche1.contentslider.client.ItemsContainer;
import com.gmail.pankajche1.contentslider.client.resources.ClientBundle1;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class View3Impl extends Composite implements View3,MouseOverHandler,MouseOutHandler{

	private static View3ImplUiBinder uiBinder = GWT
			.create(View3ImplUiBinder.class);

	interface View3ImplUiBinder extends UiBinder<Widget, View3Impl> {
	}
	interface View3Style extends CssResource{
		String rootPanel();
		String content();
		String container();
		String item();
		String itemSized();
		//toggle button style:
		String toggleButton();
		String controlPanel();
		String scrollButton();
		String cachePanel();
		String holder();
	}
	private Model3 model;
	private SliderController3 controller;
	private Animator animator;
	private RestSimulator restSimulator;
	@UiField
	HTMLPanel content;
	@UiField
	HTMLPanel cachePanel;
	@UiField
	View3Style style;
	@UiField
	Button btnLeftFromRight,btnLeftToRight,btnPause;
	@UiField
	FocusPanel basePanel;
	@UiField
	HTMLPanel leftScroll,rightScroll;
	private PushButton btnLeft,btnRight;
	//containers on the cache panel:
	private List<HTMLPanel> itemHolders = new ArrayList<HTMLPanel>();
	//list of items in the cache:
	private List<HTMLPanel> itemsInCache=new ArrayList<HTMLPanel>();
	//list of items on the stage:
	private List<HTMLPanel> itemsOnStage=new ArrayList<HTMLPanel>();
	private ClientBundle1 bundle;
	@UiHandler("btnLeftFromRight")
	void onLeftFromRightScroll(ClickEvent event){
		controller.startLeftScroll();
	}
	@UiHandler("btnLeftToRight")
	void onLeftToRightScroll(ClickEvent event){
		controller.startRightScroll();
	}
	@UiHandler("btnPause")
	void onPause(ClickEvent event){
		controller.onPauseButtonClick();
	}
	private ItemsContainer cont;
	public View3Impl() {
		initWidget(uiBinder.createAndBindUi(this));
		bundle=ClientBundle1.INSTANCE;
		animator = new Animator();
		restSimulator=new RestSimulator();
		cont = new ItemsContainer("");
		cont.setStyleName(style.container());
		content.add(cont);
		basePanel.addMouseOutHandler(this);
		basePanel.addMouseOverHandler(this);
		Image imgBtnLeft=new Image(bundle.control_left());
		Image imgBtnRight=new Image(bundle.control_right());
		btnLeft=new PushButton(imgBtnLeft);
		btnLeft.getUpHoveringFace().setImage(new Image(bundle.leftButtonBlue()));
		btnLeft.setStyleName(style.scrollButton());
		btnRight=new PushButton(imgBtnRight);
		btnRight.getUpHoveringFace().setImage(new Image(bundle.rightButtonBlue()));
		btnRight.setStyleName(style.scrollButton());

		leftScroll.add(btnLeft);
		rightScroll.add(btnRight);		


	}

	@Override
	public void setModel(Model3 model) {
		this.model=model;
		
	}

	@Override
	public void addItem(String imgUrl, String link,double wItem,double hItem,double marginItemLeft,
			double marginItemRight) {
		//creating an item
		HTMLPanel item;
		item=new HTMLPanel("<a href='"+link+"'><img src='"+imgUrl+"' width='"+wItem+"px' height='"+hItem+"px'></img></a>");
		item.getElement().getStyle().setWidth(wItem, Style.Unit.PX);
		item.getElement().getStyle().setHeight(hItem, Style.Unit.PX);
		item.getElement().getStyle().setMarginLeft(marginItemLeft, Style.Unit.PX);
		item.getElement().getStyle().setMarginRight(marginItemRight, Style.Unit.PX);

		item.setStyleName(style.itemSized());
		//adding this item to the list:
		itemsInCache.add(item);
		//this item will be put on the cache:
		HTMLPanel holder = new HTMLPanel("");
		holder.setStyleName(style.holder());
		itemHolders.add(holder);//adding to the list for reference
		cachePanel.add(holder);//putting on DOM
		holder.add(item);
		//cachePanel.
		//cont.getElement().getStyle().setWidth(width, Style.Unit.PX);
		//cont.addItem(item);
		
		
	}
	@Override
	public void addItemToStage(int index,double width) {
		//get the item from the cache holder:
		//itemHolders.get(index)
		cont.getElement().getStyle().setWidth(width, Style.Unit.PX);
		cont.addItem(itemsInCache.get(index));
		itemsInCache.get(index).setStyleName(style.item());
		
	}
	@Override
	public void removeItemFromStage(int index) {
		// get the reference of the item from the cache:
		itemsInCache.get(index).removeFromParent();
		//put it back to the cache holder:
		itemHolders.get(index).add(itemsInCache.get(index));
		
	}
	@Override
	public void putItemAtLast(int index) {
		// putting an item to the last of the content on display:
		cont.addItem(itemsInCache.get(index));
		itemsInCache.get(index).setStyleName(style.item());
	}
	@Override
	public void putItemAtFirst(int index) {
		//setting the first item in the content:
		cont.getElement().insertFirst(itemsInCache.get(index).getElement());
		itemsInCache.get(index).setStyleName(style.item());
		
	}
	@Override
	public void setDisplayWidth(int width) {
		int wSidePanel=42;//42 px side panels on each side:
		//this is the mask:
		content.getElement().getStyle().setWidth(width, Style.Unit.PX);
		
		//this is the focus panel which detects the mouse movements:
		basePanel.getElement().getStyle().setWidth(width-2*wSidePanel, Style.Unit.PX);
		//basePanel.getElement().getStyle().setLeft(wSidePanel, Style.Unit.PX);
		//basePanel.getElement().getStyle().setTop(0, Style.Unit.PX);
		//side bars:
		
		leftScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		//leftScroll.getElement().getStyle().setLeft(0, Style.Unit.PX);
		rightScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		//rightScroll.getElement().getStyle().setLeft(width-wSidePanel, Style.Unit.PX);

	}

	@Override
	public void setDisplayHeight(int height) {
		//this is the mask:
		content.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//the focus panel which detects the mosue movement:
		basePanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
		leftScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);
		rightScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//buttons:
		//buttons on the above scroll bars:
		btnLeft.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnLeft.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);
		btnRight.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnRight.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);

	}

	@Override
	public void setPosition(double x, double y) {
		cont.getElement().getStyle().setLeft(x, Style.Unit.PX);
		//cont.getElement().getStyle().setTop(y, Style.Unit.PX);
		
	}

	@Override
	public void setController(SliderController3 controller) {
		this.controller = controller;
		
	}

	@Override
	public void startRun(int duration) {
		
		//restSimulator.cancel();
		animator.run(duration);
		
	}
	@Override
	public void rest(int duration){
		//animator.cancel();
		restSimulator.run(duration);
		
		
	}
	private class Animator extends Animation{

		@Override
		protected void onUpdate(double progress) {
			//calling the controller
			controller.onEnterFrame(progress);
			
		}
		@Override
		protected void onComplete(){
			controller.onComplete();
		}
		
	}
	private class RestSimulator extends Animation{

		@Override
		protected void onUpdate(double progress) {
			//do nothing
			
		}
		@Override
		protected void onComplete(){
			controller.onRestComplete();
		}		
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		controller.onMouseOut();
	}
	@Override
	public void onMouseOver(MouseOverEvent event) {
		//System.out.println("mouse in");
		controller.onMouseOver();
		
	}




}
