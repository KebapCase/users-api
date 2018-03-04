package com.kebab.user.controller

import com.kebab.core.exception.MalformedRequestDataException
import com.kebab.user.security.exception.UserAlreadyExistsException
import com.kebab.user.model.User
import com.kebab.user.service.UserService
import org.apache.http.HttpStatus
import org.springframework.http.MediaType
import com.kebab.user.security.util.JwtUtils.toToken
import com.kebab.user.service.dto.TokenDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Api(value = "Authentication Management", description = "Endpoints for managing Authentication logic")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [(RequestMethod.GET), (RequestMethod.POST)])
class AuthenticationController(private val userService: UserService) {
    @ApiOperation(value = "Login request", response = TokenDto::class)
    @ApiResponses(ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = MalformedRequestDataException.REASON))
    @PostMapping("/login", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@ApiParam("User", allowEmptyValue = true) @RequestBody user: User) =
            TokenDto(userService.findUserByUsername(user.username!!)!!.toToken())

    @ApiOperation(value = "Sign-up request", response = TokenDto::class)
    @ApiResponses(ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = MalformedRequestDataException.REASON),
            ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = UserAlreadyExistsException.REASON))
    @PostMapping("/signUp", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@ApiParam("User") @RequestBody user: User) =
            if (userService.findUserByUsername(user.username!!) != null)
                throw UserAlreadyExistsException()
            else TokenDto(userService.createUser(user).toToken())

    @ApiOperation(value = "Social login request", response = TokenDto::class)
    @ApiResponses(ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = MalformedRequestDataException.REASON))
    @PostMapping("/socialLogin", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun socialLogIn(@ApiParam("User") @RequestBody user: User) =
            TokenDto((userService.findUserByUsername(user.username!!) ?: userService.createUser(user)).toToken())

}