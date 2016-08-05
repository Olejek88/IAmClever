package ru.shtrm.iamclever.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.AnswersDBAdapter;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
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
    private int CurrentLang;
    private int CurrentLevel;
    private int CurrentType=99;
    private CountDownTimer Count;
    private int n_quest = 1, questions=0, right_question=0, exam=0, exam_complete=0, answer_correct=0, answer_incorrect=0, r_question=0;
    private StatsDBAdapter statsDBAdapter;

    public FragmentQuestion() {
        // Required empty public constructor
    }

    public static FragmentQuestion newInstance() {
        //FragmentQuestion f = new FragmentQuestion();
        //Bundle args = new Bundle();
        return (new FragmentQuestion());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_question, container, false);
        int words=0;
        File sd_card = Environment.getExternalStorageDirectory();
        statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        LanguagesDBAdapter languagesDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        count_timer = (TextView) view.findViewById(R.id.count_timer);
        int n_lang=0;
        ImageView iView = (ImageView) view.findViewById(R.id.lang_image);
        TextView tView = (TextView) view.findViewById(R.id.new_words_text_hello);

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
        Profiles user = users.getActiveUser();
        if (user==null) {
            Toast.makeText(getActivity().getApplicationContext(), "Пользователь не выбран, пожалуйста выберите или содайте профиль", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("")).commit();
        }
        if (user!=null && user.getLang1()>0) n_lang++;
        if (user!=null && user.getLang2()>0) n_lang++;
        if (user!=null && user.getLang3()>0) n_lang++;

        for (int t=0; t<20; t++) {
            Random r = new Random();
            int i1 = r.nextInt(6);
            switch (i1) {
                case 0:
                    if (user.getLang1() > 0) {
                        CurrentLang=user.getLang1();
                        CurrentLevel=user.getLevel1()+1;
                        CurrentType=0;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel);
                    }
                    break;
                case 1:
                    if (user.getLang2() > 0) {
                        CurrentLang=user.getLang2();
                        CurrentLevel=user.getLevel2()+1;
                        CurrentType=0;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel);
                    }
                    break;
                case 2:
                    if (user.getLang3() > 0) {
                        CurrentLang=user.getLang3();
                        CurrentLevel=user.getLevel3()+1;
                        CurrentType=0;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel);
                    }
                    break;
                case 3:
                    if (user.getLang1() > 0) {
                        CurrentLang=user.getLang1();
                        CurrentLevel=user.getLevel1()+1;
                        CurrentType=1;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel);
                    }
                    break;
                case 4:
                    if (user.getLang2() > 0) {
                        CurrentLang=user.getLang2();
                        CurrentLevel=user.getLevel2()+1;
                        CurrentType=1;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel+1);
                    }
                    break;
                case 5:
                    if (user.getLang3() > 0) {
                        CurrentLang=user.getLang3();
                        CurrentLevel=user.getLevel3()+1;
                        CurrentType=1;
                        words=SetQuestion(CurrentLang,CurrentType,CurrentLevel);
                    }
                    break;
            }
            if (CurrentType!=99) {
                stats = statsDBAdapter.getStatsByProfileAndLang(user.getId(), CurrentLang);
                if (stats!=null) {
                    exam = stats.getExams();
                    exam_complete = stats.getExams_complete();
                    questions = stats.getQuestions();
                    right_question = stats.getQuestions_right();
                }
            }

            if (CurrentType!=99) {
                Bitmap myBitmap;
                switch (CurrentLang)
                    {
                        case 2: myBitmap = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ita); break;
                        case 3: myBitmap = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.deu); break;
                        case 4: myBitmap = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.usa); break;
                        default:
                            myBitmap = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.eng);
                    }
                iView.setImageBitmap(myBitmap);
                //String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + languagesDBAdapter.getIconByID(CurrentLang);
                //File imgFile = new File(target_filename);
                //if (imgFile.exists()) {
                    //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //iView.setImageBitmap(myBitmap);
                //}
                break;
            }
        }

        if (n_lang==0 || words==0)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("")).commit();
        return view;
    }

    public int SetQuestion(int lang, int type, int level)
        {
            String RightAnswer;
            int words=0;
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

            question = questionDBAdapter.getRandomQuestionByLangAndLevel(lang, level, 0);

            if (question != null) {
                if (type==0)
                    RightAnswer = question.getAnswer();
                else
                    RightAnswer = question.getOriginal();

                answer = answersDBAdapter.getAnswerByQuestionAndProfile(question.getId(), user.getId());
                if (answer!=null) {
                    answer_correct = answer.getCorrect();
                    answer_incorrect = answer.getIncorrect();
                }
                if (type==0)
                    question_text.setText("Вопрос [" + n_quest + "/" + NUM_EXAM_QUESTIONS + "] " + questionTypeDBAdapter.getNameByID("" + question.getType()) + ": " + question.getOriginal());
                else
                    question_text.setText("Вопрос [" + n_quest + "/" + NUM_EXAM_QUESTIONS + "] " + questionTypeDBAdapter.getNameByID("" + question.getType()) + ": " + question.getAnswer());
                Random r = new Random();
                int i1 = r.nextInt(MAX_QUESTIONS);
                for (int i = 0; i < MAX_QUESTIONS; i++) {
                    question2 = questionDBAdapter.getRandomQuestionByLangAndLevel(lang, level, question.getType());
                    if (i1 != i && question2 != null) {
                        if (type==0)
                            rb_question.get(i).setText(question2.getAnswer());
                        else
                            rb_question.get(i).setText(question2.getOriginal());
                        words++;
                    }
                    if (i1 == i) rb_question.get(i).setText(RightAnswer);
                }
            } else
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentWelcome.newInstance("Welcome")).commit();
            return words;
        }

    public void onClick(View v) {
        final View view=v;
        switch (v.getId()) {
            case  R.id.Question_Cancel: {
                if (Count!=null)
                    Count.cancel();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentTips.newInstance()).commit();
                break;
            }
            case R.id.Question_Answer: {
                AnswersDBAdapter answersDBAdapter = new AnswersDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                questions++;
                boolean right=false;
                int word=0;

                for (int i=0; i<MAX_QUESTIONS; i++) {
                    if (CurrentType==0 && rb_question.get(i).isChecked() && rb_question.get(i).getText().equals(question.getAnswer()))
                        right = true;
                    if (CurrentType==1 && rb_question.get(i).isChecked() && rb_question.get(i).getText().equals(question.getOriginal()))
                        right = true;
                }
                if (right) {
                    answer_correct++;
                    right_question++;
                    r_question++;

                    word=question.getQuestion();
                    word++;
                    question.setQuestion(word);
                    questionDBAdapter.updateItem(question);

                    if (answer != null)
                        answer.setCorrect(answer_correct);
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
                                if (stats!=null) {
                                    stats.setQuestions_right(right_question);
                                    stats.setQuestions(questions);
                                    stats.setExams(exam);
                                    stats.setExams_complete(exam_complete);
                                    statsDBAdapter.updateItem(stats);
                                }
                                float pr = r_question * 100 / NUM_EXAM_QUESTIONS;
                                DecimalFormat twoDForm = new DecimalFormat("##.##");
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Время вышло! Результат")
                                        .setMessage("Правильных ответов " + r_question + " (" + Double.valueOf(twoDForm.format(pr)) + "%)")
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
                     SetQuestion(CurrentLang,0,CurrentLevel);
                    }
                else {
                    Count.cancel();
                    exam_complete++;
                    if (stats!=null) {
                        stats.setQuestions_right(right_question);
                        stats.setQuestions(questions);
                        stats.setExams(exam);
                        stats.setExams_complete(exam_complete);
                        statsDBAdapter.updateItem(stats);
                    }
                    float pr=r_question*100/NUM_EXAM_QUESTIONS;
                    DecimalFormat twoDForm = new DecimalFormat("##.##");
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Результат тестирования")
                            .setMessage("Правильных ответов " + r_question + " (" + Double.valueOf(twoDForm.format(pr)) + "%)")
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
