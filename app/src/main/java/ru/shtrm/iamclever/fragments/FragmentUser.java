package ru.shtrm.iamclever.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Stats;

public class FragmentUser extends Fragment implements View.OnClickListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private ImageView iView;
    protected BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private Typeface mTf;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ArrayList<Float> success = new ArrayList<Float>();

        TextView name = (TextView) view.findViewById(R.id.settings_divider0);

        TextView question = (TextView) view.findViewById(R.id.profile_question);
        TextView question_complete = (TextView) view.findViewById(R.id.profile_question_complete);
        TextView shows = (TextView) view.findViewById(R.id.profile_shows);
        TextView shows_complete = (TextView) view.findViewById(R.id.profile_shows_complete);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        Profiles user = users.getActiveUser();
        if (user!=null) {
            name.setText(user.getName());
            Stats stat = statsDBAdapter.getStatsByProfileAndLang(user.getId(),user.getLang1());
            if (stat != null) {
                question.setText(stat.getQuestions() + " (100%)");
                if (stat.getQuestions()>0) {
                    question_complete.setText(stat.getQuestions_right() + " (" + stat.getQuestions_right()*100/stat.getQuestions()  +  "%)");
                    success.add((float) (stat.getQuestions_right()*100/stat.getQuestions()));
                }
                shows.setText(stat.getExams());
                if (stat.getExams()>0) shows_complete.setText(stat.getExams_complete() + " (" + stat.getExams_complete()*100/stat.getExams()  +  "%)");
            }
        }
        tvX = (TextView) view.findViewById(R.id.tvXMax);
        tvY = (TextView) view.findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) view.findViewById(R.id.seekBar2);

        mChart = (BarChart) view.findViewById(R.id.chart1);
        //mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        //mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        //ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        //xVals.add("Вопросов");
        //yVals1.add(new BarEntry(new float[] {
//                val1, val2, val3
  //      }, i));

    //    xVals.add("Вопросов");

        // setting data
        mSeekBarY.setProgress(50);
        mSeekBarX.setProgress(12);
        //mSeekBarY.setOnSeekBarChangeListener(this);
        //mSeekBarX.setOnSeekBarChangeListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                iView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
