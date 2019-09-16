package com.example.eshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.example.eshop.R;

public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPrefsFragment()).commit();

    }

    public static class MyPrefsFragment extends PreferenceFragment
    {
        private SharedPreferences prefs;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }


        public void writeSharedPrefs(SharedPreferences prefs, String tipo, String contenido) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(tipo, contenido);
            editor.commit();
        }

    }
}
