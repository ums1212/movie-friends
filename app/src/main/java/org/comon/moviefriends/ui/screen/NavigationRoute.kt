package org.comon.moviefriends.ui.screen

enum class NAV_ROUTE(val route: String, val description: String){
    SCAFFOLD("scaffold", "스캐폴드 화면"),
    HOME("home", "홈 화면"),
    LOGIN("login", "로그인 화면"),
    SUBMIT_NICKNAME("submit_nickname", "닉네임 입력 화면"),
    MOVIE_DETAIL("movie_detail", "영화 상세 정보 화면"),
    COMMUNITY("community", "커뮤니티 화면"),
    COMMUNITY_DETAIL("community_detail", "커뮤니티 상세 글 화면"),
    WRITE_POST("write_post", "커뮤니티 글 작성 화면"),
    MY_INFO("my_info", "내 정보 화면"),
    SEARCH("search", "검색 화면"),
}

enum class NAV_MENU(val route: String, val description: String){
    HOME("home", "홈"),
    COMMUNITY("community", "커뮤니티"),
    MY_INFO("my_info", "내 정보"),
}