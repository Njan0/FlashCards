package njan.flashcards.manager;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private static CardSetManager INSTANCE;

    private Context context;
    private List<CardSet> sets;

    private CardSetManager(Context context) {
        this.context = context;
        initSets();
    }

    public static CardSetManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CardSetManager(context);
        } else {
            INSTANCE.context = context;
        }
        return INSTANCE;
    }

    public List<CardSet> getSets() {
        return Collections.unmodifiableList(sets);
    }

    public void addSet(CardSet set) {
        sets.add(set);
        saveCardSet();
    }

    public void removeSet(int index) {
        sets.remove(index);
        saveCardSet();
    }

    public void renameSet(int index, String newName) {
        sets.get(index).setName(newName);
        saveCardSet();
    }

    private void initSets() {
        sets = new ArrayList<>();

        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        if (!setsFile.exists()) {
            // store empty set
            saveCardSet();
        } else {
            // load sets
            loadCardSet();
        }
    }

    private void saveCardSet() {
        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        try {
            FileOutputStream fileStream = new FileOutputStream(setsFile);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(sets);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCardSet() {
        File setsFile = new File(context.getFilesDir(), SETS_FILE_NAME);
        try {
            FileInputStream fileStream = new FileInputStream(setsFile);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            sets = (List<CardSet>) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
