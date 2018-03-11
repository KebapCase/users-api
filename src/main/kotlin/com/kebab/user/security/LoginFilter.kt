package com.kebab.user.security

import com.kebab.core.util.mapper
import com.kebab.user.model.User
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginFilter(authManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(
            req: HttpServletRequest, res: HttpServletResponse) =
            with(mapper().readValue(req.inputStream, User::class.java)) {
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password,
                        emptyList<GrantedAuthority>()))
            }!!
}