package com.kebab.user.service

import com.kebab.core.util.findPage
import com.kebab.core.util.mergeWith
import com.kebab.core.util.validate
import com.kebab.core.util.validateOrder
import com.kebab.user.model.User
import com.kebab.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: User) =
            userRepository.save(user.validate())!!

    fun updateUserByGuid(guid: UUID, model: User) =
            userRepository.save(model.mergeWith(userRepository.findByGuid(guid)!!).validate())!!

    fun findUserByGuid(guid: UUID) = userRepository.findByGuid(guid)

    fun deleteUserByGuid(guid: UUID) = userRepository.deleteByGuid(guid)

    fun findAllUsers(page: Int,
                     limit: Int,
                     orderOptions: String,
                     queryParameters: Map<String, String>) =
            orderOptions.validateOrder(User::class.java).run {
                userRepository.findPage(page, limit, this, queryParameters)
            }
}
