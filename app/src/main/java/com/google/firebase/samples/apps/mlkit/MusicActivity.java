package com.google.firebase.samples.apps.mlkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MusicActivity extends AppCompatActivity {

    float happiness;
    boolean happyFlag = false;
    ImageView happy,sad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        happy = findViewById(R.id.happy);
        sad = findViewById(R.id.sad);

        Bundle bundle= getIntent().getExtras();

        if (bundle!= null) {
            happiness = bundle.getFloat("happiness");
            happyFlag = bundle.getBoolean("happyFlag");
        }
        if (happyFlag){

            happy.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(),"You are soooo happy",Toast.LENGTH_LONG).show();
        }else{
            sad.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Uh hoo....You are soooo Sad",Toast.LENGTH_LONG).show();


        }

    }
}
