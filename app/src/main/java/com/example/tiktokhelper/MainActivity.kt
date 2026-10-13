package com.example.tiktokhelper

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color

class MainActivity : Activity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 48, 40, 40)
            setBackgroundColor(Color.rgb(248, 249, 252))
        }

        val title = TextView(this).apply {
            text = "TikTok Helper"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(25, 35, 55))
            gravity = Gravity.CENTER
        }
        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "أداة لفحص اتصال الإنترنت"
            textSize = 17f
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 28)
            setTextColor(Color.DKGRAY)
        }
        root.addView(subtitle)

        statusText = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.rgb(35, 45, 60))
            setPadding(20, 24, 20, 24)
            text = "اضغط على الزر لفحص اتصال جهازك."
        }
        root.addView(statusText)

        val checkButton = Button(this).apply {
            text = "فحص الاتصال"
            setOnClickListener { checkConnection() }
        }
        root.addView(checkButton)

        val note = TextView(this).apply {
            text = "\nملاحظة: هذه الأداة تفحص حالة الشبكة فقط، ولا تتجاوز قيود TikTok أو تغيّر معلومات شريحة SIM."
            textSize = 14f
            setTextColor(Color.GRAY)
            setPadding(0, 24, 0, 0)
        }
        root.addView(note)

        setContentView(ScrollView(this).apply {
            addView(root)
        })
    }

    private fun checkConnection() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val network = manager.activeNetwork
        val capabilities = manager.getNetworkCapabilities(network)

        val connected = capabilities != null &&
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )

        val validated = capabilities != null &&
                capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )

        statusText.text = when {
            !connected -> "لم يتم العثور على اتصال إنترنت نشط."
            validated -> "اتصال الإنترنت متاح على جهازك.\nإذا كان TikTok لا يعمل، فقد تكون المشكلة من التطبيق أو الشبكة التي يستخدمها."
            else -> "الشبكة متصلة، لكن تعذّر تأكيد الوصول إلى الإنترنت."
        }
    }
}
