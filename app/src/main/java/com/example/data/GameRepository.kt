package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GameState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GameRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("statecraft_game_save", Context.MODE_PRIVATE)

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(GameState::class.java)

    fun hasSavedGame(): Boolean {
        return prefs.contains(KEY_GAME_STATE)
    }

    fun saveGame(state: GameState) {
        try {
            val json = adapter.toJson(state)
            prefs.edit().putString(KEY_GAME_STATE, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadGame(): GameState? {
        val json = prefs.getString(KEY_GAME_STATE, null) ?: return null
        return try {
            adapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearSavedGame() {
        prefs.edit().remove(KEY_GAME_STATE).apply()
    }

    companion object {
        private const val KEY_GAME_STATE = "saved_game_state_json"
    }
}
