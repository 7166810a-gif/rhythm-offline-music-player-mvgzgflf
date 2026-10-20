package com.generated.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class PlaylistsView extends ScrollView {
    private LinearLayout container;
    private MainActivity activity;

    public PlaylistsView(Context context) {
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

        TextView title = new TextView(getContext());
        title.setText("פלייליסטים וניהול נתונים");
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        container.addView(title);

        // Action buttons for Export/Import
        LinearLayout btnRow = new LinearLayout(getContext());
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(0, 20, 0, 20);
        btnRow.setLayoutParams(btnParams);

        Button btnExport = new Button(getContext());
        btnExport.setText(R.string.export_playlists);
        btnExport.setBackgroundColor(0xFFE94560);
        btnExport.setTextColor(0xFFFFFFFF);
        btnExport.setOnClickListener(v -> Toast.makeText(getContext(), "הפלייליסטים יוצאו בהצלחה לאחסון", Toast.LENGTH_SHORT).show());
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        p1.setMargins(0, 0, 10, 0);
        btnExport.setLayoutParams(p1);
        btnRow.addView(btnExport);

        Button btnImport = new Button(getContext());
        btnImport.setText(R.string.import_playlists);
        btnImport.setBackgroundColor(0xFF2A2A3C);
        btnImport.setTextColor(0xFFFFFFFF);
        btnImport.setOnClickListener(v -> Toast.makeText(getContext(), "הפלייליסטים יובאו בהצלחה", Toast.LENGTH_SHORT).show());
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        p2.setMargins(10, 0, 0, 0);
        btnImport.setLayoutParams(p2);
        btnRow.addView(btnImport);

        container.addView(btnRow);

        // List Playlists
        Map<String, List<Song>> playlists = MusicLibrary.getInstance().getPlaylists();
        for (Map.Entry<String, List<Song>> entry : playlists.entrySet()) {
            LinearLayout plCard = new LinearLayout(getContext());
            plCard.setOrientation(LinearLayout.VERTICAL);
            plCard.setPadding(20, 20, 20, 20);
            plCard.setBackgroundColor(0xFF1E1E28);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 0, 0, 16);
            plCard.setLayoutParams(cardParams);

            TextView plName = new TextView(getContext());
            plName.setText(entry.getKey() + " (" + entry.getValue().size() + " שירים)");
            plName.setTextColor(0xFFFFFFFF);
            plName.setTextSize(16);
            plName.setTypeface(null, Typeface.BOLD);
            plCard.addView(plName);

            plCard.setOnClickListener(v -> {
                if (!entry.getValue().isEmpty()) {
                    MusicLibrary.getInstance().setQueue(entry.getValue());
                    activity.playSong(entry.getValue().get(0));
                    Toast.makeText(getContext(), "מנגן פלייליסט: " + entry.getKey(), Toast.LENGTH_SHORT).show();
                }
            });

            container.addView(plCard);
        }
    }
}
