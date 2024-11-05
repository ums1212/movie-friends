package org.comon.moviefriends.common

import android.app.Application

class MovieFriendsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        movieFriendsApplication = this
    }

    companion object {
        private lateinit var movieFriendsApplication: MovieFriendsApplication
        fun getMovieFriendsApplication() = movieFriendsApplication
    }
}