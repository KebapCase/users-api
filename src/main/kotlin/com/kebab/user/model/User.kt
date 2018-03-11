package com.kebab.user.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.kebab.core.model.BaseEntity
import com.kebab.core.validation.Email
import com.kebab.core.validation.UrlValid
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.NotBlank
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Object contains all required information for a user")
@Table(name = "user", uniqueConstraints = [(UniqueConstraint(name = "unique_user_guid", columnNames = arrayOf("guid")))])
data class User(

        @get:NotBlank
        @get:Length(min = 4, max = 32)
        @ApiModelProperty("User's login", example = "m.ivanov", required = true)
        var username: String? = null,

        @get:NotBlank
        @get:Length(min = 4, max = 65)
        @ApiModelProperty("User's password", example = "kekCheburek", required = true)
        var password: String? = null,

        @get:NotBlank
        @get:Length(min = 2, max = 40)
        @ApiModelProperty("User's first name", example = "Mike", required = true)
        var firstName: String? = null,

        @get:NotBlank
        @get:Length(min = 2, max = 60)
        @ApiModelProperty("User's last name", example = "Ivanov", required = true)
        var lastName: String? = null,

        @get:NotBlank
        @get:Length(min = 5, max = 65)
        @get:Email
        @ApiModelProperty("User's login", example = "m.ivanov@example.com", required = true)
        var email: String? = null,

        @get:NotBlank
        @get:UrlValid
        @ApiModelProperty("User's avatar link", example = "https://assets.pcmag.com/media/images/543992-ipad-pro-2.jpg?thumb=y&width=980&height=416", required = true)
        var image: String? = null,

        @get:Enumerated(STRING)
        @ApiModelProperty("User's role", example = "ROLE_ADMIN")
        var role: UserRole? = null

) : BaseEntity() {

    companion object {

        private const val serialVersionUID = 92870891100564936L

    }
}
