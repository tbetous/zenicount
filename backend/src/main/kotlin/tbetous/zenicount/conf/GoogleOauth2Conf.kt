package tbetous.zenicount.conf

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails

@Configuration
@Order(1000)
class GoogleOauth2Conf : WebSecurityConfigurerAdapter() {

    @Bean
    @Qualifier("googleAuthorizationCodeResourceDetails")
    @ConfigurationProperties("google.oauth2.client")
    fun googleClient(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @Qualifier("googleResourceServerProperties")
    @ConfigurationProperties("google.oauth2.resource")
    fun googleResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }

    @Bean
    fun oauth2ClientFilterRegistration(filter : OAuth2ClientContextFilter): FilterRegistrationBean {
        var registration = FilterRegistrationBean()
        registration.filter = filter
        registration.order = -100
        return registration
    }
}