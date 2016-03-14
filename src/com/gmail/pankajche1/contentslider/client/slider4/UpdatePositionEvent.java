package com.gmail.pankajche1.contentslider.client.slider4;



import com.google.gwt.event.shared.GwtEvent;


public class UpdatePositionEvent extends GwtEvent<UpdatePositionEventHandler>{
	public static Type<UpdatePositionEventHandler> TYPE = new Type<UpdatePositionEventHandler>();

	@Override
	public Type<UpdatePositionEventHandler> getAssociatedType() {
		
		return TYPE;
	}

	@Override
	protected void dispatch(UpdatePositionEventHandler handler) {
		handler.onUpdatePositionEvent(this);
		
	}
}
