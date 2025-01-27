package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.viewport.StretchViewport

class GameScreen(private val game: NicaRoadRage): Screen {

    private val assets = game.assets
    private val batch = game.batch
    private val camera = game.camera
    private var state = game.state
    private val android = game.android
    private val vibrator = Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)
    private var vibrating = false

    private val stage = Stage(StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val skin = Skin()
    private val glyph = GlyphLayout()
    // private val shapes = ShapeRenderer()
    private val pixel16 = assets.get(PIXEL_EMULATOR_16, BitmapFont::class.java)
    private val pixel20 = assets.get(PIXEL_EMULATOR_20, BitmapFont::class.java)

    private val prefs = Gdx.app.getPreferences("NicaRoadRage")
    private val playerName = prefs.getString("playerName", "")
    private val kmsRecord = prefs.getInteger("kmsRecord", 0)
    private val playMusic = prefs.getBoolean("music", true)
    private val playSound = prefs.getBoolean("sound", true)
    private val musicVolume = prefs.getFloat("musicVolume", DEFAULT_MUSIC_VOLUME) / VOLUME_DIVIDER
    private val soundVolume = prefs.getFloat("soundVolume", DEFAULT_SOUND_VOLUME) / VOLUME_DIVIDER

    private var playBtn: ImageTextButton
    private var exitBtn: ImageTextButton

    private var pauseBtn: Button
    private var pauseDialog: Dialog
    private var pauseDialogResumeBtn: ImageTextButton
    private var pauseDialogExitBtn: ImageTextButton

    // Player car and road objects
    private val playerCar = PlayerCar(assets)
    private val road = Road(assets)

    // Music objects
    private val gameMusic = game.gameMusic
    private val menuMusic = game.menuMusic

    // Sound objects
    private val countdownSound = Gdx.audio.newSound(Gdx.files.internal(COUNTDOWN_SOUND_PATH))
    private val crashingSound = Gdx.audio.newSound(Gdx.files.internal(CRASHING_SOUND_PATH))
    private val explosionSound = Gdx.audio.newSound(Gdx.files.internal(EXPLOSION_SOUND_PATH))
    private val lowFuelSound = Gdx.audio.newSound(Gdx.files.internal(LOW_FUEL_SOUND_PATH))
    private val pauseSound = Gdx.audio.newSound(Gdx.files.internal(PAUSE_SOUND_PATH))
    private val restoreFuelSound = Gdx.audio.newSound(Gdx.files.internal(RESTORE_FUEL_SOUND_PATH))
    private val startSound = Gdx.audio.newSound(Gdx.files.internal(START_SOUND_PATH))

    private var countdownSoundId = 0L
    // private var crashingSoundId = 0L
    // private var explosionSoundId = 0L
    // private var lowFuelSoundId = 0L
    private var pauseSoundId = 0L
    private var startSoundId = 0L

    private var enemyCars: MutableList<EnemyCar> = mutableListOf()
    private var ambulances: MutableList<Ambulance> = mutableListOf()
    private var powerUps: MutableList<PowerUp> = mutableListOf()
    private var roadSideItems: MutableList<RoadSideItem> = mutableListOf()

    private var enemyPool = object : Pool<EnemyCar>() {
        override fun newObject(): EnemyCar {
            return EnemyCar()
        }
    }
    private var ambulancePool = object : Pool<Ambulance>() {
        override fun newObject(): Ambulance {
            return Ambulance()
        }
    }
    private var powerUpPool = object : Pool<PowerUp>() {
        override fun newObject(): PowerUp {
            return PowerUp()
        }
    }
    private var itemPool = object : Pool<RoadSideItem>() {
        override fun newObject(): RoadSideItem {
            return RoadSideItem()
        }
    }

    private var countdown = "4"
    private var startCountdownTime = 1.25f // This fixes sound bug
    private var enemyCarTime = 0f
    private var ambulanceTime = 0f
    private var powerUpTime = 0f
    private var itemTime = 0f

    init {
        // android.hideBannerAd()
        state = GameState.STARTING

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.MENU, true)
        Gdx.input.inputProcessor = stage

        // Testing
        // playerCar.score = 10000
        // playerCar.fuel = 50

        // Pixmap
        val pixmap = Pixmap(10, 10, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        skin.add("white", Texture(pixmap))
        pixmap.dispose()

        // Font
        skin.add("default", Label.LabelStyle(pixel16, Color.WHITE))

        // Rounded rectangle button
        val btnImage = Image(assets.get(BUTTON_SPRITE_PATH, Texture::class.java))
        val btnDrawable = btnImage.drawable

        // Button styles
        val pauseBtnStyle = Button.ButtonStyle()
        pauseBtnStyle.up = Image(assets.get(PAUSE_BTN_ICON_PATH, Texture::class.java)).drawable
        pauseBtnStyle.down = Image(assets.get(PAUSE_BTN_ICON_PATH, Texture::class.java)).drawable
        skin.add("pauseBtn", pauseBtnStyle)

        val playBtnStyle = ImageTextButton.ImageTextButtonStyle()
        playBtnStyle.up = skin.newDrawable(btnDrawable, Color.LIME)
        playBtnStyle.down = skin.newDrawable(btnDrawable, Color.FOREST)
        playBtnStyle.font = pixel16
        skin.add("playBtn", playBtnStyle)

        val exitBtnStyle = ImageTextButton.ImageTextButtonStyle()
        exitBtnStyle.up = skin.newDrawable(btnDrawable, Color.SCARLET)
        exitBtnStyle.down = skin.newDrawable(btnDrawable, Color.FIREBRICK)
        exitBtnStyle.font = pixel16
        skin.add("exitBtn", exitBtnStyle)

        val playBtnLbl = Label(" ¡OTRA VEZ! ", skin)
        val playBtnIcon = Image(assets.get(PLAY_ICON_PATH, Texture::class.java))
        playBtn = ImageTextButton(null, skin, "playBtn")
        playBtn.add(playBtnIcon, playBtnLbl)
        playBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        playBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), PLAY_BTN_Y)
        playBtn.isVisible = false
        playBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                game.screen = GameScreen(game)
            }
        })
        stage.addActor(playBtn)

        val exitBtnLbl = Label(" MENÚ ", skin)
        val exitBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
        exitBtn = ImageTextButton(null, skin, "exitBtn")
        exitBtn.add(exitBtnIcon, exitBtnLbl)
        exitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        exitBtn.setPosition(WINDOW_WIDTH_HALF - (BUTTON_WIDTH / 2), EXIT_BTN_Y)
        exitBtn.isVisible = false
        exitBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                dispose()
                if (game.platform == Application.ApplicationType.Android) android?.showInterstitial()
                game.screen = MainMenuScreen(game)
            }
        })
        stage.addActor(exitBtn)

        // Pause button
        pauseBtn = Button(skin, "pauseBtn")
        pauseBtn.setSize(PAUSE_BTN_WIDTH, PAUSE_BTN_HEIGHT)
        pauseBtn.setPosition(PAUSE_BTN_X, PAUSE_BTN_Y)
        pauseBtn.isVisible = false
        pauseBtn.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                pause()
            }
        })
        stage.addActor(pauseBtn)

        // Pause dialog
        val pauseDialogStyle = Window.WindowStyle()
        pauseDialogStyle.titleFont = pixel20
        pauseDialogStyle.stageBackground = skin.newDrawable("white", Color.valueOf("000000AA"))
        skin.add("pauseDialog", pauseDialogStyle)

        pauseDialog = object : Dialog(" PAUSA ", skin, "pauseDialog") {
            override fun result(result: Any) {
                val resume = result.toString().toBoolean()
                if (resume) resume() else {
                    gameMusic.stop()
                    game.state = GameState.MENU
                    game.screen = MainMenuScreen(game)
                    if (game.platform == Application.ApplicationType.Android) android?.showInterstitial()
                }
            }
        }
        pauseDialog.isVisible = false

        val pauseDialogResumeBtnLbl = Label(" CONTINUAR ", skin)
        val pauseDialogResumeBtnIcon = Image(assets.get(PLAY_ICON_PATH, Texture::class.java))
        pauseDialogResumeBtn = ImageTextButton(null, skin, "playBtn")
        pauseDialogResumeBtn.add(pauseDialogResumeBtnIcon, pauseDialogResumeBtnLbl)
        pauseDialogResumeBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        pauseDialog.button(pauseDialogResumeBtn, true)
        // pauseDialog.buttonTable.row()

        val pauseDialogExitBtnLbl = Label(" SALIR ", skin)
        val pauseDialogExitBtnIcon = Image(assets.get(EXIT_ICON_PATH, Texture::class.java))
        pauseDialogExitBtn = ImageTextButton(null, skin, "exitBtn")
        pauseDialogExitBtn.add(pauseDialogExitBtnIcon, pauseDialogExitBtnLbl)
        pauseDialogExitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        pauseDialog.button(pauseDialogExitBtn, false)

        // Music
        if (playMusic) {
            menuMusic.stop()
            gameMusic.isLooping = true
            gameMusic.volume = musicVolume
        }
    }

    override fun render(delta: Float) {
        clear()
        // camera.update()
        batch.projectionMatrix = camera.combined
        // shapes.projectionMatrix = camera.combined
        // shapes.setAutoShapeType(true)

        batch.begin()

        // Update road
        updateRoad(delta)

        when (state) {

            GameState.STARTING -> {
                var countdownNum = 0
                if (countdown != COUNTDOWN_END_STR) countdownNum = countdown.toInt()

                startCountdownTime += delta
                if (startCountdownTime >= COUNTDOWN_DELAY) {
                    if (countdownNum > 0) {

                        startCountdownTime = 0f
                        countdownNum--
                        countdown = countdownNum.toString()

                        if (countdownNum > 0) {
                            if (playSound) {
                                countdownSoundId = countdownSound.play(soundVolume)
                                countdownSound.setLooping(countdownSoundId, false)
                            }
                        } else {
                            countdown = COUNTDOWN_END_STR
                            if (playSound) {
                                countdownSound.stop(countdownSoundId)
                                startSoundId = startSound.play(soundVolume)
                                startSound.setLooping(startSoundId, false)
                            }
                        }

                    } else {
                        pauseBtn.isVisible = true
                        if (playMusic) gameMusic.play()
                        state = GameState.RUNNING
                    }
                }

                glyph.setText(pixel20, countdown)
                pixel20.draw(batch, countdown, WINDOW_WIDTH_HALF - (glyph.width / 2), WINDOW_HEIGHT_HALF)
            }

            GameState.RUNNING -> {

                // Clear inactive road items
                clearInactiveRoadSideItems()

                // Clear inactive powerups
                clearInactivePowerUps()

                // Clear inactive enemy cars
                clearInactiveEnemyCars()

                // Clear inactive ambulances
                clearInactiveAmbulances()

                // Get a new enemy car every second unless there are ambulances
                spawnEnemyCar(delta)

                // Get a new ambulance every 60 seconds
                spawnAmbulance(delta)

                // Get a new power up every 20 seconds unless there are ambulances
                spawnPowerUp(delta)

                // Get a new road side item every second
                spawnRoadSideItem(delta)

                // Draw and update player car
                updatePlayerCar(delta)

                // Draw and update ambulances
                updateAmbulances(delta)

                // Draw and update enemy cars
                updateEnemyCars(delta)

                // Draw and update power ups
                updatePowerUps()

                // Draw and update road side items
                updateRoadSideItems()

                // HUD
                pixel20.draw(batch, "JUGADOR:\n$playerName", NAME_LOCATION_X, NAME_LOCATION_Y)
                pixel20.draw(batch, "KMS:\n${playerCar.kms}", KMS_LOCATION_X, KMS_LOCATION_Y)
                pixel20.draw(batch, "COMB.:\n${playerCar.fuel}", FUEL_LOCATION_X, FUEL_LOCATION_Y)
            }

            GameState.PAUSE -> {

            }

            GameState.GAMEOVER -> {
                if (vibrator && vibrating) {
                    Gdx.input.vibrate(VIBRATE_TIME)
                    vibrating = false
                }

                if (playMusic && gameMusic.isPlaying) gameMusic.stop()

                if (playerCar.kms > kmsRecord) {
                    prefs.putInteger("kmsRecord", playerCar.kms)
                    prefs.flush()
                    glyph.setText(pixel20, KMS_RECORD_MESSAGE)
                    pixel20.color = Color.GOLD
                    pixel20.draw(batch, KMS_RECORD_MESSAGE, WINDOW_WIDTH_HALF - (glyph.width / 2), KMS_RECORD_MESSAGE_Y)
                    pixel20.color = Color.WHITE
                }

                glyph.setText(pixel20, GAME_OVER_MESSAGE)
                pixel20.draw(batch, GAME_OVER_MESSAGE, WINDOW_WIDTH_HALF - (glyph.width / 2), GAME_OVER_MESSAGE_Y)
                val kmsStr = GAME_SCORE_MESSAGE + playerCar.kms.toString() + " KMS"
                glyph.setText(pixel20, kmsStr)
                pixel20.draw(batch, kmsStr, WINDOW_WIDTH_HALF - (glyph.width / 2), KMS_MESSAGE_Y)

                pauseBtn.isVisible = false
            }

            else -> {
                println("This shouldn't happen.")
            }
        }

        batch.end()

        // shapes.setAutoShapeType(true)
        // shapes.begin()
        // shapes.end()

        stage.act(delta.coerceAtMost(1 / FRAME_RATE))
        stage.draw()
    }

    override fun hide() {}

    override fun show() {}

    override fun pause() {
        if (playMusic && gameMusic.isPlaying) gameMusic.pause()
        if (playSound) {
            pauseSoundId = pauseSound.play(soundVolume)
            pauseSound.setLooping(pauseSoundId, false)
        }

        // pauseDialog.debug = true
        pauseDialog.show(stage)
        pauseDialog.isVisible = true
        pauseBtn.isVisible = false

        pauseDialog.width = WINDOW_WIDTH
        pauseDialog.height = WINDOW_HEIGHT_HALF
        pauseDialog.y = WINDOW_HEIGHT_HALF / 2
        pauseDialog.titleLabel.setAlignment(Align.center)
        pauseDialogResumeBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        pauseDialogExitBtn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        pauseDialogResumeBtn.setPosition(PAUSE_DIALOG_BTN_X, PLAY_BTN_Y, Align.center)
        // pauseDialogResumeBtn.setPosition(120f, 175f, Align.center)
        pauseDialogExitBtn.setPosition(PAUSE_DIALOG_BTN_X, EXIT_BTN_Y, Align.center)
        // pauseDialogExitBtn.setPosition(360f, 75f, Align.center)

        state = GameState.PAUSE
    }

    override fun resume() {
        if (playMusic && !gameMusic.isPlaying) gameMusic.play()
        if (playSound) pauseSound.stop(pauseSoundId)

        pauseDialog.hide()
        pauseDialog.isVisible = false
        pauseBtn.isVisible = true
        state = GameState.RUNNING
    }

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        countdownSound.dispose()
        crashingSound.dispose()
        explosionSound.dispose()
        lowFuelSound.dispose()
        pauseSound.dispose()
        restoreFuelSound.dispose()
        startSound.dispose()

        skin.dispose()
        stage.dispose()

        enemyCars.clear()
        enemyPool.clear()

        roadSideItems.clear()
        itemPool.clear()
    }

    // Utility functions

    // Get a new enemy car every second
    private fun spawnEnemyCar(delta: Float) {
        if (playerCar.active && ambulances.size == 0) {
            enemyCarTime += delta
            if (enemyCarTime >= ENEMY_CAR_SPAWN_DELAY) {
                val newEnemyCar = enemyPool.obtain()

                var newEnemyCarOverlaps = false
                for (enemyCar in enemyCars) {
                    newEnemyCarOverlaps = newEnemyCar.sprite.boundingRectangle.overlaps(enemyCar.sprite.boundingRectangle)
                }
                if (!newEnemyCarOverlaps) {
                    for (roadSideItem in roadSideItems) {
                        if (roadSideItem.type == RoadSideItemType.CHAYOPALO_CAIDO)
                            newEnemyCarOverlaps = newEnemyCar.sprite.boundingRectangle.overlaps(roadSideItem.sprite.boundingRectangle)
                    }
                }

                if (newEnemyCarOverlaps) {
                    newEnemyCar.active = false
                } else {
                    newEnemyCar.init(assets)
                    enemyCars.add(newEnemyCar)
                }
                enemyCarTime = 0f
            }
        }
    }

    // Get a new ambulance
    private fun spawnAmbulance(delta: Float) {
        if (playerCar.active) {
            ambulanceTime += delta
            if (ambulanceTime >= AMBULANCE_SPAWN_DELAY) {
                val newAmbulance = ambulancePool.obtain()
                newAmbulance.init(assets)
                ambulances.add(newAmbulance)
                ambulanceTime = 0f
            }
        }
    }

    // Get a new power up every 30 seconds
    private fun spawnPowerUp(delta: Float) {
        if (playerCar.active && ambulances.size == 0) {
            powerUpTime += delta
            if (powerUpTime >= POWER_UP_SPAWN_DELAY) {
                val newPowerUp = powerUpPool.obtain()

                var newPowerUpOverlaps = false
                for (enemyCar in enemyCars) {
                    newPowerUpOverlaps = newPowerUp.sprite.boundingRectangle.overlaps(enemyCar.sprite.boundingRectangle)
                }
                if (!newPowerUpOverlaps) {
                    for (roadSideItem in roadSideItems) {
                        if (roadSideItem.type == RoadSideItemType.CHAYOPALO_CAIDO)
                            newPowerUpOverlaps = newPowerUp.sprite.boundingRectangle.overlaps(roadSideItem.sprite.boundingRectangle)
                    }
                }

                if (newPowerUpOverlaps) {
                    newPowerUp.active = false
                } else {
                    newPowerUp.init(assets)
                    powerUps.add(newPowerUp)
                }
                powerUpTime = 0f
            }
        }
    }

    // Get a new road side item every second
    private fun spawnRoadSideItem(delta: Float) {
        if (playerCar.active) {
            itemTime += delta
            if (itemTime >= ITEM_SPAWN_DELAY) {
                val newItem = itemPool.obtain()

                var newItemOverlaps = false
                for (enemyCar in enemyCars) {
                    if (newItem.type == RoadSideItemType.CHAYOPALO_CAIDO)
                        newItemOverlaps = newItem.sprite.boundingRectangle.overlaps(enemyCar.sprite.boundingRectangle)
                }

                if (newItemOverlaps) {
                    newItem.active = false
                } else {
                    newItem.init(assets)
                    roadSideItems.add(newItem)
                }
                itemTime = 0f
            }
        }
    }

    // Update the road
    private fun updateRoad(delta: Float) {
        road.draw(batch, 1f)
        if (playerCar.active && state == GameState.RUNNING) {
            road.act(delta)
        }
    }

    // Update the player car
    private fun updatePlayerCar(delta: Float) {
        playerCar.draw(batch)
        playerCar.update(delta, batch, camera)

        // Play explosion sound if inactive
        val active = playerCar.active
        if (!active) {
            roadSideItems.clear()
            enemyCarTime = 0f
            itemTime = 0f
            // powerUpTime = 0f
            // ambulanceTime = 0f
        }

        // Fuel checks
        val fuel = playerCar.fuel
        if (fuel <= 0) {
            vibrating = true
            state = GameState.GAMEOVER
            playBtn.isVisible = true
            exitBtn.isVisible = true
        }
    }

    private fun updateEnemyCars(delta: Float) {
        for (car in enemyCars) {
            if (car.active) {
                car.draw(batch)
                car.update(playerCar.active)
                playerCar.checkCrash(delta, car)
            }
        }
    }

    private fun updateAmbulances(delta: Float) {
        for (ambulance in ambulances) {
            if (ambulance.active) {
                ambulance.draw(batch)
                ambulance.update(delta, playerCar.sprite.x)
                playerCar.checkCrash(ambulance)
            }
        }
    }

    private fun updatePowerUps() {
        for (powerUp in powerUps) {
            if (powerUp.active) {
                powerUp.draw(batch)
                powerUp.update(playerCar.active)
                playerCar.checkCollect(powerUp)
            }
        }
    }

    private fun updateRoadSideItems() {
        for (item in roadSideItems.reversed()) {
            if (item.active) {
                item.draw(batch)
                if (playerCar.active) {
                    item.update()
                    if (item.type == RoadSideItemType.CHAYOPALO_CAIDO)
                        playerCar.checkCrash(item)
                }
            }
        }
    }

    private fun clearInactiveEnemyCars() {
        var i = enemyCars.size
        while (--i >= 0) {
            val car = enemyCars[i]
            if (!car.active) {
                enemyCars.removeAt(i)
                enemyPool.free(car)
            }
        }
    }

    private fun clearInactiveAmbulances() {
        var i = ambulances.size
        while (--i >= 0) {
            val ambulance = ambulances[i]
            if (!ambulance.active) {
                ambulances.removeAt(i)
                ambulancePool.free(ambulance)
            }
        }
    }

    private fun clearInactivePowerUps() {
        var i = powerUps.size
        while (--i >= 0) {
            val powerUp = powerUps[i]
            if (!powerUp.active) { // && powerUp.collected) {
                powerUps.removeAt(i)
                powerUpPool.free(powerUp)
            }
        }
    }

    /*
    private fun removeRoadSideItems(type: RoadSideItemType) {
        var i = roadSideItems.size - 1
        while (i >= 0) {
            val item = roadSideItems[i]
            if (!item.active && item.type == type) {
                roadSideItems.removeAt(i)
                itemPool.free(item)
            }
            i--
        }
    }
    */

    private fun clearInactiveRoadSideItems() {
        var i = roadSideItems.size
        while (--i >= 0) {
            val item = roadSideItems[i]
            if (!item.active) {
                roadSideItems.removeAt(i)
                itemPool.free(item)
            }
        }
    }
}