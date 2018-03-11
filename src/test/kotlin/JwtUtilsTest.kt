import com.kebab.user.model.User
import com.kebab.user.model.UserRole
import com.kebab.user.security.util.fromToken
import com.kebab.user.security.util.toToken

import org.junit.Test
import kotlin.test.assertEquals

class JwtUtilsTest {
    private val user = User(username = "m.ivanov", role = UserRole.ROLE_ADMIN)

    @Test
    fun jwtTest() =
            assertEquals(user, user.toToken().fromToken())
}