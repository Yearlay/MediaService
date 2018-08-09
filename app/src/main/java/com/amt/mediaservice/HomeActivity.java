package com.amt.mediaservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.amt.jni.ScanJni;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Example of a call to a native method
        TextView textView = (TextView) findViewById(R.id.sample_text);
        textView.setText((new ScanJni()).stringFromJni());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
