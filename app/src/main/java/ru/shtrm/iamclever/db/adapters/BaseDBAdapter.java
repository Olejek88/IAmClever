package ru.shtrm.iamclever.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.shtrm.iamclever.DatabaseHelper;
import ru.shtrm.iamclever.db.tables.BaseTable;

public class BaseDBAdapter {

	private DatabaseHelper mDbHelper;
	protected SQLiteDatabase mDb;
	private final Context mContext;

	private String TABLE_NAME;

	public static final String FIELD_ID = "id";
	String[] mColumns = { "*" };

	public BaseDBAdapter(Context context, String tableName) {
		mContext = context;
		mDbHelper = DatabaseHelper.getInstance(mContext);
		mDb = mDbHelper.getWritableDatabase();
		TABLE_NAME = tableName;
	}

	/**
	 * Собирает выражение sql для склеивания таблиц LEFT JOIN
	 * 
	 * @param firstTable
	 * @param secondTable
	 * @param firstField
	 * @param secondField
	 * @return
	 */
	protected static String getLeftJoinTables(String firstTable,
			String secondTable, String firstField, String secondField) {
		return getJoinTables("LEFT JOIN", firstTable, secondTable, firstField,
				secondField);
	}

	/**
	 * Собирает выражение sql для склеивания таблиц с указанным типом склеивания
	 * 
	 * @param join
	 * @param firstTable
	 * @param secondTable
	 * @param firstField
	 * @param secondField
	 * @return
	 */
	protected static String getJoinTables(String join, String firstTable,
			String secondTable, String firstField, String secondField) {

		StringBuilder result = new StringBuilder();

		result.append(join).append(' ').append(secondTable).append(" ON ")
				.append(firstTable).append('.').append(firstField).append('=')
				.append(secondTable).append('.').append(secondField);

		return result.toString();

	}

	/**
	 * Возвращает имя поля в формате table.fieldname
	 * 
	 * @param table
	 * @param field
	 * @return
	 */
	public static String getFullName(String table, String field) {
		return new StringBuilder().append(table).append('.').append(field)
				.toString();
	}

	/**
	 * Заполняет общие поля для всех объектов таблиц
	 * 
	 * @param cursor
	 * @param item
	 * @return
	 */
	protected BaseTable getItem(Cursor cursor, BaseTable item) {
		item.set_id(cursor.getLong(cursor.getColumnIndex(FIELD_ID)));
		return item;
	}

	protected ContentValues putCommonFields(BaseTable item) {
		ContentValues values = new ContentValues();
		return values;
	}

}
