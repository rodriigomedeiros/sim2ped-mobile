package br.uern.ges.med.sim2ped.adapter;

public class NavigationItemAdapter {

	public String title;
	public int counter;
	public int icon;
	public boolean isHeader;
	public boolean sub = false;
	public boolean disable = false;

	public NavigationItemAdapter(String title, int icon, boolean header,int counter) {
		this.title = title;
		this.icon = icon;
		this.isHeader = header;
		this.counter = counter;
	}
	
	public NavigationItemAdapter(String title, int icon, boolean header){
		this(title, icon, header, 0 );
	}
	
	public NavigationItemAdapter(String title, int icon) {
		this(title, icon, false);
	}
	
	public void setSub(boolean subMenu){
		this.sub = subMenu;
	}
	
	public boolean isDisable(){
		return disable;
	}
	
	public void setDisable(){
		this.disable = true;
	}
	
	public void setEnable(){
		this.disable = false;
	}
}
