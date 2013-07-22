package bing.support.whoisspy.view;

/**
 * View holder used for player and card
 * @param name
 * @param frontendImage
 * @param backgroundImage
 */
public class ViewHolder {
	private Integer frontendImage = null;
	private Integer backgroundImage = null;
	private String name = null;
	
	public ViewHolder(String name, Integer frontendImage, Integer backgroundImage){
		this.name = name;
		this.frontendImage = frontendImage;
		this.backgroundImage = backgroundImage;
	}
	
	public void setBackgroundImage(Integer img){
		this.backgroundImage = img;
	}
	public Integer getBackgroundImage(){
		return backgroundImage;
	}
	
	public void setFrontendImage(Integer img){
		this.frontendImage = img;
	}
	public Integer getFrontendImage(){
		return frontendImage;
	}
	
	public void setName(String nm){
		this.name = nm;
	}
	public String getName(){
		return name;
	}
}
