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
    public static final String FIELD_MONDAY_NAME = "mon";
    public static final String FIELD_TUESDAY_NAME = "tue";
    public static final String FIELD_WEDNESDAY_NAME = "wen";
    public static final String FIELD_THURSDAY_NAME = "tur";
    public static final String FIELD_FRIDAY_NAME = "fri";
    public static final String FIELD_SATURDAY_NAME = "sat";
    public static final String FIELD_SUNDAY_NAME = "sun";

    public static final String FIELD_HOUR_START_NAME = "hour_start";
    public static final String FIELD_HOUR_END_NAME = "hour_end";
    public static final String FIELD_PERIOD_NAME = "period";

    public static final String FIELD_ACTIVE_USER_NAME = "user_active";

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
            FIELD_ACTIVE_NAME,
            FIELD_MONDAY_NAME,
            FIELD_TUESDAY_NAME,
            FIELD_WEDNESDAY_NAME,
            FIELD_THURSDAY_NAME,
            FIELD_FRIDAY_NAME,
            FIELD_SATURDAY_NAME,
            FIELD_SUNDAY_NAME,
            FIELD_HOUR_START_NAME,
            FIELD_HOUR_END_NAME,
            FIELD_PERIOD_NAME,
            FIELD_ACTIVE_USER_NAME
    };

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
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ACTIVE_USER_NAME  + "=?",
                new String[] { "1" }, null, null, null);
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        return null;
    }

    public void setActiveUser(String login) {
       ContentValues values = new ContentValues();
        // снимаем всем статус
        values.put(FIELD_ACTIVE_USER_NAME, "0");
        mDb.update(TABLE_NAME, values, null, null);
        values.put(FIELD_ACTIVE_USER_NAME, "1");
        mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
    }

    /**
	 * <p>Возвращает запись из таблицы users</p>
	 * @param login - логин пользователя
	 * @return profile - возвращает профиль
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
        item.setCheck_weekday(1,cursor.getInt(cursor.getColumnIndex(FIELD_MONDAY_NAME)));
        item.setCheck_weekday(2,cursor.getInt(cursor.getColumnIndex(FIELD_TUESDAY_NAME)));
        item.setCheck_weekday(3,cursor.getInt(cursor.getColumnIndex(FIELD_WEDNESDAY_NAME)));
        item.setCheck_weekday(4,cursor.getInt(cursor.getColumnIndex(FIELD_THURSDAY_NAME)));
        item.setCheck_weekday(5,cursor.getInt(cursor.getColumnIndex(FIELD_FRIDAY_NAME)));
        item.setCheck_weekday(6,cursor.getInt(cursor.getColumnIndex(FIELD_SATURDAY_NAME)));
        item.setCheck_weekday(7,cursor.getInt(cursor.getColumnIndex(FIELD_SUNDAY_NAME)));
        item.setStart(cursor.getInt(cursor.getColumnIndex(FIELD_HOUR_START_NAME)));
        item.setEnd(cursor.getInt(cursor.getColumnIndex(FIELD_HOUR_END_NAME)));
        item.setPeriod(cursor.getInt(cursor.getColumnIndex(FIELD_PERIOD_NAME)));
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
        ArrayList<Profiles> arrayList = new ArrayList<>();
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
	public long replaceItem(String name, String image, String login, String pass, int lang1, int lang2, int lang3, int lastdate, int active, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, int start, int end, int period, boolean uprep) {
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
        values.put(FIELD_MONDAY_NAME, monday);
        values.put(FIELD_TUESDAY_NAME, tuesday);
        values.put(FIELD_WEDNESDAY_NAME, wednesday);
        values.put(FIELD_THURSDAY_NAME, thursday);
        values.put(FIELD_FRIDAY_NAME, friday);
        values.put(FIELD_SATURDAY_NAME, saturday);
        values.put(FIELD_SUNDAY_NAME, sunday);
        values.put(FIELD_HOUR_START_NAME, start);
        values.put(FIELD_HOUR_END_NAME, end);
        values.put(FIELD_PERIOD_NAME, period);
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
        ContentValues values = new ContentValues();
        values.put(FIELD_LANG1_NAME, lang1);
        values.put(FIELD_LANG2_NAME, lang2);
        values.put(FIELD_LANG3_NAME, lang3);
        values.put(FIELD_ACTIVE_NAME, active);
        return mDb.update(TABLE_NAME, values, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
    }

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param user - пользователь
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Profiles user) {
		return replaceItem(user.getName(), user.getImage(), user.getLogin(), user.getPass(), user.getLang1(), user.getLang2(), user.getLang3(), user.getLastDate(), user.getActive(), user.getCheck_weekday(1), user.getCheck_weekday(2), user.getCheck_weekday(3), user.getCheck_weekday(4), user.getCheck_weekday(5), user.getCheck_weekday(6), user.getCheck_weekday(7),user.getStart(), user.getEnd(), user.getPeriod(), true);
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param user - пользователь
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Profiles user) {
        return replaceItem(user.getName(), user.getImage(), user.getLogin(), user.getPass(), user.getLang1(), user.getLang2(), user.getLang3(), user.getLastDate(), user.getActive(), user.getCheck_weekday(1), user.getCheck_weekday(2), user.getCheck_weekday(3), user.getCheck_weekday(4), user.getCheck_weekday(5), user.getCheck_weekday(6), user.getCheck_weekday(7),user.getStart(), user.getEnd(), user.getPeriod(), false);
    }
    /**
     * <p>Удаляет запись в таблице users</p>
     * @param login - логин пользователя который нужно удалить
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long deleteItem(String login) {
        long id;
        id  = mDb.delete(TABLE_NAME, FIELD_LOGIN_NAME + "=?", new String[] { String.valueOf(login) });
        return id;
    }

}
