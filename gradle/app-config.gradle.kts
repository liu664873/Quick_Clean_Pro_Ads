import java.util.Properties

fun loadConfigProperties(path: String): Properties {
    val properties = Properties()
    val file = rootProject.file(path)
    if (file.isFile) {
        file.inputStream().use { input -> properties.load(input) }
    }
    return properties
}

val appDefaultConfig = loadConfigProperties("config/app.defaults.properties")
val appLocalConfig = loadConfigProperties("config/app.local.properties")

fun Properties.configValue(name: String): String? =
    if (containsKey(name)) getProperty(name) else null

fun configValue(
    name: String,
    defaultValue: String = "",
): String =
    providers
        .gradleProperty(name)
        .orElse(providers.environmentVariable(name))
        .orNull
        ?: appLocalConfig.configValue(name)
        ?: appDefaultConfig.configValue(name)
        ?: defaultValue

fun String.asBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n")}\""

val trustlookApiKey = configValue("TRUSTLOOK_QUICKCLEANPRO_API_KEY")

fun Any.callNoArgMethod(name: String): Any =
    javaClass.methods.first { method -> method.name == name && method.parameterCount == 0 }.invoke(this)

fun Any.callMethod(
    name: String,
    vararg args: Any,
) {
    javaClass.methods
        .first { method -> method.name == name && method.parameterCount == args.size }
        .invoke(this, *args)
}

val androidExtension = extensions.getByName("android")
val defaultConfig = androidExtension.callNoArgMethod("getDefaultConfig")

fun buildConfigStringField(
    name: String,
    value: String = configValue(name),
) {
    defaultConfig.callMethod("buildConfigField", "String", name, value.asBuildConfigString())
}

fun buildConfigLongField(name: String) {
    defaultConfig.callMethod("buildConfigField", "long", name, "${configValue(name)}L")
}

@Suppress("UNCHECKED_CAST")
val manifestPlaceholders =
    defaultConfig.callNoArgMethod("getManifestPlaceholders") as MutableMap<String, Any>

manifestPlaceholders.putAll(
    mapOf(
        "trustlookApiKey" to trustlookApiKey,
        "advAdmobAppId" to configValue("ADV_ADMOB_APP_ID"),
        "advFacebookAppId" to configValue("ADV_FACEBOOK_APP_ID"),
        "advFacebookClientToken" to configValue("ADV_FACEBOOK_CLIENT_TOKEN"),
    ),
)

buildConfigStringField("TRUSTLOOK_API_KEY", trustlookApiKey)

listOf(
    "ADV_PRIVACY_URL",
    "ADV_TERMS_URL",
    "ADV_ADMOB_APP_ID",
    "ADV_ADMOB_BANNER_ID",
    "ADV_ADMOB_INTERSTITIAL_ID",
    "ADV_ADMOB_NATIVE_ID",
    "ADV_ADMOB_NATIVE_IDS_JSON",
    "ADV_ADMOB_OPEN_ID",
    "ADV_ADMOB_REWARDED_ID",
    "ADV_FACEBOOK_APP_ID",
    "ADV_FACEBOOK_CLIENT_TOKEN",
    "ADV_DEFAULT_TOPIC",
    "ADV_AD_DEBUG_OVERRIDE_MODE",
    "ADV_REMOTE_CONFIG_ENCRYPTION_KEY",
    "ADV_REMOTE_CONFIG_ENCRYPTION_KEY_ID",
    "ADV_SERVER_RELEASE_HOST",
    "ADV_SERVER_TEST_HOST",
    "ADV_PLAY_INTEGRITY_PARSE_TOKEN_KEY",
    "ADV_THINKING_APP_KEY",
    "ADV_THINKING_SERVER_URL",
    "ADV_SINGULAR_API_KEY",
    "ADV_SINGULAR_SECRET",
    "ADV_TIKTOK_ACCESS_TOKEN",
    "ADV_TIKTOK_TT_APP_ID",
    "ADV_TIKTOK_APP_ID",
    "ADV_SAFE_EXPECTED_SIGNATURES",
).forEach { name -> buildConfigStringField(name) }

buildConfigLongField("ADV_PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER")
