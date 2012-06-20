package pl.mu.DB;

import java.sql.SQLException;

import pl.mu.data.ReminderObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
 
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "ormliteandroid.db";
	private static final int DATABASE_VERSION = 1;
 
	private Dao<ReminderObject, String> reminderDao = null;
	private RuntimeExceptionDao<ReminderObject, String> reminderRuntimeDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
 
	public Dao<ReminderObject, String> getReminderDao() throws SQLException {
		if (reminderDao == null) {
			reminderDao = getDao(ReminderObject.class);
		}
		return reminderDao;
	}
 
	public RuntimeExceptionDao<ReminderObject, String> getReminderDataDao() {
		if (reminderRuntimeDao == null) {
			reminderRuntimeDao = getRuntimeExceptionDao(ReminderObject.class);
		}
		return reminderRuntimeDao;
	}
 
	@Override
	public void close() {
		super.close();
		reminderRuntimeDao = null;
	}
 
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ReminderObject.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ReminderObject.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
}