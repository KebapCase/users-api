package com.kebab.user.service

import com.kebab.core.exception.EntityNotFoundException
import com.kebab.core.util.findPage
import com.kebab.core.util.mergeWith
import com.kebab.core.util.validate
import com.kebab.core.util.validateOrder
import com.kebab.user.model.User
import com.kebab.user.repository.UserRepository
import com.kebab.user.service.exception.IncorrectPasswordException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository,
                  private val passwordEncoder: PasswordEncoder) {

    fun getValidUserIfExists(user: User) =
            user.apply {
                if (!passwordEncoder.matches(password!!, findUserByUsernameIfExists(username!!).password))
                    throw IncorrectPasswordException()
            }

    fun createUser(user: User) =
            userRepository.save(user.apply {
                password = passwordEncoder.encode(password)
            }.validate())!!

    fun updateUserById(id: Long, model: User) =
            userRepository.save(model.mergeWith(userRepository.findOne(id)!!).validate())!!

    fun findUserById(id: Long) = userRepository.findOne(id) ?: throw EntityNotFoundException()

    fun findUserByUsername(username: String) = userRepository.findOne(username)

    fun findUserByUsernameIfExists(username: String) = userRepository.findOne(username)
            ?: throw EntityNotFoundException()

    fun checkIfUserExists(user: User) = userRepository.existsByUsername(user.username!!)

    @Transactional
    fun deleteUserById(id: Long) = userRepository.deleteById(id)

    fun findAllUsers(page: Int,
                     limit: Int,
                     orderOptions: String,
                     queryParameters: Map<String, String>) =
            orderOptions.validateOrder(User::class.java).run {
                userRepository.findPage(page, limit, this, queryParameters)
            }
}
