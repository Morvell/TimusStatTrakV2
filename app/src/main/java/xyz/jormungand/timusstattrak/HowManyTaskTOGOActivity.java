package xyz.jormungand.timusstattrak;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class HowManyTaskTOGOActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    EditText mTextValue;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_many_task_togo);

        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        mTextValue = (EditText) findViewById(R.id.editTextValue);

        seekBar.setOnSeekBarChangeListener(this);
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


