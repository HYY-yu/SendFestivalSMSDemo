package com.app.feng.sendfestivalsmsdemo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.feng.changecoloriconview.ChangeColorIconView;
import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.fragment.FestivalCategoryFragment;
import com.app.feng.sendfestivalsmsdemo.fragment.FestivalTypeFragment;
import com.app.feng.sendfestivalsmsdemo.fragment.MyFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<ChangeColorIconView> iconViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("节日短信祝福");
        //设置状态栏透明
        initStatusBar();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        initView();

        initEvent();

    }

    private void initStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("SDK_VERSION", "当前版本大于等于19");
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);
        }
    }

    private void initEvent() {

        viewPager.addOnPageChangeListener(this);


    }

    private void initView() {
        ChangeColorIconView iconView1 = (ChangeColorIconView) findViewById(R.id.bottombar_one);
        ChangeColorIconView iconView2 = (ChangeColorIconView) findViewById(R.id.bottombar_two);
        ChangeColorIconView iconView3 = (ChangeColorIconView) findViewById(R.id.bottombar_three);

        iconView1.setOnClickListener(this);
        iconView2.setOnClickListener(this);
        iconView3.setOnClickListener(this);

        iconView1.setIconAlpha(1.0f);

        iconViews.add(iconView1);
        iconViews.add(iconView2);
        iconViews.add(iconView3);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new FestivalCategoryFragment();
                    case 1:
                        return FestivalTypeFragment.newInstance(null, null);
                    case 2:
                        return MyFragment.newInstance(null, null);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return iconViews.size();
            }
        });

    }

    boolean flag = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        flag = true;
                    }
                }, 3000);
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                flag = false;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        }
        return true;
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
        if (id == R.id.action_aboutme) {

            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //每次点击使所有的底部View重置颜色
        resetAllIconView();

        switch (v.getId()) {
            case R.id.bottombar_one:
                iconViews.get(0).setIconAlpha(1f);
                viewPager.setCurrentItem(0, false);
                break;

            case R.id.bottombar_two:
                iconViews.get(1).setIconAlpha(1f);
                viewPager.setCurrentItem(1, false);
                break;

            case R.id.bottombar_three:
                iconViews.get(2).setIconAlpha(1f);
                viewPager.setCurrentItem(2, false);
                break;
        }


    }

    private void resetAllIconView() {
        for (ChangeColorIconView view :
                iconViews) {
            view.setIconAlpha(0f);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            //Log.i("PageScroll", "position = " + position + " positionOffset =" + positionOffset);
            ChangeColorIconView viewleft = iconViews.get(position);
            ChangeColorIconView viewright = iconViews.get(position + 1);

            viewleft.setIconAlpha(1 - positionOffset);
            viewright.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
