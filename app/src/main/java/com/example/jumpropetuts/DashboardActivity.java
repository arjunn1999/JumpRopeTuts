package com.example.jumpropetuts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;

public class DashboardActivity extends AppCompatActivity {
    TextView name,weight,burnt,duration,title;
    DatabaseReference r;
    FirebaseUser user;
    ImageView i;
    CardView c;
    Button change;
    View v;
    AlertDialog dialogs;
    AlertDialog.Builder mbuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        user = FirebaseAuth.getInstance().getCurrentUser();

        i = (ImageView) findViewById(R.id.profile);
        Glide.with(DashboardActivity.this).load(user.getPhotoUrl().toString()).into(i);
        name = (TextView) findViewById(R.id.name);
        weight = (TextView) findViewById(R.id.weight);
        title = (TextView) findViewById(R.id.date);
        burnt = (TextView) findViewById(R.id.burnt);
        duration = (TextView) findViewById(R.id.duration);
        name.setText(user.getDisplayName());
        r= FirebaseDatabase.getInstance().getReference().child(user.getUid());

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String w = dataSnapshot.child("Weight").getValue().toString()+" kg";
                weight.setText(w);
                if(dataSnapshot.hasChild("Workout")){
                    c = (CardView) findViewById(R.id.cardView3);
                    c.setVisibility(View.VISIBLE);

                    title = (TextView) findViewById(R.id.date);
                    burnt = (TextView) findViewById(R.id.burnt);
                    duration = (TextView) findViewById(R.id.duration);
                     DataSnapshot s =  dataSnapshot.child("Workout");
                    String t = s.child("Title").getValue().toString();
                    String dur = s.child("Duration").getValue().toString();
                    String cal = s.child("Calories").getValue().toString();

                    title.setText(t);
                    burnt.setText(cal);
                    duration.setText(dur+" seconds");
                }
                else{
                    c = (CardView) findViewById(R.id.cardView3);
                    c.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        v = getLayoutInflater().inflate(R.layout.add_weight,null);
        final EditText wt = (EditText) v.findViewById(R.id.wt);
        final Button submit = (Button) v.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuid = user.getUid();
                r = FirebaseDatabase.getInstance().getReference();
                String weight = wt.getText().toString();
                r.child(uuid).child("Weight").setValue(weight);
                dialogs.dismiss();

            }
        });
        change = (Button) findViewById(R.id.change_wt);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View z) {
                mbuilder = new AlertDialog.Builder(DashboardActivity.this);
                mbuilder.setView(v);
                dialogs = mbuilder.create();
                dialogs.show();
            }
        });
    }
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(DashboardActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DashboardActivity.this,MainActivity.class));
    }
    public void gotoWorkout(View v){
        Intent i = new Intent(DashboardActivity.this,WorkoutActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
