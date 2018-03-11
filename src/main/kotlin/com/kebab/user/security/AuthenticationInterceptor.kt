package com.kebab.user.security

import com.kebab.user.security.util.HEADER_STRING
import com.kebab.user.security.util.fromToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_FORBIDDEN

@Component
class AuthenticationInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?) =
            getAuthentication(request).run {
                this?.let {
                    SecurityContextHolder.getContext().authentication = this; true
                }
            } ?: let { response!!.status = SC_FORBIDDEN; false }

    private fun getAuthentication(request: HttpServletRequest): Authentication? =
            request.getHeader(HEADER_STRING)?.run {
                fromToken().run {
                    UsernamePasswordAuthenticationToken(username, null, emptyList<GrantedAuthority>())
                }
            }
}