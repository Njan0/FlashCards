package njan.flashcards.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import njan.flashcards.R;

public abstract class ThemeManager {
    public static boolean setTheme(Context context, String themePreference) {
        if (themePreference.equals(context.getString(R.string.dark_theme_preference_value))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (themePreference.equals(context.getString(R.string.light_theme_preference_value))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (themePreference.equals(context.getString(R.string.system_theme_preference_value))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            return false;
        }

        return true;
    }

    public static String getThemePreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.theme_preferences_key), context.getString(R.string.system_theme_preference_value));
    }

    public static void setThemeFromPreference(Context context) {
        setTheme(context, getThemePreference(context));
    }
}
