package com.kebab.user.validation

import com.kebab.user.repository.UserRepository
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.reflect.KClass

/**
 * Validates that username is unique
 *
 * @author Valentin Trusevich
 */
@Retention
@Target(PROPERTY_GETTER, FIELD, CONSTRUCTOR)
@MustBeDocumented
@Constraint(validatedBy = [UserExistsValidator::class])
annotation class UserExists(val message: String = "user with this username already exists",
                            val groups: Array<KClass<*>> = [],
                            val payload: Array<KClass<out Payload>> = [])

/**
 * @author Valentin Trusevich
 */
class UserExistsValidator(private val userRepository: UserRepository) : ConstraintValidator<UserExists, String?> {

    override fun initialize(constraint: UserExists) {}

    override fun isValid(username: String?, context: ConstraintValidatorContext) =
            username?.let {
                !userRepository.existsByUsername(it)
            } ?: true
}