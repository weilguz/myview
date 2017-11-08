package com.myview.acti;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.myview.R;

import java.io.File;

/**
 * 调用摄像头拍照和调用相册选择照片
 */

public class ThreeActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri imageUri;
    private static final int TAKE_PHOTO =1;
    private static final int TAKE_PHOTO_TWO =2;
    private ImageView image;
    private String imagePath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        image = (ImageView) findViewById(R.id.iv_three);
        Button btn_image = (Button) findViewById(R.id.btn_image);
        Button btn_three = (Button) findViewById(R.id.btn_three);

        btn_image.setOnClickListener(this);
        btn_three.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_image:
//                Toast.makeText(ThreeActivity.this,"btn_image",Toast.LENGTH_SHORT).show();
                getImageProvider();
                break;
            case R.id.btn_three:
                getImageFormCapture();
                break;
        }
    }

    //打开相册
    private void openAlbumm() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,TAKE_PHOTO_TWO);
    }

    //打开相册获取
    private void getImageProvider() {
        getUserPermission();
    }

    //获取权限
    private void getUserPermission() {
        if (ContextCompat.checkSelfPermission(ThreeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ThreeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            openAlbumm();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    openAlbumm();
                }else{
                    Toast.makeText(ThreeActivity.this,"onRequestPermissionsResult--",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //开启摄像头拍照
    public void getImageFormCapture() {
        File outpurImage = new File(getExternalCacheDir(), "outputImage.jpg");
        try{
            if (outpurImage.exists()){
                outpurImage.delete();
            }
            outpurImage.createNewFile();
        }catch(Exception e){
//            Log.e("weilgu","getImageFormCapture处没发现的异常啦"+e.getMessage());
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(ThreeActivity.this,
                    "com.myview.fileprovider", outpurImage);
        }else{
            imageUri = Uri.fromFile(outpurImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("weilgu","requestCode+++"+requestCode+"resultCode---"+resultCode+"RESULT_OK***"+RESULT_OK);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try{
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.
                                decodeStream(getContentResolver().openInputStream(imageUri));
                        image.setImageBitmap(bitmap);

                    }catch(Exception e){
                        Log.e("weilgu","onActivityResult处没发现的异常啦"+e.getMessage());
                    }
                }
                break;
            case TAKE_PHOTO_TWO:
                if(resultCode == RESULT_OK){
                    //检测手机版本
                    if (Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforKitKat(data);
                    }
                }
                break;
        }
    }

    private void handleImageBeforKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(ThreeActivity.this,uri)){
            //如果是Document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.docments".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"+"+id;
                imagePath2 = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath2 = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri,则使用普通方式处理
            imagePath2 = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri.直接获得图片路径即可
            imagePath2 = uri.getPath();
        }
        displayImage(imagePath2);
    }

    private void displayImage(String selection) {
        if (selection != null){
            Bitmap bitmap = BitmapFactory.decodeFile(selection);
            image.setImageBitmap(bitmap);
        }else{
            Toast.makeText(ThreeActivity.this,"啊啊啊啊啊",Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri,String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
