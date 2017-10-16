package tbetous.zenicount.counter.provider

import tbetous.zenicount.counter.CounterProvider
import tbetous.zenicount.counter.CounterProviderConf

class CounterDisplayNumberProvider(private val conf: CounterProviderConf) : CounterProvider {

    override fun getCount(): Int {
        return conf.start ?: 0
    }

}