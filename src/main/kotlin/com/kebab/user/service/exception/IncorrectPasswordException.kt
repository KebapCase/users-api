package com.kebab.user.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN,
        reason = "Incorrect password")
class IncorrectPasswordException : RuntimeException() {
    companion object {
        const val REASON = "Incorrect password"
    }
}