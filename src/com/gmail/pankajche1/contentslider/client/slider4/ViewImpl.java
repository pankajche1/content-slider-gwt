package com.gmail.pankajche1.contentslider.client.slider4;


import java.util.ArrayList;
import java.util.List;




import com.gmail.pankajche1.contentslider.client.resources.ClientBundle1;
import com.gmail.pankajche1.contentslider.client.slider4.ContentSlider4.IAjaxLoader;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class ViewImpl extends Composite implements IView4,  MouseOverHandler,MouseOutHandler{

	private static ViewImplUiBinder uiBinder = GWT
			.create(ViewImplUiBinder.class);

	interface ViewImplUiBinder extends UiBinder<Widget, ViewImpl> {
	}
	interface MyStyle extends CssResource{
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
	private Model model;
	private Controller4 controller;
	@Override
	public void setController(Controller4 controller) {
		this.controller = controller;
	}
	private HandlerManager eventManager;
	//private IAjaxLoader loader;
	@UiField
	HTMLPanel content;
	@UiField
	HTMLPanel cachePanel;
	@UiField
	MyStyle style;
	private ClientBundle1 bundle;
	@UiField
	Button btnLoadMore;
	//@UiField
	//Button btnLeftFromRight,btnLeftToRight,btnStart;
	//@UiField
	//Button btnStart;
	@UiField
	FocusPanel basePanel;
	@UiField
	InlineLabel lblItemCount;
	@UiField
	HTMLPanel ajaxLoadPanel;
	@UiField
	HTMLPanel leftScroll,rightScroll,controlPanel;
	private PushButton btnLeft,btnRight;
	private ToggleButton btnPausePlay;
	private ItemsContainer cont;
	//containers on the cache panel:
	private List<HTMLPanel> itemHolders = new ArrayList<HTMLPanel>();
	//list of items in the cache:
	private List<HTMLPanel> items=new ArrayList<HTMLPanel>();
	//list of items on the stage:
	//private List<HTMLPanel> itemsOnStage=new ArrayList<HTMLPanel>();
	private Animator animator;
	private RestSimulator restSimulator;
	private Image imgAjaxLoader;
	public ViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		bundle=ClientBundle1.INSTANCE;
		animator = new Animator();
		restSimulator=new RestSimulator();
		cont = new ItemsContainer("");
		cont.setStyleName(style.container());
		content.add(cont);
		//ajax loader:
		imgAjaxLoader=new Image(bundle.ajaxLoaderGreenArrows());
		
	}
	private void setButtons(double height){
		
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
				controller.startLeftScroll();
				
			}});
		btnRight.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				controller.startRightScroll();
				
			}});
		//pause/play toggle button:
		btnPausePlay = new ToggleButton(new Image(bundle.control_play_blue()),
				new Image(bundle.control_pause_blue())
				);
		btnPausePlay.setStyleName(style.toggleButton());
		controlPanel.add(btnPausePlay);
		btnPausePlay.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				controller.togglePauseResume(btnPausePlay.isDown());
				
				
		}});
		basePanel.addMouseOutHandler(this);
		basePanel.addMouseOverHandler(this);
		
	}

	/*
	@UiHandler("btnLeftFromRight")
	void onLeftFromRightScroll(ClickEvent event){
		controller.startLeftScroll();
	}
	@UiHandler("btnLeftToRight")
	void onLeftToRightScroll(ClickEvent event){
		controller.startRightScroll();
	}
	*/
	/*
	@UiHandler("btnStart")
	void onPause(ClickEvent event){
		//controller.onPauseButtonClick();
		controller.onStartButtonClick();
		//controller.onResumeButtonClick();
	}
	*/
	@UiHandler("btnLoadMore")
	void onLoadMoreClick(ClickEvent event){
		
		controller.load();
	}
	@Override
	public void setModel(final Model model) {
		this.model=model;
		setDisplayWidth(model.getWidth());
		setDisplayHeight(model.getHeight());
		setButtons(model.getHeight());
		eventManager=model.getEventHandler();
		//handler for update position event:
		eventManager.addHandler(SliderEvent.TYPE,
		    		new SliderEventHandler(){

						@Override
						public void onSliderEvent(SliderEvent event) {
							if(event.name.equals("updatePosition")){
								cont.getElement().getStyle().setLeft(model.getxContent(), Style.Unit.PX);
							}else if(event.name.equals("scrollComplete")){
								//removing the required item from the display:
								//items.get(model.iRemovedFromDisplay).removeFromParent();
								//CAUSE of ERROR: the list size causes error of array out of bound size.
								if(cont.getNumItems()!=model.getnDisplay()){
									//model.resetDisplay();
									
									//set items again on display:
									List<Integer> listIndex=model.getIndexDisplay();
									//create a new display list:
									cont.removeAllItems();
									//now put the items again:
									for(int index:listIndex){
										addItemToStage(index);
									}
									
								}
								cont.removeItem(model.iRemovedFromDisplay);
								//if anticlock wise: remove the first item and shift the content to the right one scroll span:
								
								
								//else remove the last item
								// get the reference of the item from the cache:
								//items.get(model.getiFirstPrev()).removeFromParent();
								//put it back to the cache holder:
								itemHolders.get(model.iTargetItem).add(items.get(model.iTargetItem));
								// putting an item to the last of the content on display:
								//cont.addItem(items.get(model.getiLast()));
								//items.get(model.getiLast()).setStyleName(style.item());
								//placing the content on the origin
								cont.getElement().getStyle().setLeft(model.getxContent(), Style.Unit.PX);
								restSimulator.run(model.gettRest());
							}else if(event.name.equals("scrollStarted")){
								//this event is fired when the start button is pressed first time.
								startRightToLeft();
							
							}else if(event.name.equals("add")){
								addItem(event.intData);
								updateItemsInfo();
							}else if(event.name.equals("display")){
								addItemToStage(event.intData); 
							}else if(event.name.equals("prime")){
								//this event is fired from the model.onRestComplete()
								//there can be situations:
								//(1) we have to start the next scroll
								//(2) we don't have to start the next scroll
								onRestComplete(event.isNextStart);
							}else if(event.name.equals("startLeftToRight")){
								startLeftToRight();
							}else if(event.name.equals("startLeftFromRight")){
								startRightToLeft();
							}else if(event.name.equals("newDataAdded")){
								addNewItems(event.intData,false);
								updateItemsInfo();
							}else if(event.name.equals("newDataAddedPause")){
								addNewItems(event.intData,true);
								updateItemsInfo();
							}else if(event.name.equals("startEnterFrame")){
								animator.run(model.gettScroll());
							}
							
							
						}

					
							
		});
		//handler for on scroll complete event:
		//handler for on rest complete event:
	}
	private void updateItemsInfo(){
		lblItemCount.setText(model.getItemsInfo());
		//do not display the load more button if you don't
		if(model.getnItems()>=model.getnTotalItems()) btnLoadMore.setVisible(false);
		else btnLoadMore.setVisible(true);
	}
	private void addNewItems(int iStart,boolean isPaused){
		//adding the new items to the cache panel:
		for(int i=iStart;i<model.getnItems();i++){
		
			addItem(i);
		}
		//now remove all the items from the display and put them back to the cache panel:
		//get the items ids on the display at this time:
		List<Integer> idsOnDisplay = cont.getIdList();
		//to remove concurrent error get the ids in an array:
		
		if(idsOnDisplay!=null){
			int[] array = new int[idsOnDisplay.size()];
			for(int i=0;i<array.length;i++){
				array[i]=idsOnDisplay.get(i);
			}
			for(int id:array){
				
				HTMLPanel item = cont.removeItemById(id);
				if(item!=null){
					//put the above at the right index in the cache panel:
					itemHolders.get(id).add(item);
				}
			}
		}

		//now create the updated display list:
		List<Integer> listDisplay = model.getIndexDisplay();
		for(int index:listDisplay){
			addItemToStage(index);
		}
		//now run the enterFrame machine:
		if(!isPaused) animator.run(model.gettScroll());
	}
	/**
	 * first time left to right start
	 */
	private void startLeftToRight(){
		//cont.getElement().getStyle().setWidth(model.getwContent(), Style.Unit.PX);
		
		cont.removeItem(model.iRemovedFromDisplay);
		cont.insertAtFirst(items.get(model.iItemToDisplay),model.iItemToDisplay);
		
		items.get(model.iItemToDisplay).setStyleName(style.item());
		items.get(model.iItemToDisplay).getElement().getStyle().setMarginLeft(model.getMarginItemLeft(), Style.Unit.PX);
		items.get(model.iItemToDisplay).getElement().getStyle().setMarginRight(model.getMarginItemRight(), Style.Unit.PX);

		cont.getElement().getStyle().setLeft(model.getxContent(), Style.Unit.PX);
		btnPausePlay.setDown(true);
		animator.run(model.gettScroll());
	}
	private void startRightToLeft(){
		//cont.addItem(items.get(model.iItemToDisplay));
		
		//items.get(model.iItemToDisplay).setStyleName(style.item());
		cont.getElement().getStyle().setLeft(model.getxContent(), Style.Unit.PX);
		btnPausePlay.setDown(true);
		animator.run(model.gettScroll());
	}
	private void onRestComplete(boolean isNextStart){

		// putting an item for display:
		//see where the new item is to be put:
		if(model.iInsertedToDisplay==0){
			cont.insertAtFirst(items.get(model.iItemToDisplay),model.iItemToDisplay);
		}else{
			cont.addItem(items.get(model.iItemToDisplay),model.iItemToDisplay);
		}
		items.get(model.iItemToDisplay).setStyleName(style.item());
		items.get(model.iItemToDisplay).getElement().getStyle().setMarginLeft(model.getMarginItemLeft(), Style.Unit.PX);
		items.get(model.iItemToDisplay).getElement().getStyle().setMarginRight(model.getMarginItemRight(), Style.Unit.PX);

		//placing the content on the origin
		cont.getElement().getStyle().setLeft(model.getxContent(), Style.Unit.PX);
		//now run the enterFrame machine:
		if(isNextStart) animator.run(model.gettScroll());
	}
	private void addItem(int index){
		//get the item from the model:
		//model.getImageUrl(index);
		HTMLPanel item;
		//item=new HTMLPanel("<a href='"+link+"'><img src='"+imgUrl+"' width='"+wItem+"px' height='"+hItem+"px'></img></a>");
		item=new HTMLPanel("<a href='"+model.getLinkUrl(index)+"'><img src='"+model.getImageUrl(index)+"' width='"+model.getwItem()+"px'></img></a>");
		items.add(item);
		//this item will be put on the cache:
		HTMLPanel holder = new HTMLPanel("");
		holder.setStyleName(style.holder());
		itemHolders.add(holder);//adding to the list for reference
		cachePanel.add(holder);//putting on DOM
		holder.add(item);
	/*
		cont.getElement().getStyle().setWidth(model.getwContent(), Style.Unit.PX);
		cont.addItem(items.get(index));
		items.get(index).setStyleName(style.item());
		*/

	}
	public void addItemToStage(int index) {
		
		//get the item from the cache holder:
		//itemHolders.get(index)
		cont.getElement().getStyle().setWidth(model.getwContent(), Style.Unit.PX);
		cont.addItem(items.get(index),index);
		items.get(index).setStyleName(style.item());
		items.get(index).getElement().getStyle().setMarginLeft(model.getMarginItemLeft(), Style.Unit.PX);
		items.get(index).getElement().getStyle().setMarginRight(model.getMarginItemRight(), Style.Unit.PX);
		
	}
	public void setDisplayWidth(double width) {
		int wSidePanel=model.getwSidePanel();//42 px side panels on each side:
		//this is the mask:
		content.getElement().getStyle().setWidth(width-2*wSidePanel, Style.Unit.PX);
		
		//this is the focus panel which detects the mouse movements:
		basePanel.getElement().getStyle().setWidth(width-2*wSidePanel, Style.Unit.PX);
		basePanel.getElement().getStyle().setLeft(wSidePanel, Style.Unit.PX);
		//basePanel.getElement().getStyle().setTop(0, Style.Unit.PX);
		//side bars:
		
		leftScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		//leftScroll.getElement().getStyle().setLeft(0, Style.Unit.PX);
		rightScroll.getElement().getStyle().setWidth(wSidePanel, Style.Unit.PX);
		//rightScroll.getElement().getStyle().setLeft(width-wSidePanel, Style.Unit.PX);

	}
	public void setDisplayHeight(double height) {
		//this is the mask:
		content.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//the focus panel which detects the mosue movement:
		basePanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
		leftScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);
		rightScroll.getElement().getStyle().setHeight(height, Style.Unit.PX);
		//buttons:
		//buttons on the above scroll bars:
		/*
		btnLeft.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnLeft.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);
		btnRight.getElement().getStyle().setTop(height/2-12,  Style.Unit.PX);
		btnRight.getElement().getStyle().setLeft(42/2-12,  Style.Unit.PX);
		*/

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
			//controller.onComplete();
			controller.onScrollComplete();
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
		
		controller.resumeByMouseOut();
		
	}
	@Override
	public void onMouseOver(MouseOverEvent event) {
		controller.pauseByMouseOver();
		
	}
	@Override
	public void showAjaxLoading(){
		ajaxLoadPanel.add(imgAjaxLoader);
		
	}
	@Override
	public void removeAjaxLoading(){
		imgAjaxLoader.removeFromParent();
	}
	}


