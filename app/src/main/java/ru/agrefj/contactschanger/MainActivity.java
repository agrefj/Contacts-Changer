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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    LinearLayout ll_progress;
    TextView tv_done;
    TextView tv_from;

    Integer done = 0;

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



        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                200);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 100
        );


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ll_progress = (LinearLayout)findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.INVISIBLE);


        tv_from = (TextView)findViewById(R.id.tv_from);
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


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }



        if (et_newDigits.getText().toString().length() > 0 && et_digits.getText().toString().length() > 0) {

            ChangeContactsActivity();

        } else {
            Toast.makeText(MainActivity.this, this.getText(R.string.caution), Toast.LENGTH_SHORT).show();

        }
    }



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
        Cursor cursorAll = contentResolver.query(uri,projection,selection,selectionArgs,null);
        int count = cursorAll.getCount();

        tv_from.setText(String.valueOf(count));
        ll_progress.setVisibility(View.VISIBLE);

        if(cursorAll !=null){


            while (cursorAll.moveToNext())
            {
                String contactID = cursorAll.getString(cursorAll.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                String rawID = cursorAll.getString(cursorAll.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phoneNumCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.Contacts._ID + " = ?", new String[]{rawID}, null);

                while (phoneNumCursor.moveToNext()){
                    String phoneNumber = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    String numI = phoneNumCursor.getString(phoneNumCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                    updateContact(rawID,phoneNumber);


                }

                phoneNumCursor.close();


            }
        }



    }


    public void updateContact(String rawID,String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "").replace("),", "").replace("(", "").replace("-", "").replace("—", "");
        Log.e("num",phoneNumber);
        String digits = et_digits.getText().toString();
        int length = digits.length();
        String newDigits = et_newDigits.getText().toString();

        done++;
        String doneNums = String.valueOf(done);
        tv_done = (TextView)findViewById(R.id.tv_done);
        tv_done.setText(doneNums);



        if(phoneNumber.length()>digits.length()){
            String firstDig = phoneNumber.substring(0,length);

            if (firstDig.equals(digits)){
                String newNum = phoneNumber.substring(length);
                newNum = newDigits + newNum;


                try {
                    ContentResolver contentResolver = this.getContentResolver();



                    String where = ContactsContract.Contacts._ID+ " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] numberParams = new String[]{rawID, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
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
