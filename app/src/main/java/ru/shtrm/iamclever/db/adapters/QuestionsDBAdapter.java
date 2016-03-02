package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.shtrm.iamclever.db.tables.Questions;

public class QuestionsDBAdapter extends BaseDBAdapter {
	public static final String TABLE_NAME = "Questions";

	public static final String FIELD_ID_NAME = "id";
	public static final String FIELD_LANG_NAME = "lang";
    public static final String FIELD_TYPE_NAME = "type";
	public static final String FIELD_LEVEL_NAME = "level";
	public static final String FIELD_LEVELA_NAME = "levelA";
	public static final String FIELD_QUESTION_NAME = "question";
    public static final String FIELD_ORIGINAL_NAME = "original";
    public static final String FIELD_ANSWER_NAME = "answer";

	String[] mColumns = {
			FIELD_ID_NAME,
			FIELD_LANG_NAME,
            FIELD_TYPE_NAME,
			FIELD_LEVEL_NAME,
			FIELD_LEVELA_NAME,
			FIELD_QUESTION_NAME,
            FIELD_ORIGINAL_NAME,
            FIELD_ANSWER_NAME};

    public QuestionsDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

	public Questions getRandomQuestionByLangAndLevel(int lang, int level) {
		Cursor cursor;
        // TODO проверить на RAND
		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LANG_NAME + "=? AND " + FIELD_LEVEL_NAME + "<=?", new String[]{""+lang, ""+level}, null, null, "RANDOM() LIMIT 1");
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

	/**
	 * @param id - идентификатор
	 */
	public Questions getQuestionById(int id) {
		Cursor cursor;

		cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ID_NAME + "=?", new String[]{""+id}, null, null, null);
		if (cursor.moveToFirst()) {
			return getItem(cursor);
		}

		return null;
	}

    /**
     * @param original - оригинальное слово/выражение
     */
    public Questions getQuestionByName(String original) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ORIGINAL_NAME + "=?", new String[]{original}, null, null, null);
        if (cursor.getCount() > 0)
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        return null;
    }

    public Questions getItem(Cursor cursor) {
        Questions item = new Questions();
        item.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID_NAME)));
		item.setLang(cursor.getInt(cursor.getColumnIndex(FIELD_LANG_NAME)));
        item.setType(cursor.getInt(cursor.getColumnIndex(FIELD_TYPE_NAME)));
		item.setLevel(cursor.getInt(cursor.getColumnIndex(FIELD_LEVEL_NAME)));
		item.setLevelA1(cursor.getString(cursor.getColumnIndex(FIELD_LEVELA_NAME)));
		item.setQuestion(cursor.getInt(cursor.getColumnIndex(FIELD_QUESTION_NAME)));
        item.setOriginal(cursor.getString(cursor.getColumnIndex(FIELD_ORIGINAL_NAME)));
        item.setAnswer(cursor.getString(cursor.getColumnIndex(FIELD_ANSWER_NAME)));
		return item;
	}

    /**
     * <p>
     * Возвращает все записи из таблицы
     * </p>
     *
     * @return list - возвращает все вопросы
     */
    public ArrayList<Questions> getAllItems() {
        ArrayList<Questions> arrayList = new ArrayList<>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Questions question = getItem(cursor);
                arrayList.add(question);
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
	public long replaceItem(int lang, int type, int level, String levelA, int question, String original, String answer) {
		long id;
		ContentValues values = new ContentValues();
		values.put(FIELD_LANG_NAME, lang);
        values.put(FIELD_TYPE_NAME, type);
		values.put(FIELD_LEVEL_NAME, level);
        values.put(FIELD_LEVELA_NAME, levelA);
		values.put(FIELD_QUESTION_NAME, question);
		values.put(FIELD_ORIGINAL_NAME, original);
        values.put(FIELD_ANSWER_NAME, answer);
        id  = mDb.replace(TABLE_NAME, null, values);
		return id;
	}

    /**
	 * <p>Добавляет запись в таблице users</p>
	 * @param question - вопрос
	 * @return long id столбца или -1 если не удалось добавить запись
	 */
	public long replaceItem(Questions question) {
		return replaceItem(question.getLang(),question.getType(),question.getLevel(),question.getLevelA1(),question.getQuestion(),question.getOriginal(),question.getAnswer());
	}
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param question - вопрос
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Questions question) {
        return replaceItem(question.getLang(),question.getType(),question.getLevel(),question.getLevelA1(),question.getQuestion(),question.getOriginal(),question.getAnswer());
    }

}
