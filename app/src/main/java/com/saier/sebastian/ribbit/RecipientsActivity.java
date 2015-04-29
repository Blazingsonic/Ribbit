package com.saier.sebastian.ribbit;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RecipientsActivity extends ActionBarActivity {

    public static final String TAG = RecipientsActivity.class.getSimpleName();

    private List<ParseUser> mFriends;
    private ParseRelation<ParseUser> mFriendsRelation;
    private ParseUser mCurrentUser;

    @InjectView(R.id.recipientsListView) ListView mListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----- Show ProgressBar -----

        setContentView(R.layout.activity_recipients);
        ButterKnife.inject(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.isSelected() == true) { // Condition is always wrong
                    // Add friend
                    mFriendsRelation.add(mFriends.get(position)); // Frontend
                }
                else {
                    // Remove friend
                    mFriendsRelation.add(mFriends.get(position));
                    //mFriendsRelation.remove(mUsers.get(position));
                }

                mCurrentUser.saveInBackground(new SaveCallback() { // Backend
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        });
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
