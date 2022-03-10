package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class PlayerCar(assets: AssetManager) {

    private val clickArea = Rectangle(0f, 0f, WINDOW_WIDTH, WINDOW_HEIGHT_HALF)
    private val touchPos = Vector3()
    private val leftBound = 90f
    private val carSpriteShadow = 32f
    private val rightBound = 300f + leftBound - CAR_SPRITE_SIZE + carSpriteShadow

    var active = true

    var crashing = false
    private var crashTime = 0f

    var kms = 0
    private var kmsTime = 0f

    var fuel = 100
    private var fuelTime = 0f

    private val prefs: Preferences = Gdx.app.getPreferences("NicaRoadRage")
    private var carColor = getColorFromStr(prefs.getString("carColor", "white"))
    private val carSprite = Sprite(assets.get(PLAYER_CAR_SPRITE_PATH, Texture::class.java))
    val sprite = carSprite

    private var explosionAnimation: Animation<TextureRegion>
    private var explosionTime = 0f
    private val vibrator = Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)

    private val playSound = prefs.getBoolean("sound", true)
    private val soundVolume = prefs.getFloat("soundVolume", DEFAULT_SOUND_VOLUME) / VOLUME_DIVIDER
    private val crashingSound = Gdx.audio.newSound(Gdx.files.internal(CRASHING_SOUND_PATH))
    private val explosionSound = Gdx.audio.newSound(Gdx.files.internal(EXPLOSION_SOUND_PATH))
    private val lowFuelSound = Gdx.audio.newSound(Gdx.files.internal(LOW_FUEL_SOUND_PATH))
    private val restoreFuelSound = Gdx.audio.newSound(Gdx.files.internal(RESTORE_FUEL_SOUND_PATH))
    private var crashingSoundId = 0L
    private var explosionSoundId = 0L
    private var lowFuelSoundId = 0L
    private var restoreFuelSoundId = 0L

    init {
        reset()

        val explosionSheet = assets.get(EXPLOSION_SPRITE_PATH, Texture::class.java)
        val splitExplosionSheet = TextureRegion.split(explosionSheet, CAR_TILE_SIZE, CAR_TILE_SIZE)
        val explosionFrames = arrayOf<TextureRegion>(
                splitExplosionSheet[0][0],
                splitExplosionSheet[0][1],
                splitExplosionSheet[0][2]
        )
        explosionAnimation = Animation(EXPLOSION_DELAY, *explosionFrames)
        explosionAnimation.playMode = Animation.PlayMode.NORMAL
    }

    fun update(delta: Float, batch: SpriteBatch, camera: OrthographicCamera) {
        if (active) {

            kmsTime += delta
            if (kmsTime >= KMS_DELAY) {
                kmsTime = 0f
                kms++
            }

            fuelTime += delta
            if (fuelTime >= FUEL_DELAY) {
                fuelTime = 0f
                updateFuel(-1)
                if (fuel <= 10) {
                    if (playSound) {
                        lowFuelSoundId = lowFuelSound.play(soundVolume)
                        lowFuelSound.setLooping(lowFuelSoundId, false)
                    }
                } else if (fuel > 10) {
                    if (playSound) lowFuelSound.stop(lowFuelSoundId)
                }
            }

            // Move by touching the screen, unless you're crashing
            if (!crashing) {
                for (i in 0..2) {
                    if (Gdx.input.isTouched(i)) {
                        touchPos.set(Gdx.input.getX(i).toFloat(), Gdx.input.getY(i).toFloat(), 0f)
                        camera.unproject(touchPos)

                        if (clickArea.contains(touchPos.x, touchPos.y)) {
                            if (sprite.x != touchPos.x - CAR_SPRITE_SIZE) {
                                val movement = CAR_MOVING_SPEED * delta
                                if (sprite.x > touchPos.x) { // If you touch left, you move to the left
                                    sprite.x -= movement
                                } else if (sprite.x < touchPos.x - CAR_SPRITE_SIZE) { // If you touch right, you move to the right
                                    sprite.x += movement
                                }
                            }
                        }
                    }
                }
            }
            checkBounds()
        } else {
            // Play explosion animation
            explosionTime += delta
            val currentFrame = explosionAnimation.getKeyFrame(explosionTime, false)
            if (!active && explosionAnimation.isAnimationFinished(explosionTime)) {
                updateFuel(CAR_CRASH_FUEL_LOSS)
                reset()
            } else {
                batch.draw(currentFrame, sprite.x + (sprite.width / 2), sprite.y + (sprite.height / 2))
            }
        }
    }

    fun draw(batch: Batch) {
        if (active) {
            batch.color = carColor
            batch.draw(sprite, sprite.x, sprite.y, sprite.originX, sprite.originY, sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)
            batch.color = Color.WHITE
        }
    }

    fun checkCrash(delta: Float, car: EnemyCar) {
        if (active) {
            val playerCarRectangle = sprite.boundingRectangle
            val enemyCarRectangle = car.sprite.boundingRectangle
            val playerCarCenterX = sprite.x + (sprite.width / 2)
            // val playerCarCenterY = sprite.y + (sprite.height / 2)
            val enemyCarCenterX = car.sprite.x + (car.sprite.width / 2)
            // val enemyCarCenterY = car.sprite.y + (car.sprite.height / 2)
            val overlap = playerCarRectangle.overlaps(enemyCarRectangle)

            if (overlap && !crashing) {
                car.crashed = true
                if (car.deadly) {
                    active = false
                    if (playSound) {
                        explosionSoundId = explosionSound.play(soundVolume)
                        explosionSound.setLooping(explosionSoundId, false)
                    }
                    if (vibrator) Gdx.input.vibrate(VIBRATE_TIME)
                } else {
                    crashing = true
                    if (playSound) {
                        crashingSoundId = crashingSound.play(soundVolume)
                        crashingSound.setLooping(crashingSoundId, false)
                    }
                }
            }

            if (crashing) {
                crashTime += delta
                if (crashTime >= CAR_CRASH_DURATION) {
                    crashing = false
                    crashTime = 0f
                    if (playSound) crashingSound.stop(crashingSoundId)
                }

                // If you come from the left, you rotate clockwise
                if (playerCarCenterX < enemyCarCenterX) {
                    sprite.rotate(-CAR_CRASH_ROTATION * delta)
                    sprite.x -= CAR_CRASH_MOVEMENT * delta
                }
                // If you come from the right, you rotate counter-clockwise
                else if (playerCarCenterX >= enemyCarCenterX) {
                    sprite.rotate(CAR_CRASH_ROTATION * delta)
                    sprite.x += CAR_CRASH_MOVEMENT * delta
                }
                checkBounds()
            } else {
                sprite.rotation = 0f
                // if (!car.active && !car.crashed) score += car.points
            }
        }
    }

    fun checkCrash(ambulance: Ambulance) {
        if (active && !ambulance.warning && sprite.boundingRectangle.overlaps(ambulance.sprite.boundingRectangle)) {
            active = false
            if (playSound) {
                explosionSoundId = explosionSound.play(soundVolume)
                explosionSound.setLooping(explosionSoundId, false)
            }
        }
    }

    fun checkCrash(item: RoadSideItem) {
        if (active && sprite.boundingRectangle.overlaps(item.sprite.boundingRectangle)) {
            active = false
            item.active = false
            if (playSound) {
                explosionSoundId = explosionSound.play(soundVolume)
                explosionSound.setLooping(explosionSoundId, false)
            }
        }
    }

    fun checkCollect(powerUp: PowerUp) : Boolean {
        var collected = false

        if (active) {
            if (sprite.boundingRectangle.overlaps(powerUp.sprite.boundingRectangle)) {
                updateFuel(POWER_UP_FUEL)
                powerUp.active = false
                fuelTime = 0f
                collected = true
                if (playSound) {
                    restoreFuelSoundId = restoreFuelSound.play(soundVolume)
                    restoreFuelSound.setLooping(restoreFuelSoundId, false)
                }
            }
        }

        return collected
    }

    // Clamp the car to the road
    private fun checkBounds() {
        val isOffLeft = sprite.x <= leftBound
        val isOffRight = sprite.x >= rightBound

        if (isOffLeft || isOffRight) {
            if (isOffLeft)
                sprite.x = leftBound
            else if (isOffRight)
                sprite.x = rightBound

            if (crashing) {
                active = false
                if (playSound) {
                    explosionSoundId = explosionSound.play(soundVolume)
                    explosionSound.setLooping(explosionSoundId, false)
                }
            }
        }
    }

    private fun updateFuel(newFuel: Int) {
        fuel += newFuel
        if (fuel <= 0) {
            active = false
        } else if (fuel > 10) {
            if (fuel > 100) fuel = 100
        }
    }

    private fun reset() {
        sprite.setPosition(CAR_INITIAL_X, CAR_INITIAL_Y)
        sprite.rotation = 0f
        explosionTime = 0f
        crashTime = 0f
        fuelTime = 0f
        active = true
        crashing = false
    }
}