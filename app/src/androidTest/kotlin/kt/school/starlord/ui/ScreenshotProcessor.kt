package kt.school.starlord.ui

import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import kt.school.starlord.BuildConfig
import timber.log.Timber

class ScreenshotProcessor : BasicScreenCaptureProcessor() {

    init {
        this.mDefaultScreenshotPath = File(
            File(
                getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
                BuildConfig.SCREENSHOTS_FOLDER
            ).absolutePath
        )
    }

    override fun getFilename(prefix: String): String = prefix
}

fun takeScreenshot(screenShotName: String) {
    val screenCapture = Screenshot.capture()
    val processors = setOf(ScreenshotProcessor())

    runCatching {
        Timber.d("Screenshots", "Taking screenshot of '$screenShotName'")

        screenCapture.apply {
            name = screenShotName
            process(processors)
        }
    }.fold({
        Timber.d("Screenshots", "Screenshot taken")
    }, {
        Timber.e("Screenshots", "Could not take the screenshot", it)
    })
}
