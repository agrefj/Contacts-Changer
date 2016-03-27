//package ru.agrefj.contactschanger;
//
//
//import android.app.Activity;
//import android.content.ContentProviderOperation;
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.ContactsContract;
//import android.util.Log;
//
//import java.util.List;
//import java.util.Vector;
//
///**
// * Created by agref on 26.03.2016.
// */
//public class ChangeContactsActivity extends Activity {
//
//
//        while (cursorAll.moveToNext()) {
//            String contactID = cursorAll.getString(cursorAll.getColumnIndex(ContactsContract.Contacts._ID));
////            String phoneNID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
////            Log.e("s1",phoneNID);
//
//
//
//            Cursor phoneNumCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);
//
//            phoneNumCursor.moveToFirst();
//
//            Vector<String> phoneTypeList = new Vector<String>();
//            Vector<String> phoneNumberList = new Vector<String>();
//
//            while (!phoneNumCursor.isAfterLast()) {
//                String cID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                Log.e("cID", contactID);
//                String phoneNID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//                Log.e("s1",phoneNID);
//
//
//                phoneNumCursor.moveToNext();
//            }
//        }
//
////        while (cursor.moveToNext()){
////            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
////            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////            Log.e("name", name);
////            Log.e("number", number);
////
////
////            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
////            String selectionWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
////            String[] numberParams = new String[]{contactID, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
////
////
////            String newPhone = "77777";
////
////            ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
////
////
////            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
////            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
////
////            builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
////            builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?"+ " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)});
////            builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhone);
////            ops.add(builder.build());
//
//
//
////            contentProviderOperations.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
////                    .withSelection(selectionWhere,numberParams)
////                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,newPhone)
////                    .build()
////            );
//
////            try {
////                contentResolver.applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
////            } catch (RemoteException e) {
////                e.printStackTrace();
////            } catch (OperationApplicationException e) {
////                e.printStackTrace();
////            }
//
//        // Update
////            try
////            {
////                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
////            }
////            catch (Exception e)
////            {
////                e.printStackTrace();
////            }
//
//
//
//
////        }
//
//
//
//
//
//
//
//
//}
