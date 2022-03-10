package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*

class Road(assets: AssetManager): Actor() {

    private var speed = ROAD_SPEED

    private val road = Sprite(assets.get(ROAD_SPRITE_PATH, Texture::class.java))

    init {
        x = 0f
        y = 0f
        width = WINDOW_WIDTH
        height = WINDOW_HEIGHT
        setPosition(0f, height)
        addAction(forever(sequence(moveTo(0f, 0f, speed), moveTo(0f, height))))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(road, x, y - height, width, height * 2)
    }
}