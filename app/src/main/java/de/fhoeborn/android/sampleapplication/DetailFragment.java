package de.fhoeborn.android.sampleapplication;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.fhoeborn.android.sampleapplication.content.ComicsDatabase;
import de.fhoeborn.android.sampleapplication.views.ComicView;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int URL_LOADER = 0;

    private int id;

    private ComicView comicView;
    private TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comicView = (ComicView) view.findViewById(R.id.comic);
        titleView = (TextView) view.findViewById(R.id.title);
    }

    public void setId(int id) {
        this.id = id;

        getLoaderManager().<Cursor>initLoader(URL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case URL_LOADER:
                return new CursorLoader(
                        this.getActivity(),
                        Uri.parse("content://de.fhoeborn.android.comic/comics/" + id),
                        new String[]{ComicsDatabase.COLUMN_ID, ComicsDatabase.COLUMN_IMG_PATH, ComicsDatabase.COLUMN_TITLE, ComicsDatabase.COLUMN_ALT_TEXT},
                        null,
                        null,
                        null
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Drawable image = Drawable.createFromPath(data.getString(data.getColumnIndex(ComicsDatabase.COLUMN_IMG_PATH)));
        comicView.setImageDrawable(image);
        comicView.setAlternativeText(data.getString(data.getColumnIndex(ComicsDatabase.COLUMN_ALT_TEXT)));
        titleView.setText(data.getString(data.getColumnIndex(ComicsDatabase.COLUMN_TITLE)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nothing to do here
    }
}