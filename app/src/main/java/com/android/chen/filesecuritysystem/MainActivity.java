package com.android.chen.filesecuritysystem;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout.setTitle("文件加解密系统");
        mCollapsingToolbarLayout.setExpandedTitleColor(R.color.white);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(R.color.black);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
