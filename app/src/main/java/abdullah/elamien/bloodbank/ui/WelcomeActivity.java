package abdullah.elamien.bloodbank.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.di.components.ActivityComponent;
import abdullah.elamien.bloodbank.di.components.DaggerActivityComponent;
import abdullah.elamien.bloodbank.utils.Constants;


public class WelcomeActivity extends AhoyOnboarderActivity {

    @Inject
    FirebaseMessaging mFirebaseMessaging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
        startOnBoardingCards();
        setupOnBoardingCards();

    }

    private void initDagger() {
        ActivityComponent component = DaggerActivityComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
        component.inject(this);
    }


    private void setupOnBoardingCards() {
        setGradientBackground();
        setFinishButtonTitle("Start");
        setInactiveIndicatorColor(R.color.primaryColor);
        setActiveIndicatorColor(R.color.white);
    }

    private void startOnBoardingCards() {
        List<AhoyOnboarderCard> welcomeCards = new ArrayList<>();
        welcomeCards.add(firstCard());
        welcomeCards.add(secondCard());
        welcomeCards.add(thirdCard());
        setOnboardPages(welcomeCards);
    }

    private AhoyOnboarderCard firstCard() {
        AhoyOnboarderCard card = new AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher);
        card.setTitleColor(android.R.color.white);
        return card;
    }

    private AhoyOnboarderCard secondCard() {
        AhoyOnboarderCard card = new AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher);
        card.setTitleColor(android.R.color.white);
        return card;
    }

    private AhoyOnboarderCard thirdCard() {
        AhoyOnboarderCard card = new AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher);
        card.setTitleColor(android.R.color.white);
        return card;
    }

    @Override
    public void onFinishButtonPressed() {
        saveUserFirstLaunch();
        startRegisterActivity();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUserFirstLaunch() {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.FIRST_LAUNCH_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.FIRST_LAUNCH_KEY, true);
        editor.apply();
    }
}
