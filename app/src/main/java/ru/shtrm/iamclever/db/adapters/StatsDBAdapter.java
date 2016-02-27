package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.shtrm.iamclever.db.tables.Stats;

public class StatsDBAdapter extends BaseDBAdapter {
	public static final String TABLE_NAME = "UserStats";

	public static final String FIELD_ID_NAME = "id";
	public static final String FIELD_LANG_NAME = "lang";
    public static final String FIELD_PROFILE_NAME = "profile";
	public static final String FIELD_DAYS_NAME = "days";
	public static final String FIELD_EXAMS_NAME = "exams";
	public static final String FIELD_EXAMS_COMPLETE_NAME = "exams_complete";
    public static final String FIELD_QUESTION_NAME = "questions";
    public static final String FIELD_QUESTION_RIGHT_NAME = "questions_right";

	String[] mColumns = {
			FIELD_ID_NAME,
			FIELD_LANG_NAME,
            FIELD_PROFILE_NAME,
			FIELD_DAYS_NAME,
			FIELD_EXAMS_NAME,
			FIELD_EXAMS_COMPLETE_NAME,
            FIELD_QUESTION_NAME,
            FIELD_QUESTION_RIGHT_NAME};

    public StatsDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

	/**
	 * 
	 * @param profile
	 * @return
	 */
	public Stats getStatsByProfileAndLang(int profile,int lang) {
		Cursor cursor;

		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_PROFILE_NAME + "=? AND " + FIELD_LANG_NAME + "=?", new String[]{""+profile,""+lang}, null, null, null);
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

    public Stats getItem(Cursor cursor) {
        Stats item = new Stats();
		item.setLang(cursor.getInt(cursor.getColumnIndex(FIELD_LANG_NAME)));
        item.setProfile(cursor.getInt(cursor.getColumnIndex(FIELD_PROFILE_NAME)));
		item.setDays(cursor.getInt(cursor.getColumnIndex(FIELD_DAYS_NAME)));
		item.setExams(cursor.getInt(cursor.getColumnIndex(FIELD_EXAMS_NAME)));
		item.setExams_complete(cursor.getInt(cursor.getColumnIndex(FIELD_EXAMS_COMPLETE_NAME)));
        item.setQuestions(cursor.getInt(cursor.getColumnIndex(FIELD_QUESTION_NAME)));
        item.setQuestions_right(cursor.getInt(cursor.getColumnIndex(FIELD_QUESTION_RIGHT_NAME)));
		return item;
	}

    /**
     * <p>
     * Возвращает все записи из таблицы
     * </p>
     *
     * @return list
     */
    public ArrayList<Stats> getAllItems() {
        ArrayList<Stats> arrayList = new ArrayList<Stats>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Stats stats = getItem(cursor);
                arrayList.add(stats);
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

	public long replaceItem(int lang, int profile, int days, int exams, int exams_complete, int question, int question_right) {
		long id;
		ContentValues values = new ContentValues();
		values.put(FIELD_LANG_NAME, lang);
        values.put(FIELD_PROFILE_NAME, profile);
		values.put(FIELD_DAYS_NAME, days);
		values.put(FIELD_EXAMS_NAME, exams);
        values.put(FIELD_EXAMS_COMPLETE_NAME, exams_complete);
		values.put(FIELD_QUESTION_NAME, question);
        values.put(FIELD_QUESTION_RIGHT_NAME, question_right);
        id  = mDb.replace(TABLE_NAME, null, values);
		return id;
	}

    public long deleteAllItem(int profile) {
        long id;
        id  = mDb.delete(TABLE_NAME, FIELD_PROFILE_NAME + "=?", new String[] { String.valueOf(profile) });
        return id;
    }

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param stat
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Stats stat) {
		long id  = replaceItem(stat.getLang(),stat.getProfile(),stat.getDays(),stat.getExams(),stat.getExams_complete(),stat.getQuestions(),stat.getQuestions_right());
		return id;
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param stat
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Stats stat) {
        long id  = replaceItem(stat.getLang(),stat.getProfile(),stat.getDays(),stat.getExams(),stat.getExams_complete(),stat.getQuestions(),stat.getQuestions_right());
        return id;
    }

}
