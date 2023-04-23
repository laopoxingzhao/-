package com.hu.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hu.R;
import com.hu.utils.MD5Utils;
import com.hu.utils.UtilsHelper;
import com.hu.utils.IdentifyingCode;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_main_title;
    private TextView tv_back, tv_register, tv_find_psw;
    private Button btn_login;

    private ImageView identifyingCode;
    private String userName, psw, spPsw ,realCode,ident_code;
    private EditText et_user_name, et_psw, indent_tv;

    Button xml_switch;

    Integer xml;//判断使用的是哪个布局文件 1为activity_login 2为activity_login_phone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        xml = 1;

        init();
    }
    /**
     * 获取界面控件
     */
    private void init() {
        xml_switch = findViewById(R.id.xml_switch);
        xml_switch.setOnClickListener(this);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("密码登录");
        tv_back = findViewById(R.id.tv_back);
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        indent_tv = findViewById(R.id.identifyingcode_edittext);

        tv_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_find_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        //这里是初始化并设置监听，记得要保存对的验证码，用来验证后面用户输入的对不对
        identifyingCode=(ImageView)findViewById(R.id.identifyingcode_image);
        identifyingCode.setOnClickListener(this);
        identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
        realCode=IdentifyingCode.getInstance().getCode().toLowerCase();
    }

    /**
     * 切换到 activity_login_phone 时的初始化
     */
    private  void init2(){
        xml_switch = findViewById(R.id.xml_switch);
        xml_switch.setOnClickListener(this);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = findViewById(R.id.tv_back);
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);


        tv_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_find_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.xml_switch){
            View layout;
            LayoutInflater inflater = LayoutInflater.from(this);
            if (xml==1){
                layout= inflater.inflate(R.layout.activity_login_phone,null);
                setContentView(layout);
                xml = 2;
                init2();
            }
            else {
                layout = inflater.inflate(R.layout.activity_login,null);
                setContentView(layout);
                init();
                xml = 1;
            }


        }
        if (xml==1)
            switch (view.getId()) {
                case R.id.tv_back:     //"返回"按钮的点击事件
                    this.finish();
                    break;
                case R.id.tv_register: //"立即注册"文本的点击事件
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.tv_find_psw://"找回密码？"文本的点击事件
                    //跳转到找回密码界面
                    Intent findPswIntent = new Intent(LoginActivity.this,FindPswActivity.class);
                    startActivity(findPswIntent);
                    break;
                case R.id.btn_login: //"登录"按钮的点击事件
                    userName=et_user_name.getText().toString().trim();
                    psw=et_psw.getText().toString().trim();
                    ident_code = indent_tv.getText().toString().trim();
                    String md5Psw=MD5Utils.md5(psw);
                    spPsw=UtilsHelper.readPsw(LoginActivity.this,userName);
                    if(TextUtils.isEmpty(userName)){
                        Toast.makeText(LoginActivity.this, "请输入用户名",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(TextUtils.isEmpty(spPsw)){
                        Toast.makeText(LoginActivity.this, "此用户名不存在",
                                Toast.LENGTH_SHORT).show();
                        return;

                    }
                    else if(TextUtils.isEmpty(ident_code)) {
                        Toast.makeText(LoginActivity.this, "验证码为空",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(TextUtils.isEmpty(psw)){
                        Toast.makeText(LoginActivity.this, "请输入密码",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else if((!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
                        Toast.makeText(LoginActivity.this, "输入的密码不正确",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if((!TextUtils.isEmpty(spPsw)&&!realCode.equals(ident_code))){
                        Toast.makeText(LoginActivity.this, "输入的验证码不正确",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(md5Psw.equals(spPsw)){
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        //保存登录状态和登录的用户名
                        UtilsHelper.saveLoginStatus(LoginActivity.this,true,userName);
                        //把登录成功的状态传递到MainActivity中
                        Intent data=new Intent();
                        data.putExtra("isLogin", true);
                        setResult(RESULT_OK, data);
                        LoginActivity.this.finish();
                    }
                    break;
                case R.id.identifyingcode_image:
                    identifyingCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
                    realCode=IdentifyingCode.getInstance().getCode().toLowerCase();
                    break;
            }
        else if (xml==2){
            switch (view.getId()) {
                case R.id.tv_back:     //"返回"按钮的点击事件
                    this.finish();
                    break;
                case R.id.tv_register: //"立即注册"文本的点击事件
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.tv_find_psw://"找回密码？"文本的点击事件
                    //跳转到找回密码界面
                    Intent findPswIntent = new Intent(LoginActivity.this, FindPswActivity.class);
                    startActivity(findPswIntent);
                    break;
                case R.id.btn_login: //"登录"按钮的点击事件
                    break;
            }
        }
    }
    /**
     * 保存登录状态和登录用户名到SharedPreferences文件中
     */
    private void saveLoginStatus(Context context,boolean status,String userName){
        SharedPreferences sp= context.getSharedPreferences("loginInfo",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();      //获取编辑器
        editor.putBoolean("isLogin", status);            //存入boolean类型的登录状态
        editor.putString("loginUserName", userName);   //存入登录时的用户名
        editor.commit();                                     //提交修改
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从注册界面传递过来的用户名
            String userName =data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
