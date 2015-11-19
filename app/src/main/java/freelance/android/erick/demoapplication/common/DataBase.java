package freelance.android.erick.demoapplication.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by erick on 19.11.15.
 */
public class DataBase extends SQLiteOpenHelper {
    private static Context context;

    public static final String VALUES = "`Values`";

    private static final String DbName = "value.db";

    private static final int DataBaseVersion = 1;

    public static void init(Context context) {
        DataBase.context = context.getApplicationContext();
    }

    private static DataBase instance;

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase(DataBase.context);
        }
        return instance;
    }

    public DataBase(Context context) {
        super(context, DbName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + VALUES + " (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `text` text(128), `created_at` text(128))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VALUES);
        onCreate(db);
    }

    public long insertRows(String TABLE_NAME, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor selectRows(String TABLE_NAME, String[] columns, String whereClause, String groupBy, String orderBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(TABLE_NAME, columns, whereClause, null, groupBy, null, orderBy);
    }

    public static ContentValues setContentValues(String id, String text, String created_at) {
        ContentValues cv = new ContentValues();
        if(id != null)
            cv.put("id", id);
        if(text != null)
            cv.put("text", text);
        if(created_at != null)
            cv.put("created_at", created_at);
        return cv;
    }

    public boolean checkIfNotificationExist(String id, String text) {
        Cursor cursor = this.selectRows(VALUES, null, "id='" + id + "' AND text='"  + text + "'", null, null);
        return !(cursor.getCount() == 0);
    }
}
