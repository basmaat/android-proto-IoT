package io.relayr.iotsmartphone.tabs.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import de.greenrobot.event.EventBus;
import io.relayr.android.RelayrSdk;
import io.relayr.iotsmartphone.R;
import io.relayr.iotsmartphone.tabs.helper.Constants;
import io.relayr.iotsmartphone.tabs.helper.SettingsStorage;
import io.relayr.java.helper.observer.SimpleObserver;
import io.relayr.java.model.models.DeviceModel;
import io.relayr.java.model.models.error.DeviceModelsException;
import io.relayr.java.model.models.transport.Transport;

public class UiHelper {

    private static final String WEAR_APP = "com.google.android.wearable.app";

    public static boolean isWearableConnected(Activity activity) {
        try {
            activity.getPackageManager().getPackageInfo(WEAR_APP, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            showSnackBar(activity, R.string.srv_no_wearable);
            return false;
        }
    }

    public static boolean isCloudConnected() {
        return RelayrSdk.isUserLoggedIn();
    }

    public static void showSnackBar(Activity activity, int stringId) {
        if (activity == null) return;
        final View view = activity.findViewById(android.R.id.content);
        if (view == null) return;
        Snackbar.make(view, activity.getString(stringId), Snackbar.LENGTH_SHORT).show();
    }

    public static void openDashboard(Context context) {
        String packageName = "io.relayr.wunderbar";
        PackageManager manager = context.getPackageManager();
        Intent startApp = manager.getLaunchIntentForPackage(packageName);
        if (startApp != null) {
            startApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startApp.addCategory(Intent.CATEGORY_LAUNCHER);

            context.startActivity(startApp);
            return;
        }

        try {
            Uri storeUri = Uri.parse("market://details?id=" + packageName);
            startStoreActivity(context, storeUri);
        } catch (ActivityNotFoundException anfe) {
            Uri webUri = Uri.parse("http://play.google.com/store/apps/details?id=" + packageName);
            startStoreActivity(context, webUri);
        }
    }

    private static void startStoreActivity(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}