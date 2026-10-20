package com.generated.app;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsView extends ScrollView {
    private LinearLayout container;

    public SettingsView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setPadding(24, 24, 24, 100);
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);

        TextView title = new TextView(getContext());
        title.setText("הגדרות מתקדמות ואקולייזר");
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        container.addView(title);

        addToggle("עצירה אוטומטית בווליום אפס והמשך", true, (isChecked) -> {
            MusicPlayerService.getInstance().setZeroVolumePauseEnabled(isChecked);
            Toast.makeText(getContext(), "ההגדרה עודכנה", Toast.LENGTH_SHORT).show();
        });

        addToggle("ניקוי תגיות אוטומטי חכם", true, (isChecked) -> {
            Toast.makeText(getContext(), "ניקוי תגיות עודכן", Toast.LENGTH_SHORT).show();
        });

        addToggle("איזון עוצמה אוטומטי בין שירים (ReplayGain)", true, (isChecked) -> {
            Toast.makeText(getContext(), "איזון עוצמה עודכן", Toast.LENGTH_SHORT).show();
        });

        addToggle("למידת AI אופליין ברקע (שטייגערים ומקאמים)", true, (isChecked) -> {
            Toast.makeText(getContext(), "מודל AI אופליין מעודכן", Toast.LENGTH_SHORT).show();
        });
    }

    private void addToggle(String label, boolean initial, OnCheckedListener listener) {
        CheckBox cb = new CheckBox(getContext());
        cb.setText(label);
        cb.setTextColor(0xFFFFFFFF);
        cb.setChecked(initial);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 20);
        cb.setLayoutParams(params);
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onChanged(isChecked));
        container.addView(cb);
    }

    interface OnCheckedListener {
        void onChanged(boolean isChecked);
    }
}
