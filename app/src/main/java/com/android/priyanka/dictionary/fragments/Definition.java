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

public class Definition extends Fragment {

    @BindView(R.id.textDefinition)
    TextView textDefinition;
    Unbinder unbinder;

    public Definition() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.definition_fragment, container, false);

       unbinder = ButterKnife.bind(this, view);

        try {
        Context context = getActivity();
        String en_definition = ((MeaningActivity)context).definition;

          //  String definitions = en_definition.substring(0,1).toUpperCase() + en_Definition.substring(1).toLowerCase();

            textDefinition.setText(en_definition);

        if(en_definition==null){
            textDefinition.setText("No definition found");
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
