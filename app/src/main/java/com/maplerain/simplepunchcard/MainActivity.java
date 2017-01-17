package com.maplerain.simplepunchcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends AppCompatActivity {
    private String FOLDERNAME = "";
    private String HEADER_STRING = "Date,Time,Record_Type,Hours,Minutes\n";
    public final static String FILE_NAME = "";
    private String recipient = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FOLDERNAME = getString(R.string.data_folder);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        recipient = sharedPref.getString("pref_email_to", getString(R.string.default_recipient));

    }

    public void onCheckInBtn(View view) {
        /*
            Writes a check-in ("IN") record when "Check In" button is pressed.
         */
        TimeHelper timenow = new TimeHelper();
        String recordType = "IN";
        String hours = "";
        String minutes = "";
        String filename = timenow.getYearMth() + ".csv";
        String outputString = timenow.getDate() + "," + timenow.getTime() + "," +
                recordType + "," + hours + "," + minutes + "\n";
        System.out.println(outputString);
        writeRecord(filename, outputString);
    }

    public void onCheckOutBtn(View view) {
        /*
            Writes a check-out ("OUT") record when "Check Out" button is pressed.
         */
        TimeHelper timenow = new TimeHelper();
        String recordType = "OUT";
        String hours = "";
        String minutes = "";
        String filename = timenow.getYearMth() + ".csv";
        String outputString = timenow.getDate() + "," + timenow.getTime() + "," +
                recordType + "," + hours + "," + minutes + "\n";
        System.out.println(outputString);
        writeRecord(filename, outputString);
    }

    public void onLogTimeBtn(View view) {
        /*
            Writes a log-time ("LOG_HOURS") record when "Log Time" button is pressed.
         */
        TimeHelper timenow = new TimeHelper();
        String recordType = "LOG_HOURS";
        EditText hour_input = UiUtils.findView(this, R.id.hour_input);
        EditText minute_input = UiUtils.findView(this, R.id.minute_input);
        String hours = hour_input.getText().toString();
        String minutes = minute_input.getText().toString();
        String filename = timenow.getYearMth() + ".csv";
        String outputString = timenow.getDate() + "," + timenow.getTime() + "," +
                recordType + "," + hours + "," + minutes + "\n";
        System.out.println(outputString);
        writeRecord(filename, outputString);
    }

    public void onShowFileBtn(View view) {
        /*
            Opens a file dialog, shows contents of the selected file.
         */

        FileChooser fileDialog = new FileChooser(this);
        fileDialog.setFileListener(new FileChooser.FileSelectedListener() {
            @Override public void fileSelected(final File file) {
                // shows contents of file in a fragment
                Intent intent = new Intent(MainActivity.this, ViewFileActivity.class);
                intent.putExtra(FILE_NAME, file.getName());
                startActivity(intent);
            }
        });
        Context context = getApplicationContext();
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + FOLDERNAME;
        File subFolder = new File(folder);
        fileDialog.refresh(subFolder);
        fileDialog.showDialog();
    }

    public void onSendFileBtn(View view) {
        /*
            Opens a file dialog, emails the selected file.
         */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        recipient = sharedPref.getString("pref_email_to", getString(R.string.default_recipient));
        FileChooser fileDialog = new FileChooser(this);
        fileDialog.setFileListener(new FileChooser.FileSelectedListener() {
            @Override public void fileSelected(final File file) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // Make sure the app can send emails, otherwise show an error message.
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
                boolean isIntentSafe = activities.size() > 0;
                //if (isIntentSafe) {
                    Uri fileUri = getUriForFile(getApplicationContext(), "com.maplerain.fileprovider", file);
                    String[] recipients = new String[1];
                    recipients[0] = recipient;
                    // The intent does not have a URI, so declare the "text/plain" MIME type
                    emailIntent.setType("text/plain");
                    //emailIntent.setData(fileUri);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients); // recipients
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
                    emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // Start chooser for user to choose emailer to send the file.
                    startActivity(Intent.createChooser(emailIntent , getString(R.string.chooser_msg)));
                //} else {
                //    Toast.makeText(MainActivity.this, getString(R.string.no_emailer), Toast.LENGTH_LONG).show();
                //}
            }
        });
        Context context = getApplicationContext();
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + FOLDERNAME;
        File subFolder = new File(folder);
        fileDialog.refresh(subFolder);
        fileDialog.showDialog();

    }

    public void onDeleteFileBtn(View view) {
        /*
            Opens a file dialog, deletes the selected file.
         */
        FileChooser fileDialog = new FileChooser(this);
        fileDialog.setFileListener(new FileChooser.FileSelectedListener() {
            @Override public void fileSelected(final File file) {
                String filename = file.getName();
                file.delete();
                String displayText = filename + getString(R.string.deleted);
                Toast.makeText(MainActivity.this, displayText, Toast.LENGTH_LONG).show();
            }
        });
        Context context = getApplicationContext();
        String folder = context.getFilesDir().getAbsolutePath() + File.separator + FOLDERNAME;
        File subFolder = new File(folder);
        fileDialog.refresh(subFolder);
        fileDialog.showDialog();
    }

    public void onSettingsBtn(View view) {
        /*
            Opens a fragment for user to change settings.
         */
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }

    public void onExitBtn(View view) {
        /*
            Exits the application.
         */
        finish();
    }

    public void writeRecord(String filename, String record) {
        /*
            Writes a record string into the specified file.
         */
        FileWriter outputStream = null;
        Boolean writeHeader = true;
        try {
            Context context = getApplicationContext();
            String folder = context.getFilesDir().getAbsolutePath() + File.separator + FOLDERNAME;
            File subFolder = new File(folder);
            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }
            File outfile = new File(subFolder, filename);
            if(outfile.exists()) {
                //checks if file exists, if not, need to write a header
                writeHeader = false;
            }
            outputStream = new FileWriter(outfile, true);
            if(writeHeader) {
                //write the header first, if necessary
                outputStream.write(HEADER_STRING);
            }
            outputStream.write(record); //writes actual record
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {   // always close the file
            if (outputStream!= null) try {
                outputStream.close();
            } catch (IOException ioe2) {
                // just ignore it
            }
        } // end try/catch/finally
    }
}
