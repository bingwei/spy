package bing.support.whoisspy.view;

public class PlayerCardData {
	private Integer image = null;
	private String name = null;
	
	
	PlayerCardData(Integer image, String name){
		this.image = image;
		this.name = name;
	}
	
	public void setImage(Integer img){
		this.image = img;
	}
	public void setName(String nm){
		this.name = nm;
	}
	public Integer getImage(){
		return image;
	}
	public String getName(){
		return name;
	}
}
