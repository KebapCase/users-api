package com.kebab.user.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS256
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.Date
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object JwtService {
     const val EXPIRATIONTIME: Long = 864000000 // 10 days
     const val SECRET = "ThisIsASecret"
     const val TOKEN_PREFIX = "Bearer"
     const val HEADER_STRING = "Authorization"

    fun addAuthentication(response: HttpServletResponse, username: String) {
        val jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(HS256, SECRET)
                .compact()
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt)
    }

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            // parse the token.
            val user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .subject

            return if (user != null)
                UsernamePasswordAuthenticationToken(user, null, emptyList<GrantedAuthority>())
            else
                null
        }
        return null
    }
}