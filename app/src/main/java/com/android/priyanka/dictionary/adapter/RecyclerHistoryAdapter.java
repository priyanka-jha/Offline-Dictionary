package com.android.priyanka.dictionary.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.priyanka.dictionary.R;
import com.android.priyanka.dictionary.model.History;
import com.android.priyanka.dictionary.view.MeaningActivity;

import java.util.ArrayList;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.HistoryViewHolder> {

    private ArrayList<History> histories;
    private Context context;

    public RecyclerHistoryAdapter(ArrayList<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView enWord;
        TextView enDef;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            enWord = itemView.findViewById(R.id.en_word);
            enDef = itemView.findViewById(R.id.en_def);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    int position = getAdapterPosition();
                    String text = histories.get(position).getEn_word();

                    Intent intent = new Intent(context, MeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",text);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

   }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item,viewGroup,false);
        return new HistoryViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
            historyViewHolder.enWord.setText(histories.get(i).getEn_word());
            historyViewHolder.enDef.setText(histories.get(i).getEn_def());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
