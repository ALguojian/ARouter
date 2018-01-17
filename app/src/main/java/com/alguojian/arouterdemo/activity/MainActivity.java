package com.alguojian.arouterdemo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alguojian.arouterdemo.R;
import com.alguojian.arouterdemo.Utils;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.facade.service.DegradeService;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "activity/mainActivity")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button button;
    protected Button button2;
    protected Button button3;
    protected Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {

            /**
             * 直接跳转
             */
            ARouter.getInstance().build(Utils.MAIN_2_ACTIVITY).navigation();

        } else if (view.getId() == R.id.button2) {

            /**
             * 携带参数
             */
            ARouter.getInstance().build(Utils.MAIN_3_ACTIVITY)
                    .withString("bookId", "123")
                    .withString("chapterId", "12")
                    .navigation();

        } else if (view.getId() == R.id.button4) {

            /**
             * 通过URL跳转
             */
            Uri uri = getIntent().getData();
            ARouter.getInstance().build(uri).navigation();

        } else if (view.getId() == R.id.button3) {

            /**
             * 直接跳转
             */
            ARouter.getInstance().build(Utils.MAIN_2_ACTIVITY).navigation();

            /**
             * 携带参数
             */
            ARouter.getInstance().build(Utils.MAIN_3_ACTIVITY)
                    .withString("bookId", "123")
                    .withString("chapterId", "12")
                    .navigation();

            /**
             * 分组
             */
            // 这个页面主动指定了Group名
            ARouter.getInstance().build("/module/2", "m2").navigation();


            /**
             * 通过URL跳转
             */
            Uri uri = getIntent().getData();
            ARouter.getInstance().build(uri).navigation();

            /**
             * 关闭Arouter
             */
            ARouter.getInstance().destroy();

            /**
             * 带有返回值的跳转
             */
            ARouter.getInstance()
                    .build("/test/activity2")
                    .navigation(this, 666);

            /**
             * 获得Fragment的实例
             */
            Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();


            /**
             * 添加跳转动画-老板本
             */
            ARouter.getInstance()
                    .build("/test/activity2")
                    .withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                    .navigation(this);


            /**
             * 新版本跳转动画
             */
            if (Build.VERSION.SDK_INT >= 16) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);

                ARouter.getInstance()
                        .build("/test/activity2")
                        .withOptionsCompat(compat)
                        .navigation();
            } else {
                Toast.makeText(this, "API < 16,不支持新版本动画", Toast.LENGTH_SHORT).show();
            }


            /**
             * 添加拦截器
             */

            ARouter.getInstance()
                    .build("/test/activity4")
                    .navigation(this, new NavCallback() {
                        @Override
                        public void onArrival(Postcard postcard) {

                        }

                        @Override
                        public void onInterrupt(Postcard postcard) {
                            Log.d("ARouter", "被拦截了");
                        }
                    });

            /**
             * 声明拦截器
             */
            @Interceptor(priority = 7, name = "测试拦截器")
            public class Test1Interceptor implements IInterceptor {
                Context mContext;

                /**
                 * The operation of this interceptor.
                 * @param postcard meta
                 * @param callback cb
                 */
                @Override
                public void process(final Postcard postcard, final InterceptorCallback callback) {
                    if ("/test/activity4".equals(postcard.getPath())) {
                        final AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.getThis());
                        ab.setCancelable(false);
                        ab.setTitle("温馨提醒");
                        ab.setMessage("想要跳转到Test4Activity么？(触发了\"/inter/test1\"拦截器，拦截了本次跳转)");
                        ab.setNegativeButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onContinue(postcard);
                            }
                        });
                        ab.setNeutralButton("算了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onInterrupt(null);
                            }
                        });
                        ab.setPositiveButton("加点料", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postcard.withString("extra", "我是在拦截器中附加的参数");
                                callback.onContinue(postcard);
                            }
                        });

                        MainLooper.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ab.create().show();
                            }
                        });
                    } else {
                        callback.onContinue(postcard);
                    }
                }

                /**
                 * Do your init work in this method, it well be call when processor has been load.
                 *
                 * @param context ctx
                 */
                @Override
                public void init(Context context) {
                    mContext = context;
                    Log.e("testService", Test1Interceptor.class.getName() + " has init.");
                }
            }


            /**
             * 传递参数，带有参数，以及序列化
             */
            TestParcelable testParcelable = new TestParcelable("jack", 666);
            TestObj testObj = new TestObj("Rose", 777);
            List<TestObj> objList = new ArrayList<>();
            objList.add(testObj);

            Map<String, List<TestObj>> map = new HashMap<>();
            map.put("testMap", objList);

            ARouter.getInstance().build("/test/activity1")
                    .withString("name", "老王")
                    .withInt("age", 18)
                    .withBoolean("boy", true)
                    .withLong("high", 180)
                    .withString("url", "https://a.b.c")
                    .withParcelable("pac", testParcelable)
                    .withObject("obj", testObj)
                    .withObject("objList", objList)
                    .withObject("map", map)
                    .navigation();


            /**
             * 接受参数使用注入
             */
            ARouter.getInstance().inject(this);

            @Autowired
            public String name;
            @Autowired
            int age;
            @Autowired(name = "girl") // 通过name来映射URL中的不同参数
                    boolean boy;
            @Autowired
            TestObj obj;    // 支持解析自定义对象，URL中使用json传递


            /**
             * 跳转失败，单独降级
             */
            ARouter.getInstance().build("/xxx/xxx").navigation(this, new NavCallback() {
                @Override
                public void onFound(Postcard postcard) {
                    Log.d("ARouter", "找到了");
                }

                @Override
                public void onLost(Postcard postcard) {
                    Log.d("ARouter", "找不到了");
                }

                @Override
                public void onArrival(Postcard postcard) {
                    Log.d("ARouter", "跳转完了");
                }

                @Override
                public void onInterrupt(Postcard postcard) {
                    Log.d("ARouter", "被拦截了");
                }
            });

            /**
             * 挑战失败添加全局降级
             */
            // 实现DegradeService接口，并加上一个Path内容任意的注解即可
            @Route(path = "/xxx/xxx")
            public class DegradeServiceImpl implements DegradeService {
                @Override
                public void onLost(Context context, Postcard postcard) {
                    // do something.
                }

                @Override
                public void init(Context context) {

                }
            }


            /**
             * 调用失败
             */
            ARouter.getInstance().navigation(MainActivity.class);


            /**
             * 通过名字用服务
             */
            ((HelloService) ARouter.getInstance().build("/service/hello").navigation()).sayHello("mike");

            /**
             * 通过TYPE调用服务
             */
            ARouter.getInstance().navigation(HelloService.class).sayHello("mike");


            /**
             *  makeSceneTransitionAnimation 使用共享元素的时候，需要在navigation方法中传入当前Activity
             */
            ARouter.getInstance()
                    .build("/test/activity2")
                    .withOptionsCompat(compat)
                    .navigation();

            /**
             *  使用绿色通道(跳过所有的拦截器)
             */

            ARouter.getInstance().build("/home/main").greenChannel().navigation();

            /**
             *使用自己的日志工具打印日志
             */
            ARouter.setLogger();
        }
    }

    private void initView() {
        button = findViewById(R.id.button);
        button.setOnClickListener(MainActivity.this);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(MainActivity.this);
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(MainActivity.this);
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(MainActivity.this);
    }
}
