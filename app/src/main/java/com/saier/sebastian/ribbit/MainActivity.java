package com.saier.sebastian.ribbit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String[] mCameraOptions;
    private FragmentTabHost mTabHost;

    @InjectView(R.id.editFriendsButton) Button mEditFriendsButton;
    @InjectView(R.id.logoutButton) Button mLogoutButton;
    @InjectView(R.id.cameraImageView) ImageView mCameraImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Check if logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, currentUser.getUsername());
        }
        else {
            //navigateToLogin();
        }

        mEditFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditFriendsActivity.class);
                startActivity(intent);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                navigateToLogin();
            }
        });

        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraOptions = getResources().getStringArray(R.array.cameraOptions);
                CameraOptionsDialogFragment dialog = CameraOptionsDialogFragment
                        .newInstance(mCameraOptions); // Why getString?
                dialog.show(getFragmentManager(), "error_dialog");
            }
        });
    }


    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Add it to the Gallery

            if (requestCode == CameraOptionsDialogFragment.CHOOSE_PICTURE_REQUEST ||
                    requestCode == CameraOptionsDialogFragment.CHOOSE_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, "Sorry, there was an error", Toast.LENGTH_LONG).show();
                }
                else {
                    CameraOptionsDialogFragment.mMediaUri = data.getData();
                }

                Log.i(TAG, "Media URI: " + CameraOptionsDialogFragment.mMediaUri);
                if (requestCode == CameraOptionsDialogFragment.CHOOSE_VIDEO_REQUEST) {
                    // make sure the file is less than 10MB
                    int fileSize = 0;
                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(CameraOptionsDialogFragment.mMediaUri);
                        fileSize = inputStream.available();
                    }
                    catch (FileNotFoundException e) {
                        Toast.makeText(this, "Sorry, there was an error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e) {
                        Toast.makeText(this, "Sorry, there was an error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally { // Always gets excecuted
                        try {
                            inputStream.close();
                        }
                        catch (IOException e) {
                            // This is intentionally blank
                        }
                    }

                    if (fileSize >= CameraOptionsDialogFragment.FILE_SIZE_LIMIT) {
                        Toast.makeText(this, "The selected file is too large! Select a new file.", Toast.LENGTH_LONG);
                        return;
                    }
                }
            }
            else {
                Log.e(TAG, "the results are ok");
                Intent mediaScantIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScantIntent.setData(CameraOptionsDialogFragment.mMediaUri);
                sendBroadcast(mediaScantIntent);
            }

            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(CameraOptionsDialogFragment.mMediaUri);
            startActivity(recipientsIntent);
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Needs to be optimized in this code
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigateToLogin();
        }

        return super.onOptionsItemSelected(item);
    }
}
