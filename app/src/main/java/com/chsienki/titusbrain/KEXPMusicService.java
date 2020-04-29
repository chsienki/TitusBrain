package com.chsienki.titusbrain;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import androidx.media.MediaBrowserServiceCompat;
import java.util.ArrayList;
import java.util.List;

// Forwards the KEXP music player service as an android auto compatible one
public class KEXPMusicService extends MediaBrowserServiceCompat {

    private  MediaBrowserCompat mBrowser;

    ComponentName componentName = new ComponentName("org.kexp.android", "org.kexp.radio.service.MusicPlaybackService");

    @Override
    public void onCreate() {
        super.onCreate();

        // connect to the KEXP music browser service, and set our session token to its one
        mBrowser = new MediaBrowserCompat(this, componentName,
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        setSessionToken(mBrowser.getSessionToken());
                    }
                }, null);
        mBrowser.connect();
    }

    @Override
    public void onDestroy() {
        //TODO: anything to do here?
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        // we can change what media is available, based on client calling us
        // for now we'll just always return the same thing
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<MediaItem>> result) {

        // make sure KEXP is running, but paused
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setAction("org.kexp.android.pausePlaybackAction");
        intent.putExtra("org.kexp.android.mediaId", "__LIVE__");
        ComponentName c = startForegroundService(intent);

        // Create a single playable item called KEXP
        MediaDescriptionCompat desc = new MediaDescriptionCompat
                                            .Builder()
                                            .setMediaId("1")
                                            .setTitle("KEXP")
                                            .setIconUri(Uri.parse("https://kexp.org/static/assets/img/KEXP_ios_safari_152px.png"))
                                            .build();
        MediaItem item = new MediaItem(desc, MediaItem.FLAG_PLAYABLE);

        // return the single item
        ArrayList<MediaItem> items = new ArrayList<>();
        items.add(item);
        result.sendResult(items);
    }
}
