package com.kebab.user.repository

import com.kebab.core.repository.GuidRepository
import com.kebab.user.model.User
import org.springframework.data.repository.PagingAndSortingRepository

interface UserRepository : PagingAndSortingRepository<User, Long>, GuidRepository<User> {

    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
    fun deleteById(id: Long)
}