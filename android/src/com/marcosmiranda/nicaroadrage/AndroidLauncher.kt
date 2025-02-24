package com.marcosmiranda.nicaroadrage

import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private const val APP_ID = "ca-app-pub-2838402743054690~2335446736"
private const val BANNER_AD_ID = "ca-app-pub-2838402743054690/4317283254"
private const val INTERSTITIAL_AD_ID = "ca-app-pub-2838402743054690/9300726517"
private const val BANNER_AD_TEST_ID = "ca-app-pub-3940256099942544/6300978111" // Banner Test
private const val INTERSTITIAL_AD_TEST_ID = "ca-app-pub-3940256099942544/1033173712" // Interstitial Test

class AndroidLauncher : AndroidApplication(), AndroidController {

    // private lateinit var adView: AdView
    private var interstitialAd: InterstitialAd? = null
    private lateinit var gameView: View
    private var backColor = Color.rgb(0, 26, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LibGDX Android config
        val cfg = AndroidApplicationConfiguration()
        cfg.useGL30 = false
        cfg.useAccelerometer = false
        cfg.useCompass = false
        cfg.useGyroscope = false
        cfg.useImmersiveMode = true
        cfg.useRotationVectorSensor = false
        cfg.useWakelock = true

        // Initialize ads
        // MobileAds.initialize(this, APP_ID)

        // Do the stuff that initialize() would do for you
        // requestWindowFeature(Window.FEATURE_NO_TITLE)
        // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

        val layout = ConstraintLayout(this)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // layout.layoutParams = params
        //
        // val admobView = createAdView()
        val gameView = createGameView(cfg)
        layout.addView(gameView)
        // admobView.adListener = object : AdListener() {
        //     override fun onAdLoaded() {
        //         layout.addView(gameView)
        //         super.onAdLoaded()
        //     }
        //
        //     override fun onAdFailedToLoad(p0: LoadAdError) {
        //         layout.addView(gameView)
        //         super.onAdFailedToLoad(p0)
        //     }
        // }
        // layout.addView(admobView)
        //
        setContentView(layout)
        // startAdvertising(admobView)
        loadInterstitial()

        // var interstitialAd = object : InterstitialAd()
        // var interstitialAd = object : AdManagerInterstitialAd()
        // interstitialAd.adUnitId = INTERSTITIAL_AD_TEST_ID
        // interstitialAd.setAdUnitId(INTERSTITIAL_AD_TEST_ID)
        // interstitialAd.adListener = object : AdListener() {
        //     override fun onAdLoaded() {
        //         Toast.makeText(
        //             applicationContext,
        //             "Finished Loading Interstitial",
        //             Toast.LENGTH_SHORT
        //         ).show()
        //     }
        //
        //     override fun onAdClosed() {
        //         Toast.makeText(applicationContext, "Closed Interstitial", Toast.LENGTH_SHORT).show()
        //     }
        // }
    }

    /*
    private fun createAdView(): AdView {
        adView = AdView(this)
        // adView.visibility = View.INVISIBLE
        adView.setBackgroundColor(backColor)
        adView.adUnitId = BANNER_AD_ID
        // adView.adUnitId = AD_UNIT_TEST_ID
        val adViewId = 12345
        adView.id = adViewId // this is an arbitrary id, allows for relative positioning in createGameView()
        adView.setAdSize(AdSize.BANNER)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        adView.layoutParams = params
        return adView
    }
    */

    private fun createGameView(cfg: AndroidApplicationConfiguration): View {
        val game = NicaRoadRage()
        game.android = this
        gameView = initializeForView(game, cfg)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        // params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        // params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        // params.addRule(RelativeLayout.ABOVE, adView.id)
        // gameView.layoutParams = params
        return gameView
    }

    /*
    private fun startAdvertising(adView: AdView) {
        val testDevices: MutableList<String> = ArrayList()
        testDevices.add("BE89C404157C24CCDB17A860A9B5B878")

        val reqConf = RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build()
        MobileAds.setRequestConfiguration(reqConf)

        adView.loadAd(AdRequest.Builder().build())
    }
    */

    public override fun onResume() {
        super.onResume()
        // adView.resume()
    }

    public override fun onPause() {
        // adView.pause()
        super.onPause()
    }

    public override fun onDestroy() {
        // adView.destroy()
        super.onDestroy()
    }

    /*
    override fun showBannerAd() {
        runOnUiThread {
            adView.visibility = View.VISIBLE
            startAdvertising(adView)
        }
    }

    override fun hideBannerAd() {
        runOnUiThread {
            adView.visibility = View.INVISIBLE
            // adView.visibility = View.GONE
        }
    }
    */

    override fun isInterstitialLoaded(): Boolean {
        return interstitialAd != null
    }

    override fun loadInterstitial() {
        runOnUiThread {
            InterstitialAd.load(this, INTERSTITIAL_AD_ID, AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("interstitial ad", adError.toString())
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("interstitial ad", "Ad was loaded.")
                    interstitialAd = ad
                }
            })
        }
    }

    override fun showInterstitial() {
        runOnUiThread {
            if (this.isInterstitialLoaded()) {
                interstitialAd?.show(this)
            } else {
                loadInterstitial()
            }
        }
    }

    override fun isWifiOn(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(network)

        return nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    override fun isDataOn(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(network)

        return nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }

    override fun hideSystemUI() {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(true)
            } else {
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                )
            }

        }
    }

    override fun showSystemUI() {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            } else {
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
            }
        }
    }

}
