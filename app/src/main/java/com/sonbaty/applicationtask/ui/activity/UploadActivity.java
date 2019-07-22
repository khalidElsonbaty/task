package com.sonbaty.applicationtask.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.sonbaty.applicationtask.R;
import com.sonbaty.applicationtask.data.api.ApiServices;
import com.sonbaty.applicationtask.data.model.storeValue.StoreValue;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sonbaty.applicationtask.data.api.RetrofitClient.getRetrofitClient;
import static com.sonbaty.applicationtask.utils.Constant.IMAGE;
import static com.sonbaty.applicationtask.utils.Constant.OK;
import static com.sonbaty.applicationtask.utils.Constant.VALUE;
import static com.sonbaty.applicationtask.utils.Constant.VIDEO;
import static com.sonbaty.applicationtask.utils.InternetState.isConnected;
import static com.sonbaty.applicationtask.utils.Utils.convertFileToMultipart;
import static com.sonbaty.applicationtask.utils.Utils.convertToRequestBody;
import static com.sonbaty.applicationtask.utils.Utils.disappearKeypad;
import static com.sonbaty.applicationtask.utils.Utils.openAlbumImage;
import static com.sonbaty.applicationtask.utils.Utils.openAlbumVideo;
import static com.sonbaty.applicationtask.utils.Validation.setEmailValidation;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.upload_editText_email)
    EditText uploadEditTextEmail;
    @BindView(R.id.upload_imageView_view)
    ImageView uploadImageViewView;
    @BindView(R.id.upload_videoView_view)
    VideoView uploadVideoViewView;
    @BindView(R.id.upload_relativeLayout_progressBar_view)
    RelativeLayout uploadRelativeLayoutProgressBarView;

    private ArrayList<AlbumFile> file = new ArrayList<>();
    private ApiServices apiServices;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        apiServices = getRetrofitClient().create(ApiServices.class);
    }

    @OnClick({R.id.upload_button_image, R.id.upload_button_video, R.id.upload_button_upload
            , R.id.upload_button_show, R.id.upload_relativeLayout_progressBar_view, R.id.upload_relativeLayout_sub_view})
    public void onViewClicked(View view) {

        disappearKeypad(this, view);

        switch (view.getId()) {
            case R.id.upload_button_image:

                //to add image to new value
                addImage();

                break;
            case R.id.upload_button_video:

                //to add video to new value
                addVideo();

                break;
            case R.id.upload_button_upload:

                //to send retrofit request to server to add new value
                addNewValueToServer();

                break;
            case R.id.upload_button_show:

                //to open Activity show All Values
                Intent intent = new Intent(UploadActivity.this, ShowActivity.class);
                startActivity(intent);
                break;
            case R.id.upload_relativeLayout_progressBar_view:

                //to DisEnabled Ui When Request Done

                break;
        }
    }

    private void addImage() {
        uploadImageViewView.setVisibility(View.VISIBLE);
        uploadVideoViewView.setVisibility(View.GONE);
        Action<ArrayList<AlbumFile>> imageAction = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                file.clear();
                file.addAll(result);
                type = IMAGE;
                Glide.with(UploadActivity.this).load(file.get(0).getPath())
                        .into(uploadImageViewView);
            }
        };
        openAlbumImage(this, imageAction);
    }

    private void addVideo() {
        uploadImageViewView.setVisibility(View.GONE);
        uploadVideoViewView.setVisibility(View.VISIBLE);

        Action<ArrayList<AlbumFile>> videoAction = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                file.clear();
                file.addAll(result);
                type = VIDEO;
                uploadVideoViewView.setVideoPath(file.get(0).getPath());
                uploadVideoViewView.setMediaController(new MediaController(UploadActivity.this));
                uploadVideoViewView.requestFocus();
                uploadVideoViewView.start();
            }
        };
        openAlbumVideo(this, videoAction);
    }

    private void addNewValueToServer() {

        if (!setEmailValidation(this, uploadEditTextEmail)) {

            return;
        }

        if (file.size() == 0) {
            Toast.makeText(this, getString(R.string.file_error_message), Toast.LENGTH_SHORT).show();
            return;
        }


        if (isConnected(this)) {

            uploadRelativeLayoutProgressBarView.setVisibility(View.VISIBLE);

            RequestBody email = convertToRequestBody(uploadEditTextEmail.getText().toString());
            RequestBody type = convertToRequestBody(this.type);
            MultipartBody.Part value = convertFileToMultipart(file.get(0).getPath(), VALUE);

            apiServices.addValueItem(email, type, value).enqueue(new Callback<StoreValue>() {
                public void onResponse(Call<StoreValue> call, Response<StoreValue> response) {
                    try {

                        uploadRelativeLayoutProgressBarView.setVisibility(View.GONE);

                        if (response.body().getStatus().equals(OK)) {
                            Toast.makeText(UploadActivity.this,getString(R.string.response_upload_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UploadActivity.this,getString(R.string.response_upload_failed), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        Toast.makeText(UploadActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<StoreValue> call, Throwable t) {
                    Toast.makeText(UploadActivity.this,t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    try {
                        uploadRelativeLayoutProgressBarView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(UploadActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } else {
            Toast.makeText(UploadActivity.this,getString(R.string.upload_connection_message), Toast.LENGTH_SHORT).show();
        }
    }

}
