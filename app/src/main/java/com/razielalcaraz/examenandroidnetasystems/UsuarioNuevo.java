package com.razielalcaraz.examenandroidnetasystems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UsuarioNuevo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "UsuarioNuevo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_nuevo);
        Intent intent = getIntent();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    public void agregarUserYa(View view){
       EditText textoName = (EditText) findViewById(R.id.nameAdd);
        EditText textoMail = (EditText) findViewById(R.id.mailAdd);
        if(textoName.getText().toString().length()>3 && textoMail.getText().toString().length()>3){
            String idi = String.valueOf(new Random().nextInt(9)+1).concat(String.valueOf(new Random().nextInt(10)))
                    .concat(String.valueOf(new Random().nextInt(10))).concat(String.valueOf(new Random().nextInt(10)));
            Double latitud = 19.29 + (19.31 - 19.29)*new Random().nextDouble();
            Double longitud = -99.30 + (-99.21 + 99.30)*new Random().nextDouble();
            String uid = (String) idi;
            String name = (String) textoName.getText().toString();

            String lat = (String)  String.valueOf(latitud.toString());
            String log = (String)  String.valueOf(longitud.toString());
            Map<String, Object> location =new HashMap<>();
            location.put("lat", lat);
            location.put("log", log);

            String mail = (String)  (String) textoMail.getText().toString();
            Map<String, Object> user = new HashMap<>();
            user.put("id", uid);
            user.put("name", name);
            user.put("location", location);
            user.put("mail", mail);
            db.collection("employees").document(uid)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
            Intent intent = new Intent(getApplicationContext(), MisColaboradores.class);

            startActivity(intent);

         //   return true;
        }
    }
    public void salirLogout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}