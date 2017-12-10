package net.zebra.ontrack.Screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.TimeManager;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.User;
import net.zebra.ontrack.tools.UserManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Log extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{
    private ListView lv;
    private TextView tv;
    private String header;
    private Button export;
    private boolean needsReversed;
    private ArrayList<Time> timeArrayList;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.log, container, false);

        lv = (ListView) v.findViewById(R.id.log_list_view);
        tv = (TextView)v.findViewById(R.id.log_header);
        export = (Button)v.findViewById(R.id.export_txt_button);

        update();

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {
                        checkExternalMedia();
                        writeToSDFile();
                    }
                        // Code for above or equal 23 API Oriented Device
                        // Your Permission granted already .Do next code
                     else {
                        requestPermission(); // Code for permission
                    }
                }
                else
                {
                    checkExternalMedia();
                    writeToSDFile();
                }
                    // Code for Below 23 API Oriented Device
                    // Do next code
                }

        });

        return v;

    }
    public void update(){
        if (UserManager.getCurrentUser() != null) {
            timeArrayList = UserManager.getCurrentUser().getTimeArray();
            if (timeArrayList != null) {
                Collections.reverse(timeArrayList);
                needsReversed = true;
            }
            else needsReversed = false;
        }


        if (UserManager.getUserList().size() > 0 && timeArrayList != null) {
            lv.setVisibility(View.VISIBLE);

            header = UserManager.getCurrentUser().getName() + ":";
            tv.setText(header);

            for (int i = 0; i < timeArrayList.size(); i++) {
                if (timeArrayList.get(i).getDate().equals("00/00/00") || timeArrayList.get(i).getTotalTime().equals("0:0:0")) {
                    timeArrayList.remove(i);
                }
            }

            String[] listItems = new String[timeArrayList.size()];
            if (timeArrayList.size() > 0) {
                if (timeArrayList.get(0) != null) {

                    for (int i = 0; i < timeArrayList.size(); i++) {
                        Time t = timeArrayList.get(i);
                        listItems[i] = t.toString();

                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                }
            }
        }
        else {
            header = "No time tracked";
            lv.setVisibility(View.INVISIBLE);
            tv.setText(header);
        }
        if (needsReversed)
            Collections.reverse(timeArrayList);
    }
    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }
    /** Method to write ascii text characters to file on SD card. Note that you must add a
     WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     a FileNotFound Exception because you won't have write permission. */

    private void writeToSDFile(){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, UserManager.getCurrentUser().getName() + ".txt");

        try {
            if (timeArrayList != null) {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                User curr = UserManager.getCurrentUser();
                pw.println("Time logged for " + curr.getName());
                pw.println("Total Time: " + curr.getTotTime());
                for (int i = 0; i < timeArrayList.size(); i++) {
                    pw.println(i + 1 + ": " + timeArrayList.get(i));
                }
                pw.flush();
                pw.close();
                f.close();
                Toast.makeText(getActivity(), "Successfully exported as a .txt to Downloads!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), "Nothing to export ;-;", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
        }
    }
}
