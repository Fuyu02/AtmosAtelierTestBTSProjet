package net.noel.ophelie.applicationatmostateliertest;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

public class SetPreferencesFragmentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(android.R.id.content, new PreferencesFragments()).commit();
        }
        catch (Exception e)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(SetPreferencesFragmentActivity.this);
            dlg.setTitle(e.getMessage());
            dlg.show();
        }
    }
}
