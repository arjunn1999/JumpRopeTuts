package com.example.jumpropetuts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartWorkoutActivity extends AppCompatActivity {
    long START_TIME_IN_MILLIS ,mTimeLeftInMillis;
    CountDownTimer count;
    Animation bttone,bttwo;
    TextView time,titlepage,subtitlepage,btnexercise;
    CardView workout;
    FirebaseUser user;
    DatabaseReference ref;
    int pos,weight;
    ImageView img;
    TextView title,subtitle;
    View V;
    AlertDialog.Builder mbuilder;
    AlertDialog dialogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        Intent i = getIntent();
        pos = i.getIntExtra("position",0);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        time =(TextView) findViewById(R.id.timer);
        title = (TextView) findViewById(R.id.title) ;
        img = (ImageView) findViewById(R.id.timg);
        subtitle = (TextView) findViewById(R.id.interval);
        img.setImageResource(Workouts.workimgs[pos]);
        subtitle.setText(Workouts.descs[pos]);
        title.setText(Workouts.workouts[pos]);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                weight = Integer.parseInt(dataSnapshot.child("Weight").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        titlepage = (TextView) findViewById(R.id.titlepagew);
        subtitlepage = (TextView) findViewById(R.id.subtitlepagew);
        workout = (CardView) findViewById(R.id.cardView);

        bttone = AnimationUtils.loadAnimation(this,R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this,R.anim.btttwo);
        workout.startAnimation(bttone);
        titlepage.startAnimation(bttwo);
        subtitlepage.startAnimation(bttwo);
        btnexercise = (TextView) findViewById(R.id.btnexercise);
        V = getLayoutInflater().inflate(R.layout.set_time,null);
        Button add = (Button) V.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText) V.findViewById(R.id.wt);
                START_TIME_IN_MILLIS = Integer.parseInt(e.getText().toString())*1000;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                dialogs.dismiss();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {

                }
                startTimer();
            }
        });
        btnexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbuilder = new AlertDialog.Builder(StartWorkoutActivity.this);
                mbuilder.setView(V);
                dialogs = mbuilder.create();
                dialogs.show();
            }
        });
        subtitlepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(Intent.ACTION_VIEW);
                intentObj.setData(Uri.parse(Workouts.videolinks[pos]));
                startActivity(intentObj);
            }
        });
    }

    void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis/1000)/60;
        int seconds = (int)(mTimeLeftInMillis/1000)%60;
        String timeleft = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        time.setText(timeleft);

    }
    public void startTimer(){
        count = new CountDownTimer(mTimeLeftInMillis,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Map<String,Object> workout = new HashMap<>();


                long t = START_TIME_IN_MILLIS/1000;
                double c = Workouts.calcCalories(Workouts.met[pos],weight,t);
                workout.put("Title",Workouts.workouts[pos]);
                workout.put("Duration",t);
                workout.put("Calories",c);
                ref.child("Workout").setValue(workout);
                Toast.makeText(StartWorkoutActivity.this, "You have burnt "+c +" calories!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartWorkoutActivity.this,WorkoutActivity.class));
                finish();
            }

        }.start();
    }
}
