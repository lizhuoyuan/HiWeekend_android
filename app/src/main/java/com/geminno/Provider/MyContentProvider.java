package com.geminno.Provider;

import java.util.HashMap;

import com.geminno.columns.Cloumns.CinemaCloumns;
import com.geminno.columns.Cloumns.MovieCloumns;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 
 * @ClassName: BaseProvider
 * @Description: 所有内容提供者的基类
 * @author: XU
 * @date: 2015年10月18日 下午8:53:48
 */
public class MyContentProvider extends ContentProvider {
    /**
     * 表名
     */
    public static final String MOVIE_TABLE_NAME = "movie";
    public static final String CINEMA_TABLE_NAME = "cinema";
    /**
     * 表的创建语句
     */
    public static final String MOVIE_CREATE_SQL = "create table "
	    + MOVIE_TABLE_NAME + "( " + MovieCloumns._ID
	    + " integer not null, " + MovieCloumns.NAME + " text not null, "
	    + MovieCloumns.POSTER + " text not null, " + MovieCloumns.INTRODUCE
	    + " text not null, " + MovieCloumns.GRADE + " float not null, "
	    + MovieCloumns.DIRECTOR + " text not null, "
	    + MovieCloumns.DISTRICT + " text not null, "
	    + MovieCloumns.DURATION + " text, " + MovieCloumns.DATE
	    + " text not null, " + MovieCloumns.GENRES + " text not null, "
	    + MovieCloumns.LANGUAGE + " text not null, " + MovieCloumns.YEAR
	    + " text not null" + " ); ";
    public static final String CINEMA_CREATE_SQL = "create table "
	    + CINEMA_TABLE_NAME + "( " + CinemaCloumns._ID
	    + " integer not null, " + CinemaCloumns.NAME + " text not null, "
	    + CinemaCloumns.ADDRESS + " text not null, " + CinemaCloumns.TEL
	    + " text not null, " + CinemaCloumns.LAT + " float not null, "
	    + CinemaCloumns.LON + " float not null, " + CinemaCloumns.GRADE
	    + " float not null, " + CinemaCloumns.CITY + " text not null, "
	    + CinemaCloumns.TRAFFIC + " text);";
    /**
     * 路径
     */
    public static final String AUTHORITY = "com.geminno.provider.DATA_BASE_PROVIDER";
    /**
     * 列集合
     */
    public static HashMap<String, String> MovieColumnsmap;
    public static HashMap<String, String> CinemaColumnsmap;

    /**
     * 路径匹配
     */
    private static UriMatcher uMatcher;
    private MyDatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;
    /**
     * 匹配结果
     */
    private static final int ALL_MOIVE = 0;
    private static final int SINGLE_MOVIE = 1;

    private static final int ALL_CINEMA = 2;
    private static final int SINGLE_CINEMA = 3;
    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "hiweek.db";
    /**
     * 版本号
     */
    private static final int DATABASE_VERSON = 1;

    static {
	uMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	uMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, ALL_MOIVE);
	uMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", SINGLE_MOVIE);
	uMatcher.addURI(AUTHORITY, CINEMA_TABLE_NAME, ALL_CINEMA);
	uMatcher.addURI(AUTHORITY, CINEMA_TABLE_NAME + "/#", SINGLE_CINEMA);
    }

    @Override
    public boolean onCreate() {
	dbHelper = new MyDatabaseOpenHelper(getContext());
	return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {
	db = dbHelper.getReadableDatabase();
	SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
	switch (uMatcher.match(uri)) {

	case ALL_MOIVE:
	    builder.setTables(MOVIE_TABLE_NAME);
	    builder.setProjectionMap(MovieColumnsmap);
	    break;
	case SINGLE_MOVIE:
	    builder.setTables(MOVIE_TABLE_NAME);
	    builder.setProjectionMap(MovieColumnsmap);
	    builder.appendWhere("_id=" + uri.getPathSegments().get(1));
	    break;
	case ALL_CINEMA:
	    builder.setTables(CINEMA_TABLE_NAME);
	    builder.setProjectionMap(CinemaColumnsmap);
	    break;
	case SINGLE_CINEMA:
	    builder.setTables(CINEMA_TABLE_NAME);
	    builder.setProjectionMap(CinemaColumnsmap);
	    builder.appendWhere("_id=" + uri.getPathSegments().get(1));
	    break;
	default:
	    throw new IllegalArgumentException("UnKnow URI " + uri);
	}

	Cursor cursor = builder.query(db, projection, selection, selectionArgs,
		null, null, sortOrder);
	cursor.setNotificationUri(getContext().getContentResolver(), uri);
	return cursor;
    }

    @Override
    public String getType(Uri uri) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
	db = dbHelper.getWritableDatabase();
	String tablename = null;
	int matchResult = uMatcher.match(uri);
	/**
	 * 偶数为ALL，奇数为SINGLE
	 */
	if (matchResult % 2 == 1) {
	    throw new IllegalArgumentException("UnKnown URI " + uri);
	}
	switch (matchResult) {
	case ALL_CINEMA:
	    tablename = CINEMA_TABLE_NAME;
	    break;
	case ALL_MOIVE:
	    tablename = MOVIE_TABLE_NAME;
	    break;
	default:
	    break;
	}
	long id = db.insert(tablename, null, values);
	if (id > 0) {
	    Uri resulturi = ContentUris.withAppendedId(
		    Uri.parse("content://" + AUTHORITY + "/" + tablename), id);

	    return resulturi;
	}
	throw new SQLException("Failed to insert into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
	db = dbHelper.getWritableDatabase();
	int count;
	int matchResult = uMatcher.match(uri);
	String tablename = null;
	switch (matchResult) {
	case ALL_CINEMA:
	case SINGLE_CINEMA:
	    tablename = CINEMA_TABLE_NAME;
	    break;
	case ALL_MOIVE:
	case SINGLE_MOVIE:
	    tablename = MOVIE_TABLE_NAME;
	    break;
	default:
	    throw new IllegalArgumentException("unKnown URI " + uri);
	}
	if (matchResult % 2 == 0) {
	    count = db.delete(tablename, selection, selectionArgs);

	} else {
	    String uriID = uri.getPathSegments().get(1);
	    count = db.delete(tablename,
		    "_id="
			    + uriID
			    + (TextUtils.isEmpty(selection) ? "" : " AND ("
				    + selection + ")"), selectionArgs);
	}
	getContext().getContentResolver().notifyChange(uri, null);
	return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
	    String[] selectionArgs) {
	int count;
	int matchResult = uMatcher.match(uri);
	String tableName;
	switch (matchResult) {
	case ALL_MOIVE:
	case SINGLE_MOVIE:
	    tableName = MOVIE_TABLE_NAME;
	    break;
	case ALL_CINEMA:
	case SINGLE_CINEMA:
	    tableName = CINEMA_TABLE_NAME;
	    break;
	default:
	    throw new IllegalArgumentException("UnKnown Uri " + uri);
	}
	if (matchResult % 2 == 0) {
	    count = db.update(tableName, values, selection, selectionArgs);

	} else {
	    String resultID = uri.getPathSegments().get(1);
	    count = db.update(tableName, values,
		    "_id="
			    + resultID
			    + (TextUtils.isEmpty(selection) ? "" : " AND ("
				    + selection + ")"), selectionArgs);
	}
	getContext().getContentResolver().notifyChange(uri, null);
	return count;
    }

    private class MyDatabaseOpenHelper extends SQLiteOpenHelper {

	public MyDatabaseOpenHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSON);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(MOVIE_CREATE_SQL);
	    db.execSQL(CINEMA_CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

    }
}
