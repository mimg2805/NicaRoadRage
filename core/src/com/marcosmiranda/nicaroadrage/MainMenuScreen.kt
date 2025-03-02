package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.StretchViewport

class MainMenuScreen(private val game: NicaRoadRage): Screen {

    private val assets = game.assets
    private var menuMusic = game.menuMusic

    private var stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val skin = Skin()

    private val prefs = Gdx.app.getPreferences("NicaRoadRage")
    private val playMusic = prefs.getBoolean("music", true)
    // private val playSound = prefs.getBoolean("sound", true)
    private val musicVolume = prefs.getFloat("musicVolume", DEFAULT_MUSIC_VOLUME) / VOLUME_DIVIDER
    // private val soundVolume = prefs.getFloat("soundVolume", DEFAULT_SOUND_VOLUME) / VOLUME_DIVIDER

    init {
        // Show ads, if there's WiFi
        // if (android.isWifiOn() || android.isDataOn()) android.showBannerAd()

        game.state = GameState.MENU
        setBackColor(BACK_COLOR)

        // Set up
        // music = prefs.getBoolean("music", true)
        Gdx.input.inputProcessor = stage

        // Add a white pixmap to the skin
        val pixmap = Pixmap(10, 10, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        skin.add("white", Texture(pixmap))
        pixmap.dispose()

        // Rounded rectangle button
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable

        // Get fonts from the asset manager & build the various element styles
        val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
        val pixel18 = assets.get(PIXEL_EMULATOR_18, BitmapFont::class.java)
        val pixel24 = assets.get(PIXEL_EMULATOR_24, BitmapFont::class.java)
        val defaultLblStyle = Label.LabelStyle(pixel18, Color.WHITE)
        val headerLblStyle = Label.LabelStyle(pixel18, Color.GOLD)
        val errorLblStyle = Label.LabelStyle(pixel16, Color.RED)
        val gameNameLblStyle = Label.LabelStyle(pixel24, Color.GOLD)
        skin.add("default", defaultLblStyle)
        skin.add("header", headerLblStyle)
        skin.add("error", errorLblStyle)
        skin.add("gameName", gameNameLblStyle)

        // Button styles
        val playBtnStyle = ImageTextButton.ImageTextButtonStyle()
        playBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIME)
        playBtnStyle.down = skin.newDrawable(btnDrawable, Color.FOREST)
        playBtnStyle.font = pixel16
        skin.add("playBtn", playBtnStyle)

        val optionsBtnStyle = ImageTextButton.ImageTextButtonStyle()
        optionsBtnStyle.up = skin.newDrawable(btnDrawable, Color.ROYAL)
        optionsBtnStyle.down = skin.newDrawable(btnDrawable, Color.NAVY)
        optionsBtnStyle.font = pixel16
        skin.add("optionsBtn", optionsBtnStyle)

        val helpBtnStyle = ImageTextButton.ImageTextButtonStyle()
        helpBtnStyle.up = skin.newDrawable(btnDrawable, Color.VIOLET)
        helpBtnStyle.down = skin.newDrawable(btnDrawable, Color.MAGENTA)
        helpBtnStyle.font = pixel16
        skin.add("helpBtn", helpBtnStyle)

        val moreAppsBtnStyle = ImageTextButton.ImageTextButtonStyle()
        moreAppsBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIGHT_GRAY)
        moreAppsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
        moreAppsBtnStyle.font = pixel16
        skin.add("moreAppsBtn", moreAppsBtnStyle)

        val exitBtnStyle = ImageTextButton.ImageTextButtonStyle()
        exitBtnStyle.up = skin.newDrawable(btnDrawable, Color.SCARLET)
        exitBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        exitBtnStyle.font = pixel16
        skin.add("exitBtn", exitBtnStyle)

        // Buttons
        val playBtnLbl = Label(" JUGAR ", skin)
        val playBtnIcon = Image(assets.get(PLAY_ICON_PATH, Texture::class.java))
        val playBtn = ImageTextButton(null, skin, "playBtn")
        playBtn.add(playBtnIcon, playBtnLbl)
        playBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        playBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 800f)
        playBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                // window.hideBannerAd()
                dispose()
                game.screen = GameScreen(game)
            }
        })
        stage.addActor(playBtn)

        val optionsBtnLbl = Label(" OPCIONES ", skin)
        val optionsBtnIcon = Image(assets.get(COG_ICON_PATH, Texture::class.java))
        val optionsBtn = ImageTextButton(null, skin, "optionsBtn")
        optionsBtn.add(optionsBtnIcon, optionsBtnLbl)
        optionsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        optionsBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 700f)
        optionsBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = OptionsScreen(game)
            }
        })
        stage.addActor(optionsBtn)

        val helpBtnLbl = Label(" AYUDA ", skin)
        val helpBtnIcon = Image(assets.get(HELP_ICON_PATH, Texture::class.java))
        val helpBtn = ImageTextButton(null, skin, "helpBtn")
        helpBtn.add(helpBtnIcon, helpBtnLbl)
        helpBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        helpBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 600f)
        helpBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                // android.showInterstitial()
                game.screen = HelpScreen(game)
            }
        })
        stage.addActor(helpBtn)

        val moreAppsBtnLbl = Label(" MÁS APPS ", skin)
        val moreAppsBtnIcon = Image(assets.get(CIRCLE_PLUS_ICON_PATH, Texture::class.java))
        val moreAppsBtn = ImageTextButton(null, skin, "moreAppsBtn")
        moreAppsBtn.add(moreAppsBtnIcon, moreAppsBtnLbl)
        moreAppsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        moreAppsBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 500f)
        moreAppsBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                if (game.platform == Application.ApplicationType.Android) Gdx.net.openURI("https://play.google.com/store/apps/developer?id=Marcos+I.+Miranda+G.")
            }
        })
        stage.addActor(moreAppsBtn)

        val exitBtnLbl = Label(" SALIR ", skin)
        val exitBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
        val exitBtn = ImageTextButton(null, skin, "exitBtn")
        exitBtn.add(exitBtnIcon, exitBtnLbl)
        exitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        exitBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 400f)
        exitBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Gdx.app.exit()
            }
        })
        stage.addActor(exitBtn)

        // Logo
        val logo = Image(assets.get(LOGO_PATH, Texture::class.java))
        logo.setScale(LOGO_SCALE)
        logo.setPosition(WINDOW_WIDTH_HALF - (logo.width * LOGO_SCALE) / 2, LOGO_Y)
        stage.addActor(logo)

        // Name
        val nameLbl = Label("NICA ROAD RAGE", skin, "gameName")
        nameLbl.setPosition(WINDOW_WIDTH_HALF - (nameLbl.width / 2), 120f)
        stage.addActor(nameLbl)

        // Music
        if (playMusic && !menuMusic.isPlaying) {
            // menuMusic = Gdx.audio.newMusic(Gdx.files.internal((MENU_MUSIC_PATH)))
            menuMusic.volume = musicVolume
            menuMusic.isLooping = true
            menuMusic.play()
        }
    }

    override fun render(delta: Float) {
        clear()
        stage.act(delta.coerceAtMost(1 / FRAME_RATE))
        stage.draw()
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        // prefs.flush()
        stage.dispose()
        skin.dispose()
    }
}