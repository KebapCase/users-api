package com.kebab.user.security.util

import com.kebab.core.exception.UnauthorizedException
import com.kebab.user.model.User
import com.kebab.user.model.UserRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS256
import org.apache.commons.lang3.time.DateUtils
import java.util.Date

const val HEADER_STRING = "Authorization"
private const val EXPIRATION_TIME: Int = 1 // 1 day
private const val SECRET = "smoke-a-nigga"
private const val TOKEN_PREFIX = "Bearer "

private const val USERNAME = "username"
private const val ROLE = "role"

fun User.toToken(): String {
    val claims = HashMap<String, Any>()
    claims[USERNAME] = username!!
    claims[ROLE] = role!!
    return claims.toJWT()
}

fun String.fromToken() = with(toClaims().body) {
    if (expiration.before(Date())) {
        throw UnauthorizedException("Token has expired")
    }
    User().apply {
        role = UserRole.valueOf(get(ROLE, String::class.java))
        username = get(USERNAME, String::class.java)
    }
}

fun Map<String, Any>.toJWT() =
        Jwts.builder()
                .setClaims(this)
                .setExpiration(DateUtils.addHours(Date(), EXPIRATION_TIME))
                .signWith(HS256, SECRET)
                .compact()!!.run { TOKEN_PREFIX + this }

fun String.toClaims() =
        Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(this.replace(TOKEN_PREFIX, ""))!!