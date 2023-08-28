package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.StretchViewport

internal class MoreAppsScreen(game: NicaRoadRage) : Screen {

    private val assets = game.assets
    private val window = game.window

    private val skin: Skin
    private val stage: Stage

    init {
        setBackColor(BACK_COLOR)

        skin = Skin()
        stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
        Gdx.input.inputProcessor = stage
        // stage.isDebugAll = true

        // Get fonts from the asset manager
        val pixel14 = loadFont(game.assets, PIXELEMULATOR_FONT_NAME, 14)
        val pixel16 = loadFont(game.assets, PIXELEMULATOR_FONT_NAME, 16)
        val pixel16LblStyle = LabelStyle(pixel16, Color.WHITE)
        val pixel14LblStyle = LabelStyle(pixel14, Color.WHITE)
        skin.add("default", pixel16LblStyle)
        skin.add("appTitle", pixel16LblStyle)
        skin.add("appDesc", pixel14LblStyle)

        // Rounded rectangle button
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable

        // App images
        val seniasnicasImg = Image(assets.get(SENIASNICAS_IMG_PATH, Texture::class.java))
        seniasnicasImg.setScale(0.2f)
        seniasnicasImg.setPosition(30f, 670f)
        seniasnicasImg.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                window.openPlayStore("seniasnicas")
            }
        })
        stage.addActor(seniasnicasImg)

        val cursoestadisticabasicaImg = Image(assets.get(CURSOESTADISTICABASICA_IMG_PATH, Texture::class.java))
        cursoestadisticabasicaImg.setScale(0.2f)
        cursoestadisticabasicaImg.setPosition(35f, 530f)
        cursoestadisticabasicaImg.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                window.openPlayStore("cursoestadisticabasica")
            }
        })
        stage.addActor(cursoestadisticabasicaImg)

        val purisimaImg = Image(assets.get(PURISIMA_IMG_PATH, Texture::class.java))
        purisimaImg.setScale(0.2f)
        purisimaImg.setPosition(20f, 290f)
        purisimaImg.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                window.openPlayStore("purisima")
            }
        })
        stage.addActor(purisimaImg)

        // Texto
        val moreApps1Lbl = Label("Más Apps", pixel16LblStyle)
        val moreApps2Lbl = Label("Click en la imagen para abrir", pixel14LblStyle)
        moreApps1Lbl.setPosition(WINDOW_WIDTH_HALF - (moreApps1Lbl.width / 2), 850f)
        moreApps2Lbl.setPosition(WINDOW_WIDTH_HALF - (moreApps2Lbl.width / 2), 820f)
        stage.addActor(moreApps1Lbl)
        stage.addActor(moreApps2Lbl)

        val lblX = 180f
        val lblWidth = 300f

        val seniasnicasTitleLbl = Label("Señas Nicas", pixel16LblStyle)
        val seniasnicasDescLbl = Label("Aprende y enseña lenguaje de señas nicaragüense (LSN)", pixel14LblStyle)
        seniasnicasTitleLbl.setPosition(lblX, 750f)
        seniasnicasDescLbl.setPosition(lblX, 700f)
        seniasnicasTitleLbl.width = lblWidth
        seniasnicasDescLbl.width = lblWidth
        seniasnicasTitleLbl.wrap = true
        seniasnicasDescLbl.wrap = true
        stage.addActor(seniasnicasTitleLbl)
        stage.addActor(seniasnicasDescLbl)

        val cursoestadisticabasicaTitleLbl = Label("Curso Estadística Básica", pixel16LblStyle)
        val cursoestadisticabasicaDescLbl = Label("Una guía para el aprendizaje de la estadística.", pixel14LblStyle)
        cursoestadisticabasicaTitleLbl.setPosition(lblX, 600f)
        cursoestadisticabasicaDescLbl.setPosition(lblX, 550f)
        cursoestadisticabasicaTitleLbl.width = lblWidth
        cursoestadisticabasicaDescLbl.width = lblWidth
        cursoestadisticabasicaTitleLbl.wrap = true
        cursoestadisticabasicaDescLbl.wrap = true
        stage.addActor(cursoestadisticabasicaTitleLbl)
        stage.addActor(cursoestadisticabasicaDescLbl)

        val purisimaTitleLbl = Label("Purísima", pixel16LblStyle)
        val purisimaDescLbl = Label("Un juego móvil casual basado en las festividades nicaragüenses de La Purísima.", pixel14LblStyle)
        purisimaTitleLbl.setPosition(lblX, 470f)
        purisimaDescLbl.setPosition(lblX, 410f)
        purisimaTitleLbl.width = lblWidth
        purisimaDescLbl.width = lblWidth
        purisimaTitleLbl.wrap = true
        purisimaDescLbl.wrap = true
        stage.addActor(purisimaTitleLbl)
        stage.addActor(purisimaDescLbl)

        val backBtnStyle = ImageTextButtonStyle()
        backBtnStyle.up = skin.newDrawable(btnDrawable, Color.SCARLET)
        backBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        backBtnStyle.font = pixel16
        skin.add("backBtn", backBtnStyle)

        val backBtnLbl = Label(" VOLVER ", skin)
        val backBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
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