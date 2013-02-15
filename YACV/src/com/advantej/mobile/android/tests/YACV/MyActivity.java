package com.advantej.mobile.android.tests.YACV;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_another);

        final AnotherCustomView anotherCustomView = (AnotherCustomView) findViewById(R.id.yaacv);

        Button left = (Button) findViewById(R.id.button_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int config = anotherCustomView.getCurrentConfiguration();
                switch (config)
                {
                    case AnotherCustomView.CONFIG_MAIN_ONLY:
                        anotherCustomView.setConfig(AnotherCustomView.CONFIG_LEFT_OPEN);
                        break;
                    case AnotherCustomView.CONFIG_LEFT_OPEN:
                        anotherCustomView.setConfig(AnotherCustomView.CONFIG_MAIN_ONLY);
                        break;
                }
            }
        });

        Button right = (Button) findViewById(R.id.button_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int config = anotherCustomView.getCurrentConfiguration();
                switch (config)
                {
                    case AnotherCustomView.CONFIG_MAIN_ONLY:
                        anotherCustomView.setConfig(AnotherCustomView.CONFIG_RIGHT_OPEN);
                        break;
                    case AnotherCustomView.CONFIG_RIGHT_OPEN:
                        anotherCustomView.setConfig(AnotherCustomView.CONFIG_MAIN_ONLY);
                        break;
                }
            }
        });
    }
}
