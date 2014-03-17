/**
 * 
 */
package app.guohow.melody.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author guohao
 * 
 */
public class SQLHelper extends SQLiteOpenHelper {

	public static String CREATE_TABLE;
	public static String TABLE_NAME = "mTable";

	public SQLHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		init();// 在oncreate()中创建表
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);// 删除表
		onCreate(db);// 重新创建
	}

	private void init() {

		CREATE_TABLE = "CREATE TABLE" + " " + TABLE_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ "title TEXT,artist TEXT,url TEXT)";// 不要忘记写空格
	}

}
