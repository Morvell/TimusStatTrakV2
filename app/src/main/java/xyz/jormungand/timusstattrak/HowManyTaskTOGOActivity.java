package xyz.jormungand.timusstattrak;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class HowManyTaskTOGOActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    EditText mTextValue;
    SeekBar seekBar;
    Button buttonSave;

    private SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_USER = "user";
    public static final String APP_PREFERENCES_AC = "AC";
    public static final String APP_PREFERENCES_TASK_TOGO = "task";
    public static final String APP_PREFERENCES_LOGIN = "logIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_many_task_togo);

        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        mTextValue = (EditText) findViewById(R.id.editTextValue);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(Integer.parseInt(mSettings.getString(APP_PREFERENCES_TASK_TOGO,"0")));
        mTextValue.setSelection(mTextValue.length());
        mTextValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextValue.setSelection(mTextValue.length());
                if (!Objects.equals(mTextValue.getText().toString(), ""))
                {
                    int value = Integer.valueOf(mTextValue.getText().toString());
                    if (value > 1100) {
                        Toast.makeText(getApplicationContext(),"Max value: 1100",Toast.LENGTH_SHORT).show();
                        mTextValue.setText("1100");
                        value = 1100;
                    }

                    seekBar.setProgress(value);
                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HowManyTaskTOGOActivity.this,MainActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_TASK_TOGO, mTextValue.getText().toString());
                editor.apply();
                finish();
            }
        });
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("ca-app-pub-8467305463887516~6261728988").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        mTextValue.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}


