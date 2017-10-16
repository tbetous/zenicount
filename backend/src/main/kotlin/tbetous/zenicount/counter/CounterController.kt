package tbetous.zenicount.counter

import org.springframework.web.bind.annotation.*
import tbetous.zenicount.counter.provider.CounterDisplayNumberProvider
import tbetous.zenicount.response.IntResponse
import tbetous.zenicount.twitter.TwitterProviderConf
import tbetous.zenicount.twitter.TwitterProviderFactory
import tbetous.zenicount.twitter.TwitterProviderType
import javax.inject.Inject

@RestController
class CounterController {

    @Inject
    lateinit var basicProviderFactory : CounterProviderFactory

    @Inject
    lateinit var twitterProviderFactory : TwitterProviderFactory

    var provider : CounterProvider? = null

    @GetMapping("/api/count")
    fun getCount() = provider?.getCount() ?: 0

    @PostMapping("/api/subscribe/basic")
    fun subscribeTwitterFollowers(@RequestBody conf : CounterProviderConf) {
        this.provider = basicProviderFactory.getProvider(CounterProviderType.COUNTER_DISPLAY_NUMBER, conf)
    }

    @PostMapping("/api/subscribe/twitterFollowers")
    fun subscribeTwitterFollowers(@RequestBody conf : TwitterProviderConf) {
        this.provider = twitterProviderFactory.getProvider(TwitterProviderType.TWITTER_FOLLOWERS_PROVIDER, conf)
    }
}