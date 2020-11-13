package com.cabe.flutter.plugin.ali_recognition

import android.util.Log
import androidx.annotation.NonNull
import com.alibaba.fastjson.JSON
import com.aliyun.aliyunface.api.ZIMCallback
import com.aliyun.aliyunface.api.ZIMFacade
import com.aliyun.aliyunface.api.ZIMFacadeBuilder
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** AliRecognitionPlugin */
class AliRecognitionPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    companion object {
        const val PLUGIN_NAME = "AliRecognition"
    }
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activityBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "AliRecognition")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        Log.d(PLUGIN_NAME, "onMethodCall: ${call.method} # ${call.arguments}")
        if(activityBinding == null) {
            result.notImplemented()
            return
        }

        when(call.method) {
            "init" -> {
                init()
                result.success("success")
            }
            "getMetaInfos" -> {
                val metaInfos = getMetaInfos()
                result.success(metaInfos)
            }
            "verify" -> {
                val params = call.arguments as? Map<String, Any>
                verify(params, ZIMCallback { response ->
                    val resultStr = JSON.toJSONString(response)
                    Log.w(PLUGIN_NAME, resultStr)
                    result.success(resultStr)
                    true
                })
            }
        }
    }

    private fun init() {
        ZIMFacade.install(activityBinding?.activity?.applicationContext)
    }

    private fun getMetaInfos(): String? {
        return ZIMFacade.getMetaInfos(activityBinding?.activity?.applicationContext)
    }

    private fun verify(params: Map<String, Any>?, callback: ZIMCallback) {
        val certifyId = params?.get("certifyId") as? String
        val useMsgBox = params?.get("useMsgBox") as? Boolean ?: true
        val extParams = params?.get("extParams") as? HashMap<String, String>
        ZIMFacadeBuilder.create(activityBinding?.activity).apply {
            if (extParams == null) verify(certifyId, useMsgBox, callback)
            else verify(certifyId, useMsgBox, extParams, callback)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        Log.w(PLUGIN_NAME, "ActivityAware#onAttachedToActivity")
    }

    override fun onDetachedFromActivity() {
        activityBinding = null
        Log.w(PLUGIN_NAME, "ActivityAware#onDetachedFromActivity")
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.w(PLUGIN_NAME, "ActivityAware#onDetachedFromActivityForConfigChanges")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.w(PLUGIN_NAME, "ActivityAware#onReattachedToActivityForConfigChanges")
    }
}
