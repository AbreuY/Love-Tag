package uk.co.spookypeanut.lovetag;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class TagInput extends ActionBarActivity {
    String mArtist;
    String mTitle;
    LastfmSession mLfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtist = this.getIntent().getStringExtra("artist");
        mTitle = this.getIntent().getStringExtra("title");
        final String tag = "Love&Tag.TagInput.onCreate";
        setContentView(R.layout.activity_tag_input);
        final EditText tagEntry = (EditText) findViewById(R.id.tagInputBox);
        ListView tagListView = (ListView) findViewById(R.id.tagList);
        final ArrayAdapter<String> tagAdaptor;
        final ArrayList<String> tagList = new ArrayList<>();
        tagAdaptor = new ArrayAdapter<> (this,
                android.R.layout.simple_list_item_1, tagList);
        tagListView.setAdapter(tagAdaptor);
//        List<String> existingTags;
//        existingTags = getIntent().getStringArrayListExtra("existing_tags");
//        Log.d(tag, "Existing tags: " + existingTags.toString());
        // This snippet should be used whenever getting a session. It's
        // the most elegant way I can figure out to do this (the only
        // inelegance is duplication of this snippet)
        mLfs = new LastfmSession();
        if (!mLfs.isLoggedIn()) {
            Intent i = new Intent();
            i.setClass(this, LoginActivity.class);
            startActivityForResult(i, getResources().getInteger(
                    R.integer.rc_log_in));
            return;
        }
        tagEntry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                        tagList.add(tagList.size(), tagEntry.getText().toString());
                        tagAdaptor.notifyDataSetChanged();
                        tagEntry.setText("");
                        tagEntry.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
        final Button okButton = (Button) findViewById(R.id.tag_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                Log.d(tag, "Tagging " + mTitle + " with " + tagList.toString());
                resultData.putExtra("tagList", tagList);
                resultData.putExtra("artist", mArtist);
                resultData.putExtra("title", mTitle);
                setResult(RESULT_OK, resultData);
                finish();
            }
        });
        final Button cancelButton = (Button) findViewById(R.id.tag_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "User cancelled tagging");
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        GetExistingCall gec = new GetExistingCall();
        gec.execute();
    }

    private void addExisting(List<String> tag_list) {
        String tag = "Love&Tag.TagInput.addExisting";
        Log.d(tag, tag_list.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_input, menu);
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
    private class GetExistingCall extends AsyncTask<String, String, String> {
        List<String> mTempTags = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {
            String tag = "Love&Tag.TagInput.GetExistingCall.doInBackground";
            mTempTags = mLfs.getTags();
            return "";
        }
        protected void onPostExecute(String result) {
            addExisting(mTempTags);
        }
    }
}
