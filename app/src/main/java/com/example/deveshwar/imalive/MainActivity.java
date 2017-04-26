package com.example.deveshwar.imalive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST_CODE = 0;

    private RemindersAdapter adapter;
    private List<Reminder> reminders;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvReminders)
    RecyclerView rvReminders;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);

        // TODO read reminders from db async
        reminders = new ArrayList<>();
        handleEmptyState();

        adapter = new RemindersAdapter(reminders);
        rvReminders.setAdapter(adapter);
        rvReminders.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvReminders.addItemDecoration(itemDecoration);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == PICK_CONTACT_REQUEST_CODE) {
            Uri uri = data.getData();

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            String contactName = cursor.getString(cursor.
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = cursor.getString(cursor.
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactPhoto = cursor.getString(cursor.
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            Intent intent = new Intent(MainActivity.this, ReminderComposerActivity.class);
            intent.putExtra("contactName", contactName);
            intent.putExtra("contactNumber", contactNumber);
            intent.putExtra("contactPhoto", contactPhoto);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            //reminders = realm.where(Reminder.class).findAll();
            // TODO re-read reminders from db
            adapter.notifyDataSetChanged();
            handleEmptyState();
        }
    }

    private void handleEmptyState() {
        if (reminders.size() == 0) {
            findViewById(R.id.empty_state).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty_state).setVisibility(View.GONE);
        }
    }

    public static Intent createIntent(Context context, IdpResponse idpResponse) {
        Intent in = IdpResponse.getIntent(idpResponse);
        in.setClass(context, MainActivity.class);
        return in;
    }
}


