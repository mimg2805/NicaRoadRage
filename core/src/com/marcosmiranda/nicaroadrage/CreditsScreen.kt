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

class CreditsScreen(game: NicaRoadRage) : Screen {

    private val assets = game.assets
    private val skin: Skin
    private val stage: Stage

    private val creditsSpeed = 3f
    private val creditsTitle: Label
    private val creditTitle1: Label
    private val credit1: Label
    private val imgCreditTitle: Label
    private val imgCreditTitle1: Label
    private val imgCredit1: Label
    private val imgCreditTitle2: Label
    private val imgCredit2: Label
    private val imgCreditTitle3: Label
    private val imgCredit3: Label
    private val imgCreditTitle4: Label
    private val imgCredit4: Label
    private val imgCreditTitle5: Label
    private val imgCredit5: Label
    private val imgCreditTitle6: Label
    private val imgCredit6: Label
    private val imgCreditTitle7: Label
    private val imgCredit7: Label
    private val musicCreditTitle: Label
    private val musicCreditTitle1: Label
    private val musicCredit1: Label
    private val musicCreditTitle2: Label
    private val musicCredit2: Label

    private val creditsTitleY          = -100f
    private val creditTitle1Y          = -180f
    private val credit1Y               = -210f
    private val imgCreditTitleY        = -260f
    private val imgCreditTitle1Y       = -300f
    private val imgCredit1Y            = -370f
    private val imgCreditTitle2Y       = -410f
    private val imgCredit2Y            = -460f
    private val imgCreditTitle3Y       = -525f
    private val imgCredit3Y            = -615f
    private val imgCreditTitle4Y       = -655f
    private val imgCredit4Y            = -790f
    private val imgCreditTitle5Y       = -830f
    private val imgCredit5Y            = -940f
    private val imgCreditTitle6Y       = -980f
    private val imgCredit6Y            = -1090f
    private val imgCreditTitle7Y       = -1130f
    private val imgCredit7Y            = -1160f
    private val musicCreditTitleY      = -1210f
    private val musicCreditTitle1Y     = -1250f
    private val musicCredit1Y          = -1425f
    private val musicCreditTitle2Y     = -1470f
    private val musicCredit2Y          = -1650f

    init {
        setBackColor(BACK_COLOR)

        skin = Skin()
        stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
        Gdx.input.inputProcessor = stage

        // Get fonts from the asset manager
        val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
        val pixel18 = assets.get(PIXEL_EMULATOR_18, BitmapFont::class.java)
        val pixel20 = assets.get(PIXEL_EMULATOR_20, BitmapFont::class.java)
        val pixel22 = assets.get(PIXEL_EMULATOR_22, BitmapFont::class.java)
        val pixel24 = assets.get(PIXEL_EMULATOR_24, BitmapFont::class.java)
        val pixel16LblStyle = LabelStyle(pixel16, Color.WHITE)
        val pixel18LblStyle = LabelStyle(pixel18, Color.WHITE)
        val pixel20LblStyle = LabelStyle(pixel20, Color.WHITE)
        val pixel22LblStyle = LabelStyle(pixel22, Color.WHITE)
        val pixel24YellowLblStyle = LabelStyle(pixel24, Color.YELLOW)
        skin.add("default", pixel16LblStyle)
        // skin.add("title", pixel20LblStyle)
        // skin.add("credits", pixel18LblStyle)

        // Rounded rectangle button
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable

        val creditsX = 20f
        creditsTitle = Label("CRÉDITOS", pixel24YellowLblStyle)
        creditsTitle.setPosition(WINDOW_WIDTH_HALF - (creditsTitle.width / 2), creditsTitleY)
        stage.addActor(creditsTitle)

        creditTitle1 = Label("Idea original y\nprogramación:", pixel20LblStyle)
        creditTitle1.setPosition(creditsX, creditTitle1Y)
        stage.addActor(creditTitle1)
        credit1 = Label("Marcos Iván Miranda García", pixel18LblStyle)
        credit1.setPosition(creditsX, credit1Y)
        stage.addActor(credit1)

        imgCreditTitle = Label("Créditos de imágenes:", pixel22LblStyle)
        imgCreditTitle.setPosition(creditsX, imgCreditTitleY)
        stage.addActor(imgCreditTitle)

        imgCreditTitle1 = Label("Bandera de carreras (Logo):", pixel20LblStyle)
        imgCreditTitle1.setPosition(creditsX, imgCreditTitle1Y)
        stage.addActor(imgCreditTitle1)
        imgCredit1 = Label("Freepik @ Flaticon\n(flaticon.com/free-icons/race),\nmodificado por mí", pixel18LblStyle)
        imgCredit1.setPosition(creditsX, imgCredit1Y)
        stage.addActor(imgCredit1)

        imgCreditTitle2 = Label("Camino:", pixel20LblStyle)
        imgCreditTitle2.setPosition(creditsX, imgCreditTitle2Y)
        stage.addActor(imgCreditTitle2)
        imgCredit2 = Label("TheInvader360\n(theinvader360.blogspot.com)", pixel18LblStyle)
        imgCredit2.setPosition(creditsX, imgCredit2Y)
        stage.addActor(imgCredit2)

        imgCreditTitle3 = Label("Árbol de la vida métalico\nalias 'Chayopalo':", pixel20LblStyle)
        imgCreditTitle3.setPosition(creditsX, imgCreditTitle3Y)
        stage.addActor(imgCreditTitle3)
        imgCredit3 = Label("Blog de Julio C. Moreno\n(3dmas.blogspot.com/2016/03/\nklimt-urbana-regular-proyecto-\nde.html)", pixel18LblStyle)
        imgCredit3.setPosition(creditsX, imgCredit3Y)
        stage.addActor(imgCredit3)

        imgCreditTitle4 = Label("Coches y camiones:", pixel20LblStyle)
        imgCreditTitle4.setPosition(creditsX, imgCreditTitle4Y)
        stage.addActor(imgCreditTitle4)
        imgCredit4 = Label("Road Fighter (NES) de Konami,\nripeado por JigglyPuffGirl\n@ The Spriter's Resource\n(spriters-resource.com/nes/\nroadfighter/sheet/57232),\nmodificado por mí", pixel18LblStyle)
        imgCredit4.setPosition(creditsX, imgCredit4Y)
        stage.addActor(imgCredit4)

        imgCreditTitle5 = Label("Árbol:", pixel20LblStyle)
        imgCreditTitle5.setPosition(creditsX, imgCreditTitle5Y)
        stage.addActor(imgCreditTitle5)
        imgCredit5 = Label("'Pine Tree Tiles' por b_o,\ncon licencias CC-BY-SA 3.0\ny GPL 2.0\n(opengameart.org/content/\npine-tree-tiles)", pixel18LblStyle)
        imgCredit5.setPosition(creditsX, imgCredit5Y)
        stage.addActor(imgCredit5)

        imgCreditTitle6 = Label("Casa:", pixel20LblStyle)
        imgCreditTitle6.setPosition(creditsX, imgCreditTitle6Y)
        stage.addActor(imgCreditTitle6)
        imgCredit6 = Label("'House sets' por Shepardskin:\n(opengameart.org/content/\nhouse-sets)\nShepardskin en Twitter:\ntwitter.com/Shepardskin", pixel18LblStyle)
        imgCredit6.setPosition(creditsX, imgCredit6Y)
        stage.addActor(imgCredit6)

        imgCreditTitle7 = Label("Iconos:", pixel20LblStyle)
        imgCreditTitle7.setPosition(creditsX, imgCreditTitle7Y)
        stage.addActor(imgCreditTitle7)
        imgCredit7 = Label("Font Awesome (fontawesome.com)", pixel18LblStyle)
        imgCredit7.setPosition(creditsX, imgCredit7Y)
        stage.addActor(imgCredit7)

        musicCreditTitle = Label("Créditos de música:", pixel22LblStyle)
        musicCreditTitle.setPosition(creditsX, musicCreditTitleY)
        stage.addActor(musicCreditTitle)

        musicCreditTitle1 = Label("Música del menú:", pixel20LblStyle)
        musicCreditTitle1.setPosition(creditsX, musicCreditTitle1Y)
        stage.addActor(musicCreditTitle1)
        musicCredit1 = Label("'Pause Menu Music'\npor Cleyton Kauffman\n(opengameart.org/content/\npause-menu-music)\n\nCleyton Kauffman\nen SoundCloud:\nsoundcloud.com/cleytonkauffman", pixel18LblStyle)
        musicCredit1.setPosition(creditsX, musicCredit1Y)
        stage.addActor(musicCredit1)

        musicCreditTitle2 = Label("Música del juego:", pixel20LblStyle)
        musicCreditTitle2.setPosition(creditsX, musicCreditTitle2Y)
        stage.addActor(musicCreditTitle2)
        musicCredit2 = Label("'Race Against Sunset'\npor Karl Casey\n@ White Bat Audio\n(whitebataudio.com/\nrace-against-sunset)\n\nSitio web de White Bat Audio:\nwhitebataudio.com", pixel18LblStyle)
        musicCredit2.setPosition(creditsX, musicCredit2Y)
        stage.addActor(musicCredit2)

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
                game.screen = HelpScreen(game)
            }
        })
        stage.addActor(backBtn)
    }

    override fun render(delta: Float) {
        clear()

        if (!Gdx.input.isTouched) {
            creditsTitle.y          += creditsSpeed
            creditTitle1.y          += creditsSpeed
            credit1.y               += creditsSpeed
            imgCreditTitle.y        += creditsSpeed
            imgCreditTitle1.y       += creditsSpeed
            imgCredit1.y            += creditsSpeed
            imgCreditTitle2.y       += creditsSpeed
            imgCredit2.y            += creditsSpeed
            imgCreditTitle3.y       += creditsSpeed
            imgCredit3.y            += creditsSpeed
            imgCreditTitle4.y       += creditsSpeed
            imgCredit4.y            += creditsSpeed
            imgCreditTitle5.y       += creditsSpeed
            imgCredit5.y            += creditsSpeed
            imgCreditTitle6.y       += creditsSpeed
            imgCredit6.y            += creditsSpeed
            imgCreditTitle7.y       += creditsSpeed
            imgCredit7.y            += creditsSpeed
            musicCreditTitle.y      += creditsSpeed
            musicCreditTitle1.y     += creditsSpeed
            musicCredit1.y          += creditsSpeed
            musicCreditTitle2.y     += creditsSpeed
            musicCredit2.y          += creditsSpeed

            if (musicCredit2.y > WINDOW_HEIGHT) {
                creditsTitle.y          = creditsTitleY
                creditTitle1.y          = creditTitle1Y
                credit1.y               = credit1Y
                imgCreditTitle.y        = imgCreditTitleY
                imgCreditTitle1.y       = imgCreditTitle1Y
                imgCredit1.y            = imgCredit1Y
                imgCreditTitle2.y       = imgCreditTitle2Y
                imgCredit2.y            = imgCredit2Y
                imgCreditTitle3.y       = imgCreditTitle3Y
                imgCredit3.y            = imgCredit3Y
                imgCreditTitle4.y       = imgCreditTitle4Y
                imgCredit4.y            = imgCredit4Y
                imgCreditTitle5.y       = imgCreditTitle5Y
                imgCredit5.y            = imgCredit5Y
                imgCreditTitle6.y       = imgCreditTitle6Y
                imgCredit6.y            = imgCredit6Y
                imgCreditTitle7.y       = imgCreditTitle7Y
                imgCredit7.y            = imgCredit7Y
                musicCreditTitle.y      = musicCreditTitleY
                musicCreditTitle1.y     = musicCreditTitle1Y
                musicCredit1.y          = musicCredit1Y
                musicCreditTitle2.y     = musicCreditTitle2Y
                musicCredit2.y          = musicCredit2Y
            }
        }

        stage.act(delta.coerceAtMost(1 / FRAME_RATE))
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun show() {}

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        stage.dispose()
        skin.dispose()
    }
}