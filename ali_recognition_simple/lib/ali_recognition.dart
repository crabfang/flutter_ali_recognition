
import 'dart:async';

import 'package:flutter/services.dart';

class AliRecognition {
  static const MethodChannel _channel = const MethodChannel('AliRecognition');

  static Future<Map> sdkInit() async {
    return await _channel.invokeMethod('init');
  }
  static Future<Map> sdkMetaInfos() async {
    return await _channel.invokeMethod('getMetaInfos');
  }
  static Future<Map> sdkVerify(Map params) async {
    return await _channel.invokeMethod('verify', params);
  }
}
