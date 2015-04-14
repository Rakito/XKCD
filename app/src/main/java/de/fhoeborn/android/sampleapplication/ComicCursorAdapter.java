package de.fhoeborn.android.sampleapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import de.fhoeborn.android.sampleapplication.content.ComicsDatabase;

class ComicCursorAdapter extends CursorAdapter {

    public ComicCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ImageView comicView = new ImageView(context);
        comicView.setScaleType(ImageView.ScaleType.CENTER);
        return comicView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView comicView = (ImageView) view;

        Drawable image = Drawable.createFromPath(cursor.getString(cursor.getColumnIndex(ComicsDatabase.COLUMN_IMG_PATH)));
        comicView.setImageDrawable(image);
    }
}
