package org.comon.moviefriends.common

sealed class IntroNavRoute(val route: String){
    data object Login: IntroNavRoute("login")
    data object SubmitNickName: IntroNavRoute("submit_nickname")
}

sealed class FullScreenNavRoute(val route: String){
    data object Search: FullScreenNavRoute("search")
    data object MovieDetail: FullScreenNavRoute("movie_detail")
    data object CommunityDetail: FullScreenNavRoute("community_detail")
    data object WritePost: FullScreenNavRoute("write_post")
    data object ProfileSetting: FullScreenNavRoute("profile_setting")
    data object ProfileWantMovie: FullScreenNavRoute("profile_want_movie")
    data object ProfileRate: FullScreenNavRoute("profile_rate")
    data object ProfileReview: FullScreenNavRoute("profile_review")
    data object ProfileCommunityPost: FullScreenNavRoute("profile_community_post")
    data object ProfileCommunityReply: FullScreenNavRoute("profile_community_reply")
    data object WorldCupGame: FullScreenNavRoute("community_worldcup")
}

sealed class ScaffoldNavRoute(val route: String){
    data object Home: ScaffoldNavRoute("home")
    data object Community: ScaffoldNavRoute("community")
    data object WatchTogether: ScaffoldNavRoute("watch_together")
    data object RequestList: ScaffoldNavRoute("request_list")
    data object ReceiveList: ScaffoldNavRoute("receive_list")
    data object ChatList: ScaffoldNavRoute("chat_list")
    data object MovieRecommend: ScaffoldNavRoute("movie_recommend")
    data object MovieWorldCup: ScaffoldNavRoute("movie_world_cup")
    data object Profile: ScaffoldNavRoute("user_profile")
}

enum class NAV_MENU(val route: String, val description: String){
    HOME("home", "홈"),
    COMMUNITY("community", "커뮤니티"),
    PROFILE("user_profile", "내 정보"),
}

enum class COMMUNITY_MENU(val route: String, val description: String){
    COMMUNITY("community", "커뮤니티"),
    WATCH_TOGETHER("watch_together", "같이 보기"),
    RECOMMEND("movie_recommend", "영화 추천"),
    WORLD_CUP("movie_world_cup", "월드컵"),
}

enum class WATCH_TOGETHER_MENU(val route: String, val description: String){
    REQUEST_LIST("request_list", "요청 내역"),
    RECEIVE_LIST("receive_list", "받은 내역"),
    CHAT_LIST("chat_list", "채팅방"),
}