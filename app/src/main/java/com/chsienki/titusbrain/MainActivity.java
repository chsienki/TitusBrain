package com.chsienki.titusbrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.chsienki.titusbrain.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        StartServiceIfPermissionGranted();
}

    private void StartServiceIfPermissionGranted() {

        if (Settings.canDrawOverlays(this)) {
            binding.permissionButton.setVisibility(View.GONE);
            binding.permissionsGranted.setVisibility(View.VISIBLE);

            Intent service = new Intent(this, BackgroundService.class);
            startService(service);
        }
    }

    private static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    public void ensurePermission(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            StartServiceIfPermissionGranted();
        }
    }
}
