package com.saier.sebastian.ribbit.ui;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.saier.sebastian.ribbit.FileHelper;
import com.saier.sebastian.ribbit.ParseConstants;
import com.saier.sebastian.ribbit.R;
import com.saier.sebastian.ribbit.ui.fragments.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RecipientsActivity extends ActionBarActivity {

    public static final String TAG = RecipientsActivity.class.getSimpleName();

    private List<ParseUser> mFriends;
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;
    private Uri mMediaUri;
    private String mFileType;

    @InjectView(R.id.recipientsListView) ListView mListView;
    @InjectView(R.id.sendButton) Button mSendButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----- Show ProgressBar -----

        setContentView(R.layout.activity_recipients);
        ButterKnife.inject(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSendButton.setVisibility(View.VISIBLE);

                ParseObject message = createMessage();
                if (message == null) {
                    // Error
                    AlertDialogFragment dialog = AlertDialogFragment
                            .newInstance(getString(R.string.error_selecting_file)); // Why getString?
                    dialog.show(getFragmentManager(), "error_dialog");
                }
                else {
                    send(message);
                    finish(); // Toasts live across activity changes
                }
            }
        });

        mMediaUri = getIntent().getData(); // get data from MainActivity
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE); // get extra from MainActivity
    }


    @Override
    public void onResume() {
        super.onResume();
        ButterKnife.inject(this);

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    mFriends = parseUsers;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>( // Make it custom
                            RecipientsActivity.this,
                            android.R.layout.simple_list_item_single_choice,
                            usernames);
                    mListView.setAdapter(adapter);
                }
                else {
                    Log.e(TAG, e.getMessage());
                    AlertDialogFragment dialog = AlertDialogFragment
                            .newInstance(getString(R.string.friends_list_error_message)); // Why getString?
                    //dialog.show(getFragmentManager(), "error_dialog");
                }
            }
        });
    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if (fileBytes == null) {
            return null;
        }
        else {
            if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }

            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, file);

            return message;
        }
    }

    private ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<String>();
        for (int i = 0; i < mListView.getCount(); i++) {
            if (mListView.isItemChecked(i)) { // This condition doesn't seem to work properly
                recipientIds.add(mFriends.get(i).getObjectId());
            }
            else {
                recipientIds.add(mFriends.get(i).getObjectId()); // In this case every id gets sent to Parse
            }
        }
        return recipientIds;
    }

    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Toast.makeText(RecipientsActivity.this, "Message sent!", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialogFragment dialog = AlertDialogFragment
                            .newInstance(getString(R.string.error_sending_message)); // Why getString?
                    dialog.show(getFragmentManager(), "error_dialog");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
