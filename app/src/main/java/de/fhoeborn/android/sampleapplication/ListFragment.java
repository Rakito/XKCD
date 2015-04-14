package de.fhoeborn.android.sampleapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import de.fhoeborn.android.sampleapplication.content.ComicsDatabase;
import de.fhoeborn.android.sampleapplication.content.service.CheckForComicsService;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public interface OnItemSelectedListener {
        public void onComicSelected(int id);
    }

    private final static int URL_LOADER = 0;

    private OnItemSelectedListener listener;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,
                container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(CheckForComicsService.ACTION_DOWNLOAD_ENDED);
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, filter);
    }

    @Override
    public void onViewCreated(View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        list = (ListView) root.findViewById(R.id.comicList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = list.getAdapter();
                Cursor currentItem = (Cursor) adapter.getItem(position);
                listener.onComicSelected(currentItem.getInt(currentItem.getColumnIndex(ComicsDatabase.COLUMN_ID)));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().<Cursor>initLoader(URL_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.menu_list_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update: {
                onUpdateClicked();
                return true;
            }
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onUpdateClicked() {
        CheckForComicsService.startActionDownloadNew(this.getActivity());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case URL_LOADER:
                return new CursorLoader(
                        this.getActivity(),
                        Uri.parse("content://de.fhoeborn.android.comic/comics"),
                        new String[]{ComicsDatabase.COLUMN_ID, ComicsDatabase.COLUMN_IMG_PATH},
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
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        ComicCursorAdapter adapter = new ComicCursorAdapter(this.getActivity(), cursor);
        list.setAdapter(adapter);
        list.setOnScrollListener(new EndlessScrollListener());
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        list.setAdapter(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }
}