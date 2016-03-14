package com.gmail.pankajche1.contentslider.client.slider4;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;

public class ItemsContainer extends HTMLPanel implements HasWidgets{
	private List<HTMLPanel> list;
	private List<Integer> ids;
	private int iCurItem=0;
	public ItemsContainer(String html) {
		super(html);
		list = new ArrayList<HTMLPanel>();
		ids = new ArrayList<Integer>();
		
	}
	public void addItem(HTMLPanel item,int id){
		list.add(item);
		ids.add(id);
		this.add(item);
		//this.in
		//this.add
		
	}
	public void insertAtFirst(HTMLPanel item,int id){
		list.add(0,item);
		ids.add(0,id);
		this.getElement().insertFirst(item.getElement());
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
		//first remove all the elements:
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
	public void removeFirstItem(){
		list.get(0).removeFromParent();
		list.remove(0);
		ids.remove(0);
	}
	public void removeLastItem(){
		list.get(list.size()-1).removeFromParent();
		list.remove(list.size()-1);
		ids.remove(ids.size()-1);
	}
	public void removeItem(int index){
		list.get(index).removeFromParent();
		list.remove(index);		
		ids.remove(index);
	}
	public int getNumItems(){
		return list.size();
	}
	public void removeAllItems(){
		list = new ArrayList<HTMLPanel>();
		ids=new ArrayList<Integer>();
		this.clear();
		
	}
	public List<Integer> getIdList(){
		return ids;
	}
	public HTMLPanel removeItemById(int id){
		HTMLPanel item=null;
		int iTarget=-1;
		int i;
		for(i=0;i<ids.size();i++){
			if(id==ids.get(i)){
				iTarget=i;
				break;
			}
		}
		if(iTarget>=0){
			item=list.get(iTarget);
			item.removeFromParent();
			list.remove(iTarget);
			ids.remove(iTarget);
					
		}
		return item;
	}

}
