package ie.ait.qspy;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.github.appintro.model.SliderPage;

import java.util.Date;

import ie.ait.qspy.firebase.entities.UserEntity;
import ie.ait.qspy.services.FirestoreService;
import ie.ait.qspy.services.UserService;
import ie.ait.qspy.utils.DeviceUtils;

public class MainActivity extends AppIntro {

    private UserService userService = new UserService();

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve a unique device id.
        deviceId = new DeviceUtils().getDeviceId(getContentResolver());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            createSlides();
            saveUser();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();

        } else {
            openMapsActivity();

        }

    }

    //create user
    private void saveUser() {
        // Create a new user
        UserEntity user = new UserEntity();
        user.setDate(new Date());
        user.setPoints(0);

        // Add a new user with a generated ID
        userService.save(deviceId, user,
                e -> Log.d("Adding user", "DocumentSnapshot added with ID: " + deviceId), // success listener
                e -> Log.w("Adding user", "Error adding document", e)); // failure listener
    }

    private void createSlides() {
        SliderPage page1 = new SliderPage();
        page1.setTitle("Getting started");
        page1.setDescription("Start using q-Spy to check the queue length for your favourite stores before heading out! See the next instructions.");
        page1.setImageDrawable(R.drawable.logo);
        page1.setTitleColor(getColor(R.color.colorSlide));
        page1.setDescriptionColor(getColor(R.color.colorSlide));
        page1.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(page1));

        SliderPage page2 = new SliderPage();
        page2.setTitle("Search a store");
        page2.setDescription("To search for you favorite store in just one click...");
        page2.setImageDrawable(R.drawable.store);
        page2.setTitleColor(getColor(R.color.colorSlide));
        page2.setDescriptionColor(getColor(R.color.colorSlide));
        page2.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(page2));

        SliderPage page3 = new SliderPage();
        page3.setTitle("Report queue length");
        page3.setDescription("Collaborate by reporting...so you can earn points and become eligible to receive exclusive offers from your favourite stores. Click done to start using the app!");
        page3.setImageDrawable(R.drawable.report);
        page3.setTitleColor(getColor(R.color.colorSlide));
        page3.setDescriptionColor(getColor(R.color.colorSlide));
        page3.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        addSlide(AppIntroFragment.newInstance(page3));

        setTransformer(AppIntroPageTransformerType.Depth.INSTANCE);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        openMapsActivity();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        openMapsActivity();
    }

    private void openMapsActivity() {
        finish();
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);

    }


}