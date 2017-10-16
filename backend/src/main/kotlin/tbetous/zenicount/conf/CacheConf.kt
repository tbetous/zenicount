package tbetous.zenicount.conf

import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.cache.guava.GuavaCacheManager
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean


@Configuration
@EnableCaching
class CacheConf {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = GuavaCacheManager("count")
        val cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(3, TimeUnit.SECONDS)
        cacheManager.setCacheBuilder(cacheBuilder)
        return cacheManager
    }

}