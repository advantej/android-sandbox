package com.advantej.mytests;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.widget.TextView;

import java.util.ArrayList;

public class MyContacts extends Activity
{
    TextView mInfoView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mInfoView = (TextView) findViewById(R.id.txt_info_view);

        MyContactsTask contactsTask = new MyContactsTask();
        contactsTask.execute();
    }

    private class MyContactsTask extends AsyncTask<Void, String, String>
    {

        @Override
        protected String doInBackground(Void... voids) {


            Cursor c = getContentResolver().query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            //String count = String.valueOf(c.getCount());

            String tag = "Foo";

            int count = 0;
            //c.moveToFirst();
            ArrayList<ContentProviderOperation> opsNote =
                    new ArrayList<ContentProviderOperation>();

            ArrayList<ContentProviderOperation> opsName =
                    new ArrayList<ContentProviderOperation>();

            while(c.moveToNext() != false){
                int colIdx = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
                String currentContactID = c.getString(c.getColumnIndex(ContactsContract.RawContacts._ID));
                String name = c.getString(colIdx);

                if(name != null && name.startsWith(tag))
                {
                    count++;

                    /*
                    // Get Middle Name and set it as first name in Data table
                    Cursor dataCur = getContentResolver().query(
                            ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.RAW_CONTACT_ID + "=?",
                            new String[] {currentContactID},
                            null);

                    dataCur.moveToFirst();

                    if (dataCur != null)
                    {
                        int idx = dataCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME);
                        String acquiredMiddleName = dataCur.getString(idx);
                        if(acquiredMiddleName != null && !"".equals(acquiredMiddleName)){
                            opsName.add(ContentProviderOperation.newUpdate(
                                    ContactsContract.Data.CONTENT_URI)
                                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME + "=?", new String[]{currentContactID, tag})
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, acquiredMiddleName)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, "")
                                    .build());
                        }

                    }

                    dataCur.close();
                    */


                    // Update Display Name in RawContacts
                    name = name.substring(tag.length() + 1);
                    opsName.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.RawContacts._ID + "=?", new String[]{currentContactID})
                    .withValue(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, name.trim())
                    .build());


                    // Put a note
                    opsNote.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValue(ContactsContract.Data.RAW_CONTACT_ID, currentContactID)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Note.NOTE, tag)
                            .build());
                }

                //c.moveToNext();
            }

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsNote);
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsName);
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (OperationApplicationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            c.close();

            String countStr = String.valueOf(count);
            return countStr;
        }

        @Override
        protected void onPostExecute(String count) {
            super.onPostExecute(count);
            mInfoView.setText("Count returned : " + count);
        }
    }
}
