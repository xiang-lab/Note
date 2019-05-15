package com.xiang.draw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private Bitmap baseBitmap;
    private Button btn_resume;
    private Button btn_save;
    private Button btn_radio;
    private Canvas canvas;
    private Paint paint;

    float radio = 5; // 笔宽

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//            radio = 5;

        iv = (ImageView) findViewById(R.id.iv);
        // 初始化一个画笔，笔触宽为5，颜色为红色
        paint = new Paint();
        paint.setStrokeWidth(radio);
        paint.setColor(Color.RED);
        iv = (ImageView) findViewById(R.id.iv);
        btn_resume = (Button) findViewById(R.id.btn_resume);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_radio = (Button) findViewById(R.id.btn_radio);

        btn_resume.setOnClickListener(click);
        btn_save.setOnClickListener(click);
        btn_radio.setOnClickListener(click);
        iv.setOnTouchListener(touch);


    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
        // 定义手指开始触摸的坐标
        float startX;
        float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                // 用户按下动作
                case MotionEvent.ACTION_DOWN:
                    // 第一次绘图初始化内存图片，指定背景为白色
                    if (baseBitmap == null) {
                        baseBitmap = Bitmap.createBitmap(iv.getWidth(),
                                iv.getHeight(), Bitmap.Config.ARGB_8888);
                        canvas = new Canvas(baseBitmap);
                        canvas.drawColor(Color.WHITE);
                    }
                    // 记录好说触摸的点的坐标
                    startX = event.getX();
                    startY = event.getY();
                    break;
                // 用户手指在屏幕上移动的动作
                case MotionEvent.ACTION_MOVE:
                    // 记录移动位置的点的坐标
                    float stopX = event.getX();
                    float stopY = event.getY();

                    // 不断的加笔宽
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            radio += 0.1;
//
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    t.start();

                    paint.setStrokeWidth(radio);
                    // 根据两点坐标，绘制连线
                    canvas.drawLine(startX, startY, stopX, stopY, paint);

                    // 更新开始点的位置
                    startX = event.getX();
                    startY = event.getY();
                    // 把图片展示到lmageView中
                    iv.setImageBitmap(baseBitmap);
                    break;
                case MotionEvent.ACTION_UP:
                    radio = 5;
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    saveBitmap();
                    break;
                case R.id.btn_resume:
                    resumeCanvas();
                    break;
                case R.id.btn_radio:
                    radioChange();
                    break;
                default:
                    break;
            }
        }
    };

    protected void radioChange() {
        if (radio == 20) {
            radio = 5;
        } else {
            radio += 5;
        }
    }

    // 保存图片到SD卡上
    protected void saveBitmap() {
        try {
            // 保存图片到SD卡上
            String fileName = "/sdcard/" + System.currentTimeMillis() + ".png";
            File file = new File(fileName);
            FileOutputStream stream = new FileOutputStream(file);
            baseBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // 压缩？？
            Toast.makeText(MainActivity.this, "保存图片成功", 1000).show();

            // Android设备Gallery应用只会在启动的时候扫描系统文件夹
            // 这里模拟一个媒体装载的广播，用于是使保存的图片可以在Gallery中查看
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "保存图片失败", 1000).show();
            e.printStackTrace();
        }
    }

    // 手动清除画板的绘图，重新创建一个画板
    protected void resumeCanvas() {
        if (baseBitmap != null) {
            baseBitmap = Bitmap.createBitmap(iv.getWidth(),
                    iv.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmap);
            canvas.drawColor(Color.WHITE);
            iv.setImageBitmap(baseBitmap);
            Toast.makeText(MainActivity.this, "清除画板成功，可以重新开始绘图", 1000).show();
        }
    }
}

// 使用内部类，定义一个简单的View
class CustomView1 extends View {

    Paint paint;

    public CustomView1(Context context) {
        super(context);
        paint = new Paint(); // 设置一个笔刷的大小是3的黄色的画笔
        paint.setColor(Color.YELLOW); // 颜色
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3); // 画笔大小
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 直接将View显示区域用某个颜色填充满
        // canvas.drawColor(Color.BLUE);

        // 绘圆
        canvas.drawCircle(100, 100, 90, paint);
        // 绘线
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        canvas.drawLine(300, 300, 400, 500, paint);

        RectF rect = new RectF(100, 100, 300, 300);

        // 绘制弧线区域
        paint.setColor(Color.RED);
        canvas.drawArc(rect,
                0,
                120,
                true,
                paint);

        // 矩形区域内切椭圆
        rect = new RectF(500, 500, 600, 700);
        canvas.drawOval(rect, paint);

        // 绘矩形
        paint.setColor(Color.BLUE);
        rect = new RectF(800, 800, 1000, 1000);
        canvas.drawRect(rect, paint);

        // 绘圆角矩阵
        paint.setColor(Color.YELLOW);
        canvas.drawRoundRect(rect,
                50,
                50,
                paint);

        Path path = new Path(); // 定义一条路径
        path.moveTo(100, 500); // 移动到坐标10,10
        path.lineTo(300, 600);
        path.lineTo(200, 500);
        path.lineTo(100, 500);
        canvas.drawPath(path, paint);

    }
}























