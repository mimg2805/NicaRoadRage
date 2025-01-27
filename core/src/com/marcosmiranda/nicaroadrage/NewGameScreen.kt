package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import java.util.Locale

class NewGameScreen(private val game: NicaRoadRage): Screen {

    private val assets = game.assets
    // private val android = game.android

    private val stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val skin = Skin()

    private val prefs = Gdx.app.getPreferences("NicaRoadRage")
    private var playerName = ""
    private var music = true
    private var sound = true
    private var carColor = ""

    init {
        // Show ads, if there's WiFi
        // if (android.isWifiOn() || android.isDataOn()) android.showBannerAd()

        setBackColor(BACK_COLOR)

        // Set up
        music = prefs.getBoolean("music", true)
        carColor = prefs.getString("carColor", "white")
        Gdx.input.inputProcessor = stage
        // stage.isDebugAll = true

        // Add a white pixmap to the skin
        val pixmap = Pixmap(10, 10, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        skin.add("white", Texture(pixmap))
        pixmap.dispose()

        // Rounded rectangle button
        val btnImg = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImg.drawable

        // Get fonts from the asset manager & build the various element styles
        val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
        val pixel18 = assets.get(PIXEL_EMULATOR_18, BitmapFont::class.java)
        val defaultLblStyle = Label.LabelStyle(pixel18, Color.WHITE)
        val headerLblStyle = Label.LabelStyle(pixel18, Color.GOLD)
        val errorLblStyle = Label.LabelStyle(pixel16, Color.RED)
        skin.add("default", defaultLblStyle)
        skin.add("header", headerLblStyle)
        skin.add("error", errorLblStyle)

        val playerNameTxtFieldStyle = TextField.TextFieldStyle()
        playerNameTxtFieldStyle.font = pixel18
        playerNameTxtFieldStyle.fontColor = Color.WHITE
        playerNameTxtFieldStyle.background = skin.newDrawable(btnDrawable, Color.WHITE)
        playerNameTxtFieldStyle.background.leftWidth = TEXTFIELD_PADDING
        playerNameTxtFieldStyle.background.rightWidth = TEXTFIELD_PADDING
        playerNameTxtFieldStyle.cursor = skin.newDrawable("white", Color.DARK_GRAY)
        playerNameTxtFieldStyle.cursor.minWidth = CURSOR_WIDTH
        skin.add("playerName", playerNameTxtFieldStyle)

        val check = Sprite(assets.get(CHECK_ICON_PATH, Texture::class.java))
        check.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        skin.add("check", check)

        val musicChkBoxStyle = CheckBox.CheckBoxStyle()
        musicChkBoxStyle.up = skin.newDrawable(btnDrawable, Color.WHITE)
        musicChkBoxStyle.down = skin.newDrawable(btnDrawable, Color.GRAY)
        musicChkBoxStyle.checkboxOn = skin.newDrawable("check", Color.BLACK)
        musicChkBoxStyle.font = pixel16
        skin.add("musicChkBox", musicChkBoxStyle)

        val colorSelectBtnStyle = Button.ButtonStyle()
        colorSelectBtnStyle.up = skin.newDrawable(btnDrawable, getColorFromStr(carColor))
        // colorSelectBtnStyle.down = skin.newDrawable(btnDrawable, getColorFromStr(carColor).mul(Color.GRAY))
        skin.add("colorSelectBtn", colorSelectBtnStyle)

        val colorSelectDialogStyle = Window.WindowStyle()
        colorSelectDialogStyle.titleFont = pixel16
        colorSelectDialogStyle.titleFontColor = Color.WHITE
        colorSelectDialogStyle.background = null //skin.newDrawable(btnDrawable, Color.ORANGE)
        colorSelectDialogStyle.stageBackground = null //skin.newDrawable("white", Color.valueOf("000000AA")); //skin.newDrawable(btnDrawable, Color.ORANGE)
        skin.add("default", colorSelectDialogStyle)

        val nextBtnStyle = ImageTextButton.ImageTextButtonStyle()
        nextBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIME)
        nextBtnStyle.down = skin.newDrawable(btnDrawable, Color.FOREST)
        nextBtnStyle.font = pixel16
        skin.add("nextBtn", nextBtnStyle)

        // Create the elements
        val welcomeLbl = Label("¡Bienvenido a Nica Road Rage!", skin, "header")
        welcomeLbl.setPosition(WINDOW_WIDTH_HALF - welcomeLbl.width / 2, 900f)
        stage.addActor(welcomeLbl)

        val headerLbl = Label("Opciones iniciales:", skin, "header")
        headerLbl.setPosition(WINDOW_WIDTH_HALF - headerLbl.width / 2, 850f)
        stage.addActor(headerLbl)

        val playerNameLbl = Label("Nombre del jugador:", skin, "default")
        playerNameLbl.setPosition(30f, 800f)
        stage.addActor(playerNameLbl)

        val playerNameTxtField = TextField(null, skin, "playerName")
        playerNameTxtField.setSize(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT)
        playerNameTxtField.setPosition(30f, 740f)
        playerNameTxtField.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                //super.clicked(event, x, y)
                Gdx.app.input.setOnscreenKeyboardVisible(true)
            }
        })
        stage.addActor(playerNameTxtField)

        val errorLbl = Label("ERROR: Tu nombre no puede estar vacío.", skin, "error")
        errorLbl.setSize(420f, 40f)
        errorLbl.setPosition(30f, 690f)
        errorLbl.setAlignment(Align.center)
        errorLbl.wrap = true
        errorLbl.isVisible = false
        stage.addActor(errorLbl)

        val musicLbl = Label("¿Activar música? ", skin, "default")
        musicLbl.height = CHECKBOX_SIZE
        musicLbl.setPosition(30f, 630f)
        stage.addActor(musicLbl)

        val musicChkBox = CheckBox(null, musicChkBoxStyle)
        musicChkBox.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        musicChkBox.setPosition(300f, 630f)
        musicChkBox.isChecked = music
        musicChkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                music = musicChkBox.isChecked
            }
        })
        stage.addActor(musicChkBox)

        val soundLbl = Label("¿Activar sonido? ", skin, "default")
        soundLbl.height = CHECKBOX_SIZE
        soundLbl.setPosition(30f, 570f)
        stage.addActor(soundLbl)

        val soundChkBox = CheckBox(null, musicChkBoxStyle)
        soundChkBox.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        soundChkBox.setPosition(300f, 570f)
        soundChkBox.isChecked = music
        soundChkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                sound = soundChkBox.isChecked
            }
        })
        stage.addActor(soundChkBox)

        // Color picker
        val colorSelectLbl = Label("Color del auto: ", skin, "default")
        colorSelectLbl.height = SMALL_BUTTON_HEIGHT
        colorSelectLbl.setPosition(30f, 510f)
        stage.addActor(colorSelectLbl)

        val colorSelectDialog = Dialog("", skin)

        val colorSelectBtn = Button(colorSelectBtnStyle)
        colorSelectBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        colorSelectBtn.setPosition(300f, 510f)
        colorSelectBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                colorSelectDialog.isVisible = true
            }
        })
        stage.addActor(colorSelectBtn)

        val colorSelectTable = Table()
        colorSelectTable.defaults().pad(10f).width(SMALL_BUTTON_WIDTH).height(SMALL_BUTTON_HEIGHT)
        colorSelectTable.row()
        val tblBtnWhite = Button(colorSelectBtnStyle)
        tblBtnWhite.color = Color.WHITE
        tblBtnWhite.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnWhite)
        val tblBtnRed = Button(colorSelectBtnStyle)
        tblBtnRed.color = Color.RED
        tblBtnRed.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnRed)
        val tblBtnBlue = Button(colorSelectBtnStyle)
        tblBtnBlue.color = Color.BLUE
        tblBtnBlue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnBlue)
        val tblBtnYellow = Button(colorSelectBtnStyle)
        tblBtnYellow.color = Color.YELLOW
        tblBtnYellow.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnYellow)
        val tblBtnPink = Button(colorSelectBtnStyle)
        tblBtnPink.color = Color.PINK
        tblBtnPink.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnPink)
        val tblBtnGreen = Button(colorSelectBtnStyle)
        tblBtnGreen.color = Color.GREEN
        tblBtnGreen.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnGreen)
        colorSelectTable.row()
        val tblBtnGray = Button(colorSelectBtnStyle)
        tblBtnGray.color = Color.GRAY
        tblBtnGray.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnGray)
        val tblBtnGold = Button(colorSelectBtnStyle)
        tblBtnGold.color = Color.GOLD
        tblBtnGold.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnGold)
        val tblBtnOrange = Button(colorSelectBtnStyle)
        tblBtnOrange.color = Color.ORANGE
        tblBtnOrange.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnOrange)
        val tblBtnBrown = Button(colorSelectBtnStyle)
        tblBtnBrown.color = Color.BROWN
        tblBtnBrown.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnBrown)
        val tblBtnMagenta = Button(colorSelectBtnStyle)
        tblBtnMagenta.color = Color.MAGENTA
        tblBtnMagenta.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnMagenta)
        val tblBtnPurple = Button(colorSelectBtnStyle)
        tblBtnPurple.color = Color.PURPLE
        tblBtnPurple.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val color = actor.color
                carColor = getColorStr(color)
                colorSelectBtn.color = color
                colorSelectDialog.isVisible = false
            }
        })
        colorSelectTable.add(tblBtnPurple)
        // colorSelectTable.setPosition(200f, 200f)
        // stage.addActor(colorSelectTable)
        colorSelectDialog.width = 400f
        colorSelectDialog.setPosition(40f, WINDOW_HEIGHT_HALF - 120)
        colorSelectDialog.contentTable.add(colorSelectTable)
        colorSelectDialog.isVisible = false
        stage.addActor(colorSelectDialog)

        // Logo
        val scale = 0.5f
        val logo = Image(assets.get(LOGO_PATH, Texture::class.java))
        logo.setScale(scale)
        logo.setPosition(WINDOW_WIDTH_HALF - (logo.width * scale) / 2, LOGO_Y)
        stage.addActor(logo)

        // Next button
        val nextBtnLbl = Label(" EMPEZAR ", skin, "default")
        val nextBtnIcon = Image(assets.get(ARROW_RIGHT_ICON_PATH, Texture::class.java))
        val nextBtn = ImageTextButton(null, skin, "nextBtn")
        nextBtn.add(nextBtnIcon, nextBtnLbl)
        nextBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        nextBtn.setPosition(MAIN_MENU_BUTTON_X, EXIT_BUTTON_Y)
        nextBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playerName = playerNameTxtField.text
                if (playerName.isBlank()) {
                    errorLbl.isVisible = true
                } else {
                    prefs.putString("playerName", playerName.uppercase(Locale.ROOT))
                    prefs.putInteger("hiScore", 0)
                    prefs.putString("carColor", carColor)
                    prefs.putBoolean("music", music)
                    prefs.putBoolean("sound", music)
                    prefs.putFloat("musicVolume", DEFAULT_MUSIC_VOLUME)
                    prefs.putFloat("soundVolume", DEFAULT_SOUND_VOLUME)
                    prefs.flush()
                    dispose()
                    // android.hideSystemUI()
                    // game.screen = GameScreen(game) //MainMenuScreen(game)
                    game.screen = MainMenuScreen(game)
                }
            }
        })
        stage.addActor(nextBtn)
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
        prefs.flush()
        stage.dispose()
        skin.dispose()
    }
}