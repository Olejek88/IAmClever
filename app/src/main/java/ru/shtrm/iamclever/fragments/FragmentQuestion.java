package ru.shtrm.iamclever.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;
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
    private static final int NUM_EXAM_QUESTIONS = 10;
    private ArrayList<RadioButton> rb_question = new ArrayList<>();
    private Questions question;
    private TextView question_text,count_timer;
    private Answers answer;
    private Stats stats;
    private CountDownTimer Count;
    private int n_quest = 1, questions=0, right_question=0, exam=0, exam_complete=0, answer_correct=0, answer_incorrect=0;

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
        final View view = inflater.inflate(R.layout.dialog_question, container, false);
        count_timer = (TextView) view.findViewById(R.id.count_timer);

        question_text = (TextView)view.findViewById(R.id.question_text);
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
        StatsDBAdapter statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();

        if (user.getLang1()>0) {
            stats = statsDBAdapter.getStatsByProfileAndLang(user.getId(), user.getLang1());
            if (stats!=null) {
                exam = stats.getExams();
                exam_complete = stats.getExams_complete();
                questions = stats.getQuestions();
                right_question = stats.getQuestions_right();
            }
            SetQuestion();
        }
        return view;
    }

    public void SetQuestion()
        {
            String RightAnswer;
            Questions question2;
            ProfilesDBAdapter users = new ProfilesDBAdapter(
                    new IDatabaseContext(getActivity().getApplicationContext()));
            QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                    new IDatabaseContext(getActivity().getApplicationContext()));
            QuestionTypeDBAdapter questionTypeDBAdapter = new QuestionTypeDBAdapter(
                    new IDatabaseContext(getActivity().getApplicationContext()));
            AnswersDBAdapter answersDBAdapter = new AnswersDBAdapter(
                    new IDatabaseContext(getActivity().getApplicationContext()));
            Profiles user = users.getActiveUser();

            for (int i=0; i<MAX_QUESTIONS; i++)
                rb_question.get(i).setChecked(false);

            question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(), 1);
            if (question != null) {
                RightAnswer = question.getAnswer();
                answer = answersDBAdapter.getAnswerByQuestionAndProfile(question.getId(), user.getId());
                if (answer!=null) {
                    answer_correct = answer.getCorrect();
                    answer_incorrect = answer.getIncorrect();
                }

                question_text.setText("Вопрос [" + n_quest + "/" + NUM_EXAM_QUESTIONS + "] " + questionTypeDBAdapter.getNameByID("" + question.getType()) + ": " + question.getOriginal());

                Random r = new Random();
                int i1 = r.nextInt(MAX_QUESTIONS);
                for (int i = 0; i < MAX_QUESTIONS; i++) {
                    question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(), 1);
                    if (i1 != i && question2 != null)
                        rb_question.get(i).setText(question2.getAnswer());
                    if (i1 == i) rb_question.get(i).setText(RightAnswer);
                }
            } else
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("Welcome")).commit();
        }

    public void onClick(View v) {
        final View view=v;
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
                    questions++;
                    stats.setQuestions(questions);
                }
                if (right) {
                    answer_correct++;
                    right_question++;
                    if (answer != null)
                        answer.setCorrect(answer_correct);
                    if (stats!=null)
                        stats.setQuestions_right(right_question);
                    //Toast.makeText(getActivity().getApplicationContext(),"Правильно!", Toast.LENGTH_LONG).show();
                }
                else {
                    answer_incorrect++;
                    if (answer != null)
                        answer.setIncorrect(answer_incorrect);
                    //Toast.makeText(getActivity().getApplicationContext(),"Неправильно (((", Toast.LENGTH_LONG).show();
                }
                if (answer != null)
                    answersDBAdapter.updateItem(answer);

                if (n_quest<NUM_EXAM_QUESTIONS) {
                    if (n_quest==1) {
                        exam++;
                        Count = new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                count_timer.setText("" + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                count_timer.setText("0");
                                float pr = right_question * 100 / NUM_EXAM_QUESTIONS;
                                DecimalFormat twoDForm = new DecimalFormat("##.##");
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Время вышло! Результат")
                                        .setMessage("Правильных ответов " + right_question + " (" + Double.valueOf(twoDForm.format(pr)) + "%)")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                }
                            }.start();
                        }
                    n_quest++;
                    SetQuestion();
                    }
                else {
                    exam_complete++;
                    stats.setExams(exam);
                    stats.setExams_complete(exam_complete);
                    if (stats!=null) statsDBAdapter.updateItem(stats);
                    float pr=right_question*100/NUM_EXAM_QUESTIONS;
                    DecimalFormat twoDForm = new DecimalFormat("##.##");
                    Count.cancel();
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Результат тестирования")
                            .setMessage("Правильных ответов " + right_question + " (" + Double.valueOf(twoDForm.format(pr)) + "%)")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
                break;
            }
        }
    }
}
