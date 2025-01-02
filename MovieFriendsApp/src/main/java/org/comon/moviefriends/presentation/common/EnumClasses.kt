package org.comon.moviefriends.presentation.common

enum class UserGender(val str: String, val kor: String) {
    NONE("", "정보 없음"),
    MALE("M", "남성"),
    FEMALE("F", "여성"),
}

enum class UserAgeRange(val num: Int, val kor: String) {
    NONE(-1, "정보 없음"),
    Age10(1, "10대"),
    Age20(2, "20대"),
    Age30(3, "30대"),
    Age40(4, "40대"),
    Age50(5, "50대"),
    AgeUpTo60(6, "60대 이상"),
}