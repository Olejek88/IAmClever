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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

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
        int n_lang=0,words=0;
        boolean form_complete=false;

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
        Profiles user = users.getActiveUser();
        if (user==null) {
            Toast.makeText(getActivity().getApplicationContext(), "Пользователь не выбран, пожалуйста выберите или содайте профиль", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("")).commit();
        }

        if (user.getLang1()>0) n_lang++;
        if (user.getLang2()>0) n_lang++;
        if (user.getLang3()>0) n_lang++;

        for (int t=0; t<20; t++) {
            Random r = new Random();
            int i1 = r.nextInt(3);
            switch (i1) {
                case 0:
                    if (user.getLang1() > 0) {
                        words=FormWords(user.getLang1(),view, user.getLevel1()+1);
                        form_complete=true;
                    }
                    break;
                case 1:
                    if (user.getLang2() > 0) {
                        words=FormWords(user.getLang2(),view, user.getLevel2()+1);
                        form_complete=true;
                    }
                    break;
                case 2:
                    if (user.getLang3() > 0) {
                        words=FormWords(user.getLang3(),view, user.getLevel3()+1);
                        form_complete=true;
                    }
                    break;
            }
            if (form_complete) break;
        }

        if (n_lang==0 || words==0)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("")).commit();
        return view;
    }

    public int FormWords(int lang, View view,int level) {
        int words=0;
        File sd_card = Environment.getExternalStorageDirectory();
        LanguagesDBAdapter languagesDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));

        ImageView iView = (ImageView) view.findViewById(R.id.lang_image);
        TextView tView = (TextView) view.findViewById(R.id.new_words_text_hello);

        String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + languagesDBAdapter.getIconByID(lang);
        File imgFile = new File(target_filename);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iView.setImageBitmap(myBitmap);
        }
        tView.setText("Язык: " + languagesDBAdapter.getNameByID("" + lang));

        for (int w_counter = 0; w_counter < MAX_WORDS; w_counter++) {
            Questions question = questionDBAdapter.getRandomQuestionByLangAndLevel(lang, level);
            if (question != null) {
                questions.add(question);
                if (question.getAnswer2().length() > 0)
                    new_words.get(w_counter).setText(question.getOriginal() + " - " + question.getAnswer() + " / " + question.getAnswer2());
                else
                    new_words.get(w_counter).setText(question.getOriginal() + " - " + question.getAnswer());
                words++;
            }
        }
        return words;
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
