package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.shtrm.iamclever.db.tables.Ratings;

public class RatingsDBAdapter extends BaseDBAdapter {
	public static final String TABLE_NAME = "Ratings";
	public static final String FIELD_ID_NAME = "id";
	public static final String FIELD_LANG_NAME = "lang";
    public static final String FIELD_USER_NAME = "user";
	public static final String FIELD_RATING_NAME = "rating";
	public static final String FIELD_PLACE_NAME = "place";

	String[] mColumns = {
			FIELD_ID_NAME,
			FIELD_LANG_NAME,
            FIELD_USER_NAME,
			FIELD_RATING_NAME,
			FIELD_PLACE_NAME};

    public RatingsDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

    public Ratings getItem(Cursor cursor) {
        Ratings item = new Ratings();
        item.set_id(cursor.getInt(cursor.getColumnIndex(FIELD_ID_NAME)));
		item.setLang(cursor.getInt(cursor.getColumnIndex(FIELD_LANG_NAME)));
        item.setUser(cursor.getString(cursor.getColumnIndex(FIELD_USER_NAME)));
		item.setPlace(cursor.getInt(cursor.getColumnIndex(FIELD_PLACE_NAME)));
		item.setRating(cursor.getFloat(cursor.getColumnIndex(FIELD_RATING_NAME)));
		return item;
	}

    /**
     * <p>
     * Возвращает все записи из таблицы
     * </p>
     *
     * @return list
     */
    public ArrayList<Ratings> getAllItems() {
        ArrayList<Ratings> arrayList = new ArrayList<>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Ratings rating = getItem(cursor);
                arrayList.add(rating);
                if (cursor.isLast())
                    break;
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<Ratings> getAllItemsByLang(int lang) {
        ArrayList<Ratings> arrayList = new ArrayList<>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LANG_NAME + "=?", new String[]{""+lang}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Ratings rating = getItem(cursor);
                arrayList.add(rating);
                if (cursor.isLast())
                    break;
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    /**
	 * <p>Добавляет/изменяет запись в таблице users</p>
	 * @return long id столбца или -1 если не удалось добавить запись
	 */

	public long replaceItem(int lang, String name, int place, float rating, boolean update) {
		long id;
		ContentValues values = new ContentValues();
		values.put(FIELD_LANG_NAME, lang);
        values.put(FIELD_USER_NAME, name);
		values.put(FIELD_RATING_NAME, rating);
		values.put(FIELD_PLACE_NAME, place);
        if (update)
            id = mDb.update(TABLE_NAME, values, FIELD_USER_NAME + "=?", new String[] { String.valueOf(name)});
        else
            id  = mDb.replace(TABLE_NAME, null, values);
		return id;
	}

    public long deleteAllItem() {
        long id;
        id  = mDb.delete(TABLE_NAME, null, null);
        return id;
    }

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param stat - статистика пользователей
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Ratings stat) {
		return replaceItem(stat.getLang(),stat.getUser(),stat.getPlace(),stat.getRating(),false);
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param stat - статистика пользователей
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Ratings stat) {
        return replaceItem(stat.getLang(),stat.getUser(),stat.getPlace(),stat.getRating(),true);
    }

}
