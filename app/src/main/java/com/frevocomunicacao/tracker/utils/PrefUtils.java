package com.frevocomunicacao.tracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.frevocomunicacao.tracker.Constants;

/**
 * Created by Vinicius on 29/01/16.
 */
public class PrefUtils {

    private Context ctx;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public PrefUtils(Context context) {
        this.ctx = context;
        this.getSharedPreferences();
    }

    private void getSharedPreferences() {
        this.prefs = this.ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = this.prefs.edit();
    }

    public String get(String key) {
        return this.prefs.getString(key, "");
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }

    public void put(String key, String value) {
        this.editor.putString(key, value).apply();
    }

    public void put(String key, int value) {
        this.editor.putInt(key, value).apply();
    }

    public boolean isEmpty() {
        if (this.prefs.contains(Constants.PREFS_KEY_USER_ID)) {
            return false;
        }

        return true;
    }

    public void empty() {
        this.editor.clear().apply();
    }

}
