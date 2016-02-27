package ru.shtrm.iamclever.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.shtrm.iamclever.R;

public class FragmentIntro extends Fragment {

    public FragmentIntro() {
        // Required empty public constructor
    }

    public static FragmentIntro newInstance(String title) {
        FragmentIntro f = new FragmentIntro();
        Bundle args = new Bundle();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        return view;
    }
}
