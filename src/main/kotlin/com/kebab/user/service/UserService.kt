package com.kebab.user.service

import com.kebab.core.exception.EntityNotFoundException
import com.kebab.core.util.findPage
import com.kebab.core.util.mergeWith
import com.kebab.core.util.validate
import com.kebab.core.util.validateOrder
import com.kebab.user.model.User
import com.kebab.user.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: User) =
            userRepository.save(user.validate())!!

    fun updateUserById(id: Long, model: User) =
            userRepository.save(model.mergeWith(userRepository.findOne(id)!!).validate())!!

    fun findUserById(id: Long) = userRepository.findOne(id) ?: throw EntityNotFoundException()

    fun findUserByUsername(username: String) = userRepository.findByUsername(username)

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
