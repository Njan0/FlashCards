package njan.flashcards.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.ViewGroup;

import java.util.List;

import njan.flashcards.CardFragment;
import njan.flashcards.CardSet;
import njan.flashcards.R;

public class CardsActivity extends AppCompatActivity {
    public static final String SET_MESSAGE = "njan.flashcards.SET.MESSAGE";
    private CardSet set;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Intent intent = getIntent();
        set = (CardSet) intent.getSerializableExtra(SET_MESSAGE);

        // setup viewPager
        viewPager = findViewById(R.id.viewPagerCards);
        CardPagerAdapter pagerAdapter = new CardPagerAdapter(this, set.generateCards(), set.getSize());
        viewPager.setAdapter(pagerAdapter);
    }

    private class CardPagerAdapter extends FragmentStateAdapter {
        private List<Pair<String, String>> data;
        private float textSize;

        public CardPagerAdapter(FragmentActivity fa, List<Pair<String, String>> data, float textSize) {
            super(fa);
            this.data = data;
            this.textSize = textSize;
        }

        @Override
        public Fragment createFragment(int position) {
            CardFragment cf = new CardFragment();

            // send front and back string bundle
            Bundle bundle = new Bundle();
            Pair<String, String> card = data.get(position);
            bundle.putString(CardFragment.FRONT_ARG, card.first);
            bundle.putString(CardFragment.BACK_ARG, card.second);
            bundle.putFloat(CardFragment.SIZE_ARG, textSize);
            cf.setArguments(bundle);

            return cf;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}