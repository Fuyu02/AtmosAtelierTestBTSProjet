package net.noel.ophelie.appCtrlAtmosphere;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.noel.ophelie.applicationatmosateliertest.R;

public class PreferencesFragments extends PreferenceFragment {
    public static final String KEY_IPSERVEUR = "PREFKEY_IP_SERVEUR";
    public static final String KEY_PORTSERVEUR = "PREFKEY_PORT_SERVEUR";
    public static final String KEY_USERNAME = "PREFKEY_USERNAME";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.userpreferencestech);
    }
}
