package ru.shtrm.iamclever.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private static final int MAX_QUESTIONS = 8;
    private ArrayList<RadioButton> rb_question = new ArrayList<>();
    private Questions question;
    private Answers answer;
    private Stats stats;

    public FragmentQuestion() {
        // Required empty public constructor
    }

    public static FragmentQuestion newInstance(String title) {
        //FragmentQuestion f = new FragmentQuestion();
        //Bundle args = new Bundle();
        return (new FragmentQuestion());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String RightAnswer;
        Questions question2;
        // TODO экзамен состоит из n вопросов с выводом результата в конце
        View view = inflater.inflate(R.layout.dialog_question, container, false);
        TextView question_text = (TextView)view.findViewById(R.id.question_text);
        rb_question.add((RadioButton)view.findViewById(R.id.answer1));
        rb_question.add((RadioButton)view.findViewById(R.id.answer2));
        rb_question.add((RadioButton)view.findViewById(R.id.answer3));
        rb_question.add((RadioButton)view.findViewById(R.id.answer4));
        rb_question.add((RadioButton)view.findViewById(R.id.answer5));
        rb_question.add((RadioButton)view.findViewById(R.id.answer6));
        rb_question.add((RadioButton)view.findViewById(R.id.answer7));
        rb_question.add((RadioButton)view.findViewById(R.id.answer8));

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
                question_text.setText(questionTypeDBAdapter.getNameByID("" + question.getType()) + ": " + question.getOriginal());
                Random r = new Random();
                int i1 = r.nextInt(MAX_QUESTIONS);
                for (int i=0; i<MAX_QUESTIONS; i++) {
                    question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(), 1);
                    if (i1 != i && question2 != null)
                        rb_question.get(i).setText(question2.getAnswer());
                    if (i1 == i) rb_question.get(i).setText(RightAnswer);
                }
            }
            else
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("Welcome")).commit();
        }
        return view;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.Question_Cancel: {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                break;
            }
            case R.id.Question_Answer: {
                AnswersDBAdapter answersDBAdapter = new AnswersDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));

                boolean right=false;
                for (int i=0; i<MAX_QUESTIONS; i++)
                    if (rb_question.get(i).isChecked() && rb_question.get(i).getText().equals(question.getAnswer())) right=true;
                if (stats!=null) {
                    stats.setQuestions(stats.getQuestions() + 1);
                    stats.setExams(stats.getExams() + 1);
                    stats.setExams_complete(stats.getExams_complete() + 1);
                }
                if (right) {
                    if (answer != null)
                        answer.setCorrect(answer.getCorrect() + 1);
                    if (stats!=null)
                        stats.setQuestions_right(stats.getQuestions_right() + 1);
                        Toast.makeText(getActivity().getApplicationContext(),
                            "Правильно!", Toast.LENGTH_LONG).show();
                    if (stats!=null) stats.setQuestions_right(stats.getQuestions_right()+1);
                }
                else {
                    if (answer != null)
                        answer.setIncorrect(answer.getIncorrect() + 1);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Неправильно (((", Toast.LENGTH_LONG).show();
                }
                if (stats!=null) statsDBAdapter.updateItem(stats);
                if (answer != null)
                    answersDBAdapter.updateItem(answer);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                break;
            }
        }
    }
}
