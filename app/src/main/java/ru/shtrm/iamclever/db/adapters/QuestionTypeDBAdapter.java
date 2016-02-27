package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.shtrm.iamclever.db.tables.Languages;

public class QuestionTypeDBAdapter extends BaseDBAdapter {

	public static final String TABLE_NAME = "Questions_type";
	public static final String FIELD_NAME = "name";

	public static final class Projection {
		public static final String ID = FIELD_ID;
		public static final String TITLE = TABLE_NAME + '_' + FIELD_NAME;
	}

	private static final Map<String, String> mProjection = new HashMap<String, String>();
	static {
		mProjection.put(Projection.ID, getFullName(TABLE_NAME, FIELD_ID)
				+ " AS " + Projection.ID);
		mProjection.put(Projection.TITLE, getFullName(TABLE_NAME, FIELD_NAME)
				+ " AS " + Projection.TITLE);
	}

	public QuestionTypeDBAdapter(Context context) {
		super(context, TABLE_NAME);
	}

	public Cursor getItem(String id) {
		Cursor cursor;
		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ID + "=?",
				new String[] { id }, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		return null;
	}

    public String getNameByID(String id) {
		Cursor cur;
		cur = mDb.query(TABLE_NAME, mColumns, FIELD_ID + "=?",
				new String[] { id }, null, null, null);
		if (cur.getCount() > 0) {
			cur.moveToFirst();
			return cur.getString(1);
		} else
			return "неизвестен";
	}

	public Cursor getAllItems_cursor() {
		Cursor cursor;
		cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor;
		}
		return null;
	}

	/**
	 * <p>
	 * Возвращает все записи из таблицы
	 * </p>
	 * 
	 * @return list
	 */
	public ArrayList<Languages> getAllItems() {
		ArrayList<Languages> arrayList = new ArrayList<Languages>();
		Cursor cursor;
		cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (true) {
				Languages lang = getItem(cursor);
				arrayList.add(lang);
				if (cursor.isLast())
					break;
				cursor.moveToNext();
			}
		}
		return arrayList;
	}
    /**
     *
     * @return list
     */
    public ArrayList<String> getItems() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(getItem(cursor).getName());
            } while (cursor.moveToNext());
        }
        return list;
    }

	public Languages getItem(Cursor cursor) {
		Languages item = new Languages();

		getItem(cursor, item);
		item.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
		return item;
	}

	/**
	 * <p>
	 * Добавляет/изменяет запись
	 * </p>
	 * 
	 * @param item
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replace(Languages item) {
		long id;
		ContentValues values = putCommonFields(item);

		values.put(FIELD_NAME, item.getName());
		id = mDb.replace(TABLE_NAME, null, values);
		return id;
	}

	/**
	 * @return the mProjection
	 */
	public static Map<String, String> getProjection() {
		Map<String, String> projection = new HashMap<String, String>();
		projection.putAll(mProjection);
		projection.remove(Projection.ID);
		return projection;
	}

}
