package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.shtrm.iamclever.db.tables.Profiles;

public class ProfilesDBAdapter extends BaseDBAdapter {

    public static final String TABLE_NAME = "Profiles";
	
	public static final String FIELD_ID_NAME = "id";
	public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_IMAGE_NAME = "image";
	public static final String FIELD_LOGIN_NAME = "login";
	public static final String FIELD_PASS_NAME = "pass";
	public static final String FIELD_LANG1_NAME = "lang1";
    public static final String FIELD_LANG2_NAME = "lang2";
    public static final String FIELD_LANG3_NAME = "lang3";
    public static final String FIELD_LAST_DATE_NAME = "lastdate";
    public static final String FIELD_ACTIVE_NAME = "active";

	String[] mColumns = {
			FIELD_ID_NAME,
			FIELD_NAME_NAME,
            FIELD_IMAGE_NAME,
			FIELD_LOGIN_NAME,
			FIELD_PASS_NAME,
			FIELD_LANG1_NAME,
            FIELD_LANG2_NAME,
            FIELD_LANG3_NAME,
            FIELD_LAST_DATE_NAME,
            FIELD_ACTIVE_NAME};

    public ProfilesDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

	public Profiles getUserByLoginAndPass(String login, String pass) {
		Cursor cursor;
		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LOGIN_NAME + "=? AND " + FIELD_PASS_NAME + "=?", new String[]{login, pass}, null, null, null);
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

	/**
	 * 
	 * @param tagId
	 * @return
	 */
	public Profiles getUserByTagId(String tagId) {
		Cursor cursor;

		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ID_NAME + "=?", new String[]{tagId}, null, null, null);
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

    public Profiles getActiveUser() {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ACTIVE_NAME  + "=?",
                new String[] { "1" }, null, null, null);
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        return null;
    }

    public void setActiveUser(String login) {
        Cursor cursor;
        ContentValues values = new ContentValues();
        // снимаем всем статус
        values.put(FIELD_ACTIVE_NAME, "0");
        mDb.update(TABLE_NAME, values, null, null);
        values.put(FIELD_ACTIVE_NAME, "1");
        mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
        return;
    }

    /**
	 * <p>Возвращает запись из таблицы users</p>
	 * @param login
	 * @return Users
	 */
	public Profiles getItem(String login) {
		Cursor cursor;

		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LOGIN_NAME + "=?", new String[]{login}, null, null, null);
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

	public Profiles getItem(Cursor cursor) {
        Profiles item = new Profiles();
        item.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID_NAME)));
		item.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME_NAME)));
        item.setImage(cursor.getString(cursor.getColumnIndex(FIELD_IMAGE_NAME)));
		item.setLogin(cursor.getString(cursor.getColumnIndex(FIELD_LOGIN_NAME)));
		item.setPass(cursor.getString(cursor.getColumnIndex(FIELD_PASS_NAME)));
		item.setLang1(cursor.getInt(cursor.getColumnIndex(FIELD_LANG1_NAME)));
        item.setLang2(cursor.getInt(cursor.getColumnIndex(FIELD_LANG2_NAME)));
        item.setLang3(cursor.getInt(cursor.getColumnIndex(FIELD_LANG3_NAME)));
        item.setLastDate(cursor.getInt(cursor.getColumnIndex(FIELD_LAST_DATE_NAME)));
        item.setActive(cursor.getInt(cursor.getColumnIndex(FIELD_ACTIVE_NAME)));
		return item;
	}

    /**
     * <p>
     * Возвращает все записи из таблицы
     * </p>
     *
     * @return list
     */
    public ArrayList<Profiles> getAllItems() {
        ArrayList<Profiles> arrayList = new ArrayList<Profiles>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Profiles profile = getItem(cursor);
                arrayList.add(profile);
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
	public long replaceItem(String name, String image, String login, String pass, int lang1, int lang2, int lang3, int lastdate, int active, boolean uprep) {
		long id;
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME_NAME, name);
        values.put(FIELD_IMAGE_NAME, image);
		values.put(FIELD_LOGIN_NAME, login);
		values.put(FIELD_PASS_NAME, pass);
		values.put(FIELD_LANG1_NAME, lang1);
        values.put(FIELD_LANG2_NAME, lang2);
        values.put(FIELD_LANG3_NAME, lang3);
        values.put(FIELD_LAST_DATE_NAME, lastdate);
        values.put(FIELD_ACTIVE_NAME, active);
		if (uprep)
            id  = mDb.replace(TABLE_NAME, null, values);
        else
            id  = mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
		return id;
	}

    /**
     * <p>Добавляет/изменяет запись в таблице users</p>
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long replaceItem(String name, String image, String login, String pass) {
        long id;
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME_NAME, name);
        values.put(FIELD_IMAGE_NAME, image);
        values.put(FIELD_LOGIN_NAME, login);
        values.put(FIELD_PASS_NAME, pass);
        id  = mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
        return id;
    }

    /**
     * <p>Добавляет/изменяет запись в таблице users</p>
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long replaceItem(int lang1, int lang2, int lang3, int active, String login) {
        long id;
        ContentValues values = new ContentValues();
        values.put(FIELD_LANG1_NAME, lang1);
        values.put(FIELD_LANG2_NAME, lang2);
        values.put(FIELD_LANG3_NAME, lang3);
        values.put(FIELD_ACTIVE_NAME, active);
        id  = mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
        return id;
    }

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param user
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Profiles user) {
		long id  = replaceItem(user.getName(), user.getImage(), user.getLogin(), user.getPass(), user.getLang1(), user.getLang2(), user.getLang3(), user.getLastDate(), user.getActive(), true);
		return id;
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param user
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Profiles user) {
        long id  = replaceItem(user.getName(), user.getImage(), user.getLogin(), user.getPass(), user.getLang1(), user.getLang2(), user.getLang3(), user.getLastDate(), user.getActive(), false);
        return id;
    }
    /**
     * <p>Удаляет запись в таблице users</p>
     * @param login
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long deleteItem(String login) {
        long id;
        id  = mDb.delete(TABLE_NAME, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
        return id;
    }

}
