package ru.agrefj.contactschanger;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_changeContacts;
    EditText et_digits;
    EditText et_newDigits;

    Integer newNumber = 11111;
    ProgressBar progressBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_changeContacts = (Button) findViewById(R.id.btn_changeContacts);
        btn_changeContacts.setOnClickListener(this);

        et_digits = (EditText) findViewById(R.id.et_digits);
        et_newDigits = (EditText) findViewById(R.id.et_newDigits);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                200);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 100
        );


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_changeContacts:
                try {
                    buttonReadyCheck();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void buttonReadyCheck() throws RemoteException, OperationApplicationException {
        progressBar.setVisibility(View.VISIBLE);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        new CountDownTimer(20000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                // When timer is finished
                // Execute your code here
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.start();


        if (et_newDigits.getText().toString().length() > 0 && et_digits.getText().toString().length() > 0) {

            new CountDownTimer(500, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                   ChangeContactsActivity();


                }
            }.start();

        } else {
            Toast.makeText(MainActivity.this, this.getText(R.string.caution), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

//    public void LoadContactsActivity() throws RemoteException, OperationApplicationException {
//
//        String digits = et_digits.getText().toString();
//
//        int length = digits.length();
//
//        String newDigits = et_newDigits.getText().toString();
//
//        ContentResolver cr = getContentResolver();
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//
//                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                Integer count = Integer.parseInt(cursor.getString(
//                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//
//
//                if (Integer.parseInt(cursor.getString(
//                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    Cursor pCur = cr.query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                            new String[]{id}, null);
//
//                    String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    while (pCur.moveToNext()) {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//
//
//                        phoneNo = phoneNo.replace(" ", "").replace("),", "").replace("(", "").replace("-", "").replace("—", "");
//
//                        if (phoneNo.length() > digits.length()) {
//
//                            String firstN = phoneNo.substring(0, length);
//                            if (firstN.equals(digits)) {
//                                ;
//                                String changedStr = phoneNo.substring(length);
//                                changedStr = newDigits + changedStr;
//
//                                updateContact(name, phoneNo, contactID, changedStr);
//
//
//                            }
//                        } else {
//
//                        }
//
//                    }
//                    pCur.close();
//
//                }
//            }
//
//        }
//
//    }
//
//





    public void ChangeContactsActivity(){

        ContentResolver contentResolver = getContentResolver();

        // Run query
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Data.CONTACT_ID
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

        //Retrieve all contacts
        Cursor cursorAll = contentResolver.query(uri,projection,selection,selectionArgs,sortOrder);

        while (cursorAll.moveToNext()) {
            String contactID = cursorAll.getString(cursorAll.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            Log.e("cid", contactID);


            Cursor phoneNumCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactID}, null);

            phoneNumCursor.moveToFirst();

            while (phoneNumCursor.moveToNext()) {
                String phoneNumber = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String numI = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                updateContact(contactID,phoneNumber,numI);
                Log.e("phones",phoneNumber);
                Log.e("phI",numI);
            }

            phoneNumCursor.close();

//

//            Integer count = phoneNumCursor.getCount();
//            Log.e("count",count.toString());
//            while (phoneNumCursor.moveToNext()){
//                String phNum = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                String numI = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//
//                //Calling a ContentProviderOperation newUpdate;
//
//
//                Log.e("phN",phNum);
//                Log.e("num",numI);
//
//
//
//
//
//            }
        }

//        while (cursor.moveToNext()){
//
//
//
//            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            String selectionWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//            String[] numberParams = new String[]{contactID, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
//
//
//            String newPhone = "77777";
//
//            ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
//
//
//            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
//
//            builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
//            builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?"+ " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)});
//            builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhone);
//            ops.add(builder.build());



//            contentProviderOperations.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(selectionWhere,numberParams)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,newPhone)
//                    .build()
//            );

//            try {
//                contentResolver.applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            } catch (OperationApplicationException e) {
//                e.printStackTrace();
//            }

            // Update
//            try
//            {
//                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }




//        }

    }


    public void updateContact(String contactId, String number, String numberId) {

        String newNum = newNumber.toString();
        newNumber++;
        try {
            ContentResolver contentResolver = this.getContentResolver();



            String where = ContactsContract.CommonDataKinds.Phone._ID+ " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] numberParams = new String[]{numberId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(where, numberParams)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNum)
                    .build());

            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ru.agrefj.contactschanger/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ru.agrefj.contactschanger/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
