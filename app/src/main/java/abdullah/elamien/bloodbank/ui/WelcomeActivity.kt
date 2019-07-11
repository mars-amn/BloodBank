package abdullah.elamien.bloodbank.ui

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import java.util.*


class WelcomeActivity : AhoyOnboarderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startOnBoardingCards()
        setupOnBoardingCards()

    }

    private fun setupOnBoardingCards() {
        setGradientBackground()
        setFinishButtonTitle("Start")
        setInactiveIndicatorColor(R.color.primaryColor)
        setActiveIndicatorColor(R.color.white)
    }

    private fun startOnBoardingCards() {
        val welcomeCards = ArrayList<AhoyOnboarderCard>()
        welcomeCards.add(firstCard())
        welcomeCards.add(secondCard())
        welcomeCards.add(thirdCard())
        setOnboardPages(welcomeCards)
    }

    private fun firstCard(): AhoyOnboarderCard {
        val card = AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher)
        card.setTitleColor(android.R.color.white)
        return card
    }

    private fun secondCard(): AhoyOnboarderCard {
        val card = AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher)
        card.setTitleColor(android.R.color.white)
        return card
    }

    private fun thirdCard(): AhoyOnboarderCard {
        val card = AhoyOnboarderCard("Welcome", "Glad that you took this step :) ",
                R.mipmap.ic_launcher)
        card.setTitleColor(android.R.color.white)
        return card
    }

    override fun onFinishButtonPressed() {
        saveUserFirstLaunch()
        startRegisterActivity()
    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveUserFirstLaunch() {
        val editor = getSharedPreferences(Constants.FIRST_LAUNCH_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(Constants.FIRST_LAUNCH_KEY, true)
        editor.apply()
    }
}
