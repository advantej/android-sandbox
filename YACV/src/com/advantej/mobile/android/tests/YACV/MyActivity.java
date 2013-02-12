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
        setContentView(R.layout.main);

        Button toggle = (Button) findViewById(R.id.button_toggle);
        final CustomView customView = (CustomView) findViewById(R.id.yacv);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customView.toggle();
            }
        });
    }
}
