package de.htwg.seapal.database.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.UpdateConflictException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import android.content.Context;
import android.util.Log;

import de.htwg.seapal.database.IBoatDatabase;
import de.htwg.seapal.model.IBoat;
import de.htwg.seapal.model.impl.Boat;

public class TouchDBBoatDatabase implements IBoatDatabase{

	private static final String TAG = "Boat-TouchDB";
	private static final String DDOCNAME = "seapal-boats";
	private static final String VIEWNAME = "boats";
	private static final String DATABASE_NAME = "seapal_boats_db";
	
	private static TouchDBBoatDatabase touchDBBoatDatabase;
	private CouchDbConnector couchDbConnector;
	private TouchDBHelper dbHelper;

	public TouchDBBoatDatabase(Context ctx) {
		dbHelper = new TouchDBHelper(VIEWNAME, DATABASE_NAME, DDOCNAME);
		dbHelper.createDatabase(ctx);
		dbHelper.pullFromDatabase();
		couchDbConnector = dbHelper.getCouchDbConnector();
		
	}
	public static TouchDBBoatDatabase getInstance(Context ctx)	{
		if (touchDBBoatDatabase == null)
			touchDBBoatDatabase = new TouchDBBoatDatabase(ctx);
		return touchDBBoatDatabase;
	}
	@Override
	public UUID newBoat() {
		IBoat boat = new Boat();
		try {
			couchDbConnector.create(boat.getId(), boat);
		} catch (UpdateConflictException e) {
			Log.e(TAG, e.toString());
		}
		UUID idx = UUID.fromString(boat.getId());
		Log.d(TAG, "Boat created: " + boat.getId());
		dbHelper.pushToDatabase();
		return idx;
	}
	@Override
	public void saveBoat(IBoat boat) {
		try {
			couchDbConnector.update(boat);
			dbHelper.pushToDatabase();
		} catch (DocumentNotFoundException e) {
			Log.d(TAG, "Document not Found");
			Log.d(TAG, e.toString());
			return;
		}
		Log.d(TAG, "Boat saved: " + boat.getId());
	}
	@Override
	public void deleteBoat(UUID id) {
		try {
			couchDbConnector.delete(getBoat(id));
			dbHelper.pushToDatabase();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return;
		}
		Log.d(TAG, "Boat deleted");
	}
	@Override
	public IBoat getBoat(UUID id) {
		IBoat boat;
		try {
			boat = couchDbConnector.get(Boat.class, id.toString());
		} catch (DocumentNotFoundException e) {
			Log.e(TAG, "Boat not found" + id.toString());
			return null;
		}
		return boat;
	}
	@Override
	public List<IBoat> getBoats() {
		List<IBoat> lst = new LinkedList<IBoat>();
		List<String> log = new LinkedList<String>();
		ViewQuery query = new ViewQuery().allDocs();		
		ViewResult vr = couchDbConnector.queryView(query);
		
	
		for(Row r : vr.getRows()) {
			lst.add(getBoat(UUID.fromString(r.getId())));
			log.add(r.getId());
		}
		Log.d(TAG, "All Boats: " + log.toString());
		return lst;
	}
	@Override
	public boolean closeDB() {
		return false;
	}
	
	

}
