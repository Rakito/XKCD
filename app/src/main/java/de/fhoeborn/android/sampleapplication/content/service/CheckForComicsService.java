package de.fhoeborn.android.sampleapplication.content.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.fhoeborn.android.comic_api.Comic;
import de.fhoeborn.android.sampleapplication.content.ComicsDatabase;
import de.fhoeborn.android.sampleapplication.content.ComicsProvider;
import retrofit.RestAdapter;

public class CheckForComicsService extends IntentService {

    public static final String ACTION_DOWNLOAD_ENDED = "de.fhoeborn.android.sampleapplication.content.service.action.ACTION_DOWNLOAD_ENDED";
    private static final String ACTION_DOWNLOAD_NEW = "de.fhoeborn.android.sampleapplication.content.service.action.ACTION_DOWNLOAD_NEW";
    private static final String ACTION_DOWNLOAD_NEXT = "de.fhoeborn.android.sampleapplication.content.service.action.ACTION_DOWNLOAD_ONE";
    private static final String EXTRA_AMOUNT = "de.fhoeborn.android.sampleapplication.content.service.extra.AMOUNT";


    public CheckForComicsService() {
        super("CheckForComicsService");
    }

    public static void startActionDownloadNew(Context context) {
        Intent intent = new Intent(context, CheckForComicsService.class);
        intent.setAction(ACTION_DOWNLOAD_NEW);
        context.startService(intent);
    }

    public static void startActionDownloadNext(Context context, int amount) {
        Intent intent = new Intent(context, CheckForComicsService.class);
        intent.setAction(ACTION_DOWNLOAD_NEXT);
        intent.putExtra(EXTRA_AMOUNT, amount);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_NEW.equals(action)) {
                handleActionDownloadNew();
            } else if (ACTION_DOWNLOAD_NEXT.equals(action)) {
                handleActionDownloadNext(intent.getIntExtra(EXTRA_AMOUNT, 0));
            }
        }
    }


    private void handleActionDownloadNew() {
        downloadComic(-1);
    }

    private boolean downloadComic(int number) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://xkcd.com")
                .build();
        XKCDService service = restAdapter.create(XKCDService.class);

        Comic comic;

        if (number < 0) {
            comic = service.getCurrentComic();
        } else {
            comic = service.getComic(number);
        }

        try {
            insertComic(comic);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    private void insertComic(Comic comic) throws IOException {
        ContentValues cv = new ContentValues();
        cv.put(ComicsDatabase.COLUMN_ALT_TEXT, comic.getAltText());
        cv.put(ComicsDatabase.COLUMN_IMG_PATH, downloadComic(comic.getImageUrl(), comic.getNumber()));
        cv.put(ComicsDatabase.COLUMN_NUMBER, comic.getNumber());
        cv.put(ComicsDatabase.COLUMN_TITLE, comic.getTitle());

        getContentResolver().insert(ComicsProvider.URI_ALL_COMICS, cv);
    }

    private String downloadComic(String downloadUrl, int number) throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File appFolder = getAppFolder(root);
        File file = new File(appFolder, number + ".jpg");
        if (file.exists()) return file.getAbsolutePath();

        InputStream in = new URL(downloadUrl).openConnection().getInputStream();

        FileOutputStream out = new FileOutputStream(file);
        try {
            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = in.read(buffer);
            }
        } finally {
            out.flush();
            out.close();
        }
        return file.getAbsolutePath();
    }

    private File getAppFolder(String root) throws IOException {
        File appFolder = new File(root, "xkcd");
        if (!appFolder.mkdirs() && !appFolder.exists()) {
            throw new IOException("Cannot create directory!");
        }
        return appFolder;
    }


    private void handleActionDownloadNext(int amount) {
        Cursor cursor = getContentResolver().query(ComicsProvider.URI_NEWEST_COMIC, null, null, null, null);
        if (isCursorEmptyOrNull(cursor)) {
            downloadComic(-1);
            amount--;
            cursor = getContentResolver().query(ComicsProvider.URI_NEWEST_COMIC, null, null, null, null);
        }

        int lowestNumber = cursor.getInt(cursor.getColumnIndex(ComicsDatabase.COLUMN_NUMBER));
        for (int i = 1; i <= amount; i++) {
            downloadComic(lowestNumber - i);
        }
        Intent broadcast = new Intent(ACTION_DOWNLOAD_ENDED);
        sendBroadcast(broadcast);
    }

    private boolean isCursorEmptyOrNull(Cursor cursor) {
        return cursor == null || cursor.getCount() == 0;
    }
}
