package com.example.dm2launcher

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import kotlinx.coroutines.delay
import kotlin.test.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartDMTest {
    companion object {
        private val TAG = StartDMTest::class.java.simpleName
        private const val DELAY = 2000L
        private const val TIMEOUT = 10000L
    }

    private val instrumentation by lazy { InstrumentationRegistry.getInstrumentation() }
    private val device by lazy { UiDevice.getInstance(instrumentation) }

    private fun clickAndWait(by: BySelector, name: String, nextResId: BySelector? = null) {
        Log.d(TAG, "Click $name")
        device.findObject(by).let {
            assertTrue("Unable to find $name") {
                it != null
            }
            it.click() // .clickAndWait(Until.newWindow(), DELAY)
            device.waitForIdle()
            Thread.sleep(DELAY)
            if (nextResId != null) {
                device.wait(Until.findObject(nextResId), TIMEOUT)
            }
        }
    }

    private fun launchApp() {
        val width = device.displayWidth
        val height = device.displayHeight

        val swipeX = width / 2
        val startSwipeY = (height * 0.9).toInt()
        val endSwipeY = (height * 0.5).toInt()

        device.swipe(swipeX, startSwipeY, swipeX, endSwipeY, 10)

        val dmSelector = By.text("Accessibility")
            .res("com.google.android.apps.nexuslauncher:id/icon")
        device.wait(Until.findObject(dmSelector), TIMEOUT)
        
        clickAndWait(
            dmSelector,
            "DM-2",
            By.res("com.android.packageinstaller:id/permission_allow_button")
        )
    }

    @Test
    fun launch() {
        Log.d(TAG, "Forcing a screen transition to ensure app launch works")

        Log.d(TAG, "Pressing search")
        device.pressSearch()
        Thread.sleep(DELAY)

        Log.d(TAG, "Press home")
        device.pressHome()
        Thread.sleep(DELAY)

        Log.d(TAG, "Press home")
        device.pressHome()
        Thread.sleep(DELAY)

        // Launch DM
        Log.d(TAG, "Launch app")
        launchApp()

        // "Click runtime permission \"allow\" [691,1017][876,1143]"
        clickAndWait(
            By.res("com.android.packageinstaller:id/permission_allow_button"),
            "external storage permission's allow button",
            By.res("android:id/button1")
        )

        // "Click \"start now\" for screen recording [711,1005][978,1131]"
        // No resId and text don't work
        device.findObject(By.text("START NOW")).click() // .click(800, 1000)
        device.wait(
            Until.findObject(By.res("org.droidmate.accessibility:id/app_icon")),
            TIMEOUT
        )

        // Click on app
        clickAndWait(
            By.res("org.droidmate.accessibility:id/app_icon"),
            "app to test",
            By.text("Accessibility")
        )

        // "Click on accessibility in the menu"
        // No resId and text don't work
        device.click(500, 600)

        device.wait(
            Until.findObject(By.res("com.android.settings:id/switch_widget")),
            TIMEOUT
        )

        // "Click on toggle to activate app"
        clickAndWait(
            By.res("com.android.settings:id/switch_widget"),
            "toggle to enable DM",
            By.res("android:id/button1")
        )

        // "Click on ok to start accessibility [810,1258][978,1384]"
        clickAndWait(
            By.res("android:id/button1"),
            "accessibility accept button"
        )
    }
}
