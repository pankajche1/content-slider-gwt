package com.gmail.pankajche1.contentslider.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


public interface ClientBundle1 extends ClientBundle {
	public static final ClientBundle1 INSTANCE =  GWT.create(ClientBundle1.class);
	ImageResource control_pause();
	ImageResource control_pause_blue();
	ImageResource control_play();
	ImageResource control_play_blue();
	ImageResource control_fastforward_blue();
	ImageResource control_rewind_blue();
	ImageResource control_left();
	ImageResource control_right();
	ImageResource leftButtonBlue();
	ImageResource rightButtonBlue();
	ImageResource arrow_left_80_000();
	ImageResource arrow_right_80_000();
	@Source("ajax-loader-green-arrows.gif")
	ImageResource ajaxLoaderGreenArrows();
}
