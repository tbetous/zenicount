package tbetous.zenicount.counter

data class CounterProviderConf(
        val start: Int?     = null,
        val end: Int?       = null,
        val interval: Int?  = null,
        val jump: Int?      = null
)