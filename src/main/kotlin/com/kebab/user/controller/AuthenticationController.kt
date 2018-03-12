package com.kebab.user.controller

import com.kebab.core.exception.MalformedRequestDataException
import com.kebab.core.util.toJson
import com.kebab.user.security.exception.UserAlreadyExistsException
import com.kebab.user.model.User
import com.kebab.user.service.UserService
import com.kebab.user.security.util.toToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
@RequestMapping
@Api(value = "Authentication Management", description = "Endpoints for managing Authentication logic")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [POST])
class AuthenticationController(private val userService: UserService) {
    @ApiOperation(value = "Login request", response = String::class)
    @ApiResponses(ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON))
    @PostMapping("/login", produces = [APPLICATION_JSON_VALUE])
    fun login(@ApiParam("User", allowEmptyValue = true) @RequestBody user: User) =
            userService.findUserByUsername(user.username!!)!!.toToken()

    @ApiOperation(value = "Sign-up request", response = String::class)
    @ApiResponses(ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON),
            ApiResponse(code = SC_FORBIDDEN, message = UserAlreadyExistsException.REASON))
    @PostMapping("/signUp", produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
    fun signUp(@ApiParam("User") @RequestBody user: User) =
            userService.findUserByUsername(user.username!!)?.let {
                throw UserAlreadyExistsException()
            } ?: userService.createUser(user).toToken()

    @ApiOperation(value = "Social login request", response = String::class)
    @ApiResponses(ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON))
    @PostMapping("/socialLogin", produces = [APPLICATION_JSON_VALUE])
    fun socialLogIn(@ApiParam("User") @RequestBody user: User) =
            (userService.findUserByUsername(user.username!!) ?: userService.createUser(user)).toToken()

}