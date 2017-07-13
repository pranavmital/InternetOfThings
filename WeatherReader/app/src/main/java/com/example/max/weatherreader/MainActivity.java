package com.example.max.weatherreader;

        import android.os.AsyncTask;
        import android.os.StrictMode;
        import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {


    TextView finalText;
    TextView bigText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        finalText = (TextView) findViewById(R.id.wText);
        bigText   = (TextView) findViewById(R.id.weatherText);

        String rtrnStr = "";
        String color   = "";
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
        } else
        if (rtrnStr.contains("clear") || rtrnStr.contains("Clear")) {
            color = "yellow";
        } else
        if (rtrnStr.contains("rain")  || rtrnStr.contains("Rain")) {
            color = "blue";
        } else
        if (rtrnStr.contains("storm") || rtrnStr.contains("Storm")) {
            color = "gray";
        }

        bigText.setText(rtrnStr);
        finalText.setText(color);

    }

}
