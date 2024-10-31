package org.comon.moviefriends.ui.screen

enum class NAV_ROUTE(val route: String, val description: String){
    HOME("home", "홈 화면"),
    LOGIN("login", "로그인 화면"),
    SUBMIT_NICKNAME("submit_nickname", "닉네임 입력 화면"),
    MOVIE_DETAIL("movie_detail", "영화 상세 정보 화면"),
}