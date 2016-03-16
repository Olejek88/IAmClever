package ru.shtrm.iamclever.db.tables;

public class Stats extends BaseTable {

	private int profile;
    private int lang;
    private int days;
    private int exams;
    private int exams_complete;
    private int questions;
    private int questions_right;

	public Stats() {
	}
	public int getProfile() {
		return profile;
	}
	public void setProfile(int profile) {
		this.profile = profile;
	}
    public int getLang() {
        return lang;
    }
    public void setLang(int lang) {
        this.lang = lang;
    }
    public int getDays() {
        return days;
    }
    public void setDays(int lang) {
        this.days = days;
    }

    public int getExams() {
        return exams;
    }
    public void setExams(int exams) {
        this.exams = exams;
    }
    public int getExams_complete() {
        return exams_complete;
    }
    public void setExams_complete(int exams_complete) {
        this.exams_complete = exams_complete;
    }

    public int getQuestions() {
        return questions;
    }
    public void setQuestions(int questions) {
        this.questions = questions;
    }
    public int getQuestions_right() {
        return questions_right;
    }
    public void setQuestions_right(int questions_right) {
        this.questions_right = questions_right;
    }
}
