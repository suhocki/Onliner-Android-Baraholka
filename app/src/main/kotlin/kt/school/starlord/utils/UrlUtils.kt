package kt.school.starlord.utils

import kt.school.starlord.BuildConfig

/**
 * Correcting URL if him not full.
 * */
fun urlCorrector(url: String) = if (url.startsWith("./")) {
    url.replace("./", BuildConfig.BARAHOLKA_ONLINER_URL + "/")
} else {
    url
}
