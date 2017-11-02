package com.xiaopo.flying.stickerview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.stickerview.util.FileUtil;

import java.io.File;

public class MyStickerActivity extends AppCompatActivity {
    private static final String TAG = MyStickerActivity.class.getSimpleName();
    public static final int PERM_RQST_CODE = 110;
    private StickerView stickerView;
    private TextSticker mReplaceSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sticker);
        //
        initToolbar();
        //
        initReplaceSticker();
        //
        initStickerView();
        //请求权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_RQST_CODE);
        } else {
            loadDefaultSticker();
        }
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.inflateMenu(R.menu.menu_save);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item_save) {
                        File file = FileUtil.getNewFile(MyStickerActivity.this, "Sticker");
                        if (file != null) {
                            stickerView.save(file);
                            Toast.makeText(MyStickerActivity.this, "saved in " + file.getAbsolutePath(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyStickerActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //                    stickerView.replace(new DrawableSticker(
                    //                            ContextCompat.getDrawable(MyStickerActivity.this, R.drawable.haizewang_90)
                    //                    ));
                    return false;
                }
            });
        }
    }

    /**
     * 初始化贴纸操作View
     */
    private void initStickerView() {
        stickerView = (StickerView) findViewById(R.id.sticker_view);
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                //如果点击某个sticker，将绘制文字颜色变红
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");

                stickerView.bringCurrentStickerToFront();
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
    }

    /**
     * 初始化替换的sticker
     */
    private void initReplaceSticker() {
        mReplaceSticker = new TextSticker(this);
        mReplaceSticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.sticker_transparent_background));
        mReplaceSticker.setText("Hello, world!");
        mReplaceSticker.setTextColor(Color.BLACK);
        mReplaceSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        mReplaceSticker.resizeText();
    }

    /**
     * 添加初始的sticker
     */
    private void loadDefaultSticker() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.emoji701);
        Drawable drawable1 =
                ContextCompat.getDrawable(this, R.drawable.emoji703);
        stickerView.addSticker(new DrawableSticker(drawable), Sticker.Position.CENTER | Sticker.Position.LEFT);
        stickerView.addSticker(new DrawableSticker(drawable1));
//        Drawable bubble = ContextCompat.getDrawable(this, R.drawable.bubble);
//        stickerView.addSticker(
//                new TextSticker(getApplicationContext())
//                        .setDrawable(bubble)
//                        .setText("Sticker\n")
//                        .setMaxTextSize(14)
//                        .resizeText()
//                , Sticker.Position.TOP);
    }

    /**
     * 请求相关权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadDefaultSticker();
        }
    }

    /**
     * 将当前的stiker替换成指定的，并且大小缩放保持不变
     *
     * @param view
     */
    public void testReplace(View view) {
        if (stickerView.replace(mReplaceSticker)) {
            Toast.makeText(MyStickerActivity.this, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyStickerActivity.this, "Replace Sticker failed!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 锁住指定的view
     */
    public void testLock(View view) {
        stickerView.setLocked(!stickerView.isLocked());
    }

    /**
     * 删除当前的sticker
     *
     * @param view
     */
    public void testRemove(View view) {
        if (stickerView.removeCurrentSticker()) {
            Toast.makeText(MyStickerActivity.this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MyStickerActivity.this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 移除所有的sticker
     *
     * @param view
     */
    public void testRemoveAll(View view) {
        stickerView.removeAllStickers();
    }


    /**
     * 重置
     *
     * @param view
     */
    public void reset(View view) {
        stickerView.removeAllStickers();
        loadDefaultSticker();
    }

    /**
     * 添加默认的sticker
     *
     * @param view
     */
    public void testAdd(View view) {
        final TextSticker sticker = new TextSticker(this);
        sticker.setText("Hello, world!");
        sticker.setTextColor(Color.BLUE);
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();

        stickerView.addSticker(sticker);
    }
}
