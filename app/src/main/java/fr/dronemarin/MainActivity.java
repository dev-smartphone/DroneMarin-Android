package fr.dronemarin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.dronemarin.controleur.Vue1Activity;
import fr.dronemarin.controleur.Vue2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,Vue2Activity.class);
               startActivityForResult(intent,1);
           }
       });

        findViewById(R.id.lireNMEA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Vue1Activity.class);
                startActivityForResult(intent,1);
                setContentView(R.layout.activity_vue1);
            }
        });
    }
}