package com.philips.lighting.quickstart;

import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import android.os.AsyncTask;
import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


class Weather {
    public String weatherString;
    public String getStr() {
        return weatherString;
    }
}

public class MyApplicationActivity extends Activity {
    private PHHueSDK phHueSDK = PHHueSDK.create();;
    private static final int MAX_HUE=65535;
    public static final String TAG = "QuickStart";
    public static PHLightState lightState = new PHLightState();
    //public PHBridge bridge = new phHueSDK.getSelectedBridge();

    TextView finalText;
    TextView bigText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        finalText = (TextView) findViewById(R.id.wText);
        bigText   = (TextView) findViewById(R.id.weatherText);

        String rtrnStr = "";
        String color   = "";

        phHueSDK = PHHueSDK.create();
        Button randomButton;
        randomButton = (Button) findViewById(R.id.buttonRand);
        randomButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                randomLights();
            }

        });

        try
        {
            URL url = new URL("http://w1.weather.gov/xml/current_obs/KAUS.rss");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null)
            {
                // str is one line of text; readLine() strips the newline character(s)
                // You can use the contain method here.
                rtrnStr = rtrnStr + str;
            }
            in.close();
        } catch (IOException e) {

        }

        rtrnStr = rtrnStr.split("<title>")[3];
        rtrnStr = rtrnStr.split("</title>")[0];
        if (rtrnStr.contains("cloud") || rtrnStr.contains("Cloud")) {
            color = "white";
            lightState.setSaturation(0);
            //bridge.updateLightState(light, lightState, listener);
        } else
        if (rtrnStr.contains("clear") || rtrnStr.contains("Clear")) {
            color = "yellow";
            lightState.setSaturation(254);
            lightState.setHue(12750);
        } else
        if (rtrnStr.contains("rain")  || rtrnStr.contains("Rain")) {
            color = "blue";
            lightState.setSaturation(254);
            lightState.setHue(47000);
        } else
        if (rtrnStr.contains("storm") || rtrnStr.contains("Storm")) {
            color = "gray";
            lightState.setSaturation(0);
            lightState.setBrightness(100);
            lightState.setHue(56666);
        }

        bigText.setText(rtrnStr);
        finalText.setText(color);

    }

    public void randomLights() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();
        
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            // To validate your lightstate is valid (before sending to the bridge) you can use:  
            // String validState = lightState.validateState();
            bridge.updateLightState(light, lightState, listener);
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }
    }
    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {
        
        @Override
        public void onSuccess() {  
        }
        
        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
           Log.w(TAG, "Light has updated");
        }
        
        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
    
    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            
            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            
            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }
}
