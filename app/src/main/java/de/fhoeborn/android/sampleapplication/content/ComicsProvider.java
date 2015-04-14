package de.fhoeborn.android.sampleapplication.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ComicsProvider extends ContentProvider {
    private static final String AUTHORITY = "de.fhoeborn.android.comic";
    private static final String PATH_COMICS = "comics";

    public static final Uri URI_ALL_COMICS = Uri.parse("content://" + AUTHORITY + "/" + PATH_COMICS);
    public static final Uri URI_NEWEST_COMIC = Uri.parse("content://" + AUTHORITY + "/" + PATH_COMICS + "/newest");

    private static final int MATCH_ALL = 0;
    private static final int MATCH_SINGLE = 1;
    private static final int MATCH_NEWEST = 2;

    private UriMatcher uriMatcher;

    private ComicsDatabaseHelper databaseHelper;

    public ComicsProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_COMICS, MATCH_ALL);
        uriMatcher.addURI(AUTHORITY, PATH_COMICS + "/#", MATCH_SINGLE);
        uriMatcher.addURI(AUTHORITY, PATH_COMICS + "/newest", MATCH_NEWEST);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case MATCH_SINGLE:
                return databaseHelper.getWritableDatabase().delete(ComicsDatabase.TABLE_NAME, "id = ?", new String[]{uri.getLastPathSegment()});
            case MATCH_ALL:
                return databaseHelper.getWritableDatabase().delete(ComicsDatabase.TABLE_NAME, selection, selectionArgs);
            default:
                return -1;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MATCH_SINGLE:
            case MATCH_ALL:
                return "vnd.android.cursor.dir/comic";
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = Uri.withAppendedPath(URI_ALL_COMICS, "" + databaseHelper.getWritableDatabase().insert(ComicsDatabase.TABLE_NAME, null, values));
        getContext().getContentResolver().notifyChange(resultUri, null);
        getContext().getContentResolver().notifyChange(URI_ALL_COMICS, null);
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new ComicsDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case MATCH_ALL:
                cursor = databaseHelper.getReadableDatabase().query(ComicsDatabase.TABLE_NAME, projection, null, null, null, null, sortOrder);
                break;
            case MATCH_SINGLE:
                cursor = databaseHelper.getReadableDatabase().query(ComicsDatabase.TABLE_NAME, projection, ComicsDatabase.COLUMN_ID + " = ?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            case MATCH_NEWEST:
                Cursor minNumber = databaseHelper.getReadableDatabase().query(ComicsDatabase.TABLE_NAME, new String[]{"MIN(" + ComicsDatabase.COLUMN_NUMBER + ") AS MIN_NUMBER"}, null, null, null, null, null);
                if (minNumber.getCount() <= 0) {
                    return null;
                } else {
                    minNumber.moveToFirst();
                    cursor = databaseHelper.getReadableDatabase().query(ComicsDatabase.TABLE_NAME, projection, ComicsDatabase.COLUMN_NUMBER + " = ?", new String[]{"" + minNumber.getInt(minNumber.getColumnIndex("MIN_NUMBER"))}, null, null, sortOrder);
                }
                break;
        }

        if (cursor != null) {
            cursor.moveToFirst();
            if (uri != null) {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }
        }


        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case MATCH_SINGLE:
                return databaseHelper.getWritableDatabase().update(ComicsDatabase.TABLE_NAME, values, "id = ?", new String[]{uri.getLastPathSegment()});
            case MATCH_ALL:
                return databaseHelper.getWritableDatabase().update(ComicsDatabase.TABLE_NAME, values, selection, selectionArgs);
            default:
                return -1;
        }
    }
}
