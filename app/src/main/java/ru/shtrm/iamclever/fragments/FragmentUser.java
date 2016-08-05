package ru.shtrm.iamclever.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Stats;

public class FragmentUser extends Fragment implements View.OnClickListener {
    protected BarChart mChart;

    public FragmentUser() {
        // Required empty public constructor
    }

    public static FragmentUser newInstance(String title) {
        //FragmentUser f = new FragmentUser();
        //Bundle args = new Bundle();
        return new FragmentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TextView shows_complete = (TextView) view.findViewById(R.id.profile_shows_complete);
        final Spinner lang1Spinner = (Spinner) view.findViewById(R.id.profile_choose_lang);
        lang1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
             setStatistics(lang1Spinner.getSelectedItemPosition()+1, view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        setStatistics(1, view);
        return view;
    }

    public void setStatistics(int lang, View view) {


        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));

        TextView name = (TextView) view.findViewById(R.id.settings_divider0);
        TextView question = (TextView) view.findViewById(R.id.profile_question);
        ArrayList<Float> success = new ArrayList<>();
        TextView shows = (TextView) view.findViewById(R.id.profile_shows);
        TextView question_total = (TextView) view.findViewById(R.id.profile_question_total);

        Profiles user = users.getActiveUser();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        if (user!=null) {
            name.setText(user.getName());
            question_total.setText(""+questionDBAdapter.getItemsCount(lang));
            Stats stat = statsDBAdapter.getStatsByProfileAndLang(user.getId(),lang);
            if (stat != null) {
                if (stat.getQuestions()>0) {
                    question.setText(stat.getQuestions_right() + "/" + stat.getQuestions() + " (" + stat.getQuestions_right()*100/stat.getQuestions() + "%)");
                    success.add((float) (stat.getQuestions_right()*100/stat.getQuestions()));
                    BarEntry v1e1 = new BarEntry(stat.getQuestions(), 0);
                    valueSet1.add(v1e1);
                    BarEntry v2e1 = new BarEntry(stat.getQuestions_right(), 0);
                    valueSet2.add(v2e1);
                }
                else {
                    question.setText("нет данных");
                    shows.setText("нет данных");
                }
                if (stat.getExams()>0) {
                    BarEntry v1e2 = new BarEntry(stat.getExams(), 1);
                    valueSet1.add(v1e2);
                    BarEntry v2e2 = new BarEntry(stat.getExams_complete(), 1);
                    valueSet2.add(v2e2);
                    shows.setText(stat.getExams_complete() + "/" + stat.getExams() + " (" + stat.getExams_complete()*100/stat.getExams() + "%)");
                }
            }
        }

        mChart = (BarChart) view.findViewById(R.id.chart1);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        ArrayList<BarDataSet> dataSets;
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Вопросы");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Экзамены");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Вопросы");
        labels.add("Экзамены");

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarChart mChart = (BarChart) view.findViewById(R.id.chart1);
        BarData data = new BarData(labels, dataSets);
        mChart.setData(data);
        mChart.setBackgroundColor(0xffffffff);

        //mChart.setDescription("Ваша статистика");
        //mChart.animateXY(2000, 2000);
        mChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }
}
