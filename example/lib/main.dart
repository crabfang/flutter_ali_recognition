
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
    Future<String> result;
    if(_counter == 0)
      result = AliRecognition.sdkInit();
    else if(_counter == 1)
      result = AliRecognition.sdkMetaInfos();
    else {
      Map params = new Map();
      params["certifyId"] = "91707dc296d469ad38e4c5efa6a0****";
      params["useMsgBox"] = false;
      result = AliRecognition.sdkVerify(params);
    }
    result.then((result) {
      _label = result;
    });
    _counter ++;
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
