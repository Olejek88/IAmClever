package ru.shtrm.iamclever.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionTypeDBAdapter;
import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.tables.Questions;

public class FragmentAddWords extends Fragment implements View.OnClickListener {
    private Spinner langSpinner;
    private Spinner levelSpinner;
    private Spinner typeSpinner;
    private EditText original, answer;

    public FragmentAddWords() {
        // Required empty public constructor
    }

    public static FragmentAddWords newInstance(String title) {
        //FragmentAddWords f = new FragmentAddWords();
        //Bundle args = new Bundle();
        return (new FragmentAddWords());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addwords, container, false);
        Button one = (Button) view.findViewById(R.id.add_word_button_submit);
        one.setOnClickListener(this);

        langSpinner = (Spinner) view.findViewById(R.id.add_word_lang);
        levelSpinner = (Spinner) view.findViewById(R.id.add_word_level);
        typeSpinner = (Spinner) view.findViewById(R.id.add_word_type);

        ArrayAdapter<String> langSpinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<String>());
        langSpinner.setAdapter(langSpinnerAdapter);
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<String>());
        typeSpinner.setAdapter(typeSpinnerAdapter);

        LanguagesDBAdapter langDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity()));
        ArrayList<String> langList = langDBAdapter.getItems();
        QuestionTypeDBAdapter typeDBAdapter = new QuestionTypeDBAdapter(
                new IDatabaseContext(getActivity()));
        ArrayList<String> typeList = typeDBAdapter.getItems();

        String no_lang_selected="Выберите язык";
        langSpinnerAdapter.clear();
        langSpinnerAdapter.add(no_lang_selected);
        langSpinnerAdapter.addAll(langList);

        typeSpinnerAdapter.clear();
        typeSpinnerAdapter.addAll(typeList);

        original = (EditText) view.findViewById(R.id.add_word_original);
        answer = (EditText) view.findViewById(R.id.add_word_answer);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_word_button_submit:
                QuestionsDBAdapter questions = new QuestionsDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                Questions question;
                question = questions.getQuestionByName(original.getText().toString());
                if (question!=null) {
                     Toast.makeText(getActivity().getApplicationContext(),
                            "Слово/выражение " + question.getOriginal() + " уже есть в локальной базе", Toast.LENGTH_LONG).show();
                     break;
                    }
                if (original.getText().toString().length()<2 || answer.getText().toString().length()<2)    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Вы должны заполнить все поля!", Toast.LENGTH_LONG).show();
                        break;
                    }
                question = new Questions();
                question.setQuestion(0);
                question.setOriginal(original.getText().toString());
                question.setAnswer(answer.getText().toString());
                question.setLang(langSpinner.getSelectedItemPosition());
                question.setType(typeSpinner.getSelectedItemPosition());
                question.setLevel(levelSpinner.getSelectedItemPosition());
                question.setLevelA1(levelSpinner.getSelectedItem().toString());
                long id = questions.updateItem(question);
                Fragment f = FragmentTips.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                if (id>0)
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Слово успешно добавлено", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

}
