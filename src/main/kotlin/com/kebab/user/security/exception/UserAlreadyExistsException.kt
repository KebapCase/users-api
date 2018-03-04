package com.kebab.user.security.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN,
        reason = "User with such username or password already exists")
class UserAlreadyExistsException : RuntimeException() {
    companion object {
       const val REASON = "User with such username or password already exists"
    }
}