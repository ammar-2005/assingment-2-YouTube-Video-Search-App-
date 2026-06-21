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

    // بيانات الـ API المحددة في الواجب
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
                // 1. معالجة حالة الإدخال الفارغ
                Toast.makeText(MainActivity.this, "Please enter a search term!", Toast.LENGTH_SHORT).show();
            } else {
                // بدء عملية الاتصال بالخلفية
                new YouTubeSearchTask().execute(query);
            }
        });
    }

    // كلاس الـ AsyncTask الاحترافي لعمل الاتصال بعيداً عن الواجهة الرئيسية
    private class YouTubeSearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE); // إظهار مؤشر التحميل
            videoList.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params) {
            String query = params[0];
            // بناء الرابط المرفق بالواجب مع استبدال القيم
            String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                    + query + "&maxResults=15&key=" + API_KEY;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString(); // إرجاع نص الـ JSON
                } else {
                    return "ERROR_API_KEY"; // معالجة الـ Key الخاطئ أو خطأ السيرفر
                }

            } catch (Exception e) {
                Log.e("YouTubeAPI", "Exception: " + e.getMessage());
                return "ERROR_NETWORK"; // معالجة انقطاع الشبكة
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE); // إخفاء مؤشر التحميل

            if (result == null || result.equals("ERROR_NETWORK")) {
                // 2. معالجة خطأ الشبكة
                Toast.makeText(MainActivity.this, "Network failure. Check internet connection.", Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("ERROR_API_KEY")) {
                // 3. معالجة خطأ مفتاح الـ API
                Toast.makeText(MainActivity.this, "Invalid or missing API key.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                // تحليل الـ JSON المسترجع من يوتيوب
                JSONObject jsonObject = new JSONObject(result);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                if (itemsArray.length() == 0) {
                    // 4. معالجة حالة عدم وجود نتائج
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

                    // جلب رابط الصورة عالية الجودة
                    String thumbnailUrl = snippet.getJSONObject("thumbnails")
                            .getJSONObject("high").getString("url");

                    // إضافة الفيديو للمجموعة
                    videoList.add(new VideoModel(title, description, channelTitle, publishTime, thumbnailUrl));
                }

                // تحديث الـ RecyclerView لعرض البيانات فوراُ
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
