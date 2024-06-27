package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.graphics.Color

const val FRAME_RATE = 30f
const val DEFAULT_MUSIC_VOLUME = 20f
const val DEFAULT_SOUND_VOLUME = 20f
const val VOLUME_DIVIDER = 100f
const val VIBRATE_TIME = 10
const val PIXELEMULATOR_FONT_NAME = "PixelEmulator"

// window stuff
const val WINDOW_WIDTH = 480f
const val WINDOW_HEIGHT = 900f
const val WINDOW_WIDTH_HALF = WINDOW_WIDTH / 2
const val WINDOW_HEIGHT_HALF = WINDOW_HEIGHT / 2
val BACK_COLOR : Color = Color(0x1f1f1fff) // Color.valueOf("003300")

// main menu screen stuff
const val BUTTON_WIDTH = 220f
const val BUTTON_HEIGHT = 70f
//const val LOGO_X = 140f
const val LOGO_Y = 130f
const val MAIN_MENU_BUTTON_X = 235f
const val EXIT_BUTTON_Y = 30f

// new game screen stuff
//const val LABEL_WIDTH = 450f
//const val LABEL_HEIGHT = 40f
const val LOGO_SCALE = 0.5f

// options screen stuff
const val TEXTFIELD_WIDTH = 300f
const val TEXTFIELD_HEIGHT = 50f
const val TEXTFIELD_PADDING = 10f
const val CURSOR_WIDTH = 2f
const val CHECKBOX_SIZE = 48f
const val SMALL_BUTTON_WIDTH = 48f
const val SMALL_BUTTON_HEIGHT = 48f

// countdown
const val COUNTDOWN_DELAY = 1.25f
const val COUNTDOWN_END_STR = "¡YA!"

// pause dialog stuff
const val PAUSE_BTN_WIDTH = 72f
const val PAUSE_BTN_HEIGHT = 72f
const val PAUSE_BTN_X = 380f
const val PAUSE_BTN_Y = WINDOW_HEIGHT - 90f
const val PAUSE_DIALOG_BTN_X = WINDOW_WIDTH - (BUTTON_WIDTH / 2.75f)

// game over screen stuff
const val GAME_OVER_MESSAGE = "¡SE ACABÓ!"
const val KMS_RECORD_MESSAGE = "¡NUEVA DISTANCIA MÁXIMA!"
const val GAME_SCORE_MESSAGE = "DISTANCIA: "
const val GAME_OVER_MESSAGE_Y = WINDOW_HEIGHT_HALF + 120f
const val KMS_RECORD_MESSAGE_Y = WINDOW_HEIGHT_HALF + 80f
const val KMS_MESSAGE_Y = WINDOW_HEIGHT_HALF + 40f
const val PLAY_BTN_Y = 350f
const val EXIT_BTN_Y = 250f

// player car stuff
const val CAR_SPRITE_SIZE = 64f
const val CAR_TILE_SIZE = 16
const val CAR_INITIAL_X = WINDOW_WIDTH_HALF - (CAR_SPRITE_SIZE / 2)
const val CAR_INITIAL_Y = 50f
const val CAR_MOVING_SPEED = 400f
const val CAR_CRASH_DURATION = 0.3f
const val CAR_CRASH_MOVEMENT = 300f
const val CAR_CRASH_ROTATION = 1200f
const val CAR_CRASH_FUEL_LOSS = -10
const val KMS_DELAY = 1f
const val FUEL_DELAY = 1f
const val EXPLOSION_DELAY = 1.5f

// enemy car stuff
//const val ENEMY_CAR_SPEEDUP_DELAY = 15
const val ENEMY_CAR_SPAWN_DELAY = 1.25f
//const val ENEMY_CAR_SPAWN_DELAY_MENU = 4f
//const val ENEMY_CAR_SPAWN_DELAY_SUM = 0.125f
//const val ENEMY_CAR_SPEED = 2f

// ambulance stuff
//const val AMBULANCE_SPEEDUP_DELAY = 15
const val AMBULANCE_SPAWN_DELAY = 10f
const val AMBULANCE_WARNING_BLINK_DELAY = 0.4f
//const val AMBULANCE_SPAWN_DELAY_MENU = 4f
//const val AMBULANCE_SPAWN_DELAY_SUM = 0.125f
//const val AMBULANCE_SPEED = 2f

// power up stuff
const val POWER_UP_SPAWN_DELAY = 20f
const val POWER_UP_FUEL = 30

// road side item stuff
//const val ITEM_SPEEDUP_DELAY = 15
const val ITEM_SPAWN_DELAY = 0.35f
//const val ITEM_SPAWN_DELAY_MENU = 3.5
//const val ITEM_SPAWN_DELAY_SUM = 0.125
//const val ITEM_FALLING_SPEED = 2
//const val ITEM_MAX_FALLING_SPEED = 12
//const val ITEM_FALLING_SPEED_SUM = 1
//const val ITEM_FALLING_SPEED_MENU = 3

// road stuff
const val ROAD_SPEED = 1.4f
const val ROAD_WIDTH = 300
const val ROAD_WIDTH_HALF = ROAD_WIDTH / 2
const val ROAD_X = 90

// message display stuff
const val NAME_LOCATION_X = 25f
const val NAME_LOCATION_Y = WINDOW_HEIGHT - 30f
const val KMS_LOCATION_X = 175f
const val KMS_LOCATION_Y = WINDOW_HEIGHT - 30f
const val FUEL_LOCATION_X = 255f
const val FUEL_LOCATION_Y = WINDOW_HEIGHT - 30f

// assets paths
const val SPRITES_FOLDER = "sprites/"
const val ICONS_FOLDER = "icons/"
const val MUSIC_FOLDER = "music/"
const val SOUNDS_FOLDER = "sounds/"
const val IMAGES_FOLDER = "images/"
//const val _SPRITE_PATH = SPRITES_FOLDER + ""
//const val _ICON_PATH = ICONS_FOLDER + ""

// sprites paths
const val LOGO_PATH = SPRITES_FOLDER + "nica_road_rage.png"
const val PLAYER_CAR_SPRITE_PATH = SPRITES_FOLDER + "player_car.png"
const val POWERUP_SPRITE_PATH = SPRITES_FOLDER + "powerup.png"
const val EXPLOSION_SPRITE_PATH = SPRITES_FOLDER + "explosion.png"

const val AMBULANCE_SPRITE_PATH = SPRITES_FOLDER + "ambulance.png"
const val RED_CROSS_DIALOG_SPRITE_PATH = SPRITES_FOLDER + "red_cross_dialog.png"
const val BUS_SPRITE_PATH = SPRITES_FOLDER + "bus.png"
const val TAXI1_SPRITE_PATH = SPRITES_FOLDER + "taxi1.png"
const val TAXI2_SPRITE_PATH = SPRITES_FOLDER + "taxi2.png"
const val TAXI3_SPRITE_PATH = SPRITES_FOLDER + "taxi3.png"
const val TAXI4_SPRITE_PATH = SPRITES_FOLDER + "taxi4.png"
const val TRUCK_SPRITE_PATH = SPRITES_FOLDER + "truck.png"

const val ROAD_SPRITE_PATH = SPRITES_FOLDER + "road.png"
const val CHAYOPALO_SPRITE_PATH = SPRITES_FOLDER + "chayopalo.png"
const val CHAYOPALO_CAIDO_SPRITE_PATH = SPRITES_FOLDER + "chayopalo_caido.png"
const val HOUSE_SPRITE_PATH = SPRITES_FOLDER + "house.png"
const val TREE_SPRITE_PATH = SPRITES_FOLDER + "tree.png"

const val BUTTON_SPRITE_PATH = SPRITES_FOLDER + "button.png"
const val BUTTON_SMALL_SPRITE_PATH = SPRITES_FOLDER + "button_small.png"

// icons paths
const val PAUSE_BTN_ICON_PATH = ICONS_FOLDER + "pause_button.png"
const val ARROW_RIGHT_ICON_PATH = ICONS_FOLDER + "arrow_right.png"
const val PLAY_ICON_PATH = ICONS_FOLDER + "play.png"
const val COG_ICON_PATH = ICONS_FOLDER + "cog.png"
const val CHECK_ICON_PATH = ICONS_FOLDER + "check.png"
const val SAVE_ICON_PATH = ICONS_FOLDER + "save.png"
const val TRASH_ICON_PATH = ICONS_FOLDER + "trash.png"
const val VOLUME_DOWN_ICON_PATH = ICONS_FOLDER + "volume_down.png"
const val VOLUME_UP_ICON_PATH = ICONS_FOLDER + "volume_up.png"
const val HELP_ICON_PATH = ICONS_FOLDER + "help.png"
const val STAR_ICON_PATH = ICONS_FOLDER + "star_black.png"
const val PLUS_ICON_PATH = ICONS_FOLDER + "plus.png"
const val CIRCLE_PLUS_ICON_PATH = ICONS_FOLDER + "circle_plus.png"
const val EXIT_ICON_PATH = ICONS_FOLDER + "exit.png"
// const val ARROW_RIGHT_ICON_PATH = ICONS_FOLDER + "arrow_right.png"

// music paths
const val GAME_MUSIC_PATH = MUSIC_FOLDER + "race_against_sunset.ogg"
const val MENU_MUSIC_PATH = MUSIC_FOLDER + "offline.ogg"

// sounds paths
const val COUNTDOWN_SOUND_PATH = SOUNDS_FOLDER + "countdown.wav"
const val CRASHING_SOUND_PATH = SOUNDS_FOLDER + "crashing.wav"
const val EXPLOSION_SOUND_PATH = SOUNDS_FOLDER + "explosion.wav"
const val LOW_FUEL_SOUND_PATH = SOUNDS_FOLDER + "lowfuel.wav"
const val PAUSE_SOUND_PATH = SOUNDS_FOLDER + "pause.wav"
const val RESTORE_FUEL_SOUND_PATH = SOUNDS_FOLDER + "restorefuel.wav"
const val START_SOUND_PATH = SOUNDS_FOLDER + "start.wav"