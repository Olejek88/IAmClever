package ru.shtrm.iamclever.db.tables;

public class Profiles extends BaseTable {

    private int id;
	private String name;
    private String image;
	private int lang1;
    private int lang2;
    private int lang3;
    private String login;
    private String pass;
    private int lastdate;
    private int active;

	public Profiles() {
	}

    public int  getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

	public int getLang1() {
		return lang1;
	}
	public void setLang1(int lang) {
		this.lang1 = lang;
	}
    public int getLang2() {
        return lang2;
    }
    public void setLang2(int lang) {
        this.lang2 = lang;
    }
    public int getLang3() {
        return lang3;
    }
    public void setLang3(int lang) {
        this.lang3 = lang;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getLastDate() {
        return lastdate;
    }
    public void setLastDate(int lastdate) {
        this.lastdate = lastdate;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
}
