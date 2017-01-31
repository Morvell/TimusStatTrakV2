package xyz.jormungand.timusstattrak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import xyz.jormungand.timusstattrak.lib.OriginalUserTimusData;
import xyz.jormungand.timusstattrak.lib.PageGetter;
import xyz.jormungand.timusstattrak.lib.TimusAuthor;
import xyz.jormungand.timusstattrak.lib.WebPageParser;

public class LogInActivity extends AppCompatActivity {

    EditText editText, editTextTaskTOGO;
    TextView textViewTaskTOGO;
    Button buttonAccept, buttonForward;
    CheckBox checkBox;

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_USER = "user";
    public static final String APP_PREFERENCES_AC = "AC";
    public static final String APP_PREFERENCES_TASK_TOGO = "task";
    public static final String APP_PREFERENCES_LOGIN = "logIn";

    TimusAuthor timusAuthor = new TimusAuthor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editText = (EditText) findViewById(R.id.editTextForName);
        editTextTaskTOGO = (EditText) findViewById(R.id.editTextTaskTOGO);
        textViewTaskTOGO = (TextView) findViewById(R.id.textViewTaskTOGO);
        buttonAccept = (Button) findViewById(R.id.buttonAccept);
        buttonForward = (Button) findViewById(R.id.buttonForward);
        checkBox = (CheckBox) findViewById(R.id.checkBox2);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = editText.getText().toString();
                if (Objects.equals(userName, "") || Objects.equals(userName, "User Name")) {
                    Snackbar.make(view, "Введите пользователя", Snackbar.LENGTH_LONG).show();
                } else if (!isOnline()) {
                    Snackbar.make(view, "Подключите интернет", Snackbar.LENGTH_LONG).show();
                } else {
                    URL url = null;

                    try {
                        url = new URL("http://acm.timus.ru/search.aspx?Str=" + userName);
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
                    OriginalUserTimusData date = WebPageParser.PageParse(page, userName);
                    if (date.getOriginalUserData() == null) {
                        Snackbar.make(view, "Данные не найдены. Проверьте имя пользователя и регистр написания", Snackbar.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    } else {
                        timusAuthor.setAllUserDate(date);
                        sharedPreferencesPutAndApply();
                        checkBox.setVisibility(View.VISIBLE);
                        checkBox.setChecked(true);
                        buttonForward.setEnabled(true);
                        textViewTaskTOGO.setVisibility(View.VISIBLE);
                        editTextTaskTOGO.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_TASK_TOGO, editTextTaskTOGO.getText().toString());
                editor.putBoolean(APP_PREFERENCES_LOGIN, true);
                editor.apply();
                finish();
            }
        });


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

            protected void sharedPreferencesPutAndApply() {

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_COUNTER, timusAuthor.getNumberOfSolvedTasks() );
                editor.putString(APP_PREFERENCES_USER, timusAuthor.getUserName());
                editor.putString(APP_PREFERENCES_AC, timusAuthor.getDataOfLastSolvedTasks());

                editor.apply();
            }
}
