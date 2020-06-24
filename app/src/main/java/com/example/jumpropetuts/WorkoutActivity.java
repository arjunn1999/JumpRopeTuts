package com.example.jumpropetuts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {
    ScrollView sc;
    TextView titlepage,subtitlepage;
    Animation bttone,bttwo;
    View divpage;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        Typeface MLight = Typeface.createFromAsset(getAssets(),"fonts/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(),"fonts/MMedium.ttf");
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(),"fonts/Vidaloka.ttf");
        ((TextView)findViewById(R.id.titlepagew)).setTypeface(Vidaloka);
        ((TextView)findViewById(R.id.subtitlepagew)).setTypeface(MLight);
        lv = (ListView)findViewById(R.id.lv);

        MyAdadpter adapter = new MyAdadpter();
        lv.setAdapter(adapter);
        bttone = AnimationUtils.loadAnimation(this,R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this,R.anim.btttwo);
        titlepage = (TextView) findViewById(R.id.titlepagew);
        subtitlepage = (TextView) findViewById(R.id.subtitlepagew);
        titlepage.startAnimation(bttone);
        subtitlepage.startAnimation(bttone);
        lv.startAnimation(bttwo);
        divpage = findViewById(R.id.view);
        divpage.startAnimation(bttone);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent a = new Intent(WorkoutActivity.this,StartWorkoutActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.putExtra("position",position);
                startActivity(a);
                finish();
            }
        });


    }
    class MyAdadpter extends BaseAdapter{

        @Override
        public int getCount() {
            return Workouts.workouts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_layout,null);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView subtitle = (TextView) v.findViewById(R.id.subtitle);
            ImageView img = (ImageView) v.findViewById(R.id.img);
            title.setText(Workouts.workouts[position]);
            subtitle.setText(Workouts.descs[position]);
            img.setImageResource(Workouts.workimgs[position]);

            return v;
        }
    }
    public void btnclick(View v){
        Intent a = new Intent(WorkoutActivity.this,DashboardActivity.class);
        startActivity(a);
        finish();
    }
}
