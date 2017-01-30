package xyz.azsoftware.timusstattrak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import xyz.azsoftware.timusstattrak.lib.OriginalUserTimusData;
import xyz.azsoftware.timusstattrak.lib.PageGetter;
import xyz.azsoftware.timusstattrak.lib.TimusAuthor;
import xyz.azsoftware.timusstattrak.lib.WebPageParser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_USER = "user";
    public static final String APP_PREFERENCES_AC = "AC";
    public static final String APP_PREFERENCES_TASK_TOGO = "task";
    public static final String APP_PREFERENCES_LOGIN = "logIn";

    TextView textViewAC, textViewHow;
    ProgressBar progressBar;
    EditText editText;

    TimusAuthor timusAuthor = new TimusAuthor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editText.getText().toString();
                if(Objects.equals(userName, "") || Objects.equals(userName, "User Name")) {
                    Snackbar.make(view, "Введите пользователя", Snackbar.LENGTH_LONG).show();
                }
                else if(!isOnline()){
                    Snackbar.make(view, "Подключите интернет", Snackbar.LENGTH_LONG).show();
                }
                else{
                    URL url = null;

                    try {
                        url = new URL("http://acm.timus.ru/search.aspx?Str="+userName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String page = null;
                    try {
                        page = new PageGetter().getPage(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    timusAuthor.setUrl(url);
                    OriginalUserTimusData date = WebPageParser.PageParse(page,userName);
                    if (date.getOriginalUserData() == null){
                        Snackbar.make(view, "Данные не найдены. Проверьте имя пользователя и регистр написания", Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        timusAuthor.setAllUserDate(date);
                        sharedPreferencesPutAndApply();
                        textViewAC.setText(timusAuthor.getDataOfLastSolvedTasks());
                        textViewHow.setText(timusAuthor.getNumberOfSolvedTasks() +"\\" + mSettings.getString(APP_PREFERENCES_TASK_TOGO,"1110"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            progressBar.setProgress(Integer.parseInt(timusAuthor.getNumberOfSolvedTasks()), true);
                        } else{
                            progressBar.setProgress(Integer.parseInt(timusAuthor.getNumberOfSolvedTasks()));
                        }
                    }
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editText = (EditText) findViewById(R.id.editText);
        textViewAC = (TextView) findViewById(R.id.textViewEditAC);
        textViewHow = (TextView) findViewById(R.id.textViewHow);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            sharedPrferencesGetAndSet();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void sharedPreferencesPutAndApply() {

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_COUNTER, timusAuthor.getNumberOfSolvedTasks() );
        editor.putString(APP_PREFERENCES_USER, timusAuthor.getUserName());
        editor.putString(APP_PREFERENCES_AC, timusAuthor.getDataOfLastSolvedTasks());

        editor.apply();
    }

    protected void sharedPrferencesGetAndSet(){
        textViewHow.setText(mSettings.getString(APP_PREFERENCES_COUNTER, "0")+ "\\" + mSettings.getString(APP_PREFERENCES_TASK_TOGO,"500"));
        textViewAC.setText(mSettings.getString(APP_PREFERENCES_AC,"non"));
        editText.setText(mSettings.getString(APP_PREFERENCES_USER,"user"));
        progressBar.setProgress(Integer.parseInt(mSettings.getString(APP_PREFERENCES_COUNTER,"0")));
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Boolean b1 = mSettings.contains(APP_PREFERENCES_LOGIN);
        Boolean b2 = mSettings.getBoolean(APP_PREFERENCES_LOGIN,false);

        if (!b1 && !b2) {

            Intent intent = new Intent(MainActivity.this,LogInActivity.class);
            startActivity(intent);
        }
    }
}
