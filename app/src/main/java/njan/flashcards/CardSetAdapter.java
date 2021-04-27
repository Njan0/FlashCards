package njan.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import njan.flashcards.manager.CardSetManager;

public class CardSetAdapter extends RecyclerView.Adapter<CardSetAdapter.ViewHolder> {

    private Context context;
    private List<CardSet> sets;
    private int selectedPos;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final TextView textViewOptions;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewSetName);
            textViewOptions = view.findViewById(R.id.textViewOptions);
            view.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            // select clicked view
            notifyItemChanged(selectedPos);
            selectedPos = getAdapterPosition();
            notifyItemChanged(selectedPos);
        }
    }

    public CardSetAdapter(Context context, List<CardSet> sets) {
        this.context = context;
        this.sets = sets;
        selectedPos = RecyclerView.NO_POSITION;
    }

    public int getSelected() {
        return selectedPos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(sets.get(position).getName());
        viewHolder.itemView.setBackgroundColor(selectedPos == position ? Color.GRAY : Color.TRANSPARENT);

        viewHolder.textViewOptions.setOnClickListener(view -> {
            showPopup(viewHolder.textViewOptions, position);
        });
    }

    /**
     * Show popup to configure a set.
     * @param anchor anchor of popup
     * @param position position of set
     */
    private void showPopup(View anchor, int position) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.inflate(R.menu.options_menu_card);

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_rename:
                    renameSet(position);
                    break;
                case R.id.menu_delete:
                    CardSetManager.getInstance(context).removeSet(position);
                    notifyDataSetChanged();
                    break;
            }
            return false;
        });
        popup.show();
    }

    /**
     * Open dialog to rename a set.
     * @param position position of set
     */
    private void renameSet(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter a new name");

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Rename", (dialog, which) -> {
            CardSetManager.getInstance(context).renameSet(position, input.getText().toString());
            notifyItemChanged(position);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }
}
