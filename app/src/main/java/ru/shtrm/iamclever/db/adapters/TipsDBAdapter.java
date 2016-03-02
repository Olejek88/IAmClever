package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import ru.shtrm.iamclever.db.tables.Tips;

public class TipsDBAdapter extends BaseDBAdapter {
	public static final String TABLE_NAME = "UserStats";

	public static final String FIELD_ID_NAME = "id";
	public static final String FIELD_LANG_NAME = "lang";
    public static final String FIELD_NAME_NAME = "name";
	public static final String FIELD_IMAGE_NAME = "image";

	String[] mColumns = {
			FIELD_ID_NAME,
			FIELD_LANG_NAME,
            FIELD_NAME_NAME,
			FIELD_IMAGE_NAME};

    public TipsDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

    public Tips getRandomTipsByLang(int lang) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LANG_NAME + "=?", new String[]{""+lang}, null, null, "RANDOM() LIMIT 1");
        if (cursor.getCount()>0)
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        return null;
    }

    public Tips getItem(Cursor cursor) {
        Tips item = new Tips();
		item.setLang(cursor.getInt(cursor.getColumnIndex(FIELD_LANG_NAME)));
        item.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME_NAME)));
		item.setImage(cursor.getString(cursor.getColumnIndex(FIELD_IMAGE_NAME)));
		return item;
	}

    /**
	 * <p>Добавляет/изменяет запись в таблице users</p>
	 * @return long id столбца или -1 если не удалось добавить запись
	 */

	public long replaceItem(int lang, String name, String image) {
		long id;
		ContentValues values = new ContentValues();
        values.put(FIELD_NAME_NAME, name);
		values.put(FIELD_LANG_NAME, lang);
		values.put(FIELD_IMAGE_NAME, image);
        id  = mDb.replace(TABLE_NAME, null, values);
		return id;
	}

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param tip - новый слайд
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Tips tip) {
		return replaceItem(tip.getLang(),tip.getName(),tip.getImage());
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param tip - статистика пользователя
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Tips tip) {
        return replaceItem(tip.getLang(),tip.getName(),tip.getImage());
    }

}
