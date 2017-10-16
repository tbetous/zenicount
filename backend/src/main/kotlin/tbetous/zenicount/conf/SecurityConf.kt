package tbetous.zenicount.conf

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.inject.Inject
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import javax.servlet.*
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableOAuth2Client
@EnableWebSecurity
class SecurityConf : WebSecurityConfigurerAdapter() {


    @Inject
    @Qualifier("oauth2ClientContext")
    lateinit var oauth2ClientContext: OAuth2ClientContext

    @Inject
    @Qualifier("googleAuthorizationCodeResourceDetails")
    lateinit var googleAuthorizationCodeResourceDetails : AuthorizationCodeResourceDetails

    @Inject
    @Qualifier("googleResourceServerProperties")
    lateinit var googleResourceServerProperties : ResourceServerProperties

    val restTemplate = RestTemplate()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .addFilterBefore(Oauth2GoogleFilter(), BasicAuthenticationFilter::class.java)
            .logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/api/logout"))
                .logoutSuccessUrl("/")
                .addLogoutHandler(Oauth2GoogleLogoutHandler())
                .permitAll()
            .and()
            .authorizeRequests()
                .antMatchers("/api/count", "/api/login", "/api/logout", "/api/user/**")
                .permitAll()
                .antMatchers(("/api/**"))
                .authenticated()
                .antMatchers("/**")
                .permitAll()
            .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    }


    private fun Oauth2GoogleFilter(): Filter {
        val googleFilter = OAuth2ClientAuthenticationProcessingFilter("/api/login")
        googleFilter.setAuthenticationFailureHandler(Oauth2GoogleAuthentificationFailureHandler())
        val googleTemplate = OAuth2RestTemplate(googleAuthorizationCodeResourceDetails, oauth2ClientContext)
        googleFilter.setRestTemplate(googleTemplate)
        val tokenServices = UserInfoTokenServices(googleResourceServerProperties.userInfoUri, googleAuthorizationCodeResourceDetails.clientId)
        tokenServices.setRestTemplate(googleTemplate)
        tokenServices.setPrincipalExtractor(Oauth2GooglePrincipalExtractor())
        googleFilter.setTokenServices(tokenServices)
        return googleFilter
    }

    inner class Oauth2GoogleLogoutHandler : LogoutHandler {
        override fun logout(p0: HttpServletRequest?, p1: HttpServletResponse?, p2: Authentication?) {
            val token = (p2?.details as OAuth2AuthenticationDetails).tokenValue
            restTemplate.getForObject("https://accounts.google.com/o/oauth2/revoke?token=$token", Any::class.java)
        }
    }

    inner class Oauth2GooglePrincipalExtractor : PrincipalExtractor {
        override fun extractPrincipal(map: MutableMap<String, Any>?): MutableMap<String, Any>? {
            if (map != null && map.containsKey("hd")) {
                var domain : String = map["hd"] as String
                if (domain == "zenika.com") {
                    println(map.toString())
                    return map
                }
            }
            throw BadCredentialsException("Only zenika members are allowed.")
        }
    }

    inner class Oauth2GoogleAuthentificationFailureHandler : AuthenticationFailureHandler {
        override fun onAuthenticationFailure(p0: HttpServletRequest?, p1: HttpServletResponse?, p2: AuthenticationException?) {
            val token = oauth2ClientContext.accessToken
            if(token != null) {
                p0?.session?.invalidate()
                restTemplate.getForObject("https://accounts.google.com/o/oauth2/revoke?token=$token", Any::class.java)
            }
            p1?.sendRedirect("/")
        }
    }
}