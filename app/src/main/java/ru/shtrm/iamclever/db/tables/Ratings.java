package ru.shtrm.iamclever.db.tables;

public class Ratings extends BaseTable {

    private int id;
	private String user;
   	private int place;
    private float rating;
    private int lang;

    public Ratings() {
	}

    public int  getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public int getLang() {
		return lang;
	}
	public void setLang(int lang) {
		this.lang = lang;
	}

    public int getPlace() {
        return place;
    }
    public void setPlace(int place) {
        this.place = place;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
}
