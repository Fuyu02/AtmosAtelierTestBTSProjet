package net.noel.ophelie.applicationatmostateliertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import net.noel.ophelie.applicationatmosateliertest.R;

import java.util.ArrayList;

public class AdapterPerso extends ArrayAdapter<Capteur> {
    // Déclaration d'une liste d'items
    private ArrayList<Capteur> objetsCapteur;
    private int item_id;

    // Ajout d'un écouteur de changement d'état du switch
    private OnSwitchChangeListener switchChangeListener;

    public AdapterPerso(Context context, int textViewResourceId, ArrayList<Capteur> objects) {
        super(context, textViewResourceId, objects);
        this.objetsCapteur = objects;
        this.item_id = textViewResourceId;
    }

    // Interface pour écouter les changements d'état du switch
    public interface OnSwitchChangeListener {
        void onSwitchChange(int position, boolean isChecked);
    }


    // Méthode pour définir l'écouteur de changement d'état du switch
    public void setOnSwitchChangeListener(OnSwitchChangeListener listener) {
        this.switchChangeListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Vue à mettre à jour
        View v = convertView;
        if (v == null) {
            //objet qui permet de créer une vue à partir de sa description xml
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //construction du layout
            v = inflater.inflate(this.item_id, null);
        }

        //lecture de l'objet courant
        Capteur capteurCourant = objetsCapteur.get(position);

        if (capteurCourant != null) {
            TextView txt_n = (TextView) v.findViewById(R.id.txtViewNomCapteur);
            if (txt_n != null) txt_n.setText(capteurCourant.getSensor());

            TextView txt_val = (TextView) v.findViewById(R.id.txtViewValeur);
            if (txt_val != null) txt_val.setText((int) capteurCourant.getValue());

            TextView txt_smin = (TextView) v.findViewById(R.id.textViewMin);
            if (txt_smin != null) txt_smin.setText((int)capteurCourant.getSeuilMin());

            TextView txt_smax = (TextView) v.findViewById(R.id.TxtvSeuilMax);
            if (txt_smax != null) txt_smax.setText((int) capteurCourant.getSeuilMax());

            Switch swh_alarme = (Switch) v.findViewById(R.id.SwitchSonAlarme);
            if (swh_alarme != null) {
                // Mettre à jour l'état du switch et ajouter un écouteur de changement d'état
                swh_alarme.setChecked(capteurCourant.getActiveAlert());
                swh_alarme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (switchChangeListener != null) {
                        switchChangeListener.onSwitchChange(position, isChecked);
                    }
                });
            }
        }
        return v;
    }
}
