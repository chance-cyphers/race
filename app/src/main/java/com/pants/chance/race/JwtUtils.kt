package com.pants.chance.race

import com.auth0.android.jwt.JWT

fun getName(token: String): String? {
    val jwt = JWT(token)
    return jwt.getClaim("name").asString()
}