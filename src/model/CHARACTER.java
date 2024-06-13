package model;

public enum CHARACTER {
	
	
	RED("character.gif", "heart.gif");
	
	private String urlCharacter;
	private String urlLife;
	
	private CHARACTER(String urlCharacter, String urlLife) {
		this.urlCharacter = urlCharacter;
		this.urlLife = urlLife;
		
	}
	
	public String getUrl() {
		return this.urlCharacter;
	}
	
	public String getUrlLife() {
		return this.urlLife;
	}
}
