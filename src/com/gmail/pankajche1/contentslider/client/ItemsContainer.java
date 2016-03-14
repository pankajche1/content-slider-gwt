package com.gmail.pankajche1.contentslider.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;

public class ItemsContainer extends HTMLPanel{
	private List<HTMLPanel> list;
	private int iCurItem=0;
	public ItemsContainer(String html) {
		super(html);
		list = new ArrayList<HTMLPanel>();
		
	}
	public void addItem(HTMLPanel item){
		list.add(item);
		this.add(item);
	}
	//putting the first item at the back
	public void shiftItemToBack(){
		//remove the current item which has gone to the left
		list.get(iCurItem).removeFromParent();
		//add it to the last
		this.add(list.get(iCurItem));
		//now the first item is:
		iCurItem++;
		if(iCurItem>=list.size()) iCurItem=0;
	}
	//putting the last item at the first position:
	public void shiftLastToFirst(){
		int iTarget=iCurItem-1;
		if(iTarget<0) iTarget=list.size()-1;
		//first remove all the elments:
		for(HTMLPanel item:list){
			item.removeFromParent();
		}
		//place the target item at the first place:
		this.add(list.get(iTarget));
		//now put rest of the items:
		for(int i=0;i<list.size()-1;i++){
			iTarget++;
			if(iTarget>=list.size())iTarget=0;
			this.add(list.get(iTarget));
		}		
		//now change the curItemIndex:
		iCurItem--;
		if(iCurItem<0) iCurItem=list.size()-1;
	}
	

}
