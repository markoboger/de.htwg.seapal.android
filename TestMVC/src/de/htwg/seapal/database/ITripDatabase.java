package de.htwg.seapal.database;

import java.util.List;
import java.util.UUID;

import de.htwg.seapal.model.ITrip;


public interface ITripDatabase {

	UUID newTrip();

	void saveTrip(ITrip trip);

	void deleteTrip(UUID id);

	ITrip getTrip(UUID id);

	List<ITrip> getTrips();

	boolean closeDB();
}