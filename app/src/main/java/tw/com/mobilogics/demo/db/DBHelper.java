package tw.com.mobilogics.demo.db;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tw.com.mobilogics.demo.db.DBHelper.Table.ClassIfication;
import tw.com.mobilogics.demo.db.DBHelper.Table.FoodItems;
import tw.com.mobilogics.demo.db.DBHelper.Table.Personal;
/**
 * Created by xuyouren on 14/12/2.
 */
public class DBHelper extends SQLiteOpenHelper {
  private static final String DB_NAME = "demo.db";
  private static final int DB_VERSION = 1;

  public DBHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    getReadableDatabase();
  }

  public abstract static class Table {
    private Table() {
    }

    // 運動分類
    public abstract static class ClassIfication {
      public static String tableName = "_ClassIfication";
      public static String primaryKey = "_PrimaryKey";
      public static String name = "_Name";
      public static String group = "_Group";

      public static int index_name  = 1;
      public static int index_group = 2;

      private ClassIfication() {
      }

      public static class Creator {
        public long insertData(Context context, String name, int group) {
          ContentValues values = new ContentValues();
          values.put(ClassIfication.name, name);
          values.put(ClassIfication.group, group);
          DBHelper dbHelper = new DBHelper(context);
          return dbHelper.getReadableDatabase().insert(ClassIfication.tableName, null, values);
        }
      }
    }

    // 食物分類
    public abstract static class FoodItems {
      public static String tableName = "_FoodItem";
      public static String primaryKey = "_PrimaryKey";
      public static String name = "_Name";
      public static String group = "_Group";
      public static String calories = "_Calories";

      public static class Creator {
        public long insertData(Context context, String name, int group, int calories) {
          DBHelper dbHelper = new DBHelper(context);
          ContentValues values = new ContentValues();
          values.put(FoodItems.name, name);
          values.put(FoodItems.group, group);
          values.put(FoodItems.calories, calories);
          return dbHelper.getWritableDatabase().insert(FoodItems.tableName, null ,values);
        }
        public Cursor queryData(Context context, int group) {
          DBHelper dbHelper = new DBHelper(context);
          return
            dbHelper.getReadableDatabase().rawQuery (
              "SELECT * FROM " + tableName + " WHERE " + FoodItems.group + " =?",
              new String[] { String.valueOf(group) }
            );
        }
      }
    }

    public abstract static class Personal {
      public static final String tableName = "_Personal";
      public static final String primarkKey = "_PrimaryKey";
      public static final String time = "_Time";
      public static final String calories = "_Calories";

      public static DBHelper mDBHelper;

      public static class Creator {
        public Creator(Context context) {
          if (context == null) throw new IllegalArgumentException("params context is null");
          mDBHelper = new DBHelper(context);
        }
        public long insertData(String time, int calories) {
          ContentValues values = new ContentValues();
          values.put(Personal.time, time);
          values.put(Personal.calories, calories);
          return mDBHelper.getWritableDatabase().insert(Personal.tableName, null, values);
        }
      }


    }

    public static Cursor getAllData(Context context, String table) {
      DBHelper dbHelper = new DBHelper(context);
      return
        dbHelper.getReadableDatabase().query (
          table, null, null, null, null, null, null
        );
    }
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    createClassIficationTable(db);
    createFoodItemsTable(db);
    createPersonalTable(db);
  }

  private void createClassIficationTable(SQLiteDatabase db) {
    db.execSQL (
      "CREATE TABLE IF NOT EXISTS " + ClassIfication.tableName + "("
      + ClassIfication.primaryKey     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + ClassIfication.name           + " TEXT, "
      + ClassIfication.group          + " INTEGER "
      + ");"
    );
  }

  private void createFoodItemsTable(SQLiteDatabase db) {
    db.execSQL (
      "CREATE TABLE IF NOT EXISTS " + Table.FoodItems.tableName + "("
        + FoodItems.primaryKey     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + FoodItems.name           + " TEXT, "
        + FoodItems.group          + " INTEGER, "
        + FoodItems.calories       + " INTEGER "
        + ");"
    );
  }

  private void createPersonalTable(SQLiteDatabase db) {
    db.execSQL (
      "CREATE TABLE IF NOT EXISTS " + Table.Personal.tableName + "("
        + Personal.primarkKey + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + Personal.time + " Date, "
        + Personal.calories + " INTEGER "
      +");"
    );
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
