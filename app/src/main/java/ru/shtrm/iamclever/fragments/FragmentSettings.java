package ru.shtrm.iamclever.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;

public class FragmentSettings extends Fragment implements View.OnClickListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private InputStream inputStream;
    private Button one;
    private ArrayAdapter<String> langSpinnerAdapter;

    private CheckBox check;

    private ArrayList<CheckBox> checks;

    private Spinner lang1Spinner;
    private Spinner lang2Spinner;
    private Spinner lang3Spinner;

    private Spinner startSpinner;
    private Spinner endSpinner;
    private Spinner periodSpinner;

    public FragmentSettings() {
        // Required empty public constructor
    }

    public static FragmentSettings newInstance() {
        //FragmentSettings f = new FragmentSettings();
        //Bundle args = new Bundle();
        return (new FragmentSettings());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        checks = new ArrayList<>();

        lang1Spinner = (Spinner) view.findViewById(R.id.profile_add_lang1);
        lang2Spinner = (Spinner) view.findViewById(R.id.profile_add_lang2);
        lang3Spinner = (Spinner) view.findViewById(R.id.profile_add_lang3);

        startSpinner = (Spinner) view.findViewById(R.id.profile_choose_time_start);
        endSpinner = (Spinner) view.findViewById(R.id.profile_choose_time_end);
        periodSpinner = (Spinner) view.findViewById(R.id.profile_choose_period);

        langSpinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<String>());
        lang1Spinner.setAdapter(langSpinnerAdapter);
        lang2Spinner.setAdapter(langSpinnerAdapter);
        lang3Spinner.setAdapter(langSpinnerAdapter);

        LanguagesDBAdapter langDBAdapter = new LanguagesDBAdapter(
                new IDatabaseContext(getActivity()));
        ArrayList<String> langList = langDBAdapter.getItems();
        String no_lang_selected="Выберите язык";
        langSpinnerAdapter.clear();
        langSpinnerAdapter.add(no_lang_selected);
        langSpinnerAdapter.addAll(langList);

        one = (Button) view.findViewById(R.id.profile_button_submit);
        one.setOnClickListener(this);

        check = (CheckBox) view.findViewById(R.id.profile_check);

        checks.add((CheckBox) view.findViewById(R.id.settings_monday));
        checks.add((CheckBox) view.findViewById(R.id.settings_tuesday));
        checks.add((CheckBox) view.findViewById(R.id.settings_wednesday));
        checks.add((CheckBox) view.findViewById(R.id.settings_thursday));
        checks.add((CheckBox) view.findViewById(R.id.settings_friday));
        checks.add((CheckBox) view.findViewById(R.id.settings_saturday));
        checks.add((CheckBox) view.findViewById(R.id.settings_sunday));

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();

        if (user!=null) {
            lang1Spinner.setSelection(user.getLang1());
            lang2Spinner.setSelection(user.getLang2());
            lang3Spinner.setSelection(user.getLang3());
            if (user.getActive()>0) check.setChecked(true);
            else check.setChecked(false);
            for (int wd=0; wd<7; wd++) {
                if (user.getCheck_weekday(wd+1) > 0) checks.get(wd).setChecked(true);
                else checks.get(wd).setChecked(false);
            }
            if (user.getEnd()>0 && user.getEnd()<endSpinner.getAdapter().getCount())
                endSpinner.setSelection(user.getEnd());
            if (user.getStart()>0 && user.getStart()<startSpinner.getAdapter().getCount())
                startSpinner.setSelection(user.getStart());
            if (user.getPeriod()>0 && user.getPeriod()<periodSpinner.getAdapter().getCount())
                periodSpinner.setSelection(user.getPeriod());

        }
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
                ImageView iView = null;
                inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                iView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));

        switch (v.getId()) {
            case R.id.profile_button_submit:
                Profiles user = users.getActiveUser();
                if (user != null) {
                    user.setLang1(lang1Spinner.getSelectedItemPosition());
                    user.setLang2(lang2Spinner.getSelectedItemPosition());
                    user.setLang3(lang3Spinner.getSelectedItemPosition());
                    user.setActive(check.isChecked() ? 1 : 0);
                    user.setCheck_weekday(1, checks.get(0).isChecked() ? 1 : 0);
                    user.setCheck_weekday(2, checks.get(1).isChecked() ? 1 : 0);
                    user.setCheck_weekday(3, checks.get(2).isChecked() ? 1 : 0);
                    user.setCheck_weekday(4, checks.get(3).isChecked() ? 1 : 0);
                    user.setCheck_weekday(5, checks.get(4).isChecked() ? 1 : 0);
                    user.setCheck_weekday(6, checks.get(5).isChecked() ? 1 : 0);
                    user.setCheck_weekday(7, checks.get(6).isChecked() ? 1 : 0);
                    user.setEnd(endSpinner.getSelectedItemPosition());
                    user.setStart(startSpinner.getSelectedItemPosition());
                    user.setPeriod(periodSpinner.getSelectedItemPosition());
                    Long id = users.updateItem(user);
                    if (id > 0) {
                        Fragment f = FragmentTips.newInstance();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                    }
                }
                break;
            default:
                break;
        }

    }
}
