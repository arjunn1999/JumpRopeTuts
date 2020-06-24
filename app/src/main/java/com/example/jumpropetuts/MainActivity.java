package com.example.jumpropetuts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView btnexercise;
    Animation animimgpage;
    ImageView imgpage;
    Animation bttone,bttwo,btththree,ltr;
    View bgprogress,bgprogresstop;
    GoogleSignInClient s;
    GoogleSignInOptions gso;
    private String TAG = "MainActivity";
    private GoogleSignInClient mgoogle;
    static  int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    Intent signInIntent;
    AlertDialog.Builder mbuilder;
    DatabaseReference r;
    View v;
    AlertDialog dialogs;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if(user!=null){

            Intent i = new Intent(MainActivity.this,DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bttone = AnimationUtils.loadAnimation(this,R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this,R.anim.btttwo);
        btththree = AnimationUtils.loadAnimation(this,R.anim.btththree);
        ltr = AnimationUtils.loadAnimation(this,R.anim.ltr);
        Typeface MLight = Typeface.createFromAsset(getAssets(),"fonts/MLight.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(),"fonts/MMedium.ttf");
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(),"fonts/Vidaloka.ttf");
        ((TextView)findViewById(R.id.titlepage)).setTypeface(Vidaloka);
        ((TextView)findViewById(R.id.titlepage)).setAnimation(bttone);
        ((TextView)findViewById(R.id.subtitlepage)).setTypeface(MLight);
        ((TextView)findViewById(R.id.subtitlepage)).setAnimation(bttone);
        btnexercise = (TextView) findViewById(R.id.btnexercise);
        btnexercise.setTypeface(MMedium);
        btnexercise.setAnimation(btththree);
        bgprogress = findViewById(R.id.v1);
        bgprogresstop = findViewById(R.id.v2);
        bgprogresstop.setAnimation(ltr);
        bgprogress.setAnimation(bttwo);
        animimgpage = AnimationUtils.loadAnimation(this,R.anim.animpage);
        imgpage = findViewById(R.id.imgpage);
        imgpage.setAnimation(animimgpage);
        btnexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        r = FirebaseDatabase.getInstance().getReference();
        v = getLayoutInflater().inflate(R.layout.add_weight,null);
        final EditText wt = (EditText) v.findViewById(R.id.wt);
        final Button submit = (Button) v.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uuid = user.getUid();
                r = FirebaseDatabase.getInstance().getReference();
                String weight = wt.getText().toString();
                r.child(uuid).child("Weight").setValue(weight);

                Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                dialogs.dismiss();
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(i);
            }
        });



    }
    private void signIn() {
        createLogin();
        signInIntent = mgoogle.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void createLogin(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mgoogle = GoogleSignIn.getClient(this,gso);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Signin failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            r.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(user.getUid())){
                                            Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(i);
                                        }
                                        else{
                                            mbuilder = new AlertDialog.Builder(MainActivity.this);
                                            mbuilder.setView(v);
                                            dialogs = mbuilder.create();
                                            dialogs.show();
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

}
