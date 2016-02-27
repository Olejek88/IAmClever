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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;

public class FragmentSettings extends Fragment implements View.OnClickListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private ImageView iView;
    private InputStream inputStream;
    private Button one;
    private EditText name,login,pass,image;
    private String image_name;
    private ArrayAdapter<String> langSpinnerAdapter;
    private CheckBox check;

    private Spinner lang1Spinner;
    private Spinner lang2Spinner;
    private Spinner lang3Spinner;

    public FragmentSettings() {
        // Required empty public constructor
    }

    public static FragmentSettings newInstance(String title) {
        FragmentSettings f = new FragmentSettings();
        Bundle args = new Bundle();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        lang1Spinner = (Spinner) view.findViewById(R.id.profile_add_lang1);
        lang2Spinner = (Spinner) view.findViewById(R.id.profile_add_lang2);
        lang3Spinner = (Spinner) view.findViewById(R.id.profile_add_lang3);

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
        one.setOnClickListener(this); // calling onClick() method

        check = (CheckBox) view.findViewById(R.id.profile_check);

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();

        if (user!=null) {
            lang1Spinner.setSelection(user.getLang1());
            lang2Spinner.setSelection(user.getLang2());
            lang3Spinner.setSelection(user.getLang3());
            if (user.getActive()>0) check.setChecked(true);
            else check.setChecked(false);
        }
        return view;
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
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
        StatsDBAdapter stats = new StatsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));

        switch (v.getId()) {
            case R.id.profile_button_submit:
                Profiles user = users.getActiveUser();
                Long id = users.replaceItem(lang1Spinner.getSelectedItemPosition(),lang2Spinner.getSelectedItemPosition(),lang3Spinner.getSelectedItemPosition(),1,user.getLogin());
                if (id>0)
                    {
                       Fragment f = FragmentWelcome.newInstance("Welcome");
                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                    }
                break;
            default:
                break;
        }

    }
}
