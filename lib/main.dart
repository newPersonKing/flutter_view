import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter View',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePage(title: 'Flutter View Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const String _channel = 'increment';
  static const String _pong = 'pong';
  static const String _emptyMessage = '';

  static const BasicMessageChannel<String> platForm = BasicMessageChannel<String>(_channel,StringCodec());

  int _counter = 0;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    platForm.setMessageHandler(_handlePlatformIncrement);
  }

  Future<String> _handlePlatformIncrement(String) async{
    setState(() {
      _counter++;
    });
    return _emptyMessage;
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Expanded(
            child: Center(
              child:   Text(
                'Platform button tapped $_counter time${_counter==1?"":'s'}',
                style: const TextStyle(fontSize: 17.0),
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.only(bottom: 15.0,left: 5.0),
            child: Row(
              children: <Widget>[

                const Text('Flutter', style: TextStyle(fontSize: 30.0)),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(onPressed:_sendFlutterIncrement,
        child: const Icon(Icons.add),),// This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  void _sendFlutterIncrement() {
    platForm.send(_pong);
  }
}
