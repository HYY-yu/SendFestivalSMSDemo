package com.app.feng.sendfestivalsmsdemo.control;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.app.feng.sendfestivalsmsdemo.element.HistoryMessage;

/**
 * Created by feng on 2015/10/25.
 */
public class SmsHistoryProvider extends ContentProvider {

    private static final String PRIVIDER_NAME = "com.feng.sendSms.control.provider.SmsHistoryProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PRIVIDER_NAME + "/sms");

    public static final int SMS_ALL = 1;
    public static final int SMS_ONE = 2;

    private static final UriMatcher uriMather;

    static {
        uriMather = new UriMatcher(UriMatcher.NO_MATCH);
        uriMather.addURI(PRIVIDER_NAME, "sms", SMS_ALL);
        uriMather.addURI(PRIVIDER_NAME, "sms/#", SMS_ONE);
    }

    private SmsSQLOpenHelper smsSQLOpenHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        smsSQLOpenHelper = SmsSQLOpenHelper.getInstance(getContext());
        database = smsSQLOpenHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = uriMather.match(uri);

        switch (match) {
            case SMS_ONE:
                long id = ContentUris.parseId(uri);
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            case SMS_ALL:
                break;
        }

        Cursor cursor = database.query(HistoryMessage.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMather.match(uri);
        if (match != SMS_ALL && match != SMS_ONE) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        long rowID = database.insert(HistoryMessage.TABLE_NAME, null, values);
        if (rowID > 0) {
            //通知刷新
            Uri uri1 = ContentUris.withAppendedId(uri, rowID);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
