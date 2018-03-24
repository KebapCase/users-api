package com.kebab.user.service

import com.kebab.core.exception.EntityNotFoundException
import com.kebab.core.jms.JmsMessageSender
import com.kebab.core.util.mergeWith
import com.kebab.core.util.validate
import com.kebab.user.model.User
import com.kebab.user.repository.UserRepository
import com.kebab.user.security.util.toToken
import com.kebab.user.service.exception.IncorrectPasswordException
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository,
                  private val passwordEncoder: PasswordEncoder,
                  private val sender: JmsMessageSender) {

    fun getValidUserIfExists(user: User) = user.apply {
        if (!passwordEncoder.matches(password!!, findUserByUsername(username!!).password))
            throw IncorrectPasswordException()
    }

    fun createUser(user: User) = userRepository.save(user.apply {
        password = passwordEncoder.encode(password)
    }.validate())!!.toToken()

    fun updateUserById(id: Long, model: User) =
            userRepository.save(model.mergeWith(userRepository.findOne(id)!!).validate())!!

    fun findUserById(id: Long) = userRepository.findOne(id) ?: throw EntityNotFoundException()

    fun findUserByUsername(username: String) = userRepository.findByUsername(username)
            ?: throw EntityNotFoundException()

    fun deleteUserById(id: Long) = userRepository.deleteById(id)
            .also { sender.sendMessage("deleteUser", id) }

    fun findAllUsers(page: Int,
                     limit: Int,
                     orderOptions: String,
                     queryParameters: Map<String, String>) =
            userRepository.findAll(PageRequest(page, limit)) ?: throw EntityNotFoundException()

    fun socialLogin(user: User) = userRepository.findByUsername(user.username!!) ?: createUser(user)
}
