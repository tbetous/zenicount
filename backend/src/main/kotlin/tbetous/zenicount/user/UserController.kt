package tbetous.zenicount.user

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tbetous.zenicount.response.BooleanResponse

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping("/")
    fun getUser() = SecurityContextHolder.getContext().authentication

    @GetMapping("/authenticated")
    fun isAuthenticated() = BooleanResponse(!SecurityContextHolder.getContext().authentication.authorities
            .map {grantedAuthority ->  grantedAuthority.authority }
            .contains("ROLE_ANONYMOUS"))
    
}