package ru.shtrm.iamclever;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ru.shtrm.iamclever.db.adapters.QuestionsDBAdapter;
import ru.shtrm.iamclever.db.tables.Questions;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private DrawerActivity activity;

    public DownloadTask(Context context, DrawerActivity a) {
        this.context = context;
        this.activity = a;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        String target_filename;
        HttpURLConnection connection = null;
        File sd_card = Environment.getExternalStorageDirectory();
        target_filename = sd_card.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + activity.getPackageName() + File.separator + "last.xml";
        File xml_file;

        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            input = connection.getInputStream();
            output = new FileOutputStream(target_filename);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (input != null)  input.close();
                if (output != null) output.close();
                xml_file = new  File(target_filename);
                if(xml_file.exists()){
                     QuestionsDBAdapter questions = new QuestionsDBAdapter(
                            new IDatabaseContext(activity.getApplicationContext()));
                     Questions question;
                     String temp;
                     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                     DocumentBuilder builder = factory.newDocumentBuilder();
                     Document doc = builder.parse(xml_file);
                     NodeList nodes = doc.getElementsByTagName("word");
                     for (int i = 0; i < nodes.getLength(); i++) {
                          Element word = (Element) nodes.item(i);
                          //temp = word.getNodeValue();
                         temp = word.getFirstChild().getNodeValue();
                          if (temp!=null) {
                              question = questions.getQuestionByName(temp);
                              if (question != null) continue;
                              question = new Questions();
                              question.setQuestion(0);
                              question.setOriginal(temp);
                              question.setAnswer(word.getAttribute("translate"));
                              question.setLang(Integer.parseInt(word.getAttribute("lang")));
                              question.setType(Integer.parseInt(word.getAttribute("type")));
                              question.setLevel(Integer.parseInt(word.getAttribute("level")));
                              question.setServer_uid(Integer.parseInt(word.getAttribute("id")));
                              question.setAnswer2(word.getAttribute("translate2"));
                              switch (Integer.parseInt(word.getAttribute("lang")))
                                {
                                    case 1: question.setLevelA1("A1"); break;
                                    case 2: question.setLevelA1("A2"); break;
                                    case 3: question.setLevelA1("B1"); break;
                                    case 4: question.setLevelA1("B2"); break;
                                    case 5: question.setLevelA1("C1"); break;
                                    case 6: question.setLevelA1("C2"); break;
                                    default: question.setLevelA1("A1"); break;
                                }
                              questions.updateItem(question);
                            }
                        }
                    }
                if (connection != null)
                    connection.disconnect();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        activity.mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        activity.mProgressDialog.setIndeterminate(false);
        activity.mProgressDialog.setMax(100);
        activity.mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        activity.mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"Обновление базы прошло успешно", Toast.LENGTH_SHORT).show();
    }
}
