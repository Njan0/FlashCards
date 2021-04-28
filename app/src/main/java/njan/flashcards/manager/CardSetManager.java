package njan.flashcards.manager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import njan.flashcards.CardSet;

public class CardSetManager {
    private static final String SETS_FILE_NAME = "sets.data";
    private static List<CardSet> sets;

    private final Context context;

    private CardSetManager(Context context) {
        this.context = context;
        if (sets == null) {
            initSets(context);
        }
    }

    public static CardSetManager getInstance(Context context) {
        return new CardSetManager(context);
    }

    public List<CardSet> getSets() {
        return Collections.unmodifiableList(sets);
    }

    public void addSet(CardSet set) {
        sets.add(set);
        saveCardSet(context);
    }

    public void removeSet(int index) {
        sets.remove(index);
        saveCardSet(context);
    }

    public void renameSet(int index, String newName) {
        sets.get(index).setName(newName);
        saveCardSet(context);
    }

    private static void initSets(Context context) {
        sets = new ArrayList<>();

        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        if (!setsFile.exists()) {
            // store empty set
            saveCardSet(context);
        } else {
            // load sets
            loadCardSet(context);
        }
    }

    private static void saveCardSet(Context context) {
        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        try {
            FileOutputStream fileStream = new FileOutputStream(setsFile);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(sets);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            Log.e("Import", "Save Set failed", e);
        }
    }

    private static void loadCardSet(Context context) {
        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        try {
            FileInputStream fileStream = new FileInputStream(setsFile);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            //noinspection unchecked
            sets = (List<CardSet>) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (IOException | ClassNotFoundException e) {
            Log.e("Import", "Load Set failed", e);
        }
    }
}
