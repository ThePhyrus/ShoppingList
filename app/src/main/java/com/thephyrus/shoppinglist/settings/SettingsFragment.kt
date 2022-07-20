package com.thephyrus.shoppinglist.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.thephyrus.shoppinglist.R
import com.thephyrus.shoppinglist.billing.BillingManager

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var removeAdsPref: Preference
    private lateinit var bManager: BillingManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
    }

    private fun init() {//lesson 60
        bManager = BillingManager(activity as AppCompatActivity)
        removeAdsPref = findPreference("remove_ads_key")!!
        removeAdsPref.setOnPreferenceClickListener {
            Toast.makeText(
                activity,
                R.string.remove_ads_button_is_pressed,
                Toast.LENGTH_LONG
            ).show()
            bManager.startConnection()
            true
        }
    }

    override fun onDestroy() {
        bManager.closeConnection()
        super.onDestroy()
    }

}