package com.kebab.user.controller

import com.kebab.core.exception.EntityNotFoundException
import com.kebab.core.exception.MalformedRequestDataException
import com.kebab.core.exception.ModelValidationException
import com.kebab.core.util.deleteRecords
import com.kebab.user.model.User
import com.kebab.user.service.UserService
import com.kebab.user.utils.toToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.apache.commons.lang3.math.NumberUtils
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Class contains all necessary methods to work and modify [User] records.
 *
 * @author Valentin Trusevich
 * @since 1.0.0
 */
@RestController
@RequestMapping("/users")
@Api(value = "User Management", description = "Endpoints for managing Users entities")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"], methods = [GET, POST, PUT, DELETE])
class UserController(private val userService: UserService) {

    @ApiOperation(value = "Retrieves a list of User records (supports pagination and ordering)",
            response = User::class, responseContainer = "List")
    @ApiResponses(ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON))
    @GetMapping(produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
   // @ApiImplicitParams(ApiImplicitParam("token", paramType = "header", required = true))
    fun findAllUsers(@ApiParam("0", allowEmptyValue = true) @RequestParam("page", required = false, defaultValue = "0") page: Int,
                     @ApiParam("0", allowEmptyValue = true) @RequestParam("limit", required = false, defaultValue = "0") limit: Int,
                     @ApiParam("{\"property\":\"name\",\"direction\":\"desc\"}", allowEmptyValue = true) @RequestParam("order", required = false, defaultValue = "") order: String,
                     @ApiParam(hidden = true) @RequestParam queryParameters: Map<String, String>) =
            userService.findAllUsers(page, limit, order, queryParameters)

    @ApiOperation(value = "Gets User record for a given guid", response = User::class)
    @ApiResponses(ApiResponse(code = SC_NOT_FOUND, message = EntityNotFoundException.REASON))
    @GetMapping("/{guid}", produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
    //@ApiImplicitParams(ApiImplicitParam("token", paramType = "header", required = true))
    fun findUserByGuid(@ApiParam("f647e1fa-ad24-4f7e-a18e-4dd197feb66b") @PathVariable("guid") guid: UUID) =
            userService.findUserByGuid(guid)

    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates a new User", response = User::class)
    @ApiResponses(
            ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON),
            ApiResponse(code = SC_UNPROCESSABLE_ENTITY, message = ModelValidationException.REASON)
    )
    @PostMapping(consumes = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE],
            produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
    fun createUser(@RequestBody model: User) =
            userService.createUser(model)

    //TODO: get token change it o normal login
    @ResponseStatus(OK)
    @ApiOperation("", response = String::class)
    @PostMapping("/token/{guid}", produces = [TEXT_PLAIN_VALUE])
    fun getToken(@ApiParam("f647e1fa-ad24-4f7e-a18e-4dd197feb66b") @PathVariable("guid") guid: UUID) =
            findUserByGuid(guid)!!.toToken()

    @ApiOperation(value = "Updates an existing User by GUID", response = User::class)
    @ApiResponses(
            ApiResponse(code = SC_BAD_REQUEST, message = MalformedRequestDataException.REASON),
            ApiResponse(code = SC_NOT_FOUND, message = EntityNotFoundException.REASON),
            ApiResponse(code = SC_UNPROCESSABLE_ENTITY, message = ModelValidationException.REASON)
    )
    @PutMapping("/{guid}", consumes = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE],
            produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
    //@ApiImplicitParams(ApiImplicitParam("token", paramType = "header", required = true))
    fun updateUserByGuid(@ApiParam("f647e1fa-ad24-4f7e-a18e-4dd197feb66b") @PathVariable("guid") guid: UUID,
                         @RequestBody user: User) =
            userService.updateUserByGuid(guid, user)

    @ApiOperation(value = "Deletes a User by GUID", response = String::class)
    @ApiResponses(ApiResponse(code = SC_NOT_FOUND, message = EntityNotFoundException.REASON))
    @DeleteMapping("/{guid}", produces = [APPLICATION_JSON_VALUE, APPLICATION_JSON_UTF8_VALUE])
    //@ApiImplicitParams(ApiImplicitParam("token", paramType = "header", required = true))
    fun deleteUserByGuid(@ApiParam("f647e1fa-ad24-4f7e-a18e-4dd197feb66b") @PathVariable("guid") guid: UUID) =
            userService.deleteUserByGuid(guid).run { deleteRecords(NumberUtils.LONG_ONE, User::class.java) }
}
