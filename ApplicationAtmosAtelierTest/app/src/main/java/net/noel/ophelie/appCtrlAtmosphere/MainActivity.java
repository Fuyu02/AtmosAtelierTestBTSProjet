package net.noel.ophelie.appCtrlAtmosphere;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import net.noel.ophelie.applicationatmosateliertest.R;

import org.chromium.base.Callback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GestionCapteur.Callback {

    static final private int CODE_REQUETE_CONFIGURER = 1;
    private static final int MENU_REGLAGES = Menu.FIRST;

    //
    private String ip;
    private String port;
    private String rafraichissement;
    //
    private ArrayList<Capteur> listeCapteurs = new ArrayList<Capteur>();
    private AdapterPerso ArrayAdapterPersoCapteurs;
    private int layoutID;
    //
    //
    // Déclarez une variable MediaPlayer globalement dans l'activité
    private MediaPlayer mediaPlayer;
    private boolean alarmeActivee = false;
    //
    private TextView textViewNom;
    private TextView textViewValeur;
    private TextView textViewMin;
    private TextView textViewMax;
    private GestionCapteur gestionCapteur;
    private ConnexionWebRest connexionWebRest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateAttributsFromPreferences();


        //Partie ajouté vu que c'est l'adapteur perso
        layoutID = R.layout.itemcapteur;
        ArrayAdapterPersoCapteurs = new AdapterPerso(MainActivity.this,layoutID,listeCapteurs);
        ListView listeCapteursaffiche = (ListView) findViewById(R.id.listViewCapteurs);
        listeCapteursaffiche.setAdapter(ArrayAdapterPersoCapteurs);

        GestionCapteur gestionCapteur = new GestionCapteur(this, this,connexionWebRest);


        //pour exécuter les méthodes automatiquement
        gestionAlarme(listeCapteursaffiche);
    }

    //ALARME VISUEL ET SONORE
    void gestionAlarme(final ListView listView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60000); // Mettre une variable ici pour changer le rafraichissement à l'avenir
                        final boolean[] alarmeActiveeTemp = {false};

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Capteur capteur : listeCapteurs) {
                                    gestionCapteur.DerniereValeur(MainActivity.this, capteur.getSensor());
                                    gestionCapteur.listerCapteursAvecSeuil();
                                }
                                int childCount = listView.getChildCount();
                                for (int i = 0; i < childCount; i++) {
                                    View view = listView.getChildAt(i);

                                    if (view != null) {

                                        TextView textViewValeurTempsReel = view.findViewById(R.id.txtViewValeur);
                                        TextView textViewSeuilCapteurAlerte = view.findViewById(R.id.TxtvSeuilMax);
                                        TextView textViewSeuilCapteurAlerteMin = view.findViewById(R.id.textViewMin);
                                        Switch switchAlarmeactive = view.findViewById(R.id.SwitchSonAlarme);

                                        String valeurTempsReelStr = textViewValeurTempsReel.getText().toString();
                                        String seuilCapteurAlerteStr = textViewSeuilCapteurAlerte.getText().toString();
                                        String seuilCapteurAlerteMinSter = textViewSeuilCapteurAlerteMin.getText().toString();

                                        try {
                                            double valeurTempsReel = Double.parseDouble(valeurTempsReelStr);
                                            double seuilCapteurAlerteMax = Double.parseDouble(seuilCapteurAlerteStr);
                                            double seuilCapteurAlerteMin = Double.parseDouble(seuilCapteurAlerteMinSter);
                                            boolean switchState = switchAlarmeactive.isChecked();


                                            if (valeurTempsReel >= seuilCapteurAlerteMax || valeurTempsReel <= seuilCapteurAlerteMin) {
                                                view.setBackgroundColor(Color.RED);
                                            } else {
                                                view.setBackgroundColor(Color.TRANSPARENT);
                                            }
                                            if ((seuilCapteurAlerteMax <= valeurTempsReel || valeurTempsReel <= seuilCapteurAlerteMin) && switchState) {
                                                alarmeActiveeTemp[0] = true;
                                            }
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                final boolean finalAlarmeActivee = alarmeActiveeTemp[0];
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalAlarmeActivee) {
                                            activerAlarmeSonore();
                                        } else {
                                            arreterAlarmeSonore();
                                        }
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    // Méthode pour activer l'alarme sonore
    private void activerAlarmeSonore() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarme1); // Récupérer le fichier audio depuis les ressources
            mediaPlayer.setLooping(true); // Répéter le son en boucle
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Démarrer la lecture du son
        }
    }
    // Méthode pour arrêter l'alarme sonore
    private void arreterAlarmeSonore() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Mettre en pause la lecture du son
            mediaPlayer.seekTo(0); // Remettre la lecture au début du fich-ier audio
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Libérer les ressources du MediaPlayer lors de la destruction de l'activité
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, MENU_REGLAGES, Menu.NONE, R.string.MenuReglages);
        //getMenuInflater().inflate(R.menu.menuapp, menu);
        return true;
    }

    //
    private void AfficheAlert(final String message, final String titre)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder dlg = new
                        AlertDialog.Builder(MainActivity.this);
                dlg.setTitle(titre);
                dlg.setMessage(message);
                dlg.show();
            }
        });
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Identifier l'élément de menu sélectionné
        switch (item.getItemId()) {
            case R.id.reglages:
                Class<SetPreferencesFragmentActivity> classe =
                        SetPreferencesFragmentActivity.class;
                Intent intention = new Intent(this, classe);
                startActivityForResult(intention, CODE_REQUETE_CONFIGURER);
                return true;
            case R.id.informations:
                // Faire quelque chose lorsque l'élément "À propos" est sélectionné
                // Par exemple, afficher une boîte de dialogue avec des in-formations sur l'application
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    // Méthode pour afficher une boîte de dialogue "À propos"
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("À propos de l'application")
                .setMessage("Version 1.0\nDéveloppé NOEL Ophélie\n© 2024 SNIR PROJET")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Bouton "OK" pour fermer la boîte de dialogue
                dialog.dismiss();
            }
        })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case MENU_REGLAGES:
                try
                {
                    Class<SetPreferencesFragmentActivity> classe =
                            SetPreferencesFragmentActivity.class;
                    Intent intention = new Intent(this, classe);
                    startActivityForResult(intention, CODE_REQUETE_CONFIGURER);
                    break;
                }
                catch (Exception e)
                {
                    AfficheAlert(e.getMessage(), "Exception");
                    return false;
                }
            default:
                AfficheAlert(getResources().getString(R.string.MenuInconnu), "Erreur");
                return false;
        }
        return true;
    }
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data){
        try
        {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CODE_REQUETE_CONFIGURER)
            {
                updateAttributsFromPreferences();

            }
        }
        catch (Exception e)
        {
            AfficheAlert(e.getMessage(), "Exception OnActivityResult");
        }
    }

    //
    private void updateAttributsFromPreferences() {
        try {
            //mise à jour des paramètres
            SharedPreferences shp =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            this.ip = shp.getString(PreferencesFragments.KEY_IPSERVEUR,
                    getResources().getString(R.string.ValeurDefautParametreAdresseServeur));
            this.port = shp.getString(PreferencesFragments.KEY_PORTSERVEUR,
                    getResources().getString(R.string.ValeurDefautParametrePortServeur));
            this.rafraichissement = shp.getString(PreferencesFragments.KEY_USERNAME,
                    getResources().getString(R.string.ValeurDefautParametreRafraichissement));
        } catch (Exception e) {
            AfficheAlert(e.getMessage(), "Exception updateAttributs");
        }
    }


    @Override
    public void SeuilCapteur(List<Capteur> lc) {
        // Mettre à jour l'interface utilisateur avec les données de seuil des capteurs
        for (Capteur capteur : lc) {
            String nomCapteur = capteur.getSensor();
            double seuilMax = capteur.getSeuilMax();
            double seuilMin = capteur.getSeuilMin();

            // Mettez à jour les TextView ou tout autre composant de l'interface utilisateur avec ces valeurs
            textViewNom.setText(nomCapteur);
            textViewMax.setText(String.valueOf(seuilMax));
            textViewMin.setText(String.valueOf(seuilMin));
        }
    }

    @Override
    public void DerniereValeur(Capteur capteur) {
        // Mettre à jour l'interface utilisateur avec la dernière valeur du capteur
        String nomCapteur = capteur.getSensor();
        double derniereValeur = capteur.getValue();
        // Mettez à jour les TextView ou tout autre composant de l'interface utilisateur avec cette valeur
        textViewNom.setText(nomCapteur);
        textViewValeur.setText(String.valueOf(derniereValeur));
    }

    @Override
    public void endErreur(String e) {
        // Traitement de l'erreur
        AfficheAlert(e, "Erreur");
    }

}