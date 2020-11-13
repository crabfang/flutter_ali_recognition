
import 'dart:async';

import 'package:flutter/services.dart';

class AliRecognitionSimple {
  static const MethodChannel _channel =
      const MethodChannel('ali_recognition_simple');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
