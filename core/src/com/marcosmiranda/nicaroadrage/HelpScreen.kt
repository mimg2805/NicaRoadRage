package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.StretchViewport

class HelpScreen(game: NicaRoadRage) : Screen {

    private val assets = game.assets
    private val skin: Skin
    private val stage: Stage

    init {
        setBackColor(BACK_COLOR)

        skin = Skin()
        stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
        Gdx.input.inputProcessor = stage
        // stage.isDebugAll = true

        // Get fonts from the asset manager
        val pixel14 = assets.get(PIXEL_EMULATOR_14, BitmapFont::class.java)
        val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
        val defaultLblStyle = LabelStyle(pixel16, Color.WHITE)
        val helpLblStyle = LabelStyle(pixel14, Color.WHITE)
        skin.add("default", defaultLblStyle)
        skin.add("help", helpLblStyle)

        // Rounded rectangle button
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable

        // Sprites
        val imgX = 20f
        val imgPlayerCar = Image(assets.get(PLAYER_CAR_SPRITE_PATH, Texture::class.java))
        // imgPlayerCar.color =
        imgPlayerCar.setPosition(imgX, 775f)
        stage.addActor(imgPlayerCar)

        val imgBus = Image(assets.get(BUS_SPRITE_PATH, Texture::class.java))
        imgBus.setPosition(imgX, 530f)
        stage.addActor(imgBus)

        val imgPowerUp = Image(assets.get(POWERUP_SPRITE_PATH, Texture::class.java))
        imgPowerUp.setPosition(imgX, 347f)
        stage.addActor(imgPowerUp)

        val imgRedCrossDialog = Image(assets.get(RED_CROSS_DIALOG_SPRITE_PATH, Texture::class.java))
        imgRedCrossDialog.setPosition(imgX, 240f)
        stage.addActor(imgRedCrossDialog)

        // Texto
        val lblX = 80f
        val lblWidth = 380f
        val lblHelp1 = Label("Al iniciar el juego, controlas un auto de tu color favorito. EL auto se controla presionando la pantalla a la izquierda o a la derecha. Empiezas con 100 puntos de combustible y acelerarás por la carretera automáticamente mientras tengas combustible.", helpLblStyle)
        lblHelp1.setPosition(lblX, 800f)
        lblHelp1.width = lblWidth
        lblHelp1.wrap = true
        stage.addActor(lblHelp1)

        val lblHelp2 = Label("Tu objetivo es esquivar los vehículos y obstáculos que aparecerán en la carretera.\nHay vehículos pequeños, de tamaño similar al tuyo, y grandes, como autobuses y camiones. Si chocas con un vehículo pequeño, derraparás un poco, y si tocas los bordes de la carretera mientras estás derrapando, explotarás. También explotarás instantáneamente si chocas con un vehículo grande, o con algún obstáculo que bloquee la carretera.\nSi explotas, perderás 10 puntos de combustible.", helpLblStyle)
        lblHelp2.setPosition(lblX, 550f)
        lblHelp2.width = lblWidth
        lblHelp2.wrap = true
        stage.addActor(lblHelp2)

        val lblHelp3 = Label("Cada cierto tiempo, aparecerá un auto de colores.\nSi lo tocas, ganarás 30 puntos de combustible.", helpLblStyle)
        lblHelp3.setPosition(lblX, 360f)
        lblHelp3.width = lblWidth
        lblHelp3.wrap = true
        stage.addActor(lblHelp3)

        val lblHelp4 = Label("¡Ten cuidado!\nSi ves un icono de cruz roja que aparece debajo de ti, ¡viene una ambulancia!\nSi chocas contra una ambulancia, explotarás.", helpLblStyle)
        lblHelp4.setPosition(lblX, 240f)
        lblHelp4.width = lblWidth
        lblHelp4.wrap = true
        stage.addActor(lblHelp4)

        val lblHelp5 = Label("¡Diviértete!", helpLblStyle)
        lblHelp5.setPosition(50f, 100f)
        stage.addActor(lblHelp5)

        val creditsBtnStyle = ImageTextButtonStyle()
        creditsBtnStyle.up = skin.newDrawable(btnDrawable, Color.VIOLET)
        creditsBtnStyle.down = skin.newDrawable(btnDrawable, Color.MAGENTA)
        creditsBtnStyle.font = pixel16
        skin.add("creditsBtn", creditsBtnStyle)

        val backBtnStyle = ImageTextButtonStyle()
        backBtnStyle.up = skin.newDrawable(btnDrawable, Color.SCARLET)
        backBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        backBtnStyle.font = pixel16
        skin.add("backBtn", backBtnStyle)

        val creditsBtnLbl = Label(" CRÉDITOS ", skin)
        val creditsBtnIcon = Image(assets.get(STAR_ICON_PATH, Texture::class.java))
        val creditsBtn = ImageTextButton(null, skin, "creditsBtn")
        creditsBtn.add(creditsBtnIcon, creditsBtnLbl)
        creditsBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        creditsBtn.setPosition(MAIN_MENU_BUTTON_X, 120f)
        creditsBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = CreditsScreen(game)
            }
        })
        stage.addActor(creditsBtn)

        val backBtnLbl = Label(" VOLVER ", skin)
        val backBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
        val backBtn = ImageTextButton(null, skin, "backBtn")
        backBtn.add(backBtnIcon, backBtnLbl)
        backBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        backBtn.setPosition(MAIN_MENU_BUTTON_X, 40f)
        backBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = MainMenuScreen(game)
            }
        })
        stage.addActor(backBtn)
    }

    override fun render(delta: Float) {
        clear()
        stage.act(delta.coerceAtMost(1 / FRAME_RATE))
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun show() {}

    override fun hide() {}

    override fun pause() {
        //dispose();
    }

    override fun resume() {}

    override fun dispose() {
        stage.dispose()
        skin.dispose()
    }
}