package ru.shtrm.iamclever.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.shtrm.iamclever.DrawerActivity;
import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.LanguagesDBAdapter;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Stats;

public class FragmentAddUser extends Fragment implements View.OnClickListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private ImageView iView;
    private Spinner lang1Spinner;
    private Spinner lang2Spinner;
    private Spinner lang3Spinner;
    private EditText name,login,pass;

    public FragmentAddUser() {
        // Required empty public constructor
    }

    public static FragmentAddUser newInstance(String title) {
        //FragmentAddUser f = new FragmentAddUser();
        //Bundle args = new Bundle();
        return (new FragmentAddUser());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addprofile, container, false);
        iView = (ImageView) view.findViewById(R.id.profile_add_image);
        iView.setOnClickListener(this); // calling onClick() method
        Button one = (Button) view.findViewById(R.id.profile_button_submit);
        one.setOnClickListener(this); // calling onClick() method

        lang1Spinner = (Spinner) view.findViewById(R.id.profile_add_lang1);
        lang2Spinner = (Spinner) view.findViewById(R.id.profile_add_lang2);
        lang3Spinner = (Spinner) view.findViewById(R.id.profile_add_lang3);

        ArrayAdapter<String> langSpinnerAdapter = new ArrayAdapter<>(getContext(),
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

        name = (EditText) view.findViewById(R.id.profile_add_name);
        login = (EditText) view.findViewById(R.id.profile_add_login);
        pass = (EditText) view.findViewById(R.id.profile_add_password);
        //langSpinnerAdapter.notifyDataSetChanged();
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
                InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                iView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_add_image:
                // do your code
                pickImage();
                break;

            case R.id.profile_button_submit:
                ProfilesDBAdapter users = new ProfilesDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                StatsDBAdapter stats = new StatsDBAdapter(
                        new IDatabaseContext(getActivity().getApplicationContext()));
                Profiles profile;
                String image_name = "profile";
                profile = users.getItem(login.getText().toString());
                if (profile!=null) {
                     Toast.makeText(getActivity().getApplicationContext(),
                            "Пользователь с логином " + profile.getLogin() + " уже есть на этом устройстве", Toast.LENGTH_LONG).show();
                     break;
                    }
                if (name.getText().toString().length()<2 || login.getText().toString().length()<2 || pass.getText().toString().length()<2)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Вы должны заполнить все поля!", Toast.LENGTH_LONG).show();
                        break;
                    }
                profile = new Profiles();
                profile.setName(name.getText().toString());
                profile.setImage(image_name);
                profile.setLogin(login.getText().toString());
                profile.setPass(pass.getText().toString());
                profile.setLang1(lang1Spinner.getSelectedItemPosition());
                profile.setLang2(lang2Spinner.getSelectedItemPosition());
                profile.setLang3(lang3Spinner.getSelectedItemPosition());
                profile.setLastDate((int)(System.currentTimeMillis() / 1000L));
                profile.setUserActive(1);
                profile.setActive(1);
                //long id = users.replaceItem(name.getText().toString(),image_name, login.getText().toString(), pass.getText().toString(),lang1Spinner.getSelectedItemPosition(), lang2Spinner.getSelectedItemPosition(), lang3Spinner.getSelectedItemPosition(), (int)(System.currentTimeMillis() / 1000L), 1, true);
                long id = users.replaceItem(profile);
                try {
                    image_name ="profile"+id+".jpg";
                    storeImage(image_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stats stat = new Stats();
                stat.setProfile((int) id);
                stat.setQuestions_right(0);
                stat.setQuestions(0);
                stat.setExams(0);
                stat.setExams_complete(0);
                stat.setDays(0);
                //getStatsByProfileAndLang
                if (lang1Spinner.getSelectedItemPosition()>0)
                    {
                     stat.setLang(lang1Spinner.getSelectedItemPosition());
                     stats.replaceItem(stat);
                    }
                if (lang2Spinner.getSelectedItemPosition()>0)
                    {
                     stat.setLang(lang2Spinner.getSelectedItemPosition());
                     stats.replaceItem(stat);
                    }
                if (lang3Spinner.getSelectedItemPosition()>0)
                    {
                     stat.setLang(lang3Spinner.getSelectedItemPosition());
                     stats.replaceItem(stat);
                    }
                profile.setImage(image_name);
                users.updateItem(profile);
                if (id>0)
                    {
                       ((DrawerActivity)getActivity()).addProfile(profile);
                        ((DrawerActivity)getActivity()).refreshProfileList();
                       //headerResult.addProfile(profile.get(cnt), headerResult.getProfiles().size());
                       Fragment f = FragmentWelcome.newInstance("Welcome");
                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                    }
                break;
            default:
                break;
        }

    }

    public void storeImage(String name) throws IOException {
        Bitmap bmp;
        File sdcard = Environment.getExternalStorageDirectory();
        String targetfilename = sdcard.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + name;
        File targetfile = new File (targetfilename);
        if (!targetfile.getParentFile().exists()) {
            targetfile.getParentFile().mkdirs();
        }
        iView.buildDrawingCache();
        bmp = iView.getDrawingCache();
        FileOutputStream out = new FileOutputStream(targetfile);
        Bitmap.createScaledBitmap(bmp, 100, 100, false);
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
        out.flush();
        out.close();

        //OutputStream outStream = new FileOutputStream(targetfile);
        //byte[] buffer = new byte[1024];
        //int len = inputStream.read(buffer);
        //while (len != -1) {
        //       outStream.write(buffer, 0, len);
        //        len = inputStream.read(buffer);
        //    }
        //outStream.close();
        //Toast.makeText(getActivity().getApplicationContext(),
        //                "Не удалось создать файл " + targetfilename, Toast.LENGTH_LONG).show();
    }
}
