package com.kebab.user.security

import com.kebab.user.security.util.JwtUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?): Boolean {

        val authentication = JwtUtils.getAuthentication(request)
        if(authentication == null) {
            response!!.status = HttpServletResponse.SC_FORBIDDEN
            return false
        }
        SecurityContextHolder.getContext().authentication = authentication
        return true
    }
}