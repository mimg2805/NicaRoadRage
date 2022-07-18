package com.marcosmiranda.nicaroadrage

import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.google.android.gms.ads.*
import java.util.*

private const val APP_ID = "ca-app-pub-2838402743054690~2335446736"
private const val AD_UNIT_ID = "ca-app-pub-2838402743054690/4317283254"
// private const val AD_INTERSTITAL_UNIT_ID = "ca-app-pub-1385681571936835/3225330348"
// private const val AD_UNIT_TEST_ID = "ca-app-pub-3940256099942544/6300978111" // Test

class AndroidLauncher : AndroidApplication(), WindowController {

    private lateinit var adView: AdView
    private lateinit var gameView: View
    private var backColor = Color.rgb(0, 26, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LibGDX Android config
        val cfg = AndroidApplicationConfiguration()
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

        val layout = RelativeLayout(this)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        layout.layoutParams = params

        val admobView = createAdView()
        val gameView = createGameView(cfg)
        admobView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                layout.addView(gameView)
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                layout.addView(gameView)
                super.onAdFailedToLoad(p0)
            }
        }
        layout.addView(admobView)

        setContentView(layout)
        startAdvertising(admobView)
    }

    private fun createAdView(): AdView {
        adView = AdView(this)
        // adView.visibility = View.INVISIBLE
        adView.setBackgroundColor(backColor)
        adView.adUnitId = AD_UNIT_ID
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

    private fun createGameView(cfg: AndroidApplicationConfiguration): View {
        gameView = initializeForView(NicaRoadRage(this), cfg)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        // val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.ABOVE, adView.id)
        gameView.layoutParams = params
        return gameView
    }

    private fun startAdvertising(adView: AdView) {
        val testDevices: MutableList<String> = ArrayList()
        // testDevices.add("3B317ED93FEC6B5C4E4469A5DA37932F")
        testDevices.add("BE89C404157C24CCDB17A860A9B5B878")

        val reqConf = RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build()
        MobileAds.setRequestConfiguration(reqConf)

        adView.loadAd(AdRequest.Builder().build())
    }

    public override fun onResume() {
        super.onResume()
        adView.resume()
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

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

    override fun isWifiOn(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(network)

        return nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    override fun isDataOn(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val nc = cm.getNetworkCapabilities(network)

        return nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
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
