package ru.shtrm.iamclever.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.shtrm.iamclever.R;

public class FragmentWelcome extends Fragment {
    private static String gTitle;

    public FragmentWelcome() {
        // Required empty public constructor
    }

    public static FragmentWelcome newInstance(String title) {
        FragmentWelcome f = new FragmentWelcome();
        Bundle args = new Bundle();
        gTitle=title;
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        TextView question_text = (TextView)view.findViewById(R.id.text2);
        if (gTitle.equalsIgnoreCase("NoLangSelected"))
            question_text.setText(R.string.no_lang_selected);
        return view;
    }
}
