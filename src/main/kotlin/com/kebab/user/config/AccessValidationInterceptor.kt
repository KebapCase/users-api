package com.kebab.user.config

import com.kebab.user.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Valentin Trusevich
 */
@Component
class AccessValidationInterceptor(

        private var repository: UserRepository

) : HandlerInterceptorAdapter() {

    //TODO: fix it
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

//        val user = request.getHeader("token").run {
//            if (this == null) {
//                if (request.method == "POST") return true
//                else throw UnauthorizedException("Missed auth token")
//            }
//            this
//        }.fromToken()
//
//
//
//        return if (repository.existsByGuid(user.guid!!)) true else throw UnauthorizedException("Such user does not exist")

        return true
    }
}
