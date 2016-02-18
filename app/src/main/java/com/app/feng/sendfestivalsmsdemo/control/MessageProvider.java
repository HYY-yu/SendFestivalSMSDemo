package com.app.feng.sendfestivalsmsdemo.control;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.app.feng.sendfestivalsmsdemo.element.Festival;
import com.app.feng.sendfestivalsmsdemo.element.Msg;

/**
 * Created by feng on 2015/10/26.
 */
public class MessageProvider extends ContentProvider {

    private static final String PRIVIDER_NAME = "com.feng.sendSms.control.provider.MessageProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PRIVIDER_NAME);
    public static final Uri CONTENT_URI_FESTIVAL = Uri.parse("content://" + PRIVIDER_NAME + "/festival");
    public static final Uri CONTENT_URI_MSG = Uri.parse("content://" + PRIVIDER_NAME + "/msg");

    public static final int FESTIVAL_ALL = 3;

    public static final int MSG_ALL = 5;


    private static final UriMatcher uriMather;

    static {
        uriMather = new UriMatcher(UriMatcher.NO_MATCH);
        uriMather.addURI(PRIVIDER_NAME, "festival", FESTIVAL_ALL);

        uriMather.addURI(PRIVIDER_NAME, "msg", MSG_ALL);

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
        switch (uriMather.match(uri)) {
            case FESTIVAL_ALL:
                Cursor c = database.query(Festival.TABLE_FESTIVAL, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            case MSG_ALL:
                Cursor c2 = database.query(Msg.TABLE_MSG, projection, selection, selectionArgs, null, null, sortOrder);
                c2.setNotificationUri(getContext().getContentResolver(), uri);
                return c2;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long count;
        switch (uriMather.match(uri)) {
            case FESTIVAL_ALL:
                count = database.insert(Festival.TABLE_FESTIVAL, null, values);
                if (count > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI_FESTIVAL, count);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            case MSG_ALL:
                count = database.insert(Msg.TABLE_MSG, null, values);
                if (count > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI_MSG, count);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMather.match(uri)) {
            case FESTIVAL_ALL:
                count = database.delete(Festival.TABLE_FESTIVAL, selection, selectionArgs);
                break;
            case MSG_ALL:
                count = database.delete(Msg.TABLE_MSG, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
