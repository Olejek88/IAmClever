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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.shtrm.iamclever.DrawerActivity;
import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;

public class FragmentEditUser extends Fragment implements View.OnClickListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 1;
    private ImageView iView;
    private EditText name,login,pass;
    private String image_name;

    public FragmentEditUser() {
        // Required empty public constructor
    }

    public static FragmentEditUser newInstance(String title) {
        //FragmentEditUser f = new FragmentEditUser();
        //Bundle args = new Bundle();
        return (new FragmentEditUser());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        iView = (ImageView) view.findViewById(R.id.profile_add_image);
        iView.setOnClickListener(this); // calling onClick() method
        Button one = (Button) view.findViewById(R.id.profile_button_submit);
        one.setOnClickListener(this); // calling onClick() method
        Button delete = (Button) view.findViewById(R.id.profile_button_delete);
        delete.setOnClickListener(this); // calling onClick() method

        name = (EditText) view.findViewById(R.id.profile_add_name);
        login = (EditText) view.findViewById(R.id.profile_add_login);
        pass = (EditText) view.findViewById(R.id.profile_add_password);
        login.setEnabled(false);
        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Profiles user = users.getActiveUser();
        if (user!=null) {
            pass.setText(user.getPass());
            login.setText(user.getLogin());
            name.setText(user.getName());
            image_name = user.getImage();

            File sdcard = Environment.getExternalStorageDirectory();
            String targetfilename = sdcard.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + user.getImage();
            File imgFile = new  File(targetfilename);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iView.setImageBitmap(myBitmap);
            }
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
                InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
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

            case R.id.profile_add_image:
                // do your code
                pickImage();
                break;

            case R.id.profile_button_submit:
                if (name.getText().toString().length()<2 || login.getText().toString().length()<2 || pass.getText().toString().length()<2)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Вы должны заполнить все поля!", Toast.LENGTH_LONG).show();
                        break;
                    }
                try {
                    // название файла аватара не меняется
                    storeImage(image_name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Long id = users.replaceItem(name.getText().toString(),image_name, login.getText().toString(), pass.getText().toString());
                if (id>0)
                    {
                       Fragment f = FragmentWelcome.newInstance("Welcome");
                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                    }
                break;

            case R.id.profile_button_delete:
                int user_id = users.getActiveUser().getId();
                stats.deleteAllItem(user_id);
                users.deleteItem(login.getText().toString());
                ((DrawerActivity)getActivity()).deleteProfile(user_id);
                Fragment f = FragmentWelcome.newInstance("Welcome");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;

            default:
                break;
        }

    }

    public void storeImage(String name) throws IOException {
        Bitmap bmp;
        File sd_card = Environment.getExternalStorageDirectory();
        String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + name;
        File target_file = new File (target_filename);
        if (!target_file.getParentFile().exists()) {
            target_file.getParentFile().mkdirs();
        }
        iView.buildDrawingCache();

        bmp = iView.getDrawingCache();
        FileOutputStream out = new FileOutputStream(target_file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
        out.flush();
        out.close();
    }
}
