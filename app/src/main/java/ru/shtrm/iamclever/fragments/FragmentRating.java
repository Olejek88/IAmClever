package ru.shtrm.iamclever.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.RatingsDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Ratings;

public class FragmentRating extends Fragment implements View.OnClickListener {
    protected BarChart mChart;

    public FragmentRating() {
        // Required empty public constructor
    }

    public static FragmentRating newInstance() {
        //FragmentUser f = new FragmentUser();
        //Bundle args = new Bundle();
        return new FragmentRating();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rating, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //final Spinner lang1Spinner = (Spinner) view.findViewById(R.id.profile_choose_lang);
        //lang1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        /*
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
             setStatistics(lang1Spinner.getSelectedItemPosition()+1, view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });*/
        setStatistics(0, view);
        return view;
    }

    public void setStatistics(int lang, View view) {
        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        RatingsDBAdapter ratingsDBAdapter = new RatingsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        ArrayList<Ratings> ratings = new ArrayList<>();
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rating_layout);
        ratings = ratingsDBAdapter.getAllItemsByLang(lang);
        for (int count=0; count<ratings.size(); count++) {
            TextView textView = new TextView(getActivity());
            textView.setText(ratings.get(count).getPlace()+". "+ratings.get(count).getUser()+" "+ratings.get(count).getRating());
            linearLayout.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }
}
