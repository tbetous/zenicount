package tbetous.zenicount.twitter

import org.springframework.cache.annotation.Cacheable
import org.springframework.core.env.Environment
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

@Service
class TwitterService(environment: Environment) {

    private var twitter : TwitterTemplate = TwitterTemplate(
            environment.getProperty("twitter.consumerKey"),
            environment.getProperty("twitter.consumerSecret"),
            environment.getProperty("twitter.accessToken"),
            environment.getProperty("twitter.accessTokenSecret"))

    @Cacheable("count")
    fun getFollowersCount(stringId : String?) : Int {
        if(!stringId.isNullOrEmpty())
            return twitter.userOperations().getUserProfile(stringId).followersCount
        else
            return 0
    }

}