package com.marcosmiranda.nicaroadrage

interface AndroidController {
    fun showSystemUI()
    fun hideSystemUI()
    fun isWifiOn(): Boolean
    fun isDataOn(): Boolean
    // fun showBannerAd()
    // fun hideBannerAd()
    fun isInterstitialLoaded(): Boolean
    fun loadInterstitial()
    fun showInterstitial()
}