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
import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Questions;

public class FragmentNewWords extends Fragment implements View.OnClickListener {
    private static final int MAX_WORDS = 10;
    private ArrayList<Questions> questions = new ArrayList<>();
    private ArrayList<CheckBox> new_words = new ArrayList<>();
    private Button mNewWordsSubmit;


    public FragmentNewWords() {
        // Required empty public constructor
    }

    public static FragmentNewWords newInstance(String title) {
        //FragmentNewWords f = new FragmentNewWords();
        //Bundle args = new Bundle();
        return (new FragmentNewWords());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_words, container, false);
        File sd_card = Environment.getExternalStorageDirectory();
        ImageView iView = (ImageView) view.findViewById(R.id.lang_image);
        TextView tView = (TextView) view.findViewById(R.id.new_words_text_hello);

        new_words.add((CheckBox)view.findViewById(R.id.new_word1));
        new_words.add((CheckBox)view.findViewById(R.id.new_word2));
        new_words.add((CheckBox)view.findViewById(R.id.new_word3));
        new_words.add((CheckBox)view.findViewById(R.id.new_word4));
        new_words.add((CheckBox)view.findViewById(R.id.new_word5));
        new_words.add((CheckBox)view.findViewById(R.id.new_word6));
        new_words.add((CheckBox)view.findViewById(R.id.new_word7));
        new_words.add((CheckBox)view.findViewById(R.id.new_word8));
        new_words.add((CheckBox)view.findViewById(R.id.new_word9));
        new_words.add((CheckBox)view.findViewById(R.id.new_word10));
        for (int i=0; i<MAX_WORDS; i++)
            new_words.get(i).setOnClickListener(this);

        mNewWordsSubmit = (Button)view.findViewById(R.id.CheckNewWords);
        mNewWordsSubmit.setOnClickListener(this);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        LanguagesDBAdapter languagesDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();

        if (user.getLang1()>0) {
            String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + languagesDBAdapter.getIconByID(user.getLang1());
            File imgFile = new  File(target_filename);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iView.setImageBitmap(myBitmap);
            }
            tView.setText(getString(R.string.new_words_hello) + " Язык: " + languagesDBAdapter.getNameByID("" + user.getLang1()));

            for (int w_counter=0;w_counter<MAX_WORDS;w_counter++) {
                Questions question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(), 1);
                if (question != null) {
                    questions.add(question);
                    if (question.getAnswer2().length()>0)
                        new_words.get(w_counter).setText(question.getOriginal() + " - " + question.getAnswer() + " / " + question.getAnswer2());
                    else
                        new_words.get(w_counter).setText(question.getOriginal() + " - " + question.getAnswer());
                    }
                }
            }
        else
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("")).commit();
        return view;
    }
    public void onClick(View v) {
        int q;
        switch (v.getId()) {
            case R.id.new_word1:
            case R.id.new_word2:
            case R.id.new_word3:
            case R.id.new_word4:
            case R.id.new_word5:
            case R.id.new_word6:
            case R.id.new_word7:
            case R.id.new_word8:
            case R.id.new_word9:
            case R.id.new_word10:
                if (new_words.get(0).isChecked() && new_words.get(1).isChecked() && new_words.get(2).isChecked() && new_words.get(3).isChecked() && new_words.get(4).isChecked())
                    mNewWordsSubmit.setEnabled(true);
                else
                    mNewWordsSubmit.setEnabled(false);
                break;
            case R.id.CheckNewWords: {
                for (int i=0; i<MAX_WORDS; i++)
                    if (new_words.get(0).isChecked()) {
                        q=questions.get(i).getQuestion();
                        q++;
                        questions.get(i).setQuestion(q);
                    }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                break;
            }
        }
    }
}
