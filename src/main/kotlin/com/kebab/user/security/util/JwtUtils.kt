package com.kebab.user.security.util

import com.kebab.core.exception.UnauthorizedException
import com.kebab.user.model.User
import com.kebab.user.model.UserRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.lang3.time.DateUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.servlet.http.HttpServletRequest

object JwtUtils {
    private const val EXPIRATION_TIME: Int = 1 // 1 day
    private const val SECRET = "smoke-a-nigga"
    private const val TOKEN_PREFIX = "Bearer"
    private const val HEADER_STRING = "Authorization"

    private const val USERNAME = "username"
    private const val ROLE = "role"

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            val user = token.fromToken()
            UsernamePasswordAuthenticationToken(user, null, emptyList<GrantedAuthority>())
        }
        return null
    }

    fun User.toToken(): String {
        val claims = HashMap<String, Any>()
        claims[USERNAME] = username!!
        claims[ROLE] = role!!
        return claims.toJWT()
    }

    fun String.fromToken() = with(toClaims().body) {
        if (expiration.before(Date()))
            throw UnauthorizedException("Token has expired")

        User().apply {
            role = UserRole.valueOf(get(ROLE, String::class.java))
            username = get(USERNAME, String::class.java)
        }
    }

    private fun Map<String, Any>.toJWT() =
            Jwts.builder()
                    .setExpiration(DateUtils.addHours(Date(), EXPIRATION_TIME))
                    .setClaims(this)
                    .signWith(SignatureAlgorithm.HS256, SECRET).compact()!!

    private fun String.toClaims() =
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(this.replace(TOKEN_PREFIX, ""))!!
}