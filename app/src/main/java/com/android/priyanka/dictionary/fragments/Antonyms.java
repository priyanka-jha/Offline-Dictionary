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

public class Antonyms extends Fragment {
    @BindView(R.id.textDefinition)
    TextView textAntonyms;
    Unbinder unbinder;

    public Antonyms() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.definition_fragment, container, false);


        unbinder = ButterKnife.bind(this, view);
        try {
        Context context = getActivity();
        String antonyms = ((MeaningActivity)context).antonyms;

        //textAntonyms.setText(antonyms);

        if(antonyms==null || antonyms.equals("NA")){
            textAntonyms.setText("No antonyms found");
        }
        else {

            antonyms = antonyms.replaceAll(",",",\n");
            textAntonyms.setText(antonyms);
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
