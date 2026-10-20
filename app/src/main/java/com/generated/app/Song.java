package com.generated.app;

import java.io.Serializable;

public class Song implements Serializable {
    private long id;
    private String title;
    private String artist;
    private String album;
    private long duration;
    private String dataPath;
    private long albumId;
    
    // AI and Analysis metrics
    private float energy;
    private int estimatedScale; // e.g. Major, Minor, AhavaRaba, Hijaz, Rast
    private int playCount;
    private boolean isLiked;
    private boolean isDisliked;
    private boolean isLiveOrRemix;
    private String lyrics;
    private String correctedArtist;
    private String correctedTitle;

    public Song(long id, String title, String artist, String album, long duration, String dataPath, long albumId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.dataPath = dataPath;
        this.albumId = albumId;
        
        // Default AI heuristic estimations based on string hashes for offline stability
        this.energy = ((Math.abs(title.hashCode()) % 100) / 100f);
        this.estimatedScale = Math.abs(title.hashCode()) % 8; // 8 scale types including Jewish/Makam
        this.playCount = 0;
        this.isLiked = false;
        this.isDisliked = false;
        
        String lower = title.toLowerCase() + " " + artist.toLowerCase();
        this.isLiveOrRemix = lower.contains("live") || lower.contains("הופעה") || lower.contains("remix") || lower.contains("רמיקס") || lower.contains("קאבר") || lower.contains("cover");
        
        this.correctedTitle = title;
        this.correctedArtist = artist;
        this.lyrics = "מילות השיר מסתנכרנות אוטומטית...\n\nברוכים הבאים ל-Rhythm\nהנגן החכם המנתח מוזיקה יהודית ומזרחית\nשטייגערים ומקאמים ברמת המכשיר\n\nמנגן אופליין באיכות גבוהה.";
    }

    public long getId() { return id; }
    public String getTitle() { return correctedTitle != null ? correctedTitle : title; }
    public String getArtist() { return correctedArtist != null ? correctedArtist : artist; }
    public String getAlbum() { return album; }
    public long getDuration() { return duration; }
    public String getDataPath() { return dataPath; }
    public long getAlbumId() { return albumId; }
    
    public float getEnergy() { return energy; }
    public int getEstimatedScale() { return estimatedScale; }
    public int getPlayCount() { return playCount; }
    public void incrementPlayCount() { this.playCount++; }
    
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; if(liked) isDisliked = false; }
    
    public boolean isDisliked() { return isDisliked; }
    public void setDisliked(boolean disliked) { isDisliked = disliked; if(disliked) isLiked = false; }
    
    public boolean isLiveOrRemix() { return isLiveOrRemix; }
    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }
    
    public void setCorrectedMetadata(String title, String artist) {
        this.correctedTitle = title;
        this.correctedArtist = artist;
    }
}
