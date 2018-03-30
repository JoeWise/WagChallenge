package com.joe_wise.wagchallenge;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LoadJSONTask extends AsyncTask<String, Void, Response> {

    private Listener mListener;

    public LoadJSONTask(Listener listener) {

        mListener = listener;
    }

    public interface Listener {
        //Defines a Listener interface for a our MainActivity to
        // implement so it knows when JSON data is done loading
        void onLoaded(ArrayList<ResultsItem> resultsList, boolean hasMore);
        void onError();
    }

    @Override
    protected Response doInBackground(String... strings) {
        //Load our JSON data Asynchronously

        try
        {
            //Load JSON as string
            String stringResponse = loadJSON(strings[0]);
            JSONObject json_obj;

            try
            {
                //Convert JSON String to JSONObject
                json_obj = new JSONObject(stringResponse);

                //Create new Response object
                Response response = new Response();

                //Get JSON Array titled "items", which is where the user data is
                JSONArray data = json_obj.getJSONArray("items");

                //Iterate through JSONArray, pull out and store relevant data
                for (int i=0; i < data.length(); i++)
                {
                    ResultsItem ri = new ResultsItem();

                    JSONObject result = data.getJSONObject(i);
                    ri.name = result.getString("display_name");
                    ri.pic_url = result.getString("profile_image");

                    JSONObject badge_counts = result.getJSONObject("badge_counts");
                    ri.bronze = Integer.toString(badge_counts.getInt("bronze"));
                    ri.silver = Integer.toString(badge_counts.getInt("silver"));
                    ri.gold = Integer.toString(badge_counts.getInt("gold"));

                    response.addResult(ri);
                }

                //Get "has_more" boolean for pagination
                response.setHasMore(json_obj.getBoolean("has_more"));

                return response;
            }
            catch (Throwable t)
            {
                Log.e("WagChallenge", "Could not parse malformed JSON: \"" + stringResponse + "\"");
                return null;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(Response response) {

        //After JSON task has been executed, called proper method in Listener to let it know the status
        if (response != null)
              mListener.onLoaded(response.getResults(), response.getHasMore());
        else
            mListener.onError();
    }

    private String loadJSON(String jsonURL) throws IOException {

        //Open a connection and load JSON response from server as a String

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();
        return response.toString();
    }
}