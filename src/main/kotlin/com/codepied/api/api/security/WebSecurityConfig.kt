package com.codepied.api.api.security

import com.codepied.api.api.config.CorsDomainProperty
import com.codepied.api.api.locale.LocaleChangeFilter
import com.codepied.api.api.role.RoleType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * settings for Spring Security config
 *
 * @author Aivyss
 * @since 11/30/2022
 */
@EnableWebSecurity // registry spring security filters to spring filter chain
@Configuration
class WebSecurityConfig(
    private val domains: CorsDomainProperty,
    private val jwtAuthFilter: JwtAuthFilter,
    private val localeChangeFilter: LocaleChangeFilter,
) {

    /**
     * 1. CSRF disabled
     * 2. Security Sesssion disabled
     * 3. http basic authorization disabled
     * 4. Cors policy
     * 5. jwt authorization
     * 6. registration of UrlPatterns
     *
     */
    @Bean
    fun configure(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.csrf().disable() // Prevent Cross-Site Request Forgery
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT Authentication does not need session
            .and()
            .formLogin().disable()
            .httpBasic().disable() // "headers": { "Authorization" $id, $pw } was not allowed

        httpSecurity.addFilter(corsWebFilter()) // Requests suitable for CORS policies are allowed
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        httpSecurity.addFilterBefore(localeChangeFilter, JwtAuthFilter::class.java)

        httpSecurity.authorizeHttpRequests()
            .antMatchers("/api/users/auths/**").permitAll()
            .antMatchers("/api/users/info/duplicate").permitAll()
            .antMatchers("/**").authenticated()
            .antMatchers("/api/management/**").hasAnyRole(RoleType.MANAGER.name, RoleType.PLATFORM_ADMIN.name)
            .antMatchers("/api/admin/**").hasRole(RoleType.PLATFORM_ADMIN.name)

        return httpSecurity.build()
    }

    /**
     * cors 접속 허용 제한
     */
    @Bean
    fun corsWebFilter(): CorsFilter {
        val corsConfig = CorsConfiguration().apply {
            allowedOrigins =
                listOf(
                    domains.localhost,
                )
            allowCredentials = true
            maxAge = 8000L
            addAllowedMethod("*")
            addAllowedHeader("*")
            addExposedHeader(HttpHeaders.CONTENT_DISPOSITION)
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsFilter(source)
    }

    /**
     * password encryptor
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}