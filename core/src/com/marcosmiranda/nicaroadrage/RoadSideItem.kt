package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Pool
import java.util.Random

class RoadSideItem : Pool.Poolable {

    var active = true
    var sprite = Sprite()
    var type = RoadSideItemType.DEFAULT

    private var itemColor = Color.WHITE
    private val fallingSpeed = 10f
    private val rand = Random()

    fun init(assets: AssetManager) {
        when (rand.nextInt(13)) {
            0, 4, 7, 9, 11 -> {
                sprite = Sprite(assets.get(TREE_SPRITE_PATH, Texture::class.java))
                type = RoadSideItemType.TREE
            }
            1, 5, 8, 10, 12 -> {
                sprite = Sprite(assets.get(HOUSE_SPRITE_PATH, Texture::class.java))
                type = RoadSideItemType.HOUSE
            }
            2, 6 -> {
                sprite = Sprite(assets.get(CHAYOPALO_SPRITE_PATH, Texture::class.java))
                itemColor = getRandColor()
                type = RoadSideItemType.CHAYOPALO
            }
            3 -> {
                sprite = Sprite(assets.get(CHAYOPALO_CAIDO_SPRITE_PATH, Texture::class.java))
                itemColor = getRandColor()
                type = RoadSideItemType.CHAYOPALO_CAIDO
            }
        }

        if (type == RoadSideItemType.CHAYOPALO_CAIDO) {
            sprite.x = 135f
        }
        else {
            sprite.x = when (rand.nextBoolean()) {
                true -> 45f
                false -> 435f
            }
        }

        active = true
        sprite.x -= sprite.width / 2
        sprite.y = 10f + WINDOW_HEIGHT
    }

    fun update() {
        if (sprite.y <= -sprite.height) active = false
        else
            if (active) sprite.y -= fallingSpeed
    }

    fun draw(batch: SpriteBatch) {
        batch.color = itemColor
        batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
        batch.color = Color.WHITE
    }

    override fun reset() {
        active = false
        sprite = Sprite()
        type = RoadSideItemType.DEFAULT
        itemColor = Color.WHITE
    }
}