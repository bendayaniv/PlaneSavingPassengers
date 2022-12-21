package com.example.planesavingpassengers.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.planesavingpassengers.Models.ScoreList;
import com.google.gson.Gson;

public class MySP {

    ////////////////////////////////////////////////////////////////////////////////////
    private static final String DB_FILE = "DB_FILE";

    private static final String SP_KEY_SCORELIST = "SP_KEY_SCORELIST";
    ////////////////////////////////////////////////////////////////////////////////////

    private static MySP instance = null;
    private static SharedPreferences preferences;

    private MySP(Context context) {
        preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void initMySP(Context context) {
        if (instance == null) {
            instance = new MySP(context);
        }
    }

    public static MySP getInstance() {
        return instance;
    }

    public static void saveToSP(ScoreList scoreList) {
        String playersListJson = new Gson().toJson(scoreList);
        getInstance().putString(SP_KEY_SCORELIST, playersListJson);
    }

    public static ScoreList loadFromSP(Class<ScoreList> scoreListClass, ScoreList scoreList) {
        String playersListAsJsonStringFromSP = getInstance().getString(SP_KEY_SCORELIST, "");

        if (!playersListAsJsonStringFromSP.isEmpty()) {
            scoreList = new Gson().fromJson(playersListAsJsonStringFromSP, scoreListClass);
        }
        return scoreList;
    }

    public void putInt(String key, int value) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static void clearAll() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
