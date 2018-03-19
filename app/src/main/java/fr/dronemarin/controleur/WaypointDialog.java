package fr.dronemarin.controleur;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import fr.dronemarin.R;
import fr.dronemarin.WaypointActivity;
import fr.dronemarin.modele.Modele;
import fr.dronemarin.modele.Waypoint;

/**
 * Created by joshu on 19/03/2018.
 */

public class WaypointDialog extends android.support.v4.app.DialogFragment {


    Waypoint waypoint;
    public interface WaypointsDialogListener {
        void onDialogPositiveClick(DialogInterface dialog);
        void onDialogNegativeClick(DialogInterface dialog);
    }



    private WaypointsDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        if( getArguments() != null && getArguments().getSerializable("waypoint")!= null){
            waypoint = (Waypoint) getArguments().getSerializable("waypoint");
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Propriété du waypoint")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(getArguments()!=null && getArguments().getSerializable("waypoint")!=null){
                            Waypoint w = (Waypoint) getArguments().getSerializable("waypoint");
                            EditText speed = ((AlertDialog) dialog).findViewById(R.id.editText2);
                            CheckBox picture = ((AlertDialog) dialog).findViewById(R.id.checkBox);
                            CheckBox stat = ((AlertDialog) dialog).findViewById(R.id.checkBox2);
                            w.setVitesse(Double.parseDouble(speed.getText().toString()));
                            w.setPriseImage(picture.isChecked());
                            w.setPointStationnaire(stat.isChecked());
                        }
                        else{
                            mListener.onDialogPositiveClick(dialog);
                        }

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(dialog);
                    }
                });



        View rootView = inflater.inflate(R.layout.dialog_waypoint,null);

        if(waypoint!=null){
            ViewGroup layout = rootView.findViewById(R.id.addButtons);
            Button suppr = new Button(layout.getContext());
            suppr.setBackgroundColor(Color.rgb(149,0,0));
            suppr.setText("Supprimer");
            suppr.setTextColor(Color.WHITE);
            suppr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Modele.getInstance().getWaypoints().remove(waypoint);
                    WaypointDialog.this.dismiss();
                    WaypointActivity act = (WaypointActivity) getArguments().getSerializable("activity");
                    act.getmMap().clear();
                    act.refreshWithExisting();
                }
            });
            layout.addView(suppr);

            EditText speed = rootView.findViewById(R.id.editText2);
            speed.setText(""+waypoint.getVitesse());
            CheckBox picture = rootView.findViewById(R.id.checkBox);
            picture.setChecked(waypoint.isPriseImage());


            CheckBox stat = rootView.findViewById(R.id.checkBox2);
            stat.setChecked(waypoint.isPointStationnaire());
        }

        builder.setView(rootView);
        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (WaypointsDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }




}
