package ru.shtrm.iamclever.db.tables;

public class Questions extends BaseTable {

    private int id;
    private int lang;
    private int type;
    private int level;
	private String levelA;
    private int question;
    private String original;
    private String answer;
    private int server_uid;
    private String answer2;

	public Questions() {
	}

    public int  getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLevelA1() {
		return levelA;
	}
	public void setLevelA1(String levelA) {
		this.levelA = levelA;
	}
    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

	public int getLang() {
		return lang;
	}
	public void setLang(int lang) {
		this.lang = lang;
	}
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getQuestion() {
        return question;
    }
    public void setQuestion(int question) {
        this.question = question;
    }
    public int getServer_uid() {
        return server_uid;
    }
    public void setServer_uid(int server_uid) {
        this.server_uid = server_uid;
    }
    public String getAnswer2() {
        return answer2;
    }
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}
