package ie.ait.qspy.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.provider.Settings;

public class DeviceUtils {

    //Get device id.
    @SuppressLint("HardwareIds")
    public String getDeviceId(ContentResolver resolver) {
        return Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
    }
}
