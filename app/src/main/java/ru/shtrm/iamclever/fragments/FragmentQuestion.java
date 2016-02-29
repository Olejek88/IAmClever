package ru.shtrm.iamclever.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.AnswersDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionTypeDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Answers;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Questions;
import ru.shtrm.iamclever.db.tables.Stats;

public class FragmentQuestion extends Fragment implements View.OnClickListener {
    private static final int MAX_QUESTIONS = 5;
    private int cnt=0;
    private RadioGroup rg;
    private RadioButton rb_text1,rb_text2,rb_text3,rb_text4,rb_text5;
    private Questions question, question2;
    private Answers answer;
    private Stats stats;
    private String RightAnswer;

    public FragmentQuestion() {
        // Required empty public constructor
    }

    public static FragmentQuestion newInstance(String title) {
        FragmentQuestion f = new FragmentQuestion();
        Bundle args = new Bundle();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_question, container, false);
        TextView question_text = (TextView)view.findViewById(R.id.question_text);
        rb_text1 = (RadioButton)view.findViewById(R.id.answer1);
        rb_text2 = (RadioButton)view.findViewById(R.id.answer2);
        rb_text3 = (RadioButton)view.findViewById(R.id.answer3);
        rb_text4 = (RadioButton)view.findViewById(R.id.answer4);
        rb_text5 = (RadioButton)view.findViewById(R.id.answer5);

        rg = (RadioGroup)view.findViewById(R.id.radio);
        Button mQuestionAnswer = (Button)view.findViewById(R.id.Question_Answer);
        mQuestionAnswer.setOnClickListener(this);

        Button mQuestionCancel = (Button)view.findViewById(R.id.Question_Cancel);
        mQuestionCancel.setOnClickListener(this);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        QuestionTypeDBAdapter questionTypeDBAdapter = new QuestionTypeDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        AnswersDBAdapter answersDBAdapter = new AnswersDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();

        if (user.getLang1()>0) {
            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
            stats = statsDBAdapter.getStatsByProfileAndLang(user.getId(),user.getLang1());
            if (question != null) {
                RightAnswer = question.getAnswer();
                answer = answersDBAdapter.getAnswerByQuestionAndProfile(question.getId(),user.getId());
                // TODO обновить статистику пользователя
                question_text.setText(questionTypeDBAdapter.getNameByID(""+question.getType()) + ": " + question.getOriginal());
                Random r = new Random();
                int i1 = r.nextInt(MAX_QUESTIONS)+1;

                question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                if (i1!=1 && question2 != null) rb_text1.setText(question2.getAnswer());
                if (i1==1) rb_text1.setText(RightAnswer);
                question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                if (i1!=2 && question2 != null) rb_text2.setText(question2.getAnswer());
                if (i1==2) rb_text2.setText(RightAnswer);
                question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                if (i1!=3 && question2 != null) rb_text3.setText(question2.getAnswer());
                if (i1==3) rb_text3.setText(RightAnswer);
                question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                if (i1!=4 && question2 != null) rb_text4.setText(question2.getAnswer());
                if (i1==4) rb_text4.setText(RightAnswer);
                question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                if (i1!=5 && question2 != null) rb_text5.setText(question2.getAnswer());
                if (i1==5) rb_text5.setText(RightAnswer);
            }
            else {
                Fragment f = FragmentWelcome.newInstance("Welcome");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            }
        }
        return view;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.Question_Cancel: {
                Fragment f = FragmentWelcome.newInstance("Welcome");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            }
            case R.id.Question_Answer: {
                AnswersDBAdapter answersDBAdapter = new AnswersDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                boolean right=false;
                if (rb_text1.isChecked() && rb_text1.getText().equals(question.getAnswer())) right=true;
                if (rb_text2.isChecked() && rb_text2.getText().equals(question.getAnswer())) right=true;
                if (rb_text3.isChecked() && rb_text3.getText().equals(question.getAnswer())) right=true;
                if (rb_text4.isChecked() && rb_text4.getText().equals(question.getAnswer())) right=true;
                if (rb_text5.isChecked() && rb_text5.getText().equals(question.getAnswer())) right=true;
                if (right) {
                    if (answer != null)
                        answer.setCorrect(answer.getCorrect() + 1);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Правильно!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (answer != null)
                        answer.setIncorrect(answer.getIncorrect() + 1);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Неправильно (((", Toast.LENGTH_LONG).show();
                }
                if (answer != null)
                    answersDBAdapter.replace(answer);

                Fragment f = FragmentWelcome.newInstance("Welcome");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            }
        }
    }
}
