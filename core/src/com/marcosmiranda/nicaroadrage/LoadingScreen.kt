package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.StretchViewport

class LoadingScreen(private val game: NicaRoadRage) : Screen {

    private val stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val skin = Skin()
    private var prefs = Gdx.app.getPreferences("NicaRoadRage")
    private var progress = 0
    private var progressStr = "Cargando: $progress%"
    private var loadingLbl: Label

    private val assets = game.assets
    private var state = game.state
    // private val android = game.android

    init {
        // Show ads, if there's WiFi
        // if (android.isWifiOn() || android.isDataOn()) android.showBannerAd()

        setBackColor(BACK_COLOR)
        state = GameState.LOADING

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.MENU, true)
        Gdx.input.inputProcessor = stage

        // set the loaders for the generator and the fonts themselves
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        // create and load default fonts
        // val arial = "arial"
        // val comic = "comic"
        // val pixel = "PixelEmulator"
        // val start = "PressStart2P"

        // createFonts(assets, arial, 12)
        // createFonts(assets, arial, 14)
        // createFonts(assets, arial, 16)
        // createFonts(assets, arial, 18)
        // createFonts(assets, arial, 20)
        // createFonts(assets, arial, 22)
        // createFonts(assets, arial, 24)
        // createFonts(assets, arial, 28)
        // createFonts(assets, arial, 32)
        // createFonts(assets, arial, 48)

        createFonts(assets, PIXELEMULATOR_FONT_NAME, 14)
        createFonts(assets, PIXELEMULATOR_FONT_NAME, 16)
        createFonts(assets, PIXELEMULATOR_FONT_NAME, 18)
        createFonts(assets, PIXELEMULATOR_FONT_NAME, 20)
        createFonts(assets, PIXELEMULATOR_FONT_NAME, 22)
        createFonts(assets, PIXELEMULATOR_FONT_NAME, 24)

        // Load all of the needed resources
        assets.load(LOGO_PATH, Texture::class.java)
        assets.load(PLAYER_CAR_SPRITE_PATH, Texture::class.java)
        assets.load(POWERUP_SPRITE_PATH, Texture::class.java)
        assets.load(EXPLOSION_SPRITE_PATH, Texture::class.java)

        assets.load(AMBULANCE_SPRITE_PATH, Texture::class.java)
        assets.load(RED_CROSS_DIALOG_SPRITE_PATH, Texture::class.java)
        assets.load(BUS_SPRITE_PATH, Texture::class.java)
        assets.load(TAXI1_SPRITE_PATH, Texture::class.java)
        assets.load(TAXI2_SPRITE_PATH, Texture::class.java)
        assets.load(TAXI3_SPRITE_PATH, Texture::class.java)
        assets.load(TAXI4_SPRITE_PATH, Texture::class.java)
        assets.load(TRUCK_SPRITE_PATH, Texture::class.java)

        assets.load(ROAD_SPRITE_PATH, Texture::class.java)
        assets.load(CHAYOPALO_SPRITE_PATH, Texture::class.java)
        assets.load(CHAYOPALO_CAIDO_SPRITE_PATH, Texture::class.java)
        assets.load(HOUSE_SPRITE_PATH, Texture::class.java)
        assets.load(TREE_SPRITE_PATH, Texture::class.java)

        assets.load(BUTTON_SPRITE_PATH, Texture::class.java)
        assets.load(BUTTON_SMALL_SPRITE_PATH, Texture::class.java)

        assets.load(PAUSE_BTN_ICON_PATH, Texture::class.java)
        assets.load(ARROW_RIGHT_ICON_PATH, Texture::class.java)
        assets.load(PLAY_ICON_PATH, Texture::class.java)
        assets.load(COG_ICON_PATH, Texture::class.java)
        assets.load(CHECK_ICON_PATH, Texture::class.java)
        assets.load(SAVE_ICON_PATH, Texture::class.java)
        assets.load(TRASH_ICON_PATH, Texture::class.java)
        assets.load(VOLUME_DOWN_ICON_PATH, Texture::class.java)
        assets.load(VOLUME_UP_ICON_PATH, Texture::class.java)
        assets.load(HELP_ICON_PATH, Texture::class.java)
        assets.load(STAR_ICON_PATH, Texture::class.java)
        assets.load(CIRCLE_PLUS_ICON_PATH, Texture::class.java)
        assets.load(EXIT_ICON_PATH, Texture::class.java)

        // Loading text
        val fontName = PIXELEMULATOR_FONT_NAME + "24.ttf"
        assets.finishLoadingAsset<BitmapFont>(fontName)
        val font = assets.get(fontName, BitmapFont::class.java)
        val defaultLblStyle = Label.LabelStyle(font, Color.WHITE)
        skin.add("default", defaultLblStyle)

        // Create the elements
        loadingLbl = Label(progressStr, skin, "default")
        loadingLbl.setPosition(WINDOW_WIDTH_HALF - (loadingLbl.width / 2), WINDOW_HEIGHT_HALF)
        stage.addActor(loadingLbl)
    }

    override fun render(delta: Float) {
        clear()
        stage.act(delta.coerceAtMost(1 / FRAME_RATE))
        stage.draw()

        if (!assets.update()) {
            progress = (assets.progress * 100f).toInt()
            progressStr = "Cargando: $progress%"
            loadingLbl.setText(progressStr)
        } else {
            prefs = Gdx.app.getPreferences("NicaRoadRage")
            val playerName = prefs.getString("playerName", "")
            if (playerName.isBlank()) game.screen = NewGameScreen(game)
            else game.screen = MainMenuScreen(game)
            // else game.screen = MoreAppsScreen(game)
        }
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        prefs.flush()
        skin.dispose()
        stage.dispose()
    }
}