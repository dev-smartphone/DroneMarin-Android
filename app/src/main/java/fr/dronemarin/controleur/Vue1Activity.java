package fr.dronemarin.controleur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.dronemarin.R;
import fr.dronemarin.modele.PositionGPS;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class Vue1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);






        Socket socket;
        // DataInputStream userInput;
        PrintStream theOutputStream;

        try {

            InetAddress serveur = InetAddress.getByName("127.0.0.1");
            socket = new Socket(serveur, 1130);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            boolean ok=true;
            //out.println(args[1]);
            PositionGPS test;
            int i=0;
            while(ok)
            {
                if(i!=0)
                {
                    test=new PositionGPS(in.readLine());
                    //test.transferertrame();
                    if(test.getTrameGPS())
                    {

                        float lon=test.getLongitude();
                        float lat=test.getLatitude();

                        System.out.println("Lat: "+lat+" Lon: "+lon);
                    }


                }

                // System.out.println(in.readLine());
                i++;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}




