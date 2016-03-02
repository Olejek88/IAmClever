package ru.shtrm.iamclever.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ru.shtrm.iamclever.IDatabaseContext;
import ru.shtrm.iamclever.R;
import ru.shtrm.iamclever.db.adapters.TipsDBAdapter;
import ru.shtrm.iamclever.db.tables.Tips;

public class FragmentTips extends Fragment {
    private ImageView iView;

    public FragmentTips() {
        // Required empty public constructor
    }

    public static FragmentTips newInstance(String title) {
        FragmentTips f = new FragmentTips();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        File sd_card = Environment.getExternalStorageDirectory();
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        TextView tip_text = (TextView)view.findViewById(R.id.tips_text);
        iView = (ImageView) view.findViewById(R.id.tips_image);
        TipsDBAdapter tips = new TipsDBAdapter(
                new IDatabaseContext(getActivity().getApplicationContext()));
        Tips tip;
        tip = tips.getRandomTipsByLang(1);
        if (tip!=null) {
            String target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getActivity().getPackageName() + File.separator + "img" + File.separator + tip.getImage();
            File imgFile = new File(target_filename);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iView.setImageBitmap(myBitmap);
            }
            tip_text.setText(tip.getName());
        }
        return view;
    }
}
