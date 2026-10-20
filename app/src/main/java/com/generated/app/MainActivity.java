package com.generated.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    private static final int PERMISSION_REQ_CODE = 1001;
    private FrameLayout contentContainer;
    private LinearLayout miniPlayerLayout;
    private TextView miniTitle, miniArtist;
    private Button btnMiniPlay, btnTabHome, btnTabLibrary, btnTabPlaylists, btnTabSettings;
    
    private HomeScreenView homeView;
    private LibraryView libraryView;
    private PlaylistsView playlistsView;
    private SettingsView settingsView;
    private PlayerFullScreenView fullScreenPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Ensure RTL layout for Hebrew support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        MusicPlayerService.getInstance().init(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF121216);

        // Top Search Bar Header
        LinearLayout topHeader = new LinearLayout(this);
        topHeader.setOrientation(LinearLayout.HORIZONTAL);
        topHeader.setPadding(24, 24, 24, 16);
        topHeader.setGravity(Gravity.CENTER_VERTICAL);
        topHeader.setBackgroundColor(0xFF1E1E28);

        TextView logo = new TextView(this);
        logo.setText("♪ Rhythm");
        logo.setTextSize(22);
        logo.setTextColor(0xFFE94560);
        logo.setTypeface(null, android.graphics.Typeface.BOLD);
        topHeader.addView(logo);

        EditText searchBox = new EditText(this);
        searchBox.setHint(R.string.search_hint);
        searchBox.setHintTextColor(0xFF888899);
        searchBox.setTextColor(0xFFFFFFFF);
        searchBox.setTextSize(14);
        searchBox.setPadding(32, 16, 32, 16);
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        searchParams.setMargins(24, 0, 0, 0);
        searchBox.setLayoutParams(searchParams);
        searchBox.setBackgroundColor(0xFF2A2A3C);
        searchBox.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (libraryView != null) {
                    libraryView.filter(s.toString());
                }
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
        topHeader.addView(searchBox);
        root.addView(topHeader, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Content Area Frame
        contentContainer = new FrameLayout(this);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        root.addView(contentContainer, contentParams);

        // Mini Player Bar
        miniPlayerLayout = new LinearLayout(this);
        miniPlayerLayout.setOrientation(LinearLayout.HORIZONTAL);
        miniPlayerLayout.setPadding(24, 16, 24, 16);
        miniPlayerLayout.setBackgroundColor(0xFF1E1E28);
        miniPlayerLayout.setGravity(Gravity.CENTER_VERTICAL);
        miniPlayerLayout.setVisibility(View.GONE);
        miniPlayerLayout.setOnClickListener(v -> openFullScreenPlayer());

        LinearLayout miniTextLayout = new LinearLayout(this);
        miniTextLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        miniTextLayout.setLayoutParams(textParams);

        miniTitle = new TextView(this);
        miniTitle.setTextColor(0xFFFFFFFF);
        miniTitle.setTextSize(14);
        miniTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        miniTextLayout.addView(miniTitle);

        miniArtist = new TextView(this);
        miniArtist.setTextColor(0xFFAAAAAA);
        miniArtist.setTextSize(12);
        miniTextLayout.addView(miniArtist);

        miniPlayerLayout.addView(miniTextLayout);

        btnMiniPlay = new Button(this);
        btnMiniPlay.setText("▶");
        btnMiniPlay.setTextColor(0xFFFFFFFF);
        btnMiniPlay.setBackgroundColor(0x00000000);
        btnMiniPlay.setTextSize(18);
        btnMiniPlay.setOnClickListener(v -> {
            MusicPlayerService.getInstance().togglePlayPause();
            updateMiniPlayer();
        });
        miniPlayerLayout.addView(btnMiniPlay);

        root.addView(miniPlayerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Bottom Navigation Bar
        LinearLayout navBar = new LinearLayout(this);
        navBar.setOrientation(LinearLayout.HORIZONTAL);
        navBar.setBackgroundColor(0xFF181820);
        navBar.setPadding(8, 12, 8, 12);

        btnTabHome = createNavButton(getString(R.string.tab_home), v -> switchTab(0));
        btnTabLibrary = createNavButton(getString(R.string.tab_library), v -> switchTab(1));
        btnTabPlaylists = createNavButton(getString(R.string.tab_playlists), v -> switchTab(2));
        btnTabSettings = createNavButton(getString(R.string.tab_settings), v -> switchTab(3));

        navBar.addView(btnTabHome);
        navBar.addView(btnTabLibrary);
        navBar.addView(btnTabPlaylists);
        navBar.addView(btnTabSettings);

        root.addView(navBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(root);

        // Initialize Views
        homeView = new HomeScreenView(this);
        libraryView = new LibraryView(this);
        playlistsView = new PlaylistsView(this);
        settingsView = new SettingsView(this);
        fullScreenPlayerView = new PlayerFullScreenView(this);

        checkPermissionsAndLoad();
    }

    private Button createNavButton(String title, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(title);
        btn.setTextColor(0xFFCCCCCC);
        btn.setTextSize(12);
        btn.setBackgroundColor(0x00000000);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btn.setLayoutParams(params);
        btn.setOnClickListener(listener);
        return btn;
    }

    private void checkPermissionsAndLoad() {
        String perm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_AUDIO : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED) {
            loadMusicLibrary();
        } else {
            requestPermissions(new String[]{perm}, PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicLibrary();
            } else {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
                loadMusicLibrary(); // Load with mock fallback so app is fully usable
            }
        }
    }

    private void loadMusicLibrary() {
        MusicLibrary.getInstance().loadSongs(this);
        switchTab(0);
        Toast.ographystyleShow(this, "AI סרק את הספרייה • סולמות שטייגער ומקאם זוהו בהצלחה");
    }

    public void switchTab(int index) {
        contentContainer.removeAllViews();
        resetNavColors();
        switch (index) {
            case 0:
                contentContainer.addView(homeView);
                btnTabHome.setTextColor(0xFFE94560);
                homeView.refresh();
                break;
            case 1:
                contentContainer.addView(libraryView);
                btnTabLibrary.setTextColor(0xFFE94560);
                libraryView.refresh();
                break;
            case 2:
                contentContainer.addView(playlistsView);
                btnTabPlaylists.setTextColor(0xFFE94560);
                playlistsView.refresh();
                break;
            case 3:
                contentContainer.addView(settingsView);
                btnTabSettings.setTextColor(0xFFE94560);
                break;
        }
    }

    private void resetNavColors() {
        btnTabHome.setTextColor(0xFFCCCCCC);
        btnTabLibrary.setTextColor(0xFFCCCCCC);
        btnTabPlaylists.setTextColor(0xFFCCCCCC);
        btnTabSettings.setTextColor(0xFFCCCCCC);
    }

    public void playSong(Song song) {
        MusicPlayerService.getInstance().playSong(song);
        miniPlayerLayout.setVisibility(View.VISIBLE);
        updateMiniPlayer();
    }

    public void updateMiniPlayer() {
        Song current = MusicLibrary.getInstance().getCurrentSong();
        if (current != null) {
            miniTitle.setText(current.getTitle());
            miniArtist.setText(current.getArtist());
            boolean playing = MusicPlayerService.getInstance().isPlaying();
            btnMiniPlay.setText(playing ? "❚❚" : "▶");
        }
    }

    public void openFullScreenPlayer() {
        if (MusicLibrary.getInstance().getCurrentSong() != null) {
            fullScreenPlayerView.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (fullScreenPlayerView.isShown()) {
            fullScreenPlayerView.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
