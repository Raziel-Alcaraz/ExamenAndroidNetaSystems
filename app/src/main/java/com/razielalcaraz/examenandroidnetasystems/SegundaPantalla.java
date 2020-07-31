package com.razielalcaraz.examenandroidnetasystems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.lang.String.*;

public class SegundaPantalla extends AppCompatActivity {
    private static final String TAG = "debuging";
    private FirebaseAuth mAuth;
    RequestQueue requestQueue;
    String file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);
        Intent intent = getIntent();
 //       String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        Log.d(TAG, message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth !=null){
            updateUI(null);
        }
        EditText pas = (EditText)findViewById(R.id.documento);
        pas.setText("https://www.dropbox.com/s/uiqhemeo7ly8igo/getFile.json?dl=0");

        //--------------------del volley inicio------------------------------------------------------


// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();




        //------------------------------del volley fin-----------------------------------------------
    }
    public void bajarDatos(View v){
        EditText pas = (EditText)findViewById(R.id.documento);
        Log.d(TAG, "Clickeado, bajar datos:" + pas.getText());
        bajarLleison();
    }
    public void salirLogout(View v){
        FirebaseAuth.getInstance().signOut();
        sendMessage();
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
        }else{
sendMessage();
        }
    }
    public void sendMessage() {

        Intent intent = new Intent(this, MainActivity.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String EXTRA_MESSAGE = "com.razielalcaraz.examenandroidnetasystems.MainActivity";
        intent.putExtra(EXTRA_MESSAGE, user);
        startActivity(intent);


    }

   public void bajarLleison() {
       EditText pas = (EditText)findViewById(R.id.documento);

       String url = (String) pas.getText().toString();

Log.v(TAG,"Bajando lleison");
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
               (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {
                       Log.d(TAG, "Respuesta: "+response);
                     //
                       Gson gson = new Gson();
                    //   String name = response.optString("data.file");
                    //   int profileIconId =response.optInt("profileIconId");
                       try {
                           Log.d(TAG, "Lleison bajado");
                         JSONObject data =  response.getJSONObject("data");
                          file = data.getString("file");
                           Log.d(TAG, "-------FILE:" + file);
                           bajarFail(file);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // TODO: Handle error

                   }
               });

// Access the RequestQueue through your singleton class.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
   }
void bajarFail(String link){
        if(link.contains("firebasestorage")){
            Log.d("ok", "-----bajando archivo de firebase---");

            try {
                bajardeFireBase(link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(TAG, "Bajando file");
            String mUrl = link;
            InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                    new Response.Listener<byte[]>() {
                        @Override
                        public void onResponse(byte[] response) {
                            // TODO handle the response
                            try {
                                if (response != null) {

                                    FileOutputStream outputStream;
                                    String name = "employees_data.json.zip";
                                    outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                                    outputStream.write(response);
                                    outputStream.close();
                                   // Toast.makeText(Context, "Bajado de un server distinto a Firebase! Puede causar errores.",  Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                Log.d("ERROR", "-----NO PUDO BAJAR ARCHIVO---");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO handle the error
                    error.printStackTrace();
                }
            }, null);
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
            mRequestQueue.add(request);
            Log.d(TAG, "Fail bajado");

            try {
                openfile("employes_data.json.zip", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
void openfile(String cual,FileDownloadTask.TaskSnapshot tasksnapshot) throws IOException {
if(tasksnapshot!=null) {
}else{

    String directorio;
    directorio = getFilesDir().getAbsolutePath().toString();
    String[] files = fileList();
    Log.d(TAG, "-------------Filelist:"+fileList());
    Log.d(TAG, "Fail bajado"+directorio);
    //employes_data.json
    cual = directorio+"/employes_data.json.zip";
    Uri uri = Uri.parse(cual);
    File cual2 = new File(this.getFilesDir().getAbsolutePath(), "/employes_data.json.zip");
   //JSONObject archivozip = new JSONObject((Map) new ZipFile(cual2));
   Map mapa = ((Map) new ZipFile(valueOf(cual2)));
    JSONObject archivozip = new JSONObject(mapa);
    Log.d(TAG, "JSON to string del zip"+archivozip.toString());
}
}
void bajardeFireBase(String link) throws IOException {
    FirebaseStorage storage = FirebaseStorage.getInstance();
   // StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/examenandroid-79fa7.appspot.com/o/employees_data.json.zip");
    StorageReference httpsReference = storage.getReferenceFromUrl(link);


    final File localFile = File.createTempFile("employees_data.json", ".zip");

    httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            // Local temp file has been created
            Log.d(TAG, "Bajada la file----"+taskSnapshot);
         //   try {
            //    ZipFile archivobajado =  new ZipFile(localFile);
              //  JSONObject archivozip = new JSONObject(archivobajado);
                Log.d(TAG, "JSON to string del zip: "+localFile.toString());
            try {
             //   String dstPath = Environment.getExternalStorageDirectory() + File.separator + "com.razielalcaraz.examenandroidnetasystems" + File.separator+"employees_data.json";
             //   File dst = new File(dstPath);

             //   unzip(localFile, dst);
                //getFilesDir().getAbsolutePath()
                //+ "/employees_data.json"
                unzip(localFile, new File(valueOf(getApplicationContext().getFilesDir())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            File[] carpeta = Environment.getExternalStorageDirectory().listFiles();
           // Environment.getExternalStorageDirectory() + File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator



    Log.d(TAG, "carpeta: " +Environment.getExternalStorageDirectory() + File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator+"/employees_data.json"
    );

        //abrir el archivo json------------------------------------------------------------------
        Log.d("string desde funcion",readFromFile(getApplicationContext(), Environment.getExternalStorageDirectory() + File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator+"employees_data.json"
        ));
        Gson gson = new Gson();
        String obyeto = gson.toJson(readFromFile(getApplicationContext(),
                Environment.getExternalStorageDirectory() + File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator +"employees_data.json"
        ));

        Log.d("disque json",obyeto);

       // JSONArray obj = gson.fromJson(obyeto,Employee.class);
        //Log.d(TAG,obj);

        //abrir el archivo json------------------------------------------------------------------

       // Log.d(TAG, "parseado ok3!! " +obyeto2);

     //   JSONObject archivojsonparseado = new JSONObject(archivojsonparseado0);
    //    JSONObject data =   archivojsonparseado.getJSONObject("data");
    //    JSONArray employees = data.getJSONArray("employees");
    //    for(int j = 0; j<employees.length();j++){
    //       Log.d(TAG, "---Empleado: " +employees.getJSONObject(j).toString());
    //    }



        }


    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle any errors
        }


    });


    }



    public void unzip(File zipFile, File targetDirectory) throws IOException {
 //       File export = new File(String.valueOf(getApplicationContext().getFilesDir()+"/employees_data.json"));
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
 //               FileOutputStream fout2 = new FileOutputStream(export);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
//                    fout2.write(buffer, 0, count);
                    Log.d(TAG, "extrayendo"+buffer);
                } finally {
                    Log.d(TAG, "----extrayendo---"+fout);

                    fout.close();
                //    fout2.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
         //   export=file;
            }
        } finally {
            Log.d(TAG, "----extraido---"+zis);

            zis.close();
            //+ File.separator + "com.razielalcaraz.examenandroidnetasystems"
            String dstPath = Environment.getExternalStorageDirectory() + File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator;
            File dst = new File(dstPath);
File newExport = new File(String.valueOf(getApplicationContext().getFilesDir()+ File.separator +"/employees_data.json"));
            exportFile(newExport, dst);
        }

    }
    private String readFromFile(Context context,String file) {

           String ret = "";

        try {

         File file2= new File(Environment.getExternalStorageDirectory() +
                 File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator +"employees_data.json");
            InputStream inputStream = context.openFileInput(Environment.getExternalStorageDirectory() +
                    File.separator+ "com.razielalcaraz.examenandroidnetasystems"  + File.separator +"employees_data.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    private static File exportFile(File src, File dst) throws IOException {
/* call this func:
String dstPath = Environment.getExternalStorageDirectory() + File.separator + "myApp" + File.separator;
File dst = new File(dstPath);

exportFile(pictureFile, dst);
 */
        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        File expFile = new File(dst.getPath() + File.separator + "employees_data.json");
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
        Log.d(TAG,"si funciona");
        return expFile;
    }

}
