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

    private int check_mon;
    private int check_tue;
    private int check_wen;
    private int check_tur;
    private int check_fri;
    private int check_sat;
    private int check_sun;

    private int hour_start;
    private int hour_end;
    private int period;

    private int user_active;

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
    public int getUserActive() {
        return user_active;
    }
    public void setUserActive(int active) {
        this.user_active = active;
    }

    public int getCheck_weekday(int weekday) {
        switch (weekday) {
            case 1: return check_mon;
            case 2: return check_tue;
            case 3: return check_wen;
            case 4: return check_tur;
            case 5: return check_fri;
            case 6: return check_sat;
            case 7: return check_sun;
        }
        return 0;
    }
    public void setCheck_weekday(int weekday, int status) {
        switch (weekday) {
            case 1: this.check_mon = status;
            case 2: this.check_tue = status;
            case 3: this.check_wen = status;
            case 4: this.check_tur = status;
            case 5: this.check_fri = status;
            case 6: this.check_sat = status;
            case 7: this.check_sun = status;
        }
    }

    public int getStart() {
        return hour_start;
    }
    public void setStart(int start) {
        this.hour_start = start;
    }
    public int getEnd() {
        return hour_end;
    }
    public void setEnd(int end) {
        this.hour_end = end;
    }

    public int getPeriod() {
        return period;
    }
    public void setPeriod(int period) {
        this.period = period;
    }
}
