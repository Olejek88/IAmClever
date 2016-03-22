package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.shtrm.iamclever.db.tables.Answers;

public class AnswersDBAdapter extends BaseDBAdapter {

	public static final String TABLE_NAME = "answers";

    public static final String FIELD_QUESTION_NAME = "question";
    public static final String FIELD_PROFILE_NAME = "profile";
    public static final String FIELD_LEVEL_NAME = "level";
    public static final String FIELD_CORRECT_NAME = "correct";
    public static final String FIELD_INCORRECT_NAME = "incorrect";

    String[] mColumns = {
            FIELD_QUESTION_NAME,
            FIELD_PROFILE_NAME,
            FIELD_LEVEL_NAME,
            FIELD_CORRECT_NAME,
            FIELD_INCORRECT_NAME};

    public AnswersDBAdapter(Context context) {
        super(context, TABLE_NAME);
    }

    public void replace(Answers answer) {
        ContentValues values = new ContentValues();
        values.put(FIELD_QUESTION_NAME, answer.getQuestion());
        values.put(FIELD_PROFILE_NAME, answer.getProfile());
        values.put(FIELD_LEVEL_NAME, answer.getLevel());
        values.put(FIELD_CORRECT_NAME, answer.getCorrect());
        values.put(FIELD_INCORRECT_NAME, answer.getIncorrect());
        mDb.replace(TABLE_NAME,null,values);
    }

    public Answers getBadAnswerByLevelAndProfile(int level, int profile) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_LEVEL_NAME + "=? AND " + FIELD_PROFILE_NAME + "<=?", new String[]{""+level, ""+profile}, null, null, null);
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        return null;
    }

    public Answers getAnswerByQuestionAndProfile(int question, int profile) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_QUESTION_NAME + "=? AND " + FIELD_PROFILE_NAME + "<=?", new String[]{""+question, ""+profile}, null, null, null);
        if (cursor.getCount()==0) {
            replaceItem(question, profile, 1,  0, 0, false);
            cursor = mDb.query(TABLE_NAME, mColumns, FIELD_QUESTION_NAME + "=? AND " + FIELD_PROFILE_NAME + "<=?", new String[]{""+question, ""+profile}, null, null, null);
        }
        if (cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                return getItem(cursor);
            }
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @param id - идентификатор
     */
    public Answers getAnswerById(int id) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_ID + "=?", new String[]{""+id}, null, null, null);
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @param id - идентификатор
     */
    public Answers getAnswerByQuestion(int id) {
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, FIELD_QUESTION_NAME + "=?", new String[]{""+id}, null, null, null);
        if (cursor.moveToFirst()) {
            return getItem(cursor);
        }
        cursor.close();
        return null;
    }

    public Answers getItem(Cursor cursor) {
        Answers item = new Answers();
        item.setQuestion(cursor.getInt(cursor.getColumnIndex(FIELD_QUESTION_NAME)));
        item.setProfile(cursor.getInt(cursor.getColumnIndex(FIELD_PROFILE_NAME)));
        item.setLevel(cursor.getInt(cursor.getColumnIndex(FIELD_LEVEL_NAME)));
        item.setCorrect(cursor.getInt(cursor.getColumnIndex(FIELD_CORRECT_NAME)));
        item.setIncorrect(cursor.getInt(cursor.getColumnIndex(FIELD_INCORRECT_NAME)));
        return item;
    }

    /**
     * <p>
     * Возвращает все записи из таблицы
     * </p>
     *
     * @return list
     */
    public ArrayList<Answers> getAllItems() {
        ArrayList<Answers> arrayList = new ArrayList<>();
        Cursor cursor;
        cursor = mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (true) {
                Answers answer = getItem(cursor);
                arrayList.add(answer);
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
    public long replaceItem(int question, int profile, int level, int correct, int incorrect, boolean update) {
        long id;
        ContentValues values = new ContentValues();
        values.put(FIELD_QUESTION_NAME, question);
        values.put(FIELD_PROFILE_NAME, profile);
        values.put(FIELD_LEVEL_NAME, level);
        values.put(FIELD_CORRECT_NAME, correct);
        values.put(FIELD_INCORRECT_NAME, incorrect);
        if (update)
            id = mDb.update(TABLE_NAME, values, FIELD_QUESTION_NAME + "=? AND " + FIELD_PROFILE_NAME + "=?", new String[] { String.valueOf(question), String.valueOf(profile)});
        else
            id  = mDb.replace(TABLE_NAME, null, values);
        return id;
    }

    /**
     * <p>Добавляет запись в таблице users</p>
     * @param answer - ответ
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long replaceItem(Answers answer) {
        return replaceItem(answer.getQuestion(),answer.getProfile(),answer.getLevel(),answer.getCorrect(),answer.getIncorrect(),false);
    }
    /**
     * <p>Иизменяет запись в таблице users</p>
     * @param answer - ответ
     * @return long id столбца или -1 если не удалось добавить запись
     */
    public long updateItem(Answers answer) {
        return replaceItem(answer.getQuestion(),answer.getProfile(),answer.getLevel(),answer.getCorrect(),answer.getIncorrect(),true);
    }

}
