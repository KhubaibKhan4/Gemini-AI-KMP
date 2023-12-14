import androidx.compose.ui.window.ComposeUIViewController
import org.gemini.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
