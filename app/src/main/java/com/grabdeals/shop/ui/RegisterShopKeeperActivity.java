package com.grabdeals.shop.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.grabdeals.shop.R;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkImageViewRounded;
import com.grabdeals.shop.util.NetworkManager;
import com.grabdeals.shop.util.VolleyCallbackListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class RegisterShopKeeperActivity extends BaseAppCompatActivity implements View.OnClickListener,VolleyCallbackListener{

    private static final int TAKE_PICTURE = 100;
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FILE = 103;
    private final String TAG = "RegisterShopKeeperActivity";

    private ScrollView mCreateNewAcForm;
    private LinearLayout mLlParentCreateAcc;
    private NetworkImageViewRounded mImage;
    private ImageView mImageCamera;
    private EditText mPhoneNo;
    private EditText mShopName;
    private EditText mPassword;
    private Button mBtnCreateAcc;
    private Button mBtnLogin;

    final CharSequence[] items = { "Take Photo", "Choose from Library",
            "Cancel" };

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-29 18:26:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop_keeper);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        findViews();


    }

    private void findViews() {
        mCreateNewAcForm = (ScrollView)findViewById( R.id.create_new_ac_form );
        mLlParentCreateAcc = (LinearLayout)findViewById( R.id.ll_parent_create_acc );
        mImage = (NetworkImageViewRounded)findViewById( R.id.image );
        mImageCamera = (ImageView) findViewById( R.id.iv_camera );

        mPhoneNo = (EditText)findViewById( R.id.phone_no );
        mShopName = (EditText)findViewById( R.id.shop_name );
        mPassword = (EditText)findViewById( R.id.password );
        mBtnCreateAcc = (Button)findViewById( R.id.btn_create_acc );
        mBtnLogin = (Button)findViewById( R.id.btn_login );

        mBtnCreateAcc.setOnClickListener( this );
        mBtnLogin.setOnClickListener( this );
        mImageCamera.setOnClickListener( this );

        mImage.setDefaultImageResId(R.drawable.default_user);

    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-10-29 18:26:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == mBtnCreateAcc ) {
            // Handle clicks for mBtnCreateAcc
            NetworkManager.getInstance().postRequest(Constants.COMMAND_REGISTER_SHOPKEEPER,preparePostParams(),this);
        } else if ( v == mBtnLogin ) {
            // Handle clicks for mBtnLogin
        }else if (v == mImageCamera){
            selectImage();
        }
    }

    private HashMap<String,String> preparePostParams(){
        HashMap<String, String> jsonParams = new HashMap<>();
        jsonParams.put("param1",mPhoneNo.getText().toString());
        jsonParams.put("param2",mShopName.getText().toString());
        jsonParams.put("param3",mPassword.getText().toString());

        return jsonParams;
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    mImage.setLocalImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                mImage.setLocalImageBitmap(bm);

            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void getResult(Object object) {
        if (object != null) {
            JSONObject jsonObject = (JSONObject) object;
            startActivity(new Intent(this,ConfirmOTPActivity.class));

        }
    }

    @Override
    public void getErrorResult(Object object) {

    }
}
