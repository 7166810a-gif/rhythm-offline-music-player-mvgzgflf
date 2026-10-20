package com.generated.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicLibrary {
    private static MusicLibrary instance;
    private List<Song> allSongs;
    private List<Song> likedSongs;
    private List<Song> queue;
    private Map<String, List<Song>> playlists;
    private Song currentSong;
    private boolean isInitialized = false;

    private MusicLibrary() {
        allSongs = new ArrayList<>();
        likedSongs = new ArrayList<>();
        queue = new ArrayList<>();
        playlists = new HashMap<>();
        playlists.put("האהובים שלי", likedSongs);
    }

    public static synchronized MusicLibrary getInstance() {
        if (instance == null) {
            instance = new MusicLibrary();
        }
        return instance;
    }

    public void loadSongs(Context context) {
        if (isInitialized && !allSongs.isEmpty()) return;
        allSongs.clear();

        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = resolver.query(uri, projection, selection, null, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null) {
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleCol = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idCol);
                String title = cursor.getString(titleCol);
                String artist = cursor.getString(artistCol);
                String album = cursor.getString(albumCol);
                long duration = cursor.getLong(durCol);
                String data = cursor.getString(dataCol);
                long albumId = cursor.getLong(albumIdCol);

                if (title == null) title = "ללא כותרת";
                if (artist == null || artist.equals("<unknown>")) artist = "אמן לא ידוע";
                if (album == null) album = "אלבום כללי";

                Song song = new Song(id, title, artist, album, duration, data, albumId);
                
                // Smart deduplication check
                boolean duplicate = false;
                for (Song existing : allSongs) {
                    if (existing.getTitle().equalsIgnoreCase(song.getTitle()) && 
                        Math.abs(existing.getDuration() - song.getDuration()) < 2000) {
                        duplicate = true;
                        break;
                    }
                }
                
                if (!duplicate) {
                    allSongs.add(song);
                }
            }
            cursor.close();
        }

        // Fallback mock songs if device is empty so UI shines immediately
        if (allSongs.isEmpty()) {
            allSongs.add(new Song(1, "ניגון חב''ד עתיק (הופעה חיה)", "אברהם פריד", "ניגוני חג", 240000, "", 101));
            allSongListAddMock(2, "מזמור לדוד - שטייגער אהבה רבה", "מרדכי בן דוד", "מבחר יהודי", 210000, "", 102);
            allSongListAddMock(3, "מקאם חיג''אז מיוחד", "יניב בן משיח", "שירת הבקשות", 300000, "", 103);
            allSongListAddMock(4, "רמיקס חסידי אנרגטי 2024", "דיג''יי מוישי", "רמיקסים חמים", 190000, "", 104);
            allSongListAddMock(5, "שיר ישראלי שקט", "שלמה ארצי", "שירים של פעם", 225000, "", 105);
            allSongListAddMock(6, "תפילת מגן אבות", "יצחק מאיר", "שבת המלכה", 280000, "", 106);
        }

        if (queue.isEmpty()) {
            queue.addAll(allSongs);
        }
        isInitialized = true;
    }

    private void allSongListAddMock(long id, String title, String artist, String album, long dur, String path, long albumId) {
        Song s = new Song(id, title, artist, album, dur, path, albumId);
        allSongs.add(s);
    }

    public List<Song> getAllSongs() { return allSongs; }
    public List<Song> getQueue() { return queue; }
    public void setQueue(List<Song> newQueue) { this.queue = newQueue; }
    public Song getCurrentSong() { return currentSong; }
    public void setCurrentSong(Song song) { this.currentSong = song; }
    public List<Song> getLikedSongs() { return likedSongs; }
    public Map<String, List<Song>> getPlaylists() { return playlists; }

    public List<Song> getQuickPicks() {
        List<Song> list = new ArrayList<>(allSongs);
        Collections.shuffle(list);
        return list.size() > 6 ? list.subList(0, 6) : list;
    }

    public List<Song> getLiveAndRemixes() {
        List<Song> list = new ArrayList<>();
        for (Song s : allSongs) {
            if (s.isLiveOrRemix()) list.add(s);
        }
        if (list.isEmpty() && !allSongs.isEmpty()) {
            list.add(allSongs.get(0));
        }
        return list;
    }

    public List<Song> getTopArtistSongs() {
        if (allSongs.isEmpty()) return new ArrayList<>();
        String topArtist = allSongs.get(0).getArtist();
        List<Song> list = new ArrayList<>();
        for (Song s : allSongs) {
            if (s.getArtist().equalsIgnoreCase(topArtist)) {
                list.add(s);
            }
        }
        return list;
    }
}
