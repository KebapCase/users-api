package com.kebab.user.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * @author Valentin Trusevich
 */
@Configuration
class WebConfig(val interceptor: AccessValidationInterceptor) : WebMvcConfigurerAdapter() {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).excludePathPatterns("/", "/swagger.json", "/swagger/swagger.json", "/logs")
    }
}
