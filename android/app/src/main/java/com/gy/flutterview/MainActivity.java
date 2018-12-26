package com.gy.flutterview;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;

public class MainActivity extends FlutterActivity {

  private FlutterView flutterView;
  private int counter;
  private static final String CHANNEL = "increment";
  private static final String EMPTY_MESSAGE = "123123";
  private static final String PING = "ping";
  private BasicMessageChannel<String> messageChannel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    String[] args = getArgsFromIntent(getIntent());

    /*确保Flutterview 加载完成*/
    FlutterMain.ensureInitializationComplete(getApplicationContext(), args);

    setContentView(R.layout.flutter_view_layout);

    FlutterRunArguments runArguments = new FlutterRunArguments();
    /*/data/data/com.example.flutterapp/app_flutter/flutter_assets */
    /*绑定flutter运行路径，以及入口 这里的参数 暂时没看懂 到底怎么填*/
    runArguments.bundlePath = FlutterMain.findAppBundlePath(getApplicationContext());
    runArguments.entrypoint = "main";

    flutterView = findViewById(R.id.flutter_view);
    flutterView.runFromBundle(runArguments);

    messageChannel = new BasicMessageChannel<>(flutterView,CHANNEL,StringCodec.INSTANCE);

    messageChannel.setMessageHandler(new BasicMessageChannel.MessageHandler<String>() {
      @Override
      public void onMessage(String s, BasicMessageChannel.Reply<String> reply) {
        onFlutterIncrement();
        reply.reply(EMPTY_MESSAGE);
      }
    });

    Button button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        messageChannel.send("is me");
      }
    });

  }

  private void onFlutterIncrement() {
    counter++;
    TextView textView = findViewById(R.id.button_tap);
    String value = "Flutter button tapped " + counter + (counter == 1 ? " time" : " times");
    textView.setText(value);
  }


  private int getBatteryLevel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).
              registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
              intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }
  }

  private String[] getArgsFromIntent(Intent intent) {
    // Before adding more entries to this list, consider that arbitrary
    // Android applications can generate intents with extra data and that
    // there are many security-sensitive args in the binary.
    ArrayList<String> args = new ArrayList<>();
    if (intent.getBooleanExtra("trace-startup", false)) {
      args.add("--trace-startup");
    }
    if (intent.getBooleanExtra("start-paused", false)) {
      args.add("--start-paused");
    }
    if (intent.getBooleanExtra("enable-dart-profiling", false)) {
      args.add("--enable-dart-profiling");
    }
    if (!args.isEmpty()) {
      String[] argsArray = new String[args.size()];
      return args.toArray(argsArray);
    }
    return null;
  }

  @Override
  protected void onDestroy() {
    if (flutterView != null) {
      flutterView.destroy();
    }
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    super.onPause();
    flutterView.onPause();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    flutterView.onPostResume();
  }
}
