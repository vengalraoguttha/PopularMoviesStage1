package com.vengalrao.android.popularmovies;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by vengalrao on 12-03-2017.
 */

public class PreferenceSetting extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_movie_setting);
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen=getPreferenceScreen();

        int count=preferenceScreen.getPreferenceCount();
        for(int i=0;i<count;i++){
            Preference preference =preferenceScreen.getPreference(i);
            String value=sharedPreferences.getString(preference.getKey(),"");
            setPreferenceSummary(preference,value);
        }
    }

    private void setPreferenceSummary(Preference preference,String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference=(ListPreference)preference;
            int prefIndex=listPreference.findIndexOfValue(value);
            if(prefIndex>=0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference=findPreference(key);
        if(preference!=null){
            String value=sharedPreferences.getString(preference.getKey(),"");
            setPreferenceSummary(preference,value);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
