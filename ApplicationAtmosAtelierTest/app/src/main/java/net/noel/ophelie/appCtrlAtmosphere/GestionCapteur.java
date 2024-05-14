package net.noel.ophelie.appCtrlAtmosphere;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionCapteur {

    private Callback callback;
    private Context context;
    private ConnexionWebRest connexionWebRest;

    public interface Callback {
        //il en manque ??
        void SeuilCapteur(List<Capteur> lc);
        void DerniereValeur(Capteur capteur);
        void EtatAlarme(Capteur capteur); //pas de méthode qui utlise ça pour callback

        void endErreur(String e);
    }

    public GestionCapteur(Context context, ConnexionWebRest connexionWebRest) {
        this.callback = (Callback) context;
        this.connexionWebRest = connexionWebRest; // Initialisation de connexionWebRest
    }

    public void listerCapteursAvecSeuil() {
        String url = "http://" + connexionWebRest.getAdresseIP() + ":" + connexionWebRest.getPort() + "/Projet2024-Controle_Atmosphere_Atelier-API_SERVER/Index.php/lireSeuilsCapteurs";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        List<Capteur> capteurs = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String sensor = jsonObject.getString("sensor");
                                double seuilMin = jsonObject.getDouble("seuilMin");
                                double seuilMax = jsonObject.getDouble("seuilMax");
                                boolean valideSeuilMin = jsonObject.getInt("valideSeuilMin") == 1;
                                boolean valideSeuilMax = jsonObject.getInt("valideSeuilMax") == 1;
                                boolean activeAlert = jsonObject.getInt("ActiveAlert") == 1;
                                String unite = jsonObject.getString("unite");
                                double seuilEchMin = jsonObject.getDouble("seuilEchMin");
                                double seuilEchMax = jsonObject.getDouble("seuilEchMax");
                                String id_wasp = "";
                                Double value = 0.0;
                                Date timestamp = new Date();

                                Capteur capteur = new Capteur(id_wasp, sensor, value, timestamp, seuilMin, seuilMax,
                                        valideSeuilMin, valideSeuilMax, activeAlert, unite,
                                        seuilEchMin, seuilEchMax);
                                capteurs.add(capteur);
                            }
                            callback.SeuilCapteur(capteurs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.endErreur("Erreur lors de l'analyse JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.endErreur("Erreur de réseau");
            }
        });

        requestQueue.add(request);
    }

    // Méthode pour obtenir les dernières valeurs
    public Object DerniereValeur(Context context, String sensor) {
        String url = "http://" + connexionWebRest.getAdresseIP() + ":" + connexionWebRest.getPort() + "/Projet2024-Controle_Atmosphere_Atelier-API_SERVER/Index.php/lireDerniereValeur?sensor=" + sensor;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id_wasp = response.getString("id_wasp");
                            double value = response.getDouble("value");
                            String timestampString = response.getString("timestamp");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date timestamp = dateFormat.parse(timestampString);

                            // Autres valeurs de capteur
                            double seuilMin = 0.0;
                            double seuilMax = 0.0;
                            boolean valideSeuilMin = false;
                            boolean valideSeuilMax = false;
                            boolean activeAlert = false;
                            String unite = "";
                            double seuilEchMin = 0.0;
                            double seuilEchMax = 0.0;

                            Capteur capteur = new Capteur(id_wasp, sensor, value, timestamp, seuilMin, seuilMax,
                                    valideSeuilMin, valideSeuilMax, activeAlert, unite, seuilEchMin, seuilEchMax);
                            callback.DerniereValeur(capteur);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            callback.endErreur("Erreur lors de l'analyse JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.endErreur("Erreur de réseau");
            }
        });

        requestQueue.add(request);
        return null;
    }


    // Méthode pour activer l'alarme à revoir
    //ça retourne juste un message de confirmation
    //pourquoi type void au lieu de type Object ??
    //pourquoi stringrequest ??
    //comment manipuler le message ??
    public void AlarmeActive(String sensor, boolean activeAlarme) {
        String url = "http://"+ connexionWebRest.getAdresseIP() + ":" + connexionWebRest.getPort() +"/Projet2024-Controle_Atmosphere_Atelier-API_SERVER/Index.php/ActiveAlarme?sensor=" + sensor + "&ActiveAlarme=" + (activeAlarme ? "1" : "0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            callback.EtatAlarme(response); //trouver ce qu'il faut mettre ici
                            // Gérer la réponse si nécessaire
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Gérer l'erreur si nécessaire
                            callback.endErreur("Erreur lors de l'analyse JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Gérer l'erreur si nécessaire
                callback.endErreur("Erreur de réseau");
            }
        });

        requestQueue.add(request);
    }

}