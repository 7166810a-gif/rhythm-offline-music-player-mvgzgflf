package com.generated.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LibraryView extends ScrollView {
    private LinearLayout container;
    private MainActivity activity;
    private String currentFilter = "";

    public LibraryView(Context context) {
        super(context);
        this.activity = (MainActivity) context;
        init();
    }

    private void init() {
        setPadding(24, 24, 24, 100);
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);
        refresh();
    }

    public void refresh() {
        renderList(MusicLibrary.getInstance().getAllSongs());
    }

    public void filter(String query) {
        this.currentFilter = query.toLowerCase();
        List<Song> all = MusicLibrary.getInstance().getAllSongs();
        LinearLayout listLayout = (LinearLayout) container.getChildAt(0);
        if (listLayout == null) return;
        listLayout.removeAllViews();

        for (Song song : all) {
            if (currentFilter.isEmpty() || 
                song.getTitle().toLowerCase().contains(currentFilter) ||
                song.getArtist().toLowerCase().contains(currentFilter) ||
                song.getAlbum().toLowerCase().contains(currentFilter) ||
                song.getLyrics().toLowerCase().contains(currentFilter)) {
                
                listLayout.addView(createSongRow(song));
            }
        }
    }

    private void renderList(List<Song> songs) {
        container.removeAllViews();

        TextView title = new TextView(getContext());
        title.setText("ספריית המוזיקה במכשיר");
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 0, 0, 24);
        title.setLayoutParams(p);
        container.addView(title);

        LinearLayout listLayout = new LinearLayout(getContext());
        listLayout.setOrientation(LinearLayout.VERTICAL);

        for (Song song : songs) {
            listLayout.addView(createSongRow(song));
        }

        container.addView(listLayout);
    }

    private View createSongRow(Song song) {
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(16, 16, 16, 16);
        row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        row.setLayoutParams(params);
        row.setBackgroundColor(0xFF1A1A22);
        row.setOnClickListener(v -> {
            activity.playSong(song);
            Toast.makeText(getContext(), "מנגן: " + song.getTitle(), Toast.LENGTH_SHORT).show();
        });

        LinearLayout info = new LinearLayout(getContext());
        info.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        info.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(song.getTitle());
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTextSize(14);
        tvTitle.setTypeface(null, Typeface.BOLD);
        info.addView(tvTitle);

        TextView tvArtist = new TextView(getContext());
        tvArtist.setText(song.getArtist() + " • " + song.getAlbum());
        tvArtist.setTextColor(0xFFAAAAAA);
        tvArtist.setTextSize(12);
        info.addView(tvArtist);

        row.addView(info);

        // Three dots menu button
        TextView menuBtn = new TextView(getContext());
        menuBtn.setText("⋮");
        menuBtn.setTextSize(20);
        menuBtn.setTextColor(0xFFCCCCCC);
        menuBtn.setPadding(20, 10, 20, 10);
        menuBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "תפריט פעולות עבור " + song.getTitle(), Toast.LENGTH_SHORT).show();
        });
        row.addView(menuBtn);

        return row;
    }
}
