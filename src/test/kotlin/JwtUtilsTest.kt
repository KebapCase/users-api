import com.kebab.user.model.User
import com.kebab.user.model.UserRole
import com.kebab.user.security.util.JwtUtils.fromToken
import com.kebab.user.security.util.JwtUtils.toToken

import org.junit.Test
import kotlin.test.assertEquals

class JwtUtilsTest {
    private val user = User(username = "m.ivanov", role = UserRole.ROLE_ADMIN)
    private val token = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInVzZXJuYW1lIjoibS5pdmFub3YifQ.jUBhU2AHoZWYK8YxZEtLhPZ7TTFKkzPOFOw3Y0JpaWE3uSIRNu-5OTdPnzsz7o5iRBQib5twsuPQ86uVsQ-oGg"

    @Test
    fun toTokenTest() {
        assertEquals(token, user.toToken())
    }

    @Test
    fun fromTokenTest() {
        assertEquals(user, token.fromToken())
    }
}