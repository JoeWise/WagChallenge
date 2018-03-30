package com.joe_wise.wagchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadJSONTask.Listener {

    //the main ListView that displays Stack Overflow users
    private ListView mListView;

    //Pagination buttons
    private Button mNextButton;
    private Button mPrevButton;

    //Progress bar to display while results are loading
    private ProgressBar mPB;

    //List of ResultsItems that populate the ListView
    private ArrayList<ResultsItem> mResultsList = new ArrayList<>();

    //Keeps track of our current page for pagination
    private int mCurrPage = 1;
    //Keeps track of the has_more JSON variable returned from Stack Exchange
    private boolean mHasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get references for views
        mListView = findViewById(R.id.results_list);
        mNextButton =  findViewById(R.id.next);
        mPrevButton =  findViewById(R.id.prev);
        mPB =  findViewById(R.id.results_progress);

        //Disable buttons while initial request is loading
        mNextButton.setEnabled(false);
        mPrevButton.setEnabled(false);

        //Start initial JSON request
        String request = constructURL();
        new LoadJSONTask(this).execute(request);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onLoaded(ArrayList<ResultsItem> resultsList, boolean more) {

        //Get list of ResultsItems and has_more from JSON request
        mResultsList = resultsList;
        mHasMore = more;

        //Turn off our progress bar when data is loaded
        mPB.setVisibility(View.GONE);

        loadListView();
        prepButtons();
    }


    @Override
    public void onError()
    {
        //Display toast to alert user to error
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        mPB.setVisibility(View.GONE);

        prepButtons();
    }

    private void loadListView() {
        //Set our ListView's adapter to our custom adapter
        ListAdapter adapter = new ListViewAdapter(MainActivity.this, mResultsList);
        mListView.setAdapter(adapter);

        //Make sure loading animation plays
        mListView.postInvalidate();
    }

    private String constructURL(){
        //Construct API request based on our current page
        return "https://api.stackexchange.com/2.2/users?site=stackoverflow&key=kcn71Vn6EhSX8wzl23vlcQ((&page=" + Integer.toString(mCurrPage);
    }

    public int prevPage(){
        //Decrement and return the current page number
        if(mCurrPage > 1)
            return mCurrPage -= 1;
        else
            return mCurrPage;
    }

    public int nextPage(){
        //Increment and return the current page number
        if(mHasMore)
            return mCurrPage += 1;
        else
            return mCurrPage;
    }

    public void prepButtons(){

        //Set up Prev and Next buttons for pagination

        final LoadJSONTask.Listener l = this;
        final MainActivity m = this;

        //Prev button
        if(mCurrPage <= 1)
        {
            //If we're at the first page, there is no previous page, so disable it
            mPrevButton.setEnabled(false);
        }
        else
        {
            mPrevButton.setEnabled(true);
            mPrevButton.setOnClickListener(new View.OnClickListener() {

                LoadJSONTask.Listener li = l;
                MainActivity ma = m;

                @Override
                public void onClick(View arg0)
                {
                    //When we click, display progress bar and load the previous page of results
                    ProgressBar pb = findViewById(R.id.results_progress);
                    pb.setVisibility(View.VISIBLE);

                    Button next = findViewById(R.id.next);
                    next.setEnabled(false);

                    Button prev = findViewById(R.id.prev);
                    prev.setEnabled(false);

                    ma.prevPage();
                    String prev_url = ma.constructURL();
                    new LoadJSONTask(li).execute(prev_url);
                }
            });
        }


        //Next button
        if(!mHasMore)
        {
            //If there are no more pages, disable next button
            mNextButton.setEnabled(false);
        }
        else
        {
            mNextButton.setEnabled(true);
            mNextButton.setOnClickListener(new View.OnClickListener() {

                LoadJSONTask.Listener li = l;
                MainActivity ma = m;

                @Override
                public void onClick(View arg0)
                {
                    //When we click, display progress bar and load the next page of results
                    ProgressBar pb = findViewById(R.id.results_progress);
                    pb.setVisibility(View.VISIBLE);

                    Button next = findViewById(R.id.next);
                    next.setEnabled(false);

                    Button prev = findViewById(R.id.prev);
                    prev.setEnabled(false);

                    ma.nextPage();
                    String next_url = ma.constructURL();
                    new LoadJSONTask(li).execute(next_url);
                }
            });
        }
    }
}
