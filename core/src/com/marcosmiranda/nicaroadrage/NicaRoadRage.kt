package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class NicaRoadRage() : Game() {

    lateinit var platform: Application.ApplicationType
    lateinit var assets: AssetManager
    lateinit var batch: SpriteBatch
    lateinit var camera: OrthographicCamera
    lateinit var gameMusic: Music
    lateinit var menuMusic: Music
    lateinit var state: GameState
    var android: AndroidController? = null

    override fun create() {
        platform = Gdx.app.type
        assets = AssetManager()
        batch = SpriteBatch()
        camera = OrthographicCamera()
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT)
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal(GAME_MUSIC_PATH))
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal(MENU_MUSIC_PATH))
        state = GameState.RUNNING

        this.setScreen(LoadingScreen(this))
    }

    override fun dispose() {
        assets.dispose()
        batch.dispose()
        gameMusic.dispose()
        menuMusic.dispose()
    }
}