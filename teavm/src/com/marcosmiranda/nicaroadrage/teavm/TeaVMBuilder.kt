package com.marcosmiranda.nicaroadrage.teavm

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass
import org.teavm.vm.TeaVMOptimizationLevel
import java.io.File
import java.io.IOException

/** Builds the TeaVM/HTML application.  */
@SkipClass
object TeaVMBuilder {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val teaBuildConfiguration = TeaBuildConfiguration()
        teaBuildConfiguration.assetsPath.add(AssetFileHandle("../assets"))
        teaBuildConfiguration.webappPath = File("build/dist").canonicalPath

        // Register any extra classpath assets here:
        // teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/marcosmiranda/nicaroadrage/asset.extension");

        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("com.marcosmiranda.nicaroadrage.reflect");
        val tool = TeaBuilder.config(teaBuildConfiguration)
        tool.mainClass = TeaVMLauncher::class.java.name
        // For many (or most) applications, using the highest optimization won't add much to build time.
        // If your builds take too long, and runtime performance doesn't matter, you can change FULL to SIMPLE .
        tool.optimizationLevel = TeaVMOptimizationLevel.FULL
        tool.setObfuscated(true)
        TeaBuilder.build(tool)
    }
}
