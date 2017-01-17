package com.maplerain.simplepunchcard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ViewFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        Intent intent = getIntent();
        String filename = intent.getStringExtra(MainActivity.FILE_NAME);
        loadText(filename);
    }

    public void loadText(String filename) {
        Context context = getApplicationContext();
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + getString(R.string.data_folder);
        File subFolder = new File(folder);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
        File infile = new File(subFolder, filename);
        if(infile.exists()) {
            try {
                BufferedReader r = new BufferedReader(new FileReader(infile));
                StringBuilder total = new StringBuilder();
                String line;
                while((line = r.readLine()) != null) {
                    total.append(line);
                    total.append("\n");
                }
                r.close();
                // then get the TextView and set its text
                TextView txtView = (TextView)findViewById(R.id.text_window);
                txtView.setText(total.toString());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            Toast.makeText(ViewFileActivity.this, R.string.file_not_exist, Toast.LENGTH_LONG).show();
        }
    }

    public void onCloseBtn(View view) {
        finish();
    }
}
