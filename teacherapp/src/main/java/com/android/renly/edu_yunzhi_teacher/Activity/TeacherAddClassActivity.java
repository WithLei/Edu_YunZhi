package com.android.renly.edu_yunzhi_teacher.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Bean.Course_forAdd;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.Fragment.MyclassFragment;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.UI.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

public class TeacherAddClassActivity extends BaseActivity {
    @Bind(R.id.ci_addclass_back)
    CircleImageView ciAddclassBack;
    @Bind(R.id.tv_addclass_title)
    TextView tvAddclassTitle;
    @Bind(R.id.tv_addclass_finish)
    TextView tvAddclassFinish;
    @Bind(R.id.et_addclass_classid)
    EditText etAddclassClassid;
    @Bind(R.id.et_addclass_classname)
    EditText etAddclassClassname;
    @Bind(R.id.et_addclass_classdesc)
    EditText etAddclassClassdesc;
    @Bind(R.id.iv_addclass_classiv)
    ImageView ivAddclassClassiv;
    @Bind(R.id.btn_addclass_takephone)
    Button btnAddclassTakephone;
    @Bind(R.id.btn_addclass_fromlocal)
    Button btnAddclassFromlocal;
    private Course_forAdd addedCourse;

    //初始化addedCourse，通过MyclassFragment的intent传输Course对象，其中Course类要实现Parcelable接口
    //为简单起见，先用Course_forAdd实现操作
    @Override
    protected void initData() {
        addedCourse = new Course_forAdd();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_add_class;
    }


    //是否退出当前页面的标识
    private static boolean EXIT = true;
    //handler的what标识
    private static final int WHAT_RESET_BACK = 1;
    //用于2S后改变Tag EXIT的属性
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    EXIT = true;//复原
                    //                    Toast.makeText(NoticeAddActivity.this,"EXIT :"+EXIT,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //设置ci_addclass_back、tv_addclass_finish、btn_addclass_takephone以及btn_addclass_fromlocal的点击事件监听
    //ci_addclass_back：退出当前页面，如果有数据一般不给予退出操作，需要在2S内点击两次才响应退出操作
    //tv_addclass_finish：结束addclass操作，通过intent携带数据，在MyclassFragment中onActivityResult中取得
    //btn_addclass_takephone：通过调用相机应用获取照片，通过intent携带bitmap信息返回给当前Activity
    //btn_addclass_fromlocal：通过intent调用本地图库获取照片，通过intent携带bitmap信息返回给当前Activity
    private static final int REQUEST_TAKE_PHOTO = 444;
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;
    @OnClick({R.id.ci_addclass_back, R.id.tv_addclass_finish, R.id.btn_addclass_takephone, R.id.btn_addclass_fromlocal})
    public void onClick(View v) {
        boolean idEmptyTag = TextUtils.isEmpty(etAddclassClassid.getText().toString());
        boolean nameEmptyTag = TextUtils.isEmpty(etAddclassClassname.getText().toString());
        switch (v.getId()) {
            case R.id.ci_addclass_back:
                if ((!idEmptyTag || !nameEmptyTag) && EXIT) {
                    Toast.makeText(TeacherAddClassActivity.this, "亲，课程编号和课程标题还有内容，再按一次退出", Toast.LENGTH_SHORT).show();
                    EXIT = false;
                    handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
                } else if (!EXIT) {
                    EXIT = true;
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.tv_addclass_finish:
                if (idEmptyTag || nameEmptyTag) {
                    Toast.makeText(TeacherAddClassActivity.this, "亲，课程编号和课程标题必填！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherAddClassActivity.this, "完成添加课程操作", Toast.LENGTH_SHORT).show();
                    // ImageView对象(ivAddclassClassiv)必须做如下设置后，才能获取其中的图像
                    // 获取ImageView中的图像
                    ivAddclassClassiv.setDrawingCacheEnabled(true);
                    //得到Bitmap对象
                    Bitmap classLogo = Bitmap.createBitmap(ivAddclassClassiv.getDrawingCache());

                    //从ImaggeView对象中获取图像后，要记得调用setDrawingCacheEnabled(false)清空画图缓

                    //冲区，否则，下一次用getDrawingCache()方法回去图像时，还是原来的图像

                    ivAddclassClassiv.setDrawingCacheEnabled(false);

//                    //将bitmap写入本地App文件中
//                    FileOutputStream m_fileOutPutStream = null;
//
//                    String filepath = Environment.getExternalStorageDirectory() + File.separator + etAddclassClassname.getText().toString() + ".png";
//
//                    try {
//                        m_fileOutPutStream = new FileOutputStream(filepath);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    //压缩成png格式存储到本地
//                    //将来可以上传到后台
//                    classLogo.compress(Bitmap.CompressFormat.PNG, 100, m_fileOutPutStream);
//
//                    try {
//                        m_fileOutPutStream.flush();
//                        m_fileOutPutStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    Intent intent = new Intent(TeacherAddClassActivity.this, MyclassFragment.class);
                    //返回MyclassFragment，intent携带Course对象数据返回Fragment刷新显示
                    //此处用Course_foradd简单实现
                    String className = etAddclassClassname.getText().toString();
                    String classId = etAddclassClassid.getText().toString();
                    String classDesc = etAddclassClassdesc.getText().toString();
                    //最终的教师名称是通过用户信息确定，此处先指定一个值
                    String teacherName = "Xiong";
                    addedCourse.setClassLogo(classLogo);
                    addedCourse.setDescript(classDesc);
                    addedCourse.setId(Integer.parseInt(classId));
                    addedCourse.setName(className);
                    addedCourse.setTeacherName(teacherName);
                    int resultCode = 2;
                    intent.putExtra("CourseMsg", addedCourse);
                    setResult(resultCode, intent);
                    finish();
                }
                break;
            case R.id.btn_addclass_takephone:
                //                Toast.makeText(TeacherAddClassActivity.this, "拍照获取图片", Toast.LENGTH_SHORT).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 确保有相机应用
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//判断是否有相机应用
                    // 创建一个存放图片的临时文件
                    File photoFile = null;
                    photoFile = createImageFile();//创建临时图片文件
                    //只有当临时文件成功创建才进行以下操作
                    if (photoFile != null) {
                        //FileProvider 是一个特殊的 ContentProvider 的子类，
                        //它使用 content:// Uri 代替了 file:/// Uri. ，更便利而且安全的为另一个app分享文件
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.android.renly.edu_yunzhi",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }else{
                    Toast.makeText(TeacherAddClassActivity.this,"亲，没有相机应用",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_addclass_fromlocal:
//                Toast.makeText(TeacherAddClassActivity.this, "从本地获取图片", Toast.LENGTH_SHORT).show();
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
                break;
        }
    }

    // 存放图片的临时文件的路径
    String mCurrentPhotoPath;

    //创建图片的临时文件
    private File createImageFile() {
        //创建一个图片文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* 前缀 */
                    ".jpg",         /* 后缀 */
                    storageDir      /* 文件夹 */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                //将拍照所得的图片显示到ivAddclassClassiv上
                if (resultCode != Activity.RESULT_OK) return;
                ivAddclassClassiv.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
                break;
            case IMAGE_CODE:
                //将从图库选择的图片显示到ivAddclassClassiv上
                Bitmap bm = null;
                ContentResolver resolver = getContentResolver();
                try {
                    Uri originalUri = data.getData();
                    bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    ivAddclassClassiv.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bm == null){
                    //防止选择出错或退出操作，给提示
                    Toast.makeText(TeacherAddClassActivity.this,"亲，没有选择成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
