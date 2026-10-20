package com.generated.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HomeScreenView extends ScrollView {
    private LinearLayout container;
    private MainActivity activity;

    public HomeScreenView(Context context) {
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
        container.removeAllViews();

        // Welcome / Mixes Shelf
        addShelfTitle(getContext().getString(R.string.mixes_shelf));
        addHorizontalSongList(MusicLibrary.getInstance().getAllSongs());

        // Quick Picks
        addShelfTitle(getContext().getString(R.string.quick_picks));
        addHorizontalSongList(MusicLibrary.getInstance().getQuickPicks());

        // Top Artist Shelf
        addShelfTitle(getContext().getString(R.string.top_artist_shelf));
        addHorizontalSongList(MusicLibrary.getInstance().getTopArtistSongs());

        // Live & Remix Shelf
        addShelfTitle(getContext().getString(R.string.live_shelf));
        addHorizontalSongList(MusicLibrary.getInstance().getLiveAndRemixes());
    }

    private void addShelfTitle(String title) {
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setTextSize(18);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(0xFFFFFFFF);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 32, 0, 16);
        tv.setLayoutParams(params);
        container.addView(tv);
    }

    private void addHorizontalSongList(List<Song> songs) {
        HorizontalScrollView hScroll = new HorizontalScrollView(getContext());
        hScroll.setHorizontalScrollBarEnabled(false);

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);

        for (Song song : songs) {
            LinearLayout card = new LinearLayout(getContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(16, 16, 16, 16);
            card.setBackgroundColor(0xFF1E1E28);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(260, 200);
            cardParams.setMargins(0, 0, 16, 0);
            card.setLayoutParams(cardParams);
            card.setOnClickListener(v -> {
                activity.playSong(song);
                Toast.makeText(getContext(), "מנגן: " + song.getTitle(), Toast.LENGTH_SHORT).show();
            });

            TextView title = new TextView(getContext());
            title.setText(song.getTitle());
            title.setTextColor(0xFFFFFFFF);
            title.setTextSize(14);
            title.setTypeface(null, Typeface.BOLD);
            title.setMaxLines(2);
            card.addView(title);

            TextView artist = new TextView(getContext());
            artist.setText(song.getArtist());
            artist.setTextColor(0xFFAAAAAA);
            artist.setTextSize(12);
            LinearLayout.LayoutParams artistParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            artistParams.setMargins(0, 8, 0, 0);
            artist.setLayoutParams(artistParams);
            card.addView(artist);

            row.addView(card);
        }

        hScroll.addView(row);
        container.addView(hScroll);
    }
}
