package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Pool
import java.util.Random

class EnemyCar : Pool.Poolable {

    var active = true
    var crashed = false
    var deadly = false
    var sprite = Sprite()

    private var color = Color.WHITE
    private var fallingSpeed = 10f
    private val rand = Random()

    fun init(assets: AssetManager) {
        when (rand.nextInt(6)) {
            0 -> {
                sprite = Sprite(assets.get(TAXI1_SPRITE_PATH, Texture::class.java))
            }
            1 -> {
                sprite = Sprite(assets.get(TAXI2_SPRITE_PATH, Texture::class.java))
            }
            2 -> {
                sprite = Sprite(assets.get(TAXI3_SPRITE_PATH, Texture::class.java))
            }
            3 -> {
                sprite = Sprite(assets.get(TAXI4_SPRITE_PATH, Texture::class.java))
            }
            4 -> {
                sprite = Sprite(assets.get(BUS_SPRITE_PATH, Texture::class.java))
                deadly = true
            }
            5 -> {
                sprite = Sprite(assets.get(TRUCK_SPRITE_PATH, Texture::class.java))
                color = getRandColor()
                deadly = true
            }
        }

        active = true
        fallingSpeed = 10f
        sprite.x = getRandLocation(ROAD_X, ROAD_WIDTH - ROAD_X)
        sprite.y = WINDOW_HEIGHT
    }

    fun update(playerCarActive: Boolean) {
        if (sprite.y + sprite.height < 0f) active = false
        else {
            if (active) {
                if (playerCarActive) sprite.y -= fallingSpeed
                else sprite.y += fallingSpeed
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        batch.color = color
        batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
        batch.color = Color.WHITE
    }

    override fun reset() {
        sprite = Sprite()
        color = Color.WHITE
        deadly = false
        active = false
        crashed = false
    }
}