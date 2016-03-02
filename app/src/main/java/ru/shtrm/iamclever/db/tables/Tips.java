package ru.shtrm.iamclever.db.tables;

public class Tips extends BaseTable {

    private int lang;
    private String name;
    private String image;

	public Tips() {
	}
    public int getLang() {
        return lang;
    }
    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
