package ru.shtrm.iamclever.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Questions;
import ru.shtrm.iamclever.db.tables.Stats;

public class FragmentNewWords extends Fragment implements View.OnClickListener {
    private static final int MAX_WORDS = 3;
    private int cnt=0;
    private CheckBox new_word1,new_word2,new_word3,new_word4,new_word5;
    private Questions question;
    private Stats stats;
    private ImageView iView;
    private TextView tView;
    private Button mNewWordsSubmit;

    private StatsDBAdapter statsDBAdapter;

    public FragmentNewWords() {
        // Required empty public constructor
    }

    public static FragmentNewWords newInstance(String title) {
        FragmentNewWords f = new FragmentNewWords();
        Bundle args = new Bundle();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_words, container, false);
        File sd_card = Environment.getExternalStorageDirectory();
        iView = (ImageView) view.findViewById(R.id.lang_image);
        tView = (TextView) view.findViewById(R.id.new_words_text_hello);

        new_word1 = (CheckBox)view.findViewById(R.id.new_word1);
        new_word1.setOnClickListener(this);
        new_word2 = (CheckBox)view.findViewById(R.id.new_word2);
        new_word2.setOnClickListener(this);
        new_word3 = (CheckBox)view.findViewById(R.id.new_word3);
        new_word3.setOnClickListener(this);
        new_word4 = (CheckBox)view.findViewById(R.id.new_word4);
        new_word4.setOnClickListener(this);
        new_word5 = (CheckBox)view.findViewById(R.id.new_word5);
        new_word5.setOnClickListener(this);

        mNewWordsSubmit = (Button)view.findViewById(R.id.CheckNewWords);
        mNewWordsSubmit.setOnClickListener(this);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        LanguagesDBAdapter languagesDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();
        statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));

        if (user.getLang1()>0) {
            String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + languagesDBAdapter.getIconByID(user.getLang1());
            File imgFile = new  File(target_filename);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iView.setImageBitmap(myBitmap);
            }
            tView.setText(getString(R.string.new_words_hello)+ " Язык: " + languagesDBAdapter.getNameByID(""+user.getLang1()));

            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            if (question != null) new_word1.setText(question.getOriginal() + " - " + question.getAnswer());
            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            if (question != null) new_word2.setText(question.getOriginal() + " - " + question.getAnswer());
            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            if (question != null) new_word3.setText(question.getOriginal() + " - " + question.getAnswer());
            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            if (question != null) new_word4.setText(question.getOriginal() + " - " + question.getAnswer());
            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            if (question != null) new_word5.setText(question.getOriginal() + " - " + question.getAnswer());
            }
        else
            {
                Fragment f = FragmentWelcome.newInstance("");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            }
        return view;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_word1:
            case R.id.new_word2:
            case R.id.new_word3:
            case R.id.new_word4:
            case R.id.new_word5:
                if (new_word1.isChecked() && new_word2.isChecked() && new_word3.isChecked() && new_word4.isChecked() && new_word5.isChecked())
                    mNewWordsSubmit.setEnabled(true);
                else
                    mNewWordsSubmit.setEnabled(false);
                break;
            case R.id.CheckNewWords: {
                Fragment f = FragmentTips.newInstance("Tips");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            }
        }
    }
}
