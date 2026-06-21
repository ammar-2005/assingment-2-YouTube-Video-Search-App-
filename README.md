# 📱 YouTube Video Search App

An Android application developed as part of the **Mobile App Development 2** course. The app connects to the **YouTube Data API** to allow users to search for videos and view structured results in real-time.

---

## 🎯 Features
- **User Input Search:** Enter any query to fetch matching YouTube videos.
- **Real-time Data Fetching:** Integrates with YouTube API using `HttpURLConnection` and `AsyncTask` for background network operations.
- **Dynamic List Display:** Uses `RecyclerView` with an optimized `ViewHolder` pattern to display:
  - Video Title
  - Description
  - Channel Title
  - Publish Time
  - Video Thumbnail Image
---
## 🚀 Setup & Installation Steps 
1. **Clone the repository** to your local machine.
2. **Open Android Studio** and select **File -> Open**, then choose the project folder.
3. Wait for the **Gradle sync** to complete successfully.
4. Run the application on an **Emulator** or a physical device.

---

## 📱 How to Use the App 
1. **Enter a Search Query:** Type any topic or video title in the search input field at the top.
2. **Trigger Search:** Click the **Search** button to send a request to the YouTube API.
3. **Browse Results:** Scroll through the vertical `RecyclerView` to see video titles, descriptions, channel names, and publication dates.
4. **Error Feedback:** If you clear the text and press search, or turn off the internet, you will see a clear error message (`Toast`) as required.
