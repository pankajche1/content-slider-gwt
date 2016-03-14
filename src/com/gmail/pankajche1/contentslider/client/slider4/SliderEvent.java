package com.gmail.pankajche1.contentslider.client.slider4;



import com.google.gwt.event.shared.GwtEvent;


public class SliderEvent extends GwtEvent<SliderEventHandler>{
	public static Type<SliderEventHandler> TYPE = new Type<SliderEventHandler>();
	public String name="";
	public int intData;
	public boolean isNextStart=false;
	public SliderEvent(String name){
		this.name=name;
	}
	public SliderEvent(String name,int intData){
		this.name=name;
		this.intData=intData;
	}
	@Override
	public Type<SliderEventHandler> getAssociatedType() {
		
		return TYPE;
	}

	@Override
	protected void dispatch(SliderEventHandler handler) {
		handler.onSliderEvent(this);
		
	}
}
