package com.cabe.flutter.plugin.ali_recognition;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.aliyun.aliyunface.api.ZIMCallback;
import com.aliyun.aliyunface.api.ZIMFacade;
import com.aliyun.aliyunface.api.ZIMFacadeBuilder;
import com.aliyun.aliyunface.api.ZIMResponse;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** AliRecognitionPlugin */
public class AliRecognitionPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ActivityPluginBinding activityBinding = null;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "AliRecognition");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    switch (call.method) {
      case "init":
        try {
          init();
          Map<String, Object> data = new HashMap<>();
          data.put("code", 0);
          result.success(data);
        } catch (Exception e) {
          e.printStackTrace();
          result.error("1", e.getMessage(), e);
        }
        break;
      case "getMetaInfos":
        try {
          String metaInfoStr = getMetaInfos();
          Map metaData = (Map) JSON.parse(metaInfoStr);
          Map<String, Object> data = new HashMap<>();
          data.put("code", 0);
          data.put("data", metaData);
          result.success(data);
        } catch (Exception e) {
          e.printStackTrace();
          result.error("1", e.getMessage(), e);
        }
        break;
      case "verify":
        try {
          Map<String, Object> params = (Map<String, Object>) call.arguments;
          final String certifyId = (String) params.get("certifyId");
          verify(params, new ZIMCallback() {
            @Override
            public boolean response(ZIMResponse zimResponse) {
              String jsonStr = JSON.toJSONString(zimResponse);
              Map data = JSON.parseObject(jsonStr, Map.class);
              data.put("certifyId", certifyId);
              result.success(data);
              return true;
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
          result.error("1", e.getMessage(), e);
        }
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  private void init() {
    ZIMFacade.install(activityBinding.getActivity().getApplicationContext());
  }

  private String getMetaInfos() {
    return ZIMFacade.getMetaInfos(activityBinding.getActivity().getApplicationContext());
  }

  private void verify(Map<String, Object> params, ZIMCallback callback) {
    String certifyId = (String) params.get("certifyId");
    boolean useMsgBox = (boolean) params.get("useMsgBox");
    HashMap<String, String> extParams = (HashMap<String, String>) params.get("extParams");
    ZIMFacade facade = ZIMFacadeBuilder.create(activityBinding.getActivity());
    if (extParams == null) facade.verify(certifyId, useMsgBox, callback);
    else facade.verify(certifyId, useMsgBox, extParams, callback);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activityBinding = binding;
  }

  @Override
  public void onDetachedFromActivity() {
    activityBinding = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }
}