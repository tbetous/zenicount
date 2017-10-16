package tbetous.zenicount.twitter

import org.springframework.stereotype.Component
import tbetous.zenicount.counter.CounterProvider
import tbetous.zenicount.twitter.provider.TwitterFollowersProvider
import javax.inject.Inject

@Component
class TwitterProviderFactory {

    @Inject
    lateinit var twitterService : TwitterService

    fun getProvider(type : TwitterProviderType?, config : TwitterProviderConf) : CounterProvider? {
        when(type) {
            TwitterProviderType.TWITTER_FOLLOWERS_PROVIDER -> return TwitterFollowersProvider(twitterService, config)
            else -> return null
        }
    }

}