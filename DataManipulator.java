/*Include a packages statement for your specific project, similar to this
package com.layout;
*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public class DataManipulator {
	private static final  String DATABASE_NAME = "YourDatabaseName.db"; //Change this to something unique
	private static final int DATABASE_VERSION = 1;
	static final String TABLE_NAME = "YourTableNameHere";
	private static Context context;
	static SQLiteDatabase db;
	private SQLiteStatement insertStmt;
	//Modify the following to match the insert method below, this specifies the columns of your database
    private static final String INSERT = "insert into " + TABLE_NAME + " (email1,email2,content,dateCreated,date1,date2,tab) values (?,?,?,?,?,?,?)";

	//Modify the following to match the insert string above, this will add new entries to your database
	public long insert(String email1,String email2,String content,String dateCreated, String date1, String date2, String tab) {
		this.insertStmt.bindString(1, email1);
		this.insertStmt.bindString(2, email2);
		this.insertStmt.bindString(3, content);
		this.insertStmt.bindString(4, dateCreated);
		this.insertStmt.bindString(5, date1);
		this.insertStmt.bindString(6, date2);
		this.insertStmt.bindString(7, tab);
		return this.insertStmt.executeInsert();
	}
	//Don't mess with this method
	public DataManipulator(Context context) {
		DataManipulator.context = context;
		OpenHelper openHelper = new OpenHelper(DataManipulator.context);
		DataManipulator.db = openHelper.getWritableDatabase();
		this.insertStmt = DataManipulator.db.compileStatement(INSERT);
	}
	//This will null all entries in your database
	public void deleteAll() {
		db.delete(TABLE_NAME, null, null);
	}
	//This will select all entries in your database
	public List<String[]> selectAll() {
		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = db.query(TABLE_NAME, new String[] { "id","email1","email2","content","dateCreated", "date1", "date2", "tab" },
				null, null, null, null, "id asc"); 
		int x = 0;
		if (cursor.moveToFirst()) {
			do {
				String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)};
				list.add(b1);
				x++;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		} 
		cursor.close();
		return list;
	}
	//This custom method will return an array for all values that match the id passed
	public String[] selectByID(int id){
		Cursor c = db.query(TABLE_NAME, new String[] { "id","email1","email2","content","dateCreated", "date1", "date2", "tab" }, "id=?", new String[]{String.valueOf(id)}, null, null, null);
		String[] ret = new String[]{c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4), c.getString(5), c.getString(6), c.getString(7)};
		return ret;
	}
	//This custom method will update tab column of a specific entry specified by the id passed
	public void updateTab(int id, String tab){
		ContentValues args = new ContentValues();
		args.put("tab", tab);
		db.update(TABLE_NAME, args, "id=?", new String[]{String.valueOf(id)});
	}
	//Same as above, but a different field, date2
	public void updateDate2(int id, String date2) {
		ContentValues args = new ContentValues();
		args.put("date2", date2);
		db.update(TABLE_NAME, args, "id=?", new String[]{String.valueOf(id)});		
	}
	//Don't modify this:
	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, email1 TEXT, email2 TEXT, content TEXT, dateCreated TEXT, date1 TEXT, date2 TEXT, tab TEXT)");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}