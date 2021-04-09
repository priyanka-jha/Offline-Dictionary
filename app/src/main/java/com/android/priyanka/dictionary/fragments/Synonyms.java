package com.android.priyanka.dictionary.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.priyanka.dictionary.R;
import com.android.priyanka.dictionary.view.MeaningActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Synonyms extends Fragment {

    @BindView(R.id.textDefinition)
    TextView textSynonyms;
    Unbinder unbinder;

    public Synonyms() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.definition_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        try {
        Context context = getActivity();
        String synonyms = ((MeaningActivity)context).synonyms;

        //textSynonyms.setText(synonyms);

        if(synonyms==null || synonyms.equals("NA")){
            textSynonyms.setText("No synonyms found");
        }
        else {

            synonyms = synonyms.replaceAll(",",",\n");
            textSynonyms.setText(synonyms);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
