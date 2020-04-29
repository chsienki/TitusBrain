package com.chsienki.titusbrain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference drawOverPreference = findPreference("drawOver");
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
        drawOverPreference.setIntent(intent);

        SetUIStates();
    }

    @Override
    public void onResume(){
        super.onResume();
        SetUIStates();
    }

    private void SetUIStates(){
        //TODO: could we make drawover be a custom preference that handles this?
        Preference drawOverPreference = findPreference("drawOver");
        if (Settings.canDrawOverlays(getContext())){
            drawOverPreference.setSummary("Draw over app permission is granted");
        }else{
            drawOverPreference.setSummary("Draw over app permission is denied");
        }
    }

}
