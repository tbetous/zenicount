package tbetous.zenicount.twitter.provider

import tbetous.zenicount.counter.CounterProvider
import tbetous.zenicount.twitter.TwitterProviderConf
import tbetous.zenicount.twitter.TwitterService

class TwitterFollowersProvider(private val twitterService: TwitterService, private val providerConf : TwitterProviderConf) : CounterProvider {

    override fun getCount(): Int {
        return this.twitterService.getFollowersCount(providerConf.stringId)
    }

}