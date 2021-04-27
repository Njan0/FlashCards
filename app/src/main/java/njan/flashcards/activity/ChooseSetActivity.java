package njan.flashcards.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import njan.flashcards.CardSet;
import njan.flashcards.CardSetAdapter;
import njan.flashcards.manager.CardSetManager;
import njan.flashcards.R;

public class ChooseSetActivity extends AppCompatActivity {
    private List<CardSet> sets;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_set);

        sets = CardSetManager.getInstance(this).getSets();

        rView = findViewById(R.id.recyclerViewCardSets);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(new CardSetAdapter(this, sets));
    }

    @Override
    protected void onResume() {
        super.onResume();
        rView.getAdapter().notifyDataSetChanged();
    }

    public void chooseSet(View view) {
        int chosenSet = ((CardSetAdapter) rView.getAdapter()).getSelected();

        if (chosenSet >= 0 && chosenSet < sets.size()) {
            Intent intent = new Intent(this, CardsActivity.class);
            intent.putExtra(CardsActivity.SET_MESSAGE, sets.get(chosenSet));
            startActivity(intent);
        }
    }

    public void importSet(View view) {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
    }
}