package com.marcosmiranda.nicaroadrage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import java.util.Random

val rand = Random()

// clear the screen
fun clear() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
}

// set background color
fun setBackColor(color: Color) {
    Gdx.gl.glClearColor(color.r, color.g, color.b, color.a)
    clear()
}

fun getRandLocation(min: Int, range: Int) : Float {
    return min + rand.nextInt(range).toFloat()
}

fun getRandColor() : Color {
    return when (rand.nextInt(29)) {
        1 -> Color.BLUE
        2 -> Color.NAVY
        3 -> Color.ROYAL
        4 -> Color.SLATE
        5 -> Color.SKY
        6 -> Color.CYAN
        7 -> Color.TEAL

        8 -> Color.GREEN
        9 -> Color.CHARTREUSE
        10 -> Color.LIME
        11 -> Color.FOREST
        12 -> Color.OLIVE

        13 -> Color.YELLOW
        14 -> Color.GOLD
        15 -> Color.GOLDENROD
        16 -> Color.ORANGE

        17 -> Color.BROWN
        18 -> Color.TAN
        19 -> Color.FIREBRICK

        20 -> Color.RED
        21 -> Color.SCARLET
        22 -> Color.CORAL
        23 -> Color.SALMON
        24 -> Color.PINK
        25 -> Color.MAGENTA

        26 -> Color.PURPLE
        27 -> Color.VIOLET
        28 -> Color.MAROON

        else -> Color.WHITE
    }
}

fun getColorFromStr(color: String) : Color {
    return when (color) {
        "blue" -> Color.BLUE
        // 2 -> Color.NAVY
        // 3 -> Color.ROYAL
        // 4 -> Color.SLATE
        // 5 -> Color.SKY
        // 6 -> Color.CYAN
        // 7 -> Color.TEAL

        "green" -> Color.GREEN
        // 9 -> Color.CHARTREUSE
        // 10 -> Color.LIME
        // 11 -> Color.FOREST
        // 12 -> Color.OLIVE

        "yellow" -> Color.YELLOW
        "gold" -> Color.GOLD
        // 15 -> Color.GOLDENROD
        "orange" -> Color.ORANGE

        "brown" -> Color.BROWN
        // 18 -> Color.TAN
        // 19 -> Color.FIREBRICK

        "red" -> Color.RED
        // 21 -> Color.SCARLET
        // 22 -> Color.CORAL
        // 23 -> Color.SALMON
        "pink" -> Color.PINK
        "magenta" -> Color.MAGENTA

        "purple" -> Color.PURPLE
        // 27 -> Color.VIOLET
        // 28 -> Color.MAROON

        "gray" -> Color.GRAY
        "white" -> Color.WHITE
        else -> Color.WHITE
    }
}

fun getColorStr(color: Color) : String {
    return when (color) {
        Color.BLUE -> "blue"
        // 2 -> Color.NAVY
        // 3 -> Color.ROYAL
        // 4 -> Color.SLATE
        // 5 -> Color.SKY
        // 6 -> Color.CYAN
        // 7 -> Color.TEAL

        Color.GREEN -> "green"
        // 9 -> Color.CHARTREUSE
        // 10 -> Color.LIME
        // 11 -> Color.FOREST
        // 12 -> Color.OLIVE

        Color.YELLOW -> "yellow"
        Color.GOLD -> "gold"
        // 15 -> Color.GOLDENROD
        Color.ORANGE -> "orange"

        Color.BROWN -> "brown"
        // 18 -> Color.TAN
        // 19 -> Color.FIREBRICK

        Color.RED -> "red"
        // 21 -> Color.SCARLET
        // 22 -> Color.CORAL
        // 23 -> Color.SALMON
        Color.PINK -> "pink"
        Color.MAGENTA -> "magenta"

        Color.PURPLE -> "purple"
        // 27 -> Color.VIOLET
        // 28 -> Color.MAROON

        Color.GRAY -> "gray"
        Color.WHITE -> "white"
        else -> "white"
    }
}

