package njan.flashcards;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CardFragment extends Fragment {
    public static final String FRONT_ARG = "FRONT_ARGUMENT";
    public static final String BACK_ARG = "BACK_ARGUMENT";
    public static final String SIZE_ARG = "SIZE_ARGUMENT";

    private String front;
    private String back;
    private TextSwitcher textSwitcher;
    private boolean backActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        // get front and back string
        front = getArguments().getString(FRONT_ARG);
        back = getArguments().getString(BACK_ARG);

        TextView tw1 = view.findViewById(R.id.textViewSwitch1);
        tw1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getArguments().getFloat(SIZE_ARG));

        TextView tw2 = view.findViewById(R.id.textViewSwitch2);
        tw2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getArguments().getFloat(SIZE_ARG));

        // setup textSwitcher
        textSwitcher = view.findViewById(R.id.textSwitcherCard);
        textSwitcher.setInAnimation(view.getContext(), R.anim.grow_in);
        textSwitcher.setOutAnimation(view.getContext(), R.anim.shrink_out);
        textSwitcher.setOnClickListener(v -> {
            backActive = !backActive;
            updateCard();
        });

        // show front card
        backActive = false;
        updateCard();

        return view;
    }

    private void updateCard() {
        if (backActive) {
            textSwitcher.setText(back);
        } else {
            textSwitcher.setText(front);
        }
    }
}