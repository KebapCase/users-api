package com.kebab.user.repository

import com.kebab.core.repository.GuidRepository
import com.kebab.user.model.User
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.UUID

interface UserRepository : PagingAndSortingRepository<User, Long>, GuidRepository<User> {

    fun existsByGuid(guid: UUID): Boolean
}