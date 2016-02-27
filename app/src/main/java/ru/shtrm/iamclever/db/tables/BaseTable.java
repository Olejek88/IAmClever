package ru.shtrm.iamclever.db.tables;

public class BaseTable {
	
	protected long _id;

	public BaseTable() {
	}
	
	/**
	 * @return the _id
	 */
	public long get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(long _id) {
		this._id = _id;
	}

}
