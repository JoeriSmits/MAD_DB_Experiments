package nl.han.ica.mad.leveldb;

import android.content.Context;

import com.github.hf.leveldb.LevelDB;
import com.github.hf.leveldb.exception.LevelDBException;

import nl.han.ica.mad.leveldb.model.Weather;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 30-10-2016
 */

public class LevelDBHandler {

    private static final String LEVEL_DB_PATH = "/db/leveldb";

    private LevelDB db;
    private Context context;

    /**
     * Creates a new LevelDB handler.
     *
     * @param context The context the LevelDB is being opened from (eg. Activity or Fragment)
     * @throws LevelDBException if there was a problem creating or opening the database file
     */
    public LevelDBHandler(Context context) throws LevelDBException {
        this.context = context;
        db = getInstance();
    }

    public void put(Weather wather) throws LevelDBException {
        LevelDB currentInstance = getInstance();

    }

    private LevelDB getInstance() throws LevelDBException {
        if (db == null || db.isClosed()) {
            db = LevelDB.open(LEVEL_DB_PATH, LevelDB.configure().createIfMissing(true));
        }
        return db;
    }

    private void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
    }
}
