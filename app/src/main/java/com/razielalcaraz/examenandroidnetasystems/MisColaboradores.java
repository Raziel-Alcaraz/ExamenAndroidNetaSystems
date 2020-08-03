package com.razielalcaraz.examenandroidnetasystems;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MisColaboradores extends AppCompatActivity {
String TAG = "MisColaboradores";
public static int idEste;
   public static Map<String, Object> todos =new HashMap<>();
    public static Map<String, Object> uno =new HashMap<>();
    public static Map<String, Object> ids =new HashMap<>();
    public static Boolean abrirTodos = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_colaboradores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

    }
    @Override
    protected void onStart(){
        super.onStart();
        poblarLista();
    }
    public void agregarUsuario(View v) {

        Intent intent = new Intent(this, UsuarioNuevo.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.MainActivity";
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);


    }
    public void poblarLista(){
       todos =new HashMap<>();
      uno =new HashMap<>();
       ids =new HashMap<>();
        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LinearLayout lista = (LinearLayout) findViewById(R.id.listaVista);
                            lista.removeAllViews();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(true) {
                                    final int aidi = Integer.parseInt(document.getId());
                                    Button btn = new Button(getApplicationContext());
                                    btn.setId(Integer.parseInt(document.getId()));

                                    final String name = (String) document.get("name");
                                    final String det = (String) document.get("mail");
                                    final Map<String, Object> ubicacion = (Map) document.get("location");

                                    btn.setText(name +"\n"+det);

                                     lista = (LinearLayout) findViewById(R.id.listaVista);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    lista.addView(btn, params);
                                    Log.v("Mapador","La id del boton es: "+btn.getId());
                                    ids.put(String.valueOf(btn.getId()),aidi);

                             todos.put(document.getId()+"-loc", ubicacion);
                                todos.put(document.getId()+"-nam", name);
                                    todos.put(document.getId()+"-det", det);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                       abrirTodos = false;
                                       idEste = view.getId();

                                       Log.v("Mapador","La id del boton es: "+idEste);
                                        startActivity(intent);
                                    }
                                });
                            }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void verTodos(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        abrirTodos = true;
        idEste = view.getId();
        startActivity(intent);
    }
}