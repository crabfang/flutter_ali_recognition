
import 'dart:convert';

import 'package:ali_recognition/ali_recognition.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _label = 'ali recognition demo';
  int _counter = 0;

  @override
  void initState() {
    super.initState();
  }

  void _incrementCounter() {
    print("_incrementCounter:$_counter");
    Future<Map> result;
    if(_counter == 0)
      result = AliRecognition.sdkInit();
    else if(_counter == 1)
      result = AliRecognition.sdkMetaInfos();
    else {
      Map params = new Map();
      params["certifyId"] = "6f89d803bf7d1d575c217436a0020081";
      params["useMsgBox"] = false;
      result = AliRecognition.sdkVerify(params);
    }
    result.then((result) {
      print(result);
      setState(() {
        _label = result.toString();
      });
    });
    _counter ++;
    if(_counter > 2) _counter = 0;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Center(
            child: Text(_label),
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: _incrementCounter,
            tooltip: 'Increment',
            child: Icon(Icons.add),
          )
      ),
    );
  }
}
