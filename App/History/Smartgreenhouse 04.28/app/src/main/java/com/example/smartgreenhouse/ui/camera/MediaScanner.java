package com.example.smartgreenhouse.ui.camera;



import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

public class MediaScanner {
    private Context ctxt;
    private String file_Path;
    private MediaScannerConnection mMediaScanner;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;
    private Context mContext;



    public static MediaScanner newInstance(Context context)
    {
        return new MediaScanner (context);
    }

    private MediaScanner (Context context) {

        ctxt = context;

    }

    public void mediaScanning(final String path) {
        if (mMediaScanner == null) {
            mMediaScannerClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override public void onMediaScannerConnected() {
                    mMediaScanner.scanFile(file_Path, null);
                }

                @Override public void onScanCompleted(String path, Uri uri) {
                    System.out.println("::::MediaScan Success::::");

                    mMediaScanner.disconnect();
                }
            };

            mMediaScanner = new MediaScannerConnection(mContext, mMediaScannerClient);
        }

        String mPath = path;
        mMediaScanner.connect();
    }
}

