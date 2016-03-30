package ru.shtrm.iamclever;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.adapters.RatingsDBAdapter;
import ru.shtrm.iamclever.db.adapters.StatsDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.db.tables.Stats;

public class SendProfileTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private DrawerActivity activity;
    private StatsDBAdapter statsDBAdapter;
    private Stats stats;

    public SendProfileTask(Context context, DrawerActivity a) {
        this.context = context;
        this.activity = a;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        String target_filename, image;
        String response, result;
        int exams = 0,question=0,question_right=0;
        HttpURLConnection connection = null;
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        File sd_card = Environment.getExternalStorageDirectory();
        target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + activity.getPackageName() + File.separator + "img" + File.separator;
        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(activity.getApplicationContext()));
        statsDBAdapter = new StatsDBAdapter(
                new IDatabaseContext(activity.getApplicationContext()));
        Profiles user = users.getActiveUser();
        if (user != null) {
            publishProgress(10);
            if (user.getImage() != null) {
                image = target_filename + user.getImage();
                postDataParams.put("image", user.getImage());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                File imgFile = new File(image);
                if (imgFile.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(image, options);
                    ByteArrayOutputStream ba_os = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, ba_os);
                    byte[] byteImage_photo = ba_os.toByteArray();
                    String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
                    postDataParams.put("photo", encodedImage);
                }
            }
            postDataParams.put("profile", user.getId() + "");
            postDataParams.put("name", user.getName());
            postDataParams.put("login", user.getLogin());
            postDataParams.put("pass", user.getPass());
            postDataParams.put("active", user.getActive() + "");
            if (user.getLang1() > 0) {
                    stats = statsDBAdapter.getStatsByProfileAndLang(user.getId(), user.getLang1());
                    postDataParams.put("lang1", user.getLang1() + "");
                    postDataParams.put("level1", user.getLevel1() + "");
                    postDataParams.put("exam1", stats.getExams() + "");
                    postDataParams.put("question1", stats.getQuestions() + "");
                    postDataParams.put("question_right1", stats.getQuestions_right() + "");
                    exams += stats.getExams();
                    question+=stats.getQuestions();
                    question_right+=stats.getQuestions_right();
                }
            if (user.getLang2() > 0) {
                    stats = statsDBAdapter.getStatsByProfileAndLang(user.getId(), user.getLang2());
                    postDataParams.put("lang2", user.getLang2() + "");
                    postDataParams.put("level2", user.getLevel2() + "");
                    postDataParams.put("exam2", stats.getExams() + "");
                    postDataParams.put("question2", stats.getQuestions() + "");
                    postDataParams.put("question_right2", stats.getQuestions_right() + "");
                    exams += stats.getExams();
                    question+=stats.getQuestions();
                    question_right+=stats.getQuestions_right();
                }
             postDataParams.put("exams", exams + "");
             postDataParams.put("question", question + "");
             postDataParams.put("question_right", question_right + "");
             publishProgress(20);
            }
        response = performPostCall("http://shtrm.ru/senduser.php", postDataParams);
        if (response.length()>0) {
            result = "не получен ответ от сервера";
            publishProgress(50);
            postDataParams.clear();
            postDataParams.put("profile", user.getLogin() + "");
            response = performPostCall("http://shtrm.ru/getusers.php", postDataParams);
            if (response.length() > 0) {
                parseUsersRating(response);
                publishProgress(100);
                return null;
               }
            else {
                result = "не получен ответ от сервера";
                return result;
            }
        }
        else {
            result = "не получен ответ от сервера";
            return result;
        }
    }

    protected void parseUsersRating(String response) {
        RatingsDBAdapter ratings = new RatingsDBAdapter(
                new IDatabaseContext(activity.getApplicationContext()));
        String[] lines = response.split(";");
        ratings.deleteAllItem();
        if (lines!=null) {
            for (int line=0; line<lines.length; line++) {
                List<String> tList = Arrays.asList(lines[line].split(","));
                //Олежек,155,1,65
                //Лена,5,1,5
                if (tList.get(0).length()>0 && tList.get(1).length()>0) {
                    try {
                        Float f=Float.parseFloat(tList.get(1));
                        ratings.replaceItem(0, tList.get(0), line + 1, f, false);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        activity.mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        activity.mProgressDialog.setIndeterminate(false);
        activity.mProgressDialog.setMax(100);
        activity.mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        activity.mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context,"Ошибка связи с сервером: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"Синхронизация с сервером успешна", Toast.LENGTH_SHORT).show();
    }

    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
