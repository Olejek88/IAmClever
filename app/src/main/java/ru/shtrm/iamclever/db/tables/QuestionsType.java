package ru.shtrm.iamclever.db.tables;

public class QuestionsType extends BaseTable {
	private String name;

	public QuestionsType() {
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
