package com.kebab.user.repository

import com.kebab.core.repository.BaseEntityRepository
import com.kebab.user.model.User

interface UserRepository : BaseEntityRepository<User> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?

    fun deleteById(id: Long)
}