package fr.dronemarin.controleur;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.dronemarin.R;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

public class WaypointManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_manager);

        int index = 0;
        for(Waypoint w : Modele.getInstance().getWaypoints()){
            Button b = new Button(this);
            b.setText(index+"");
            index++;
            ViewGroup layout = (ViewGroup)findViewById(R.id.linear);
            layout.addView(b);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
