package nero_soft.com.nero_soft;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.CallSuper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import nero_soft.com.nero_soft.DialogClass.ChangeAdmin;
import nero_soft.com.nero_soft.DialogClass.CustomUserDailog;
import nero_soft.com.nero_soft.DialogClass.DailogMessage;
import nero_soft.com.nero_soft.DialogClass.EditeDialogNameLed;
import nero_soft.com.nero_soft.DialogClass.EditeDialogUser;
import nero_soft.com.nero_soft.interFace.Setting;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    public static Activity activity;
    private DrawerLayout mDrawerLayout;
    public static NavigationView navigationView;
    public static RelativeLayout relativeLayout;
    public FloatingActionButton fab;
    private SearchView mSearchView;
    List<SearchItem> suggestionsList = new ArrayList<>();
    private SearchHistoryTable mHistoryDatabase;
    private SearchAdapter searchAdapter;
    public static Contrdol.SH SmartHome = new Contrdol.SH();
    public static Contrdol.DB DashBoard = new Contrdol.DB();
    Intent mServiceIntent;
    private ServiceConnection serviceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        relativeLayout = (RelativeLayout) findViewById(R.id.remaster);

        /*  تعريف الزر الخاص بأغلاق البحث  */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSeearchActive();
            }
        });

        /* تعريف toolbar  */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /*  تعريب اداة البحث  */
        setupSearchView();


        /* ActionBar  */
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        ServiceConnection.SPIR(); // تمهيد الحاله الخاصه بحساس الحركه
        ServiceConnection.SIR(); // تمهيد الحاله الخاصه بحساس الريموت

        /*  تعريف السيرفيس   */
        serviceConnection = new ServiceConnection(getBaseContext());
        mServiceIntent = new Intent(getBaseContext(), serviceConnection.getClass());
        startService(mServiceIntent);

        ViewPager  viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setBackgroundResource(R.color.wight);
        if (viewPager != null) {
            setupViewPager(viewPager, SmartHome, DashBoard);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.menu_likeUs:
//                Intent browserIntentYoutubeL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC7fpUDkbNDP2ioHvvTupSaQ"));
//                startActivity(browserIntentYoutubeL);
//                break;
//            case R.id.menu_FaceBook:
//                Intent browserIntentFaceBook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mor.lena.33"));
//                startActivity(browserIntentFaceBook);
//                break;
//            case R.id.menu_Youtube:
//                Intent browserIntentYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC7fpUDkbNDP2ioHvvTupSaQ"));
//                startActivity(browserIntentYoutube);
//                break;
//

            case R.id.action_CH:
                final DailogMessage message = new DailogMessage();
                message.setDialogMessage(
                        "NerO-SofT Clear History For Search",
                        "Do You Want to Accept To Clear History",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                ClearHistory();
                                message.getDialog().cancel();
                            }
                        });
                message.show(getFragmentManager(), null);
                break;

            case R.id.Menu_search:
                checkSeearchActive();
                mSearchView.open(true, item);
                return true;
            //  }

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(final ViewPager viewPager, Fragment sm, final Fragment db) {
        final Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(sm, "Smart Home");

        if (ServiceConnection.TypeOfUser == "admin")
            adapter.addFragment(db, "DashBoard");
        else
            navigationView.getMenu().findItem(R.id.setting_admin).setVisible(false);

        viewPager.setAdapter(adapter);
    }

    //-------- NavigationView and method -----------\\
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {

                        final DailogMessage message = new DailogMessage();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_PowerOF:
                                if (ServiceConnection.checkNetworkState(getApplicationContext())) {
                                    message.setDialogMessage("NerO-SofT Power OFF ALL Led",
                                            "Do You Want to Accept",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendLedMessage("allled", "ledallstop");
                                                    message.getDialog().cancel();
                                                }
                                            });
                                    message.show(getFragmentManager(), null);
                                } else
                                    ServiceConnection.MeassagDialog("Please Check Your Connection And Try Again", relativeLayout, null);

                                break;
                            case R.id.nav_PowerON:

                                if (ServiceConnection.checkNetworkState(getApplicationContext())) {
                                    message.setDialogMessage(
                                            "NerO-SofT Power ON ALL Led",
                                            "Do You Want to Accept", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendLedMessage("allled", "ledallrun");
                                                    message.getDialog().cancel();
                                                }
                                            }).show(getFragmentManager(), null);
                                } else MeassagDialog("Please Check Your Connection And Try Again");

                                break;


                            case R.id.nav_ChangeNameLed:
                                if (ServiceConnection.checkNetworkState(getApplicationContext()))
                                    new EditeDialogNameLed().show(getFragmentManager(), null);
                                else MeassagDialog("Please Check Your Connection And Try Again");
                                break;

                            case R.id.nav_CustomUser:
                                if (ServiceConnection.checkNetworkState(getApplicationContext()))
                                    new CustomUserDailog().show(getFragmentManager(), null);
                                else MeassagDialog("Please Check Your Connection And Try Again");
                                break;

                            case R.id.nav_CreateUser:
                                if (ServiceConnection.checkNetworkState(getApplicationContext()))
                                    new EditeDialogUser().CreateUser("create user").show(getFragmentManager(), null);
                                else MeassagDialog("Please Check Your Connection And Try Again");

                                break;

                            case R.id.nav_ChangeAdmin:
                                if (ServiceConnection.checkNetworkState(getApplicationContext()))
                                    new ChangeAdmin().show(getFragmentManager(), null);
                                else MeassagDialog("Please Check Your Connection And Try Again");

                                break;


                            case R.id.nav_PIRON:
                                if (ServiceConnection.checkNetworkState(getBaseContext())) {

                                    if (menuItem.isChecked()) {
                                        MeassagDialog("This item is checked");
                                        break;
                                    }
                                    message.setDialogMessage(
                                            "NerO-SofT Power ON PIR",
                                            "Do You Want to Accept To Power ON PIR",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendSensorLedMessage("sensor", "pir", "on");
                                                    menuItem.setChecked(true);
                                                    navigationView.getMenu().findItem(R.id.nav_PIROF).setChecked(false);
                                                    message.getDialog().cancel();
                                                }
                                            });
                                    message.show(getFragmentManager(), null);
                                } else MeassagDialog("Please Check Your Connection And Try Again");
                                break;

                            case R.id.nav_PIROF:
                                if (ServiceConnection.checkNetworkState(getBaseContext())) {
                                    if (menuItem.isChecked()) {
                                        MeassagDialog("This item is checked");
                                        break;
                                    }
                                    message.setDialogMessage(
                                            "NerO-SofT Power OFF PIR",
                                            "Do You Want to Accept To Power OFF PIR",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendSensorLedMessage("sensor", "pir", "of");
                                                    menuItem.setChecked(true);
                                                    navigationView.getMenu().findItem(R.id.nav_PIRON).setChecked(false);
                                                    message.getDialog().cancel();
                                                }
                                            });
                                    message.show(getFragmentManager(), null);

                                } else MeassagDialog("Please Check Your Connection And Try Again");
                                break;

                            case R.id.nav_IRON:
                                if (ServiceConnection.checkNetworkState(getBaseContext())) {
                                    if (menuItem.isChecked()) {
                                        MeassagDialog("This item is checked");
                                        break;
                                    }
                                    message.setDialogMessage(
                                            "NerO-SofT Power ON IR",
                                            "Do You Want to Accept To Power ON IR",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendSensorLedMessage("sensor", "ir", "on");
                                                    menuItem.setChecked(true);
                                                    navigationView.getMenu().findItem(R.id.nav_IROF).setChecked(false);
                                                    message.getDialog().cancel();
                                                }
                                            });
                                    message.show(getFragmentManager(), null);

                                } else MeassagDialog("Please Check Your Connection And Try Again");

                                break;

                            case R.id.nav_IROF:
                                if (ServiceConnection.checkNetworkState(getBaseContext())) {
                                    if (menuItem.isChecked()) {
                                        MeassagDialog("This item is checked");
                                        break;
                                    }
                                    message.setDialogMessage(
                                            "NerO-SofT Power OF IR",
                                            "Do You Want to Accept To Power OF IR",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    ServiceConnection.sendSensorLedMessage("sensor", "ir", "of");
                                                    menuItem.setChecked(true);
                                                    navigationView.getMenu().findItem(R.id.nav_IRON).setChecked(false);
                                                    message.getDialog().cancel();
                                                }
                                            });
                                    message.show(getFragmentManager(), null);
                                } else MeassagDialog("Please Check Your Connection And Try Again");
                                break;
                            case R.id.log_out:


                                new DailogMessage().setDialogMessage("Exit", "Do You Want To Log Out From Smart Home", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ServiceConnection.logOut(new Setting.GetPost.getSettingLedInfo() {

                                            @Override
                                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                                try {
                                                    if (result.getBoolean("state")) {
                                                        stopService(mServiceIntent);
                                                        finish();
                                                    }else {
                                                        stopService(mServiceIntent);
                                                        finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    stopService(mServiceIntent);
                                                    finish();
                                                }
                                            }
                                        }, new JSONObject());

                                    }
                                }).show(getFragmentManager(), null);


                                break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    void ClearHistory() {

                mHistoryDatabase.clearDatabase();
                searchAdapter.mResultList.clear(); //   مسح للهستورع
                Snackbar.make(relativeLayout, "Search history deleted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }





    void MeassagDialog(String s) {

        Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    //--------- search View -----------\\
    void setupSearchView() {
        mSearchView = (SearchView) findViewById(R.id.searchView);
        setSearchView();
        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        mSearchView.setTheme(SearchView.THEME_LIGHT, true);
        //  mSearchView.setTextInput("neroSarch");

    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        // mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setHint(R.string.search);
            mSearchView.setVoiceText("Speak Now");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query);
                    //mSearchView.close(false);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {
                    suggestionsList.clear();
                }

                @Override
                public void onClose() {
                    //.....
                }
            });
            mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {

                }
            });

            if (mSearchView.getAdapter() == null) {

                searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView =  view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        getData(query);
                        // mSearchView.close(false);
                    }
                });
                mSearchView.setAdapter(searchAdapter);
            }

        }
    }


    protected void getData(String text) {
        mHistoryDatabase.addItem(new SearchItem(text));
        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        mSearchView.setTheme(SearchView.THEME_LIGHT, true);
        mSearchView.setTextInput(text);
        goSearch(text);

    }

    void goSearch(String SearchItem) {
        int ItemSize = 0;
        for (int i = 0; i < ServiceConnection.Led.length(); i++) {
            try {
                if (!ServiceConnection.Led.getJSONObject(i).getString("name").contains(SearchItem)){
                    ServiceConnection.Led.getJSONObject(i).put("visibility", "0");
                    ItemSize++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (ItemSize > 0){
            SmartHome.smartHome.notifyDataSetChanged();
            fab.show();
        }
        else ServiceConnection.MeassagDialog("No Item Found", relativeLayout, null);



    }

    void checkSeearchActive() {
        for (int i = 0; i < ServiceConnection.Led.length(); i++) {
            try {
                ServiceConnection.Led.getJSONObject(i).put("visibility", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        SmartHome.smartHome.notifyDataSetChanged();
        fab.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    if (mSearchView != null) {
                        mSearchView.setQuery(searchWrd);
                        mSearchView.setTextInput(searchWrd);

                    }
                }
            }
        }

    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }

    // Before 2.0
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // activity
    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }
}

