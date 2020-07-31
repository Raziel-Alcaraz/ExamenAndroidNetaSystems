package com.razielalcaraz.examenandroidnetasystems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import static com.razielalcaraz.examenandroidnetasystems.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "debuging";
    private FirebaseAuth mAuth;
    public static final String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.SegundaPantalla";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
if(mAuth !=null){
    updateUI(null);
}

    }


    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        super.onStart();

        Log.w(TAG, "START");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);


    }
    private void updateUI(FirebaseUser user2){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "User:" + user);
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            sendMessage();
        }
    }
    public void sendMessage() {

        Intent intent = new Intent(this, SegundaPantalla.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);


    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validarForma()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
public void cliqueado( View  v ){
        if (v.getId()==R.id.emailSignInButton){
            EditText imeil =(EditText)findViewById(R.id.fieldEmail);
            EditText pas = (EditText)findViewById(R.id.fieldPassword);
            signIn(imeil.getText().toString(),pas.getText().toString());
        }
        else if (v.getId()==R.id.emailCreateAccountButton){
                EditText imeil =(EditText)findViewById(R.id.fieldEmail);
                EditText pas = (EditText)findViewById(R.id.fieldPassword);
                createAccount(imeil.getText().toString(),pas.getText().toString());
            }

}
private void signIn(String email, String password){
    Log.d(TAG, "signIn:" + email);
    if (!validarForma()) {
        return;
    }
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        // ...
                    }

                    // ...
                }
            });
}
private boolean validarForma(){
        if(true){
            return true;
        }else{
            return false;
        }

}


}