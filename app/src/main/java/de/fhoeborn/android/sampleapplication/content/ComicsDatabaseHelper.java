package de.fhoeborn.android.sampleapplication.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ComicsDatabaseHelper extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;

    public ComicsDatabaseHelper(Context context) {
        super(context, "comics.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ComicsDatabase.TABLE_NAME + //
                " (" + ComicsDatabase.COLUMN_ID + " integer PRIMARY KEY autoincrement, " + ComicsDatabase.COLUMN_NUMBER + " INT, " + ComicsDatabase.COLUMN_TITLE + //
                " TEXT, " + ComicsDatabase.COLUMN_ALT_TEXT + " TEXT, " + ComicsDatabase.COLUMN_IMG_PATH + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do atm
    }
}
