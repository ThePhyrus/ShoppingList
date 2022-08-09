package com.thephyrus.shoppinglist.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.thephyrus.shoppinglist.R
import com.thephyrus.shoppinglist.billing.BillingManager
import com.thephyrus.shoppinglist.databinding.ActivityMainBinding
import com.thephyrus.shoppinglist.dialogs.NewListDialog
import com.thephyrus.shoppinglist.fragments.FragmentManager
import com.thephyrus.shoppinglist.fragments.NoteFragment
import com.thephyrus.shoppinglist.fragments.ShopListFragment
import com.thephyrus.shoppinglist.fragments.ShopListNamesFragment
import com.thephyrus.shoppinglist.settings.SettingsFragment
//FIXME
// 1 - Тема должна применяться сразу
// 2 - Добавить возможность удаления елментов из списка покупок по отдельности
// 3 - Что вообще за дизаин такой?!
// 4 - Навигация - отстой
// 5 - ...


class MainActivity : AppCompatActivity(), NewListDialog.Listener {

    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list //lesson 54
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null //lesson 57
    private var adShowCounter = 0
    private var adShowCounterMax = 3
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme()) //lesson 55
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTheme = defPref.getString("theme_key", "blue").toString() //lesson 56
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()

        if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) loadInterAd()//lesson 61
    }

    private fun loadInterAd() { //lesson 57
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                    Toast.makeText(
                        this@MainActivity,
                        R.string.fail_to_load_inter_ad,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {//lesson 57
        if (iAd != null && adShowCounter > adShowCounterMax) {
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            iAd?.show(this)
        } else {
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object :
                        AdListener { //lesson 57
                        override fun onFinish() {
                            FragmentManager.openSettings(SettingsFragment(), this@MainActivity)
                        }
                    })
                }
                R.id.notes -> {
                    showInterAd(object :
                        AdListener { //lesson 57
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes //lesson 54
                            FragmentManager.setFragment(
                                NoteFragment.newInstance(),
                                this@MainActivity
                            )
                        }
                    })
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list //lesson 54
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    private fun getSelectedTheme(): Int { //lesson 55
        return if (defPref.getString("theme_key", "blue") == "blue") {
            R.style.Theme_ShoppingListBlue
        } else {
            R.style.Theme_ShoppingListRed
        }
    }


    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key", "blue") != currentTheme) recreate() //lesson 56
    }

    override fun onClick(name: String) {
        Log.d("@@@", "works $name")
    }

    interface AdListener {
        fun onFinish()
    }

    override fun onBackPressed() {
        if (FragmentManager.currentFrag is ShopListFragment || FragmentManager.currentFrag == null) {
            FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
            binding.bNav.selectedItemId = R.id.shop_list
        } else {
            super.onBackPressed()
        }
    }
}