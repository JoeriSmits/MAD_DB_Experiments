package mad.ica.han.ivobrands.couchbase;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.Replication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chevr on 09/02/2016.
 */
public class MainActivity extends Activity{
    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";
    private static final String ATT_NAME = "binaryData";
    private static final String MIME_TYPE = "application/octet-stream";
    private static final String username = "couchbase_user";
    private static final String password = "mobile";

    Manager manager = null;
    Database database = null;
    Replication push = null;
    Replication pull = null;
    URL syncUrl = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        helloCBL();
    }

    private void helloCBL() {
        Log.d(TAG, "Begin Couchbase Events App");
        try{
            manager = getManagerInstance();
            database = getDatabaseInstance();
        }
        catch(Exception e){
            Log.e(TAG, "Error getting database", e);
            return;
        }
        try{
            startReplications();
        }
        catch(Exception e){
            Log.e(TAG, "Error starting replication", e);
            return;
        }
        // Create the document
        String documentId = createDocument(database);
        /* Get and output the contents */
        outputContents(database, documentId);
        /*' Update the document and add an attachment */
        updateDoc(database, documentId);
        outputContents(database, documentId);
        // Add an attachment
        addAttachment(database, documentId);
        /* Get and output the contents with the attachment */
        //outputContentsWithAttachment(database, documentId);
        /* Get and delete the document from the database */
        //deleteDocument(database, documentId);
        Log.d(TAG, "End Couchbase Events App");
        closeDB();
    }

    private void closeDB() {
        database.close();
        manager.close();
    }

    private void startReplications() throws CouchbaseLiteException {
        syncUrl = createSyncURL(false);
        pull = this.getDatabaseInstance().createPullReplication(syncUrl);
        push = this.getDatabaseInstance().createPushReplication(syncUrl);
        Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator(username, password);
        pull.setAuthenticator(authenticator);
        push.setAuthenticator(authenticator);
        pull.setContinuous(true);
        push.setContinuous(true);
        addChangeListener();
        pull.start();
        push.start();
        Log.d(TAG, " PUSH AND PULL STARTED");
    }

    private void addChangeListener(){
        push.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                // will be called back when the push replication status changes
                Log.d(TAG, event.toString());
            }
        });
        pull.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                // will be called back when the pull replication status changes
                Log.d(TAG, event.toString());
            }
        });
    }

    private URL createSyncURL(boolean isEncrypted){
        URL syncURL = null;
        String host = "http://10.0.2.2";
        String port = "4984";
        String dbName = "couchbaseevents";
        try {
            syncURL = new URL(host + ":" + port + "/" + dbName + "/");
        } catch (MalformedURLException me) {
            me.printStackTrace();
        }
        Log.d("URL", syncURL.toString());
        return syncURL;
    }

    private void deleteDocument(Database database, String documentId) {
        Document retrievedDocument = database.getDocument(documentId);
        try {
            retrievedDocument.delete();
            Log.d (TAG, "Deleted document, deletion status = " + retrievedDocument.isDeleted());
        } catch (CouchbaseLiteException e) {
            Log.e (TAG, "Cannot delete document", e);
        }
    }

    private void outputContentsWithAttachment(Database database, String documentId) {
        Document fetchedSameDoc = database.getExistingDocument(documentId);
        SavedRevision saved = fetchedSameDoc.getCurrentRevision();
        // The content of the attachment is a byte[] we created
        Attachment attach = saved.getAttachment(ATT_NAME);
        int i = 0;
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(attach.getContent()));
        }
        catch (Exception e){
            Log.e(TAG, "Error while retrieving attachement",e );
        }
        if(reader != null){
            StringBuffer values = new StringBuffer();
            while (i++ < 4) {
                // We knew the size of the byte array
                // This is the content of the attachment
                try{
                    values.append(reader.read() + " ");
                }
                catch(IOException e){
                    Log.e(TAG, "Error while reading attachment " + i, e);
                }
            }
            Log.v(TAG, "The docID: " + documentId + ", attachment contents was: " + values.toString());
        }
    }

    private void addAttachment(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
        /* Add an attachment with sample data as POC */
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0, 0, 0, 0 });
            UnsavedRevision revision = document.getCurrentRevision().createRevision();
            revision.setAttachment(ATT_NAME, MIME_TYPE , inputStream);
        /* Save doc & attachment to the local DB */
            revision.save();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    private void updateDoc(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            //updatedProperties.put("eventDescription", "Everyone is invited!");
            //updatedProperties.put("address", "123 Elm St.");
            // Save to the Couchbase local Couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    private void outputContents(Database database, String documentId) {
        Document test = database.getDocument(documentId);
        if(null != test){
            Log.d(TAG, "retrievedDocument=" + String.valueOf(test.getProperties()));
            TextView textView = (TextView) findViewById(R.id.text);
            textView.setText(String.valueOf(test.getProperties()));
        }
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("date", "2016-10-26");
        map.put("forecast", "Sunny");
        map.put("humidity", "80%");
        subMap.put("direction", 280);
        subMap.put("speed", "50 knots");
        map.put("wind", subMap);
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }
}