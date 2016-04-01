package ru.shtrm.iamclever;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.RecyclerViewCacheUtil;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.shtrm.iamclever.db.adapters.ProfilesDBAdapter;
import ru.shtrm.iamclever.db.tables.Profiles;
import ru.shtrm.iamclever.fragments.FragmentAddUser;
import ru.shtrm.iamclever.fragments.FragmentAddWords;
import ru.shtrm.iamclever.fragments.FragmentEditUser;
import ru.shtrm.iamclever.fragments.FragmentIntro;
import ru.shtrm.iamclever.fragments.FragmentNewWords;
import ru.shtrm.iamclever.fragments.FragmentQuestion;
import ru.shtrm.iamclever.fragments.FragmentRating;
import ru.shtrm.iamclever.fragments.FragmentSettings;
import ru.shtrm.iamclever.fragments.FragmentTips;
import ru.shtrm.iamclever.fragments.FragmentUser;

public class DrawerActivity extends AppCompatActivity {
    private static final int PROFILE_ADD = 1;
    private static final int PROFILE_SETTING = 2;
    private static final int MAX_USER_PROFILE = 10;

    private static final int NO_FRAGMENT = 0;
    private static final int FRAGMENT_LESSON = 1;
    private static final int FRAGMENT_EXAM = 2;
    private static final int FRAGMENT_WELCOME = 3;
    private static final int FRAGMENT_TIPS = 4;
    private static final int FRAGMENT_UPDATE = 5;
    private static final int FRAGMENT_USER = 6;
    private static final int FRAGMENT_RATING = 7;
    private static final int FRAGMENT_OTHER = 10;

    protected static boolean isVisible = false;
    public boolean isOnline = true;
    private boolean isActive = false;
    private int ActiveUserID;
    private Timer tShow = new Timer();
    private Timer tQuest = new Timer();
    private Timer tTips = new Timer();
    private Handler handler = new Handler();
    ProgressDialog mProgressDialog;

    //save our header or result
    public AccountHeader headerResult = null;

    public int currentFragment = NO_FRAGMENT;

    private Drawer result = null;
    private ArrayList<IProfile> iprofilelist;
    private List<Profiles> profilesList;
    private int cnt = 0;
    private int period_quest = 1200;
    private int hour_start = 0;
    private int hour_end = 23;
    private int users_id[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_dark_toolbar);

        iprofilelist = new ArrayList<>();
        users_id = new int[MAX_USER_PROFILE];

        if (!initDB()) finish();

        ProfilesDBAdapter users = new ProfilesDBAdapter(
                new IDatabaseContext(getApplicationContext()));

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.drawable.header);
        toolbar.setSubtitle("Level up your skills");

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileSettingDrawerItem().withName("Добавить аккаунт").withDescription("Добавить новый профиль пользователя").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_insert_emoticon).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_ADD),
                        new ProfileSettingDrawerItem().withName("Редактировать профиль").withDescription("Редактировать профиль пользователя").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_edit).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_ADD) {
                            currentFragment=FRAGMENT_USER;
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentAddUser.newInstance("AddProfile")).commit();
                        }
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            currentFragment=FRAGMENT_USER;
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentEditUser.newInstance("EditProfile")).commit();
                        }
                        if (profile instanceof IDrawerItem && profile.getIdentifier() > PROFILE_SETTING) {
                            int profileId = profile.getIdentifier() - 2;
                            int profile_pos=0;
                            for (profile_pos = 0; profile_pos < iprofilelist.size(); profile_pos++)
                                if (users_id[profile_pos] == profileId) break;

                            ProfilesDBAdapter profileDBAdapter = new ProfilesDBAdapter(
                                    new IDatabaseContext(getApplicationContext()));
                            headerResult.setActiveProfile(iprofilelist.get(profile_pos));
                            profileDBAdapter.setActiveUser(profilesList.get(profile_pos).getLogin());
                            profilesList.get(profile_pos).setUserActive(1);
                            currentFragment=FRAGMENT_USER;
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentSettings.newInstance()).commit();
                        }
                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        fillProfileList();

        Profiles user = users.getActiveUser();
        if (user != null) {
            ActiveUserID = user.getId();
            isActive = user.getActive() > 0;
            if (user.getPeriod() > 0)
                period_quest = getResources().getIntArray(R.array.time_sec)[user.getPeriod()];
            if (user.getStart() > 0)
                hour_start = user.getStart();
            if (user.getEnd() > 0)
                hour_end = user.getEnd();
        }

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Настройки").withDescription("Выбор иностранных языков для изучения").withIcon(GoogleMaterial.Icon.gmd_book).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("Статистика").withDescription("Текущий счет и анализ успехов").withIcon(FontAwesome.Icon.faw_list).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("Рейтинг").withDescription("Рейтинг среди изучающих").withIcon(FontAwesome.Icon.faw_bar_chart).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName("Добавить слова").withDescription("Наполнить словарь").withIcon(FontAwesome.Icon.faw_briefcase).withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName("Обновить базу вопросов").withDescription("Загрузить с интернет-сервера").withIcon(FontAwesome.Icon.faw_download).withIdentifier(5).withSelectable(false),
                        new DividerDrawerItem(),
                        new SecondarySwitchDrawerItem().withName("On-line профиль").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener).withIdentifier(11),
                        new SecondarySwitchDrawerItem().withName("Сделать паузу в обучении").withIcon(Octicons.Icon.oct_tools).withChecked(isActive).withOnCheckedChangeListener(onCheckedChangeListener).withIdentifier(12),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Новые слова").withDescription("Запомнить новые слова").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName("Проверить себя").withDescription("Проверить свои знания").withIcon(FontAwesome.Icon.faw_check).withIdentifier(7).withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("О программе").withDescription("Информация о версии").withIcon(FontAwesome.Icon.faw_info).withIdentifier(8).withSelectable(false),
                        new PrimaryDrawerItem().withName("Выход").withDescription("Закрыть программу").withIcon(FontAwesome.Icon.faw_undo).withIdentifier(14).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            //Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                currentFragment=FRAGMENT_USER;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentSettings.newInstance()).commit();
                            } else if (drawerItem.getIdentifier() == 2) {
                                currentFragment=FRAGMENT_OTHER;
                                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ChartsFragment.newInstance("")).commit();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentUser.newInstance("")).commit();
                            } else if (drawerItem.getIdentifier() == 3) {
                                currentFragment = FRAGMENT_RATING;
                                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ChartsFragment.newInstance("")).commit();
                                mProgressDialog = new ProgressDialog(DrawerActivity.this);
                                mProgressDialog.setMessage("Синхронизируем пользователей");
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setCancelable(true);

                                final SendProfileTask sendProfileTask = new SendProfileTask(getApplicationContext(), DrawerActivity.this);
                                if (sendProfileTask!=null) {
                                    sendProfileTask.execute();
                                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            sendProfileTask.cancel(true);
                                        }
                                    });
                                }
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentRating.newInstance()).commit();
                            } else if (drawerItem.getIdentifier() == 4) {
                                currentFragment=FRAGMENT_OTHER;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentAddWords.newInstance("Add new words")).commit();
                            } else if (drawerItem.getIdentifier() == 5) {
                                currentFragment=FRAGMENT_UPDATE;
                                mProgressDialog = new ProgressDialog(DrawerActivity.this);
                                mProgressDialog.setMessage("загружаем обновления");
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setCancelable(true);
                                // execute this when the downloader must be fired
                                final DownloadTask downloadTask = new DownloadTask(getApplicationContext(), DrawerActivity.this);
                                downloadTask.execute("http://shtrm.ru/genxml.php");
                                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        downloadTask.cancel(true);
                                    }
                                });
                            } else if (drawerItem.getIdentifier() == 6) {
                                currentFragment=FRAGMENT_LESSON;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentNewWords.newInstance("Learn words")).commit();
                            } else if (drawerItem.getIdentifier() == 7) {
                                currentFragment=FRAGMENT_EXAM;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentQuestion.newInstance()).commit();
                            } else if (drawerItem.getIdentifier() == 8) {
                                //currentFragment=FRAGMENT_OTHER;
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Информация о программе")
                                        .setMessage("I Am Clever v1.0.1\nOlejek(shtrm.ru) (c) 2016")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentIntro.newInstance("Information")).commit();
                            } else if (drawerItem.getIdentifier() == 14) {
                                System.exit(0);
                            }
                            //if (intent != null) {
                                //DrawerActivity.this.startActivity(intent);
                            //}
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            if (iprofilelist.size() > 0) {
                for (cnt = 0; cnt < iprofilelist.size(); cnt++) {
                    if (ActiveUserID > 0 && iprofilelist.get(cnt).getIdentifier() == ActiveUserID + 2)
                        headerResult.setActiveProfile(iprofilelist.get(cnt));
                }
            }
        }

        Fragment f = FragmentIntro.newInstance("Demo");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

        if (ActiveUserID <= 0) {
            Toast.makeText(getApplicationContext(),
                    "Пожалуйста выберите профиль", Toast.LENGTH_LONG).show();
        }

        tShow.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isActive) {
                    PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                            PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                    screenLock.acquire();
                    checkApplicationRunning();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            RunShow(0);
                        }
                    }, 2000);
                }
            }
        }, 50 * 1000, period_quest * 200);

        tQuest.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isActive) {
                    checkApplicationRunning();
                    Calendar cal = Calendar.getInstance();
                    int hour = cal.get(Calendar.HOUR);
                    if (hour >= hour_start && hour < hour_end)
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                RunShow(1);
                            }
                        }, 2000);
                }
            }
        }, 100 * 1000, period_quest * 300);

        tTips.schedule(new TimerTask() {
            @Override
            public void run() {
                // check settings
                ProfilesDBAdapter users = new ProfilesDBAdapter(
                        new IDatabaseContext(getApplicationContext()));
                Profiles user = users.getActiveUser();
                if (user != null) {
                    if (user.getPeriod() >= 0)
                        period_quest = getResources().getIntArray(R.array.time_sec)[user.getPeriod()];
                    if (user.getStart() >= 0)
                        hour_start = user.getStart();
                    if (user.getEnd() >= 0)
                        hour_end = user.getEnd();
                }
                if (isActive) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            RunShow(2);
                        }
                    }, 2000);
                }
            }
        }, 120000, 180 * 1000);

        isVisible = true;
    }

    private void RunShow(int type) {
        if (isActive) {
            Fragment f;
            switch (type) {
                case 0:
                    if (currentFragment==FRAGMENT_LESSON || currentFragment==FRAGMENT_EXAM || currentFragment==FRAGMENT_USER) return;
                    f = FragmentNewWords.newInstance("Lesson");
                    currentFragment=FRAGMENT_LESSON;
                    break;
                case 1:
                    if (currentFragment==FRAGMENT_LESSON || currentFragment==FRAGMENT_EXAM || currentFragment==FRAGMENT_USER) return;
                    f = FragmentQuestion.newInstance();
                    currentFragment=FRAGMENT_EXAM;
                    break;
                case 2:
                    if (currentFragment==FRAGMENT_LESSON || currentFragment==FRAGMENT_EXAM || currentFragment==FRAGMENT_USER) return;
                    f = FragmentTips.newInstance();
                    currentFragment = FRAGMENT_TIPS;
                    break;
                default:
                    currentFragment=FRAGMENT_WELCOME;
                    f = FragmentIntro.newInstance("Welcome");
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f, type + "").commit();
        }
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            ProfilesDBAdapter users = new ProfilesDBAdapter(
                    new IDatabaseContext(getApplicationContext()));
            Profiles user = users.getActiveUser();
            if (drawerItem.getIdentifier() == 11) {
                if (isChecked) {
                    isOnline = true;
                    user.setUserActive(1);
                    users.updateItem(user);
                } else
                    isOnline = false;
                user.setUserActive(0);
                users.updateItem(user);
            }
            if (drawerItem.getIdentifier() == 12) {
                if (isChecked) {
                    isActive = true;
                    user.setUserActive(1);
                    users.updateItem(user);
                } else
                    isActive = false;
                user.setUserActive(0);
                users.updateItem(user);
            }
        }
    };

    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
            return false;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            //moveTaskToBack(true);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //super.onBackPressed();
        }
    }

    public void addProfile(Profiles item) {
        IProfile new_profile;
        String target_filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + getPackageName() + File.separator + "img" + File.separator + item.getImage();
        File imgFile = new File(target_filename);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            // first two elements reserved
            new_profile = new ProfileDrawerItem().withName(item.getName()).withEmail(item.getLogin()).withIcon(myBitmap).withIdentifier(item.getId() + 2).withOnDrawerItemClickListener(onDrawerItemClickListener);
        } else
            new_profile = new ProfileDrawerItem().withName(item.getName()).withEmail(item.getLogin()).withIcon(R.drawable.profile_default_small).withIdentifier(item.getId() + 2).withOnDrawerItemClickListener(onDrawerItemClickListener);
        iprofilelist.add(new_profile);
        headerResult.addProfile(new_profile, headerResult.getProfiles().size());
    }

    public void refreshProfileList() {
        ProfilesDBAdapter profileDBAdapter = new ProfilesDBAdapter(
                new IDatabaseContext(getApplicationContext()));
        profilesList = profileDBAdapter.getAllItems();
        cnt = 0;
        for (Profiles item : profilesList) {
            users_id[cnt] = item.getId();
            cnt = cnt + 1;
            if (cnt > MAX_USER_PROFILE) break;
        }
    }

    public void deleteProfile(int id) {
        for (cnt = 0; cnt < iprofilelist.size(); cnt++) {
            if (users_id[cnt] == id) {
                iprofilelist.remove(cnt);
                //headerResult.removeProfile(cnt);
                headerResult.removeProfileByIdentifier(users_id[cnt]+2);
            }
        }
        refreshProfileList();
    }

    public void fillProfileList() {
        ProfilesDBAdapter profileDBAdapter = new ProfilesDBAdapter(
                new IDatabaseContext(getApplicationContext()));
        profilesList = profileDBAdapter.getAllItems();

        cnt = 0;
        for (Profiles item : profilesList) {
            addProfile(item);
            users_id[cnt] = item.getId();
            cnt = cnt + 1;
            if (cnt > MAX_USER_PROFILE) break;
        }
    }

    public boolean initDB() {
        boolean success = false;
        DatabaseHelper helper;
        // создаём базу данных, в качестве контекста передаём свой, с
        // переопределёнными путями к базе
        try {
            helper = DatabaseHelper.getInstance(new IDatabaseContext(
                    getApplicationContext()));
            helper.isDBActual();
            success = true;
        } catch (Exception e) {
            Toast toast = Toast.makeText(this,
                    "Не удалось открыть/обновить базу данных!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
        return success;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    public DrawerActivity getDrawerActivity() {
        return DrawerActivity.this;
    }

    public void checkApplicationRunning() {
        if (!isVisible) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                setActivePackages();
            } else {
                setActivePackagesCompat();
            }
        }
    }

    void setActivePackagesCompat() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task_list = am.getRunningTasks(10);
        for (int i = 0; i < task_list.size(); i++) {
            ActivityManager.RunningTaskInfo task_info = task_list.get(i);
            if (task_info.topActivity.getPackageName().equals(getApplicationInfo().packageName))
                am.moveTaskToFront(task_info.id, 0);
        }
    }

    void setActivePackages() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (processInfo.pkgList[0].equals(getApplicationInfo().packageName))
                    am.moveTaskToFront(processInfo.uid,0);
            }
        }
    }
}