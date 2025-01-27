package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.StretchViewport

class OptionsScreen(private val game: NicaRoadRage) : Screen {

    private val assets = game.assets
    private val stage: Stage
    private val skin: Skin

    private val musicVolumeLbl: Label
    private val musicChkBox: CheckBox
    private val musicVolumeSld: Slider
    private val soundVolumeLbl: Label
    private val soundVolumeSld: Slider
    private val soundChkBox: CheckBox

    private val prefs: Preferences
    private var playerName: String
    private var kmsRecord: Int
    private var carColor: String
    private var playMusic: Boolean
    private var musicVolume: Float
    private var playSound: Boolean
    private var soundVolume: Float

    private val menuMusic = game.menuMusic
    private val crashingSound = Gdx.audio.newSound(Gdx.files.internal(CRASHING_SOUND_PATH))
    private var crashingSoundId = 0L

    init {
        setBackColor(BACK_COLOR)

        // Get the player preferences
        prefs = Gdx.app.getPreferences("NicaRoadRage")
        playerName = prefs.getString("playerName", "")
        kmsRecord = prefs.getInteger("kmsRecord", 0)
        carColor = prefs.getString("carColor", "white")
        playMusic = prefs.getBoolean("music", true)
        playSound = prefs.getBoolean("sound", true)
        musicVolume = prefs.getFloat("musicVolume", DEFAULT_MUSIC_VOLUME)
        soundVolume = prefs.getFloat("soundVolume", DEFAULT_SOUND_VOLUME)

        // Menu music
        if (playMusic && !menuMusic.isPlaying) {
            menuMusic.volume = musicVolume
            menuMusic.isLooping = true
            menuMusic.play()
        }

        // Create the appropiate objects for drawing
        skin = Skin()
        stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
        Gdx.input.inputProcessor = stage
        // stage.isDebugAll = true

        // Add a white pixmap to the skin
        val pixmap = Pixmap(10, 10, Pixmap.Format.RGBA8888)
        val pixmapName = "white"
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        skin.add(pixmapName, Texture(pixmap))

        // Rounded rectangle buttons
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable
        val smallBtnImage = Image(assets.get(BUTTON_SMALL_SPRITE_PATH, Texture::class.java))
        val smallBtnDrawable = smallBtnImage.drawable

        // add the check icon for use with the checkbox
        val check = Sprite(assets.get(CHECK_ICON_PATH, Texture::class.java))
        check.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        skin.add("check", check)

        // Get fonts from the asset manager & build the various element styles
        val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
        val pixel18 = assets.get(PIXEL_EMULATOR_18, BitmapFont::class.java)
        val defaultLblStyle = Label.LabelStyle(pixel16, Color.WHITE)
        val optionsLblStyle = Label.LabelStyle(pixel18, Color.WHITE)
        val resultLblStyle = Label.LabelStyle(pixel16, Color.WHITE)
        skin.add("default", defaultLblStyle)
        skin.add("options", optionsLblStyle)
        skin.add("result", resultLblStyle)

        val playerOptsBtnStyle = ImageTextButton.ImageTextButtonStyle()
        playerOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIGHT_GRAY)
        playerOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
        playerOptsBtnStyle.font = pixel16
        skin.add("playerOptsBtn", playerOptsBtnStyle)

        val soundOptsBtnStyle = ImageTextButton.ImageTextButtonStyle()
        soundOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.WHITE)
        soundOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
        soundOptsBtnStyle.font = pixel16
        skin.add("soundOptsBtn", soundOptsBtnStyle)

        val playerNameTxtFieldStyle = TextField.TextFieldStyle()
        playerNameTxtFieldStyle.font = pixel18
        playerNameTxtFieldStyle.fontColor = Color.WHITE
        playerNameTxtFieldStyle.background = skin.newDrawable(btnDrawable, Color.WHITE)
        playerNameTxtFieldStyle.background.leftWidth = TEXTFIELD_PADDING
        playerNameTxtFieldStyle.background.rightWidth = TEXTFIELD_PADDING
        playerNameTxtFieldStyle.cursor = skin.newDrawable(pixmapName, Color.DARK_GRAY)
        playerNameTxtFieldStyle.cursor.minWidth = CURSOR_WIDTH
        skin.add("playerNameTxtField", playerNameTxtFieldStyle)

        val greenBtnStyle = ImageButton.ImageButtonStyle()
        greenBtnStyle.up = skin.newDrawable(smallBtnDrawable, Color.LIME)
        greenBtnStyle.down = skin.newDrawable(btnDrawable, Color.FOREST)
        skin.add("greenBtn", greenBtnStyle)

        val redBtnStyle = ImageButton.ImageButtonStyle()
        redBtnStyle.up = skin.newDrawable(smallBtnDrawable, Color.SCARLET)
        redBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        skin.add("redBtn", redBtnStyle)

        val colorSelectBtnStyle = Button.ButtonStyle()
        colorSelectBtnStyle.up = skin.newDrawable(btnDrawable, Color.WHITE)
        // colorSelectBtnStyle.down = skin.newDrawable(btnDrawable, getColorFromStr(carColor).mul(Color.GRAY))
        skin.add("colorSelectBtn", colorSelectBtnStyle)

        val colorSelectDialogStyle = Window.WindowStyle()
        colorSelectDialogStyle.titleFont = pixel16
        colorSelectDialogStyle.titleFontColor = Color.WHITE
        colorSelectDialogStyle.background = null //skin.newDrawable(btnDrawable, Color.ORANGE)
        colorSelectDialogStyle.stageBackground = null //skin.newDrawable("white", Color.valueOf("000000AA")); //skin.newDrawable(btnDrawable, Color.ORANGE)
        skin.add("default", colorSelectDialogStyle)

        val musicChkBoxStyle = CheckBox.CheckBoxStyle()
        musicChkBoxStyle.up = skin.newDrawable(smallBtnDrawable, Color.WHITE)
        musicChkBoxStyle.down = skin.newDrawable(smallBtnDrawable, Color.GRAY)
        musicChkBoxStyle.checkboxOn = skin.newDrawable("check", Color.BLACK)
        musicChkBoxStyle.font = pixel16
        skin.add("musicChkBox", musicChkBoxStyle)

        val volumeSldStyle = Slider.SliderStyle()
        volumeSldStyle.knob = skin.newDrawable(btnDrawable, Color.BLUE)
        volumeSldStyle.knobDown = skin.newDrawable(btnDrawable, Color.ROYAL)
        volumeSldStyle.background = skin.newDrawable(btnDrawable, Color.TEAL)
        val knobWidth = 30f
        val knobHeight = 70f
        val sliderHeight = 50f
        volumeSldStyle.knob.minWidth = knobWidth
        volumeSldStyle.knobDown.minWidth = knobWidth
        volumeSldStyle.knob.minHeight = knobHeight
        volumeSldStyle.knobDown.minHeight = knobHeight
        volumeSldStyle.background.minHeight = sliderHeight
        skin.add("volumeSld", volumeSldStyle)

        val backBtnStyle = ImageTextButton.ImageTextButtonStyle()
        backBtnStyle.up = skin.newDrawable(btnDrawable, Color.SCARLET)
        backBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        backBtnStyle.font = pixel16
        skin.add("backBtn", backBtnStyle)

        // Create the elements

        // Change player name row
        val playerNameLbl = Label("Nombre del jugador:", skin, "options")
        playerNameLbl.setPosition(20f, 770f)
        stage.addActor(playerNameLbl)

        val playerNameTxtField = TextField(playerName, skin, "playerNameTxtField")
        playerNameTxtField.setSize(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT)
        playerNameTxtField.setPosition(20f, 700f)
        stage.addActor(playerNameTxtField)

        val resultLbl = Label("", skin, "result")
        resultLbl.setSize(400f, 48f)
        resultLbl.setPosition(20f, 640f)
        // resultLbl.isVisible = false
        resultLbl.wrap = true
        stage.addActor(resultLbl)

        val saveBtnIcon = Image(game.assets.get(SAVE_ICON_PATH, Texture::class.java))
        val saveBtn = ImageButton(greenBtnStyle)
        saveBtn.add(saveBtnIcon)
        saveBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        saveBtn.setPosition(340f, 700f)
        saveBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playerName = playerNameTxtField.text
                if (playerName.isBlank()) {
                    resultLbl.setText("ERROR: El campo no puede estar vacío.")
                    resultLbl.color = Color.RED
                    resultLbl.isVisible = true
                } else {
                    prefs.putString("playerName", playerName)
                    prefs.flush()
                    resultLbl.setText("¡Guardado!")
                    resultLbl.color = Color.GREEN
                    resultLbl.isVisible = true
                }
            }
        })
        stage.addActor(saveBtn)

        // Reset high score row
        val kmsRecordLbl = Label("Distancia máxima: $kmsRecord", skin, "options")
        kmsRecordLbl.height = 48f
        kmsRecordLbl.setPosition(20f, 550f)
        stage.addActor(kmsRecordLbl)
        val eraseBtnIcon = Image(game.assets.get(TRASH_ICON_PATH, Texture::class.java))
        val eraseBtn = ImageButton(redBtnStyle)
        eraseBtn.add(eraseBtnIcon)
        eraseBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        eraseBtn.setPosition(400f, 550f)
        eraseBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                kmsRecord = 0
                kmsRecordLbl.setText("Distancia máxima: $kmsRecord")
                prefs.putInteger("kmsRecord", kmsRecord)
                prefs.flush()
            }
        })
        stage.addActor(eraseBtn)

        // Color picker
        val colorSelectLbl = Label("COLOR DEL AUTO:", skin, "options")
        colorSelectLbl.height = SMALL_BUTTON_HEIGHT
        colorSelectLbl.setPosition(20f, 450f)
        stage.addActor(colorSelectLbl)

        val colorSelectDialog = Dialog("", skin)

        val colorSelectBtn = Button(colorSelectBtnStyle)
        colorSelectBtn.color = getColorFromStr(carColor)
        colorSelectBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        colorSelectBtn.setPosition(300f, 450f)
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
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
                prefs.putString("carColor", carColor)
                prefs.flush()
            }
        })
        colorSelectTable.add(tblBtnPurple)
        // colorSelectTable.setPosition(200f, 200f)
        // stage.addActor(colorSelectTable)
        colorSelectDialog.width = 400f
        colorSelectDialog.setPosition(40f, 250f)
        colorSelectDialog.contentTable.add(colorSelectTable)
        colorSelectDialog.isVisible = false
        stage.addActor(colorSelectDialog)

        // Toggle music row
        val musicToggleY = 750f
        val musicTxtLbl = Label("Música: ", skin, "options")
        musicTxtLbl.height = 48f
        musicTxtLbl.setPosition(20f, musicToggleY)
        musicTxtLbl.isVisible = false
        stage.addActor(musicTxtLbl)
        musicChkBox = CheckBox(null, musicChkBoxStyle)
        musicChkBox.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        musicChkBox.setPosition(150f, musicToggleY)
        musicChkBox.isChecked = playMusic
        musicChkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playMusic = musicChkBox.isChecked
                if (playMusic) {
                    menuMusic.volume = musicVolume / VOLUME_DIVIDER
                    menuMusic.isLooping = true
                    menuMusic.play()
                } else {
                    menuMusic.pause()
                }
                prefs.putBoolean("music", playMusic)
                prefs.flush()
            }
        })
        musicChkBox.isVisible = false
        stage.addActor(musicChkBox)

        // Music volume row
        val musicVolumeBtnsY = 630f
        val musicVolumeTxtLbl = Label("Volumen de la música:", skin, "options")
        musicVolumeTxtLbl.setPosition(20f, musicVolumeBtnsY + 70f)
        musicVolumeTxtLbl.isVisible = false
        stage.addActor(musicVolumeTxtLbl)
        musicVolumeLbl = Label("" + musicVolume.toInt() + "", skin, "options")
        musicVolumeLbl.setPosition(350f, musicVolumeBtnsY + 70f)
        musicVolumeLbl.isVisible = false
        stage.addActor(musicVolumeLbl)

        val musicVolumeDownBtnIcon = Image(game.assets.get(VOLUME_DOWN_ICON_PATH, Texture::class.java))
        val musicVolumeDownBtn = ImageButton(redBtnStyle)
        musicVolumeDownBtn.add(musicVolumeDownBtnIcon)
        musicVolumeDownBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        musicVolumeDownBtn.setPosition(20f, musicVolumeBtnsY)
        musicVolumeDownBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                musicVolumeUpdate(musicVolume - 10)
            }
        })
        musicVolumeDownBtn.isVisible = false
        stage.addActor(musicVolumeDownBtn)

        musicVolumeSld = Slider(0f, 100f, 1f, false, skin, "volumeSld")
        musicVolumeSld.width = 250f
        musicVolumeSld.setPosition(70f, (musicVolumeBtnsY - 10))
        musicVolumeSld.value = musicVolume
        musicVolumeSld.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                musicVolumeUpdate(musicVolumeSld.value)
            }
        })
        musicVolumeSld.isVisible = false
        stage.addActor(musicVolumeSld)

        val musicVolumeUpBtnIcon = Image(game.assets.get(VOLUME_UP_ICON_PATH, Texture::class.java))
        val musicVolumeUpBtn = ImageButton(greenBtnStyle)
        musicVolumeUpBtn.add(musicVolumeUpBtnIcon)
        musicVolumeUpBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        musicVolumeUpBtn.setPosition(320f, musicVolumeBtnsY)
        musicVolumeUpBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                musicVolumeUpdate(musicVolume + 10)
            }
        })
        musicVolumeUpBtn.isVisible = false
        stage.addActor(musicVolumeUpBtn)

        // Toggle sound row
        val soundToggleY = 550f
        val soundTxtLbl = Label("Sonido: ", skin, "options")
        soundTxtLbl.height = 48f
        soundTxtLbl.setPosition(20f, soundToggleY)
        soundTxtLbl.isVisible = false
        stage.addActor(soundTxtLbl)
        soundChkBox = CheckBox(null, musicChkBoxStyle)
        soundChkBox.setSize(CHECKBOX_SIZE, CHECKBOX_SIZE)
        soundChkBox.setPosition(150f, soundToggleY)
        soundChkBox.isChecked = playSound
        soundChkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playSound = soundChkBox.isChecked
                if (playSound) {
                    crashingSoundId = crashingSound.play(soundVolume / VOLUME_DIVIDER)
                    crashingSound.setLooping(crashingSoundId, false)
                }
                prefs.putBoolean("sound", playSound)
                prefs.flush()
            }
        })
        soundChkBox.isVisible = false
        stage.addActor(soundChkBox)

        // Sound volume row
        val soundVolumeBtnsY = 430f
        val soundVolumeTxtLbl = Label("Volumen del sonido:", skin, "options")
        soundVolumeTxtLbl.setPosition(20f, soundVolumeBtnsY + 70f)
        soundVolumeTxtLbl.isVisible = false
        stage.addActor(soundVolumeTxtLbl)
        soundVolumeLbl = Label("" + soundVolume.toInt() + "", skin, "options")
        soundVolumeLbl.setPosition(350f, soundVolumeBtnsY + 70f)
        soundVolumeLbl.isVisible = false
        stage.addActor(soundVolumeLbl)

        val soundVolumeDownBtnIcon = Image(game.assets.get(VOLUME_DOWN_ICON_PATH, Texture::class.java))
        val soundVolumeDownBtn = ImageButton(redBtnStyle)
        soundVolumeDownBtn.add(soundVolumeDownBtnIcon)
        soundVolumeDownBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        soundVolumeDownBtn.setPosition(20f, soundVolumeBtnsY)
        soundVolumeDownBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                soundVolumeUpdate(soundVolume - 10)
            }
        })
        soundVolumeDownBtn.isVisible = false
        stage.addActor(soundVolumeDownBtn)

        soundVolumeSld = Slider(0f, 100f, 1f, false, skin, "volumeSld")
        soundVolumeSld.width = 250f
        soundVolumeSld.setPosition(70f, (soundVolumeBtnsY - 10))
        soundVolumeSld.value = soundVolume
        soundVolumeSld.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                soundVolumeUpdate(soundVolumeSld.value)
            }
        })
        soundVolumeSld.isVisible = false
        stage.addActor(soundVolumeSld)

        val soundVolumeUpBtnIcon = Image(game.assets.get(VOLUME_UP_ICON_PATH, Texture::class.java))
        val soundVolumeUpBtn = ImageButton(greenBtnStyle)
        soundVolumeUpBtn.add(soundVolumeUpBtnIcon)
        soundVolumeUpBtn.setSize(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT)
        soundVolumeUpBtn.setPosition(320f, soundVolumeBtnsY)
        soundVolumeUpBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                soundVolumeUpdate(soundVolume + 10)
            }
        })
        soundVolumeUpBtn.isVisible = false
        stage.addActor(soundVolumeUpBtn)

        // Options row at the top
        val playerOptsBtnLbl = Label(" JUGADOR ", skin, "default")
        val playerOptsBtnIcon = Image(game.assets.get(COG_ICON_PATH, Texture::class.java))
        val playerOptsBtn = ImageTextButton(null, skin, "playerOptsBtn")
        playerOptsBtn.add(playerOptsBtnIcon, playerOptsBtnLbl)
        playerOptsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        playerOptsBtn.setPosition(15f, 850f)
        playerOptsBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playerNameLbl.isVisible = true
                playerNameTxtField.isVisible = true
                saveBtn.isVisible = true
                kmsRecordLbl.isVisible = true
                eraseBtn.isVisible = true
                colorSelectLbl.isVisible = true
                colorSelectBtn.isVisible = true
                colorSelectDialog.isVisible = false

                musicTxtLbl.isVisible = false
                musicChkBox.isVisible = false
                musicVolumeTxtLbl.isVisible = false
                musicVolumeLbl.isVisible = false
                musicVolumeDownBtn.isVisible = false
                musicVolumeSld.isVisible = false
                musicVolumeUpBtn.isVisible = false
                soundTxtLbl.isVisible = false
                soundChkBox.isVisible = false
                soundVolumeTxtLbl.isVisible = false
                soundVolumeLbl.isVisible = false
                soundVolumeDownBtn.isVisible = false
                soundVolumeSld.isVisible = false
                soundVolumeUpBtn.isVisible = false

                playerOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIGHT_GRAY)
                playerOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
                soundOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.WHITE)
                soundOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
            }
        })
        stage.addActor(playerOptsBtn)

        val soundOptsBtnLbl = Label(" SONIDO ", skin, "default")
        val soundOptsBtnIcon = Image(game.assets.get(COG_ICON_PATH, Texture::class.java))
        val soundOptsBtn = ImageTextButton(null, skin, "soundOptsBtn")
        soundOptsBtn.add(soundOptsBtnIcon, soundOptsBtnLbl)
        soundOptsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        soundOptsBtn.setPosition(245f, 850f)
        soundOptsBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                playerNameLbl.isVisible = false
                playerNameTxtField.isVisible = false
                resultLbl.isVisible = false
                saveBtn.isVisible = false
                kmsRecordLbl.isVisible = false
                eraseBtn.isVisible = false
                colorSelectLbl.isVisible = false
                colorSelectBtn.isVisible = false
                colorSelectDialog.isVisible = false

                musicTxtLbl.isVisible = true
                musicChkBox.isVisible = true
                musicVolumeTxtLbl.isVisible = true
                musicVolumeLbl.isVisible = true
                musicVolumeDownBtn.isVisible = true
                musicVolumeSld.isVisible = true
                musicVolumeUpBtn.isVisible = true
                soundTxtLbl.isVisible = true
                soundChkBox.isVisible = true
                soundVolumeTxtLbl.isVisible = true
                soundVolumeLbl.isVisible = true
                soundVolumeDownBtn.isVisible = true
                soundVolumeSld.isVisible = true
                soundVolumeUpBtn.isVisible = true

                playerOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.WHITE)
                playerOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
                soundOptsBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIGHT_GRAY)
                soundOptsBtnStyle.down = skin.newDrawable(btnDrawable, Color.DARK_GRAY)
            }
        })
        stage.addActor(soundOptsBtn)

        // Back button
        val backBtnLbl = Label(" VOLVER ", skin, "default")
        val backBtnIcon = Image(game.assets.get(EXIT_ICON_PATH, Texture::class.java))
        val backBtn = ImageTextButton(null, skin, "backBtn")
        backBtn.add(backBtnIcon, backBtnLbl)
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        backBtn.setPosition(MAIN_MENU_BUTTON_X, EXIT_BUTTON_Y)
        backBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = MainMenuScreen(game)
            }
        })
        stage.addActor(backBtn)
    }

    private fun musicUpdate(music: Boolean) {
        this.playMusic = music
        musicChkBox.isChecked = music
        prefs.putBoolean("music", music)
        prefs.flush()
    }

    private fun musicVolumeUpdate(newMusicVolume: Float) {
        musicVolume = newMusicVolume
        when {
            musicVolume <= 0f -> {
                musicVolume = 0f
                musicUpdate(false)
            }
            musicVolume > 100f -> musicVolume = 100f
            musicVolume in 1f..100f -> musicUpdate(true)
        }
        Gdx.app.log("musicVolume", musicVolume.toString())
        menuMusic.volume = musicVolume / VOLUME_DIVIDER
        musicVolumeLbl.setText(musicVolume.toInt().toString())
        musicVolumeSld.value = musicVolume
        prefs.putFloat("musicVolume", musicVolume)
        prefs.flush()
    }

    private fun soundUpdate(sound: Boolean) {
        this.playSound = sound
        soundChkBox.isChecked = sound
        prefs.putBoolean("sound", sound)
        prefs.flush()
    }

    private fun soundVolumeUpdate(newSoundVolume: Float) {
        soundVolume = newSoundVolume
        when {
            soundVolume <= 0f -> {
                soundVolume = 0f
                soundUpdate(false)
            }
            soundVolume > 100f -> soundVolume = 100f
            soundVolume in 1f..100f -> soundUpdate(true)
        }
        soundVolumeLbl.setText(soundVolume.toInt().toString())
        soundVolumeSld.value = soundVolume
        if (playSound) {
            crashingSoundId = crashingSound.play(soundVolume / VOLUME_DIVIDER)
            crashingSound.setLooping(crashingSoundId, false)
        }
        prefs.putFloat("soundVolume", soundVolume)
        prefs.flush()
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
        crashingSound.dispose()
    }


}