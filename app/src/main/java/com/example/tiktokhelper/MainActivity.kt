
package com.example.tiktokhelper

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var statusTitle: TextView
    private lateinit var statusDetails: TextView
    private lateinit var statusDot: View

    private val navy = Color.rgb(20, 31, 58)
    private val blue = Color.rgb(70, 100, 245)
    private val ink = Color.rgb(31, 41, 61)
    private val muted = Color.rgb(105, 116, 138)
    private val page = Color.rgb(245, 247, 252)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = navy
        window.navigationBarColor = page
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(20), dp(20), dp(28))
            setBackgroundColor(page)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(24))
            background = rounded(navy, 24)
        }

        header.addView(TextView(this).apply {
            text = "TIKTOK HELPER"
            textSize = 12f
            setTextColor(Color.rgb(174, 190, 255))
            setTypeface(null, Typeface.BOLD)
        })

        header.addView(TextView(this).apply {
            text = "خلّينا نفحص الاتصال"
            textSize = 25f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            setPadding(0, dp(10), 0, dp(6))
        })

        header.addView(TextView(this).apply {
            text = "تشخيص بسيط للشبكة على جهازك"
            textSize = 14f
            setTextColor(Color.rgb(220, 226, 245))
        })

        root.addView(header, marginParams(18))

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(20), dp(20), dp(20))
            background = rounded(Color.WHITE, 22)
            elevation = dp(2).toFloat()
        }

        card.addView(TextView(this).apply {
            text = "حالة الاتصال"
            textSize = 13f
            setTextColor(muted)
        })

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setPadding(0, dp(12), 0, dp(8))
        }

        statusDot = View(this).apply {
            background = rounded(
                Color.rgb(245, 176, 65), 50
            )
        }
        row.addView(
            statusDot,
            LinearLayout.LayoutParams(dp(12), dp(12))
        )

        statusTitle = TextView(this).apply {
            text = "جاهز للفحص"
            textSize = 19f
            setTextColor(ink)
            setTypeface(null, Typeface.BOLD)
            setPadding(dp(10), 0, 0, 0)
        }
        row.addView(statusTitle)
        card.addView(row)

        statusDetails = TextView(this).apply {
            text = "اضغط على الزر حتى نفحص الشبكات التي يراها أندرويد."
            textSize = 14f
            setTextColor(muted)
            setLineSpacing(dp(3).toFloat(), 1f)
        }
        card.addView(statusDetails)
        root.addView(card, marginParams(16))

        val button = Button(this).apply {
            text = "فحص الاتصال من جديد"
            textSize = 15f
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = rounded(blue, 16)
            setOnClickListener { checkConnection() }
        }

        root.addView(button,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(56)
            ).apply {
                bottomMargin = dp(18)
            }
        )

        val info = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            background = rounded(
                Color.rgb(232, 238, 255), 20
            )
        }

        info.addView(TextView(this).apply {
            text = "شو بيعمل التطبيق؟"
            textSize = 16f
            setTextColor(navy)
            setTypeface(null, Typeface.BOLD)
        })

        info.addView(TextView(this).apply {
            text = "بيفحص معلومات الشبكة التي يوفّرها أندرويد. إذا الإنترنت شغّال لكن ظهرت نتيجة غير صحيحة، فهذا لا يعني بالضرورة أن الإنترنت مقطوع. التطبيق حالياً لا يغيّر إعدادات الشريحة ولا يصلح تيك توك تلقائياً."
            textSize = 14f
            setTextColor(Color.rgb(65, 78, 108))
            setPadding(0, dp(8), 0, 0)
            setLineSpacing(dp(3).toFloat(), 1f)
        })

        root.addView(info)

        root.addView(TextView(this).apply {
            text = "نسخة تجريبية • TikTok Helper"
            textSize = 12f
            setTextColor(muted)
            gravity = Gravity.CENTER
            setPadding(0, dp(22), 0, 0)
        })

        setContentView(ScrollView(this).apply {
            isFillViewport = true
            addView(root)
        })
    }

    private fun checkConnection() {
        val manager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        var hasInternet = false
        var hasValidated = false

        for (network: Network in manager.allNetworks) {
            val caps = manager.getNetworkCapabilities(network)
                ?: continue

            if (caps.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
            ) {
                hasInternet = true

                if (caps.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                ) {
                    hasValidated = true
                }
            }
        }

        when {
            hasValidated -> showStatus(
                "الإنترنت ظاهر وشغّال",
                "أندرويد يؤكد وجود اتصال صالح بالإنترنت. إذا تيك توك ما زال يعرض رسالة عدم الاتصال، فقد تكون المشكلة خاصة بتيك توك أو بالشبكة التي يستخدمها.",
                Color.rgb(32, 160, 105)
            )

            hasInternet -> showStatus(
                "الشبكة موجودة",
                "أندرويد يرى شبكة تدعم الإنترنت، لكنه لم يؤكد الوصول إليه. جرّب فتح موقع أو يوتيوب؛ قد يكون الاتصال شغّالاً رغم نتيجة الفحص.",
                Color.rgb(235, 157, 38)
            )

            else -> showStatus(
                "تعذّر تأكيد الاتصال",
                "أندرويد لا يعرض حالياً شبكة معلّمة كاتصال إنترنت. هذا الفحص وحده لا يثبت أن الإنترنت مقطوع؛ تأكد من اتصال Wi-Fi أو بيانات الهاتف ثم أعد المحاولة.",
                Color.rgb(220, 90, 90)
            )
        }
    }

    private fun showStatus(
        title: String,
        details: String,
        color: Int
    ) {
        statusTitle.text = title
        statusDetails.text = details
        statusDot.background = rounded(color, 50)
    }

    private fun rounded(
        color: Int,
        radiusDp: Int
    ): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radiusDp).toFloat()
        }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun marginParams(
        bottom: Int
    ): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dp(bottom)
        }
}
