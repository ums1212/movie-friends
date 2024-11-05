package org.comon.moviefriends.common

enum class NAV_ROUTE(val route: String, val description: String){
    SCAFFOLD("scaffold", "스캐폴드 화면"),
    HOME("home", "홈 화면"),
    LOGIN("login", "로그인 화면"),
    SUBMIT_NICKNAME("submit_nickname", "닉네임 입력 화면"),
    MOVIE_DETAIL("movie_detail", "영화 상세 정보 화면"),
    COMMUNITY("community", "커뮤니티 화면"),
    COMMUNITY_DETAIL("community_detail", "커뮤니티 상세 글 화면"),
    WRITE_POST("write_post", "커뮤니티 글 작성 화면"),
    PROFILE("profile", "내 정보 화면"),
    PROFILE_SETTING("profile_setting", "내 정보 수정"),
    SEARCH("search", "검색 화면"),
}

enum class NAV_MENU(val route: String, val description: String){
    HOME("home", "홈"),
    COMMUNITY("community", "커뮤니티"),
    PROFILE("profile", "내 정보"),
}

enum class COMMUNITY_MENU(val route: String, val description: String){
    COMMUNITY("community", "커뮤니티"),
    WATCH_TOGETHER("watch_together", "같이 보기"),
    RECOMMEND("recommend", "영화 추천"),
    WORLD_CUP("world_cup", "월드컵"),
}

enum class WATCH_TOGETHER_MENU(val route: String, val description: String){
    REQUEST_LIST("request_list", "요청 내역"),
    RECEIVE_LIST("receive_list", "받은 내역"),
    CHAT_ROOM_LIST("chat_room_list", "채팅방"),
}

enum class PROFILE_MENU(val route: String, val description: String){
    PROFILE_WANT_MOVIE("profile_want_movie", "보고 싶은 영화"),
    PROFILE_RATE("profile_rate", "남긴 평점"),
    PROFILE_REVIEW("profile_review", "남긴 리뷰"),
    PROFILE_COMMUNITY_POST("profile_community_post", "남긴 커뮤니티 글"),
    PROFILE_COMMUNITY_REPLY("profile_community_reply", "남긴 커뮤니티 댓글")
}