package com.kebab.user.utils

import com.kebab.core.exception.UnauthorizedException
import com.kebab.user.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import org.apache.commons.lang3.time.DateUtils.addHours
import org.springframework.beans.factory.annotation.Value
import java.util.Date
import java.util.HashMap
import java.util.UUID

/**
 * @author Valentin Trusevich
 */

private const val USER_GUID = "userGuid"

private const val EMAIL = "email"

private const val ROLE = "role"

private const val USERNAME = "username"

private const val SECRET_KEY = "hJi19anK3a"

@Value("\${jwt.alive.time:1}")
private var tokenAliveInHours: Int = 1

fun User.toToken(): String {
    val claims = HashMap<String, Any>()

    claims[USER_GUID] = guid!!
    claims[EMAIL] = email!!
    claims[ROLE] = role!!
    claims[USERNAME] = username!!
    return claims.toJWT()
}

fun String.fromToken() = with(toClaims().body) {

    if (expiration.before(Date())) throw UnauthorizedException("Token has expired")

    User().apply {
        guid = get(USER_GUID, UUID::class.java)
        email = get(EMAIL, String::class.java)
        role = get(ROLE, String::class.java)
        username = get(USERNAME, String::class.java)
    }
}

private fun Map<String, Any>.toJWT() = Jwts.builder().setExpiration(addHours(Date(), tokenAliveInHours))
        .setClaims(this).signWith(HS512, SECRET_KEY).compact()!!

private fun String.toClaims() = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(this)!!

