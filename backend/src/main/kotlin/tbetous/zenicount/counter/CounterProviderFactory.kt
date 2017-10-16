package tbetous.zenicount.counter

import org.springframework.stereotype.Component
import tbetous.zenicount.counter.provider.CounterDisplayNumberProvider

@Component
class CounterProviderFactory {

    fun getProvider(type : CounterProviderType?, config : CounterProviderConf) : CounterProvider? {
        when(type) {
            CounterProviderType.COUNTER_DISPLAY_NUMBER -> return CounterDisplayNumberProvider(config)
            else -> return null
        }
    }
}