package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Pool

class PowerUp : Pool.Poolable {

    var active = true
    var sprite = Sprite()
    private var fallingSpeed = 10f

    fun init(assets: AssetManager) {
        sprite = Sprite(assets.get(POWERUP_SPRITE_PATH, Texture::class.java))
        sprite.x = getRandLocation(ROAD_X, ROAD_WIDTH - ROAD_X)
        sprite.y = WINDOW_HEIGHT
        active = true
        fallingSpeed = 10f
    }

    fun update(playerCarActive: Boolean) {
        if (sprite.y <= -sprite.height) active = false
        else
            if (active) {
                if (playerCarActive) sprite.y -= fallingSpeed
                else sprite.y += fallingSpeed
            }
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(sprite, sprite.x, sprite.y)
    }

    override fun reset() {
        fallingSpeed = 0f
        active = false
        sprite = Sprite()
    }
}