package com.nexus.quiz.ui.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.nexus.quiz.engine.LatexParser

class MathJsInterface(private val onHeightChanged: (Int) -> Unit) {
    @JavascriptInterface
    fun onContentHeight(heightDp: Int) {
        onHeightChanged(heightDp)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MathView(
    text: String,
    latexFormula: String? = null,
    isRevealed: Boolean = false,
    modifier: Modifier = Modifier,
    minHeightDp: Int = 40
) {
    val context = LocalContext.current
    var contentHeight by remember { mutableStateOf(minHeightDp) }

    val formattedHtml = remember(text, latexFormula, isRevealed) {
        LatexParser.formatForWebView(text, latexFormula, isRevealed)
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = contentHeight.dp),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = false
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false

                addJavascriptInterface(
                    MathJsInterface { height ->
                        val density = ctx.resources.displayMetrics.density
                        val heightInDp = (height / density).toInt()
                        contentHeight = maxOf(minHeightDp, heightInDp + 20)
                    },
                    "AndroidBridge"
                )

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val jsEscape = formattedHtml
                            .replace("\\", "\\\\")
                            .replace("`", "\\`")
                            .replace("\$", "\\\$")
                        evaluateJavascript("renderMath(`$jsEscape`);", null)
                    }
                }

                loadUrl("file:///android_asset/katex/math_template.html")
            }
        },
        update = { webView ->
            val jsEscape = formattedHtml
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("\$", "\\\$")
            webView.evaluateJavascript("renderMath(`$jsEscape`);", null)
        }
    )
}
