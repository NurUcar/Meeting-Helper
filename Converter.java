package a.monster.meetinghelper2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



public class Converter extends AppCompatActivity {

    // VARIABLES
    ImageButton startConvert;
    ImageButton stopConvert;
    TextView topTxt;
    TextView Areatxt;
    EditText StrHour;
    EditText StrMinute;
    EditText StrSecond;
    TextView TfAm;
    Integer counter ;
    Integer timeHour;
    Integer timeMinute;
    Integer timeSecond=0;
    Integer second = 0;
    Integer sysHour;
    Integer sysMinute;
    Integer total = 0;
    Boolean stop = false;
    SpeechRecognizer MyRecognizer;
    Intent MyRecognizerIntent;
    File fFile;
    File sFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        // Call and initialize
        startConvert = this.findViewById(R.id.imageButton);
        stopConvert = this.findViewById(R.id.imageButton2);
        topTxt = this.findViewById(R.id.textView);
        Areatxt = this.findViewById(R.id.textView3);
        StrHour = this.findViewById(R.id.editText1);
        StrMinute = this.findViewById(R.id.editText2);
        StrSecond = this.findViewById(R.id.editText3);
        TfAm = this.findViewById(R.id.textView2);
        topTxt.setText("Enter meeting end time and press record button");
        counter = 0;

        // if the folder not exist the folder will create


        // if the folder not exist the folder will create

        String fFolder = "/Meeting Helper";
        String Directory = Environment.getExternalStorageDirectory().toString();
        fFile = new File(Directory +fFolder);
        boolean success = false;

        if (!fFile.exists()) {
            success = fFile.mkdirs();
        }
        if (!success) {


        }
        String sFolder="/Meetings";
        String Directory1=fFile.toString();
        sFile= new File(Directory1+sFolder);
        boolean success1 = false;

        if (!sFile.exists()) {
            success1 = sFile.mkdirs();
        }
        if (!success1) {


        }

        // Create Text File in Meetings Subfolder
        String rootPath =String.valueOf( sFile+sFolder);
        File root1 = new File(rootPath);
        String h = DateFormat.format("MM-dd-yyyy-h-mm", System.currentTimeMillis()).toString();
        final File myFile = new File(root1 + h+".txt");


        //Voice to world


        MyRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        MyRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        MyRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        MyRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        MyRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }


            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }


            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    Areatxt.setText(matches.get(0));
                    try {

                        FileOutputStream out = new FileOutputStream(myFile,true);
                        out.write((Areatxt.getText().toString()+ "\n").getBytes());



                        // Writes bytes from the specified byte array to this file output stream

                    out.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }





            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        //Start Button OnClick Methods

        startConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topTxt.setText("Listening..");


                //take system time
                Calendar now = Calendar.getInstance();
                sysHour = now.get(Calendar.HOUR);
                sysMinute = now.get(Calendar.MINUTE);


                if (counter == 0) {
                    timeHour = Integer.parseInt(StrHour.getText().toString());
                    timeMinute = Integer.parseInt(StrMinute.getText().toString());
                    if (Integer.parseInt(StrHour.getText().toString()) > 12) {
                        Toast.makeText(getApplicationContext(), "Please enter meeting time correctly..", Toast.LENGTH_SHORT).show();
                        StrHour.setText("");
                        topTxt.setText("Enter meeting end time and press recording button");

                    } else if (Integer.parseInt(StrMinute.getText().toString()) > 60) {
                        Toast.makeText(getApplicationContext(), "Please enter meeting time correctly..", Toast.LENGTH_SHORT).show();
                        StrMinute.setText("");
                        topTxt.setText("Enter meeting end time and press recording button");

                    }


                }



                counter++;
                if (counter == 1) {
                    topTxt.setText("Please press recording button for start record");
                    // calculate minute

                    if (timeMinute < sysMinute) {
                        timeMinute = 60 - sysMinute + timeMinute;
                    }
                    else{
                        timeMinute -= sysMinute;
                    }
                    //calculate hour

                    sysHour+=1;
                    timeHour+=1;
                    if (sysHour > timeHour) {

                        timeHour = 12 - sysHour + timeHour;
                    }
                    else if(timeHour==sysHour){
                        timeHour=0;
                    }
                    else {
                        timeHour -= sysHour;
                    }
                    //calculate second

                    if (timeSecond < second) {
                        timeSecond = 60 - second + timeSecond;
                    } else {
                        timeSecond -= second;
                    }
                    StrHour.setText(String.format("%d", timeHour));
                    StrMinute.setText(String.format("%d", timeMinute));
                    StrSecond.setText(String.format("%d", timeSecond));


                    //hour to millisecond
                    total = timeHour * 3600000;
                    //minute to millisecond
                    total = total + (timeMinute * 60000);
                    //second to millisecond
                    total = total + (timeSecond * 1000);


                }
                else {
                    topTxt.setText("MEETING STARTED!");
                    //CountDownTimer
                    new CountDownTimer(total, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (stop == true) {
                                cancel(); // If press to stopConverter, stop value will be true and countdown will be canceled
                            } else {

                                //Record
                                convertDialog();

                                int myHour = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                int myMinute = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                int mySecond = (int) (millisUntilFinished / (1000) % 60);
                                StrHour.setText(String.format("%d", myHour));
                                StrMinute.setText(String.format("%d", myMinute));
                                StrSecond.setText(String.format("%d", mySecond));
                            }

                        }


                        @Override
                        public void onFinish() {
                            topTxt.setText("Timer finished!");

                        }
                    }.start();

                    // Start Listening

                    counter = 0;

                }

            }


        });

        //Stop Button Onclick Methods

    stopConvert.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Stop record
            MyRecognizer.stopListening();
            stop = Boolean.valueOf("true");
            topTxt.setText("Meeting Finished!");
            StrHour.setText("00");
            StrMinute.setText("00");
            StrSecond.setText("00");
            timeHour = 0;
            timeMinute = 0;
            timeSecond = 0;
            counter = 0;

        }
    });

    }

    //Record function

    private void convertDialog() {

        MyRecognizer.startListening(MyRecognizerIntent);

    }

    //Voice permission
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat
                    .checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)) {
                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(i);
                finish();

            }
        }
    }






}