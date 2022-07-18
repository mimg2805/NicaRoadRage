package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport

class MainMenuScreen(private val game: NicaRoadRage): Screen {

    private val assets = game.assets
    private val window = game.window
    private var menuMusic = game.menuMusic

    private val stage: Stage = Stage(FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val skin: Skin = Skin()

    private val prefs = Gdx.app.getPreferences("NicaRoadRage")
    private val playMusic = prefs.getBoolean("music", true)
    // private val playSound = prefs.getBoolean("sound", true)
    private val musicVolume = prefs.getFloat("musicVolume", DEFAULT_MUSIC_VOLUME) / VOLUME_DIVIDER
    // private val soundVolume = prefs.getFloat("soundVolume", DEFAULT_SOUND_VOLUME) / VOLUME_DIVIDER

    init {
        // Show ads, if there's WiFi
        if (window.isWifiOn() || window.isDataOn()) window.showBannerAd()

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
        val pixel16 = loadFont(assets, PIXELEMULATOR_FONT_NAME, 16)
        val pixel18 = loadFont(assets, PIXELEMULATOR_FONT_NAME, 18)
        val pixel24 = loadFont(assets, PIXELEMULATOR_FONT_NAME, 24)
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
        playBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 750f)
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
        optionsBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 650f)
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
        helpBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 550f)
        helpBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = HelpScreen(game)
            }
        })
        stage.addActor(helpBtn)

        val exitBtnLbl = Label(" SALIR ", skin)
        val exitBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
        val exitBtn = ImageTextButton(null, skin, "exitBtn")
        exitBtn.add(exitBtnIcon, exitBtnLbl)
        exitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        exitBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), 450f)
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
        nameLbl.setPosition(WINDOW_WIDTH_HALF - (nameLbl.width / 2), 70f)
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

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        // prefs.flush()
        stage.dispose()
        skin.dispose()
    }

}