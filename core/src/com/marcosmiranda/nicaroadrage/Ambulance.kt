package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Pool

class Ambulance : Pool.Poolable {

    var active = true
    var warning = true
    private var warningVisible = true
    var warningSprite = Sprite()
    private var ambulanceSprite = Sprite()
    var sprite = Sprite()

    private var fallingSpeed = 20f
    private var warningTime = 0f
    private var blinkTimes = 0

    fun init(assets: AssetManager) {
        warningSprite = Sprite(assets.get(RED_CROSS_DIALOG_SPRITE_PATH, Texture::class.java))
        ambulanceSprite = Sprite(assets.get(AMBULANCE_SPRITE_PATH, Texture::class.java))
        sprite = warningSprite
        sprite.x = ROAD_WIDTH_HALF.toFloat() - sprite.width / 2
        sprite.y = 0f
        active = true
        warning = true
        warningVisible = true
        fallingSpeed = 20f
        warningTime = 0f
        blinkTimes = 0
        // ambulanceSprite.x = getRandLocation(ROAD_X, ROAD_WIDTH - ROAD_X)
        // ambulanceSprite.y = 10f + WINDOW_HEIGHT
    }

    fun update(delta: Float, playerCarX: Float) {
        if (blinkTimes == 3) warning = false

        if (warning) {
            sprite = warningSprite
            if (blinkTimes < 2) {
                sprite.x = playerCarX - 4f
            }

            warningTime += delta
            if (warningTime >= AMBULANCE_WARNING_BLINK_DELAY) {
                warningVisible = if (warningVisible) {
                    blinkTimes++
                    false
                } else {
                    true
                }
                warningTime = 0f
            }
        } else {
            val currentX = sprite.x
            sprite = ambulanceSprite
            sprite.x = currentX
            // sprite.y = 10f
            // sprite.setScale(1f)
            if (sprite.y > WINDOW_HEIGHT) active = false
            else
                if (active) sprite.y += fallingSpeed

        }
    }

    fun draw(batch: SpriteBatch) {
        batch.color = Color.WHITE
        if ((warning && warningVisible) || !warning)
            batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
    }

    override fun reset() {
        fallingSpeed = 0f
        active = false
        warning = false
        sprite = Sprite()
        warningTime = 0f
        blinkTimes = 0
    }
}