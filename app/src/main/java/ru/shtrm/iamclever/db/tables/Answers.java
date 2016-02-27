package ru.shtrm.iamclever.db.tables;

public class Answers extends BaseTable {
    private int question;
    private int profile;
    private int level;
    private int correct;
    private int incorrect;

	public Answers() {
	}

	public int getQuestion() {
		return question;
	}
	public void setQuestion(int question) {
		this.question = question;
	}
    public int getProfile() {
        return profile;
    }
    public void setProfile(int profile) {
        this.profile = profile;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getCorrect() {
        return correct;
    }
    public void setCorrect(int correct) {
        this.correct = correct;
    }
    public int getIncorrect() {
        return incorrect;
    }
    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }
}
