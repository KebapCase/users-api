package com.kebab.user.security.config

import com.kebab.user.security.LoginFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@EnableWebSecurity
class WebSecurity(val userDetailsService: UserDetailsService) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/", "/swagger.json", "/swagger/swagger.json", "/logs", "/swagger-ui").permitAll()
                .antMatchers(HttpMethod.POST, "/login", "/signUp", "/socialLogin").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .addFilter(LoginFilter(authenticationManager()))
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService)
    }
}