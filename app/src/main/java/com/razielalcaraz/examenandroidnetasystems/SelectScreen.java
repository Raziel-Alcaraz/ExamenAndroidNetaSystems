package com.razielalcaraz.examenandroidnetasystems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_screen);
        Intent intent = getIntent();
    }
    public void sendMessage() {

        Intent intent = new Intent(this, MainActivity.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.MainActivity";
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);


    }
    public void agregarUsr(View v){
        Intent intent = new Intent(this, UsuarioNuevo.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.MainActivity";
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);
    }
    public void verUsr(View v){
        Intent intent = new Intent(this, MainMenu.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.MainActivity";
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);
    }
}