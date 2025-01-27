package com.marcosmiranda.nicaroadrage.teavm

import com.github.xpenatan.gdx.backends.teavm.TeaApplication
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.marcosmiranda.nicaroadrage.NicaRoadRage

/**
 * Launches the TeaVM/HTML application.
 */
object TeaVMLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val config: TeaApplicationConfiguration = TeaApplicationConfiguration("canvas")
        //// If width and height are each greater than 0, then the app will use a fixed size.
        config.width = 480
        config.height = 960

        //// If width and height are both 0, then the app will use all available space.
        // config.width = 0
        // config.height = 0

        //// If width and height are both -1, then the app will fill the canvas size.
        // config.width = -1
        // config.height = -1
        TeaApplication(NicaRoadRage(), config)
    }
}
