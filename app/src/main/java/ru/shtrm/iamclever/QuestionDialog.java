package ru.shtrm.iamclever;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Questions;

public class QuestionDialog extends Activity implements View.OnClickListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_question);
        TextView question_text = (TextView)findViewById(R.id.question_text);
        RadioButton rb_text1 = (RadioButton)findViewById(R.id.answer1);
        RadioButton rb_text2 = (RadioButton)findViewById(R.id.answer2);
        RadioButton rb_text3 = (RadioButton)findViewById(R.id.answer3);
        RadioButton rb_text4 = (RadioButton)findViewById(R.id.answer4);
        RadioButton rb_text5 = (RadioButton)findViewById(R.id.answer5);

        Button mQuestionAnswer = (Button)findViewById(R.id.Question_Answer);
        mQuestionAnswer.setOnClickListener(this);
        Button mQuestionCancel = (Button)findViewById(R.id.Question_Cancel);
        mQuestionCancel.setOnClickListener(this);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getApplicationContext()));
        QuestionsDBAdapter questionDBAdapter = new QuestionsDBAdapter(
                new IDatabaseContext(getApplicationContext()));
        Profiles user = users.getActiveUser();
        if (user.getLang1()>0) {
             Questions question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
             if (question != null) {
                 question_text.setText(question.getQuestion());
                 question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                 rb_text1.setText(question.getAnswer());
                 question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                 rb_text2.setText(question.getAnswer());
                 question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                 rb_text3.setText(question.getAnswer());
                 question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                 rb_text4.setText(question.getAnswer());
                 question = questionDBAdapter.getRandomQuestionByLangAndLevel(user.getLang1(),1);
                 rb_text5.setText(question.getAnswer());
                }
            }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.Question_Cancel: {
                this.finish();
                break;
            }
            case R.id.Question_Answer: {
                this.finish();
                break;
            }
        }
    }
}