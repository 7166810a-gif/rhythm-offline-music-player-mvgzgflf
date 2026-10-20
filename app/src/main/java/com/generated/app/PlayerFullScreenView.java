package com.generated.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerFullScreenView {
    private Dialog dialog;
    private Context context;
    private TextView tvTitle, tvArtist, tvLyrics, tvScaleInfo;
    private Button btnPlayPause, btnLike;
    private android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable progressRunnable;

    public PlayerFullScreenView(Context context) {
        this.context = context;
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog(context, android.R.style.Theme_Material_Light_NoActionBar);
        
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF121216);
        root.setPadding(32, 48, 32, 48);

        // Header / Close button
        LinearLayout topBar = new LinearLayout(context);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        
        Button btnClose = new Button(context);
        btnClose.setText("✕ סגור");
        btnClose.setTextColor(0xFFFFFFFF);
        btnClose.setBackgroundColor(0x00000000);
        btnClose.setOnClickListener(v -> dismiss());
        topBar.addView(btnClose);
        root.addView(topBar);

        // Song Art Cover placeholder
        LinearLayout coverCard = new LinearLayout(context);
        coverCard.setBackgroundColor(0xFF1E1E28);
        coverCard.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams coverParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 320);
        coverParams.setMargins(0, 32, 0, 32);
        coverCard.setLayoutParams(coverParams);

        TextView coverIcon = new TextView(context);
        coverIcon.setText("♪ RHYTHM AUDIO");
        coverIcon.setTextColor(0xFFE94560);
        coverIcon.setTextSize(18);
        coverIcon.setTypeface(null, Typeface.BOLD);
        coverCard.addView(coverIcon);
        root.addView(coverCard);

        // Title & Artist
        tvTitle = new TextView(context);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTextSize(20);
        tvTitle.setTypeface(null, Typeface.BOLD);
        root.addView(tvTitle);

        tvArtist = new TextView(context);
        tvArtist.setTextColor(0xFFAAAAAA);
        tvArtist.setTextSize(14);
        LinearLayout.LayoutParams artParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        artParams.setMargins(0, 8, 0, 16);
        tvArtist.setLayoutParams(artParams);
        root.addView(tvArtist);

        // AI Scale badge info
        tvScaleInfo = new TextView(context);
        tvScaleInfo.setTextColor(0xFF00FFCC);
        tvScaleInfo.setTextSize(12);
        root.addView(tvScaleInfo);

        // Lyrics View
        ScrollView lyricsScroll = new ScrollView(context);
        LinearLayout.LayoutParams lyricsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
        lyricsParams.setMargins(0, 24, 0, 24);
        lyricsScroll.setLayoutParams(lyricsParams);
        
        tvLyrics = new TextView(context);
        tvLyrics.setTextColor(0xFFDDDDDD);
        tvLyrics.setTextSize(14);
        tvLyrics.setLineSpacing(4, 1.2f);
        lyricsScroll.addView(tvLyrics);
        root.addView(lyricsScroll);

        // Controls Layout
        LinearLayout controls = new LinearLayout(context);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER);

        Button btnPrev = new Button(context);
        btnPrev.setText("⏮");
        btnPrev.setTextColor(0xFFFFFFFF);
        btnPrev.setBackgroundColor(0x00000000);
        btnPrev.setTextSize(22);
        btnPrev.setOnClickListener(v -> Toast.makeText(context, "שיר קודם", Toast.LENGTH_SHORT).show());
        controls.addView(btnPrev);

        btnPlayPause = new Button(context);
        btnPlayPause.setText("❚❚");
        btnPlayPause.setTextColor(0xFFFFFFFF);
        btnPlayPause.setBackgroundColor(0xFFE94560);
        btnPlayPause.setTextSize(20);
        btnPlayPause.setOnClickListener(v -> {
            MusicPlayerService.getInstance().togglePlayPause();
            updateUI();
            if (context instanceof MainActivity) {
                ((MainActivity) context).updateMiniPlayer();
            }
        });
        LinearLayout.LayoutParams ppParams = new LinearLayout.LayoutParams(120, 120);
        ppParams.setMargins(32, 0, 32, 0);
        btnPlayPause.setLayoutParams(ppParams);
        controls.addView(btnPlayPause);

        Button btnNext = new Button(context);
        btnNext.setText("⏭");
        btnNext.setTextColor(0xFFFFFFFF);
        btnNext.setBackgroundColor(0x00000000);
        btnNext.setTextSize(22);
        btnNext.setOnClickListener(v -> Toast.makeText(context, "שיר הבא", Toast.LENGTH_SHORT).show());
        controls.addView(btnNext);

        root.addView(controls);

        dialog.setContentView(root);
    }

    public void show() {
        updateUI();
        dialog.show();
        startProgressLoop();
    }

    public void dismiss() {
        dialog.dismiss();
        handler.removeCallbacks(progressRunnable);
    }

    public boolean isShown() {
        return dialog != null && dialog.isShowing();
    }

    private void updateUI() {
        Song current = MusicLibrary.getInstance().getCurrentSong();
        if (current != null) {
            tvTitle.setText(current.getTitle());
            tvArtist.setText(current.getArtist());
            tvLyrics.setText(current.getLyrics());
            
            String scaleName = "מז'ור / מינור רגיל";
            int s = current.getEstimatedScale();
            if (s == 1) scaleName = "שטייגער: אהבה רבה (פריגיש)";
            else if (s == 2) scaleName = "מקאם: חיג'אז";
            else if (s == 3) scaleName = "מקאם: נהוונד";
            else if (s == 4) scaleName = "שטייגער: מגן אבות";
            else if (s == 5) scaleName = "מקאם: ראסט";
            
            tvScaleInfo.setText("זיהוי AI אופליין • סולם: " + scaleName + " | אנרגיה: " + (int)(current.getEnergy()*100) + "%");
            
            boolean playing = MusicPlayerService.getInstance().isPlaying();
            btnPlayPause.setText(playing ? "❚❚" : "▶");
        }
    }

    private void startProgressLoop() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (isShown()) {
                    updateUI();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(progressRunnable, 1000);
    }
}
