package com.example.sidhant.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by sidhant on 2017-10-13.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
   final static String  DName = "Student.db";
     final static String  TName = "Student_Table";
      final static String Col_1 = "Key_ID";
      final static String Col_2 = "Key_Message";



    public ChatDatabaseHelper(Context context) {
        super(context, DName, null,120);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i("ChatDatabaseHelper", "Calling onCreate");

     sqLiteDatabase.execSQL("CREATE TABLE " + TName +"("+Col_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ Col_2+" MESSAGE "+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TName);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + i + "newVersion=" + i1);
        onCreate(sqLiteDatabase);
    }

}
