package com.kebab.user.security.config

import com.kebab.user.security.AuthenticationInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * @author Valentin Trusevich
 */
@Configuration
class WebSecurityConfiguration(val authenticationInterceptor: AuthenticationInterceptor
) : WebMvcConfigurerAdapter() {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
                .addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
