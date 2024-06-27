package com.marcosmiranda.nicaroadrage

interface WindowController {
    fun showSystemUI()
    fun hideSystemUI()
    fun showBannerAd()
    fun hideBannerAd()
    fun isWifiOn(): Boolean
    fun isDataOn(): Boolean
    fun openPlayStore()
}