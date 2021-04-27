package njan.flashcards;

import android.app.Application;

import njan.flashcards.manager.ThemeManager;

public class FlashCardsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeManager.setThemeFromPreference(this);
    }
}
