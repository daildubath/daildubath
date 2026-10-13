package daildubath.benjch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform