package com.micasa.myutapp;
/**
 * Created by b1612 on 9/12/14.
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.micasa.myutapp.library.Httppostaux;
import com.micasa.myutapp.library.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class Login extends Activity {
    /** Called when the activity is first created. */
    EditText user;
    EditText pass;
    Button blogin;
    TextView registrar;
    Httppostaux post;
    // String URL_connect="http://www.scandroidtest.site90.com/acces.php";
//String IP_Server="192.168.60.213";//IP DE NUESTRO PC
//String URL_connect="http://"+IP_Server+"/droidlogin/acces.php";//ruta en donde estan nuestros archivos
    String URL_connect="http://www.imaginetech.com.mx/pruebas/hola/acces.php";
    boolean result_back;
    private ProgressDialog pDialog;
    private JSONObject json_data, json; //creamos un objeto JSON
    public String materias;
    public JSONArray jdata;
    JSONParser jsonParser = new JSONParser();

    //ArrayList<HashMap<String,String>> productList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> productList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        post=new Httppostaux();
        user= (EditText) findViewById(R.id.edusuario);
        pass= (EditText) findViewById(R.id.edpassword);


        blogin= (Button) findViewById(R.id.Blogin);
        registrar=(TextView) findViewById(R.id.link_to_register);
//Login button action
        blogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
//Extreamos datos de los EditText
                String usuario=user.getText().toString();
                String passw=pass.getText().toString();
                ((EditText) findViewById(R.id.edusuario)).setText("");
                ((EditText) findViewById(R.id.edpassword)).setText("");
//verificamos si estan en blanco
                if( checklogindata( usuario , passw )==true){
//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
                    new asynclogin().execute(usuario,passw);
                }else{
//si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    err_login();
                }
            }
        });
        registrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
//Abre el navegador al formulario adduser.html
                String url = "http://www.imaginetech.com.mx/pruebas/adduser.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
    //vibra y muestra un Toast
    public void err_login(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Nombre de usuario o password incorrectos", Toast.LENGTH_SHORT);
        toast1.show();
    }
    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String username ,String password ) {
        int logstatus=-1;


        ArrayList<NameValuePair> params= new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("usuario",username));
        params.add(new BasicNameValuePair("password",password));

        json = jsonParser.makeHttpRequest(URL_connect,"POST",params);

        //jdata=post.getserverdata(postparameters2send, URL_connect);
        try {
            logstatus = json.getInt("logstatus");
        } catch (Exception e){

        }

        SystemClock.sleep(950);
//si lo que obtuvimos no es null
        if (logstatus==1){
// JSONObject json_data; //creamos un objeto JSON
            try {
                JSONArray materias = json.getJSONArray("materias");

                for(int i=0;i<materias.length();i++){

                    JSONObject c = materias.getJSONObject(i);

                    String name = c.getString("nombre");
                    String calif   = c.getString("calif");
                    String uno = name + " "+calif;


                    productList.add(uno);

                }

                //json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                //logstatus=json_data.getInt("logstatus");//accedemos al valor

                Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
//validamos el valor obtenido
            if (logstatus==0){// [{"logstatus":"0"}]
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "valido");
                return true;
            }
        }else{ //json obtenido invalido verificar parte WEB.
            Log.e("JSON ", "ERROR");
            return false;
        }
    }
    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){
        if (username.equals("") || password.equals("")){
            Log.e("Login ui", "checklogindata user or pass error");
            return false;
        }else{
            return true;
        }
    }
    /* CLASE ASYNCTASK
    *
    * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
    * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir
    * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
    * ademas observariamos el mensaje de que la app no responde.
    */
    class asynclogin extends AsyncTask< String, String, String > {
        String user,pass;
        protected void onPreExecute() {
//para el progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
//obtnemos usr y pass
            user=params[0];
            pass=params[1];
//enviamos y recibimos y analizamos los datos en segundo plano.
            if (loginstatus(user,pass)==true){
                return "ok"; //login valido
            }else{
                return "err"; //login invalido
            }
        }
        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String result) {
            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);
            if (result.equals("ok")){
                Intent i=new Intent(Login.this, HiScreen.class);
                i.putExtra("user",user);
                i.putExtra("arrMaterias",productList);
                startActivity(i);
            }else{
                err_login();
            }
        }
    }


}
