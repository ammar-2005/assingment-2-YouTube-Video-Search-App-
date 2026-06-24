package com.example.youtubevideosearchapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private ProgressBar progressBar;
    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private List<VideoModel> videoList = new ArrayList<>();

    // بيانات الـ API
    private final String API_KEY = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        rvVideos = findViewById(R.id.rvVideos);

        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(this, videoList);
        rvVideos.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (query.isEmpty()) {

                Toast.makeText(MainActivity.this, "Please enter a search term!", Toast.LENGTH_SHORT).show();
            } else {

                new YouTubeSearchTask().execute(query);
            }
        });
    }
    private class YouTubeSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            videoList.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params) {
            String query = params[0];

            String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                    + query + "&maxResults=15&key=" + API_KEY;

            try {
//                 HttpURLConnection
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    return "ERROR_API_KEY";
                }

            } catch (Exception e) {
                Log.e("YouTubeAPI", "Exception: " + e.getMessage());
                return "ERROR_NETWORK";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);

            if (result == null || result.equals("ERROR_NETWORK")) {

                Toast.makeText(MainActivity.this, " Check internet connection.", Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("ERROR_API_KEY")) {

                Toast.makeText(MainActivity.this, "Invalid or missing API key.", Toast.LENGTH_LONG).show();
                return;
            }

            try {

//                  JSON Object

                JSONObject jsonObject = new JSONObject(result);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                if (itemsArray.length() == 0) {

                    Toast.makeText(MainActivity.this, "No results found for this query.", Toast.LENGTH_LONG).show();
                    return;
                }

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject item = itemsArray.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");

                    String title = snippet.getString("title");
                    String description = snippet.getString("description");
                    String channelTitle = snippet.getString("channelTitle");
                    String publishTime = snippet.getString("publishedAt");

                    String thumbnailUrl = snippet.getJSONObject("thumbnails")
                            .getJSONObject("high").getString("url");

                    videoList.add(new VideoModel(title, description, channelTitle, publishTime, thumbnailUrl));
                }

                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
