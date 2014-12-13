package com.micasa.myutapp;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by b1612 on 9/12/14.
 */
public class HiScreen extends Activity{
    String user;
    TextView txt_usr, logoff;
    private  ListView listview;
    private ArrayAdapter<String> listAdapter ;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_screen);
        Bundle extras = getIntent().getExtras();

        listview = (ListView) findViewById(R.id.calis);
        //String materias = extras.getString("arrMaterias");
        ArrayList<String> materias = extras.getStringArrayList("arrMaterias");

        //String[] materias = new String[] { "Mercury", "Venus", "Earth", "Mars",
          //      "Jupiter", "Saturn", "Uranus", "Neptune"};



       // ArrayList<String> lista = new ArrayList<String>();
        //lista.addAll(Arrays.asList(materias));

        final ArrayAdapter adapter = new ArrayAdapter(this,R.layout.textfields, materias);
        listview.setAdapter(adapter);


        txt_usr= (TextView) findViewById(R.id.usr_name);
        logoff= (TextView) findViewById(R.id.logoff);


        //Obtenemos datos enviados en el intent.
        if (extras != null) {
            user  = extras.getString("user");//usuario
        }else{
            user="error";
        }

        txt_usr.setText(user);//cambiamos texto al nombre del usuario logueado



        logoff.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //'cerrar  sesion' nos regresa a la ventana anterior.

                finish();
            }
        });
    }

    //Definimos que para cuando se presione la tecla BACK no volvamos para atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // no hacemos nada.
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}

