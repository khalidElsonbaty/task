package com.sonbaty.applicationtask.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sonbaty.applicationtask.R;
import com.sonbaty.applicationtask.adapter.ValuesAdapter;
import com.sonbaty.applicationtask.data.api.ApiServices;
import com.sonbaty.applicationtask.data.model.getValues.GetValues;
import com.sonbaty.applicationtask.data.model.getValues.GetValuesData;
import com.sonbaty.applicationtask.data.model.storeValue.StoreValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sonbaty.applicationtask.data.api.RetrofitClient.getRetrofitClient;
import static com.sonbaty.applicationtask.utils.Constant.OK;
import static com.sonbaty.applicationtask.utils.Constant.VALUE;
import static com.sonbaty.applicationtask.utils.InternetState.isConnected;
import static com.sonbaty.applicationtask.utils.Utils.convertFileToMultipart;
import static com.sonbaty.applicationtask.utils.Utils.convertToRequestBody;
import static com.sonbaty.applicationtask.utils.Utils.disappearKeypad;
import static com.sonbaty.applicationtask.utils.Validation.setEmailValidation;

public class ShowActivity extends AppCompatActivity {

    @BindView(R.id.show_editText_email)
    EditText showEditTextEmail;
    @BindView(R.id.Show_RecyclerView_Items)
    RecyclerView ShowRecyclerViewItems;
    @BindView(R.id.show_relativeLayout_progressBar_view)
    RelativeLayout showRelativeLayoutProgressBarView;

    private LinearLayoutManager linearLayoutManager;
    private ValuesAdapter valuesAdapter;
    private List<GetValuesData> valuesDataList = new ArrayList<>();
    private ApiServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);

        apiServices = getRetrofitClient().create(ApiServices.class);
        initRecyclerView();
    }

    private void initRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(this);
        ShowRecyclerViewItems.setLayoutManager(linearLayoutManager);
        ShowRecyclerViewItems.setNestedScrollingEnabled(true);

        valuesAdapter = new ValuesAdapter(this, valuesDataList);
        ShowRecyclerViewItems.setAdapter(valuesAdapter);

    }

    @OnClick({R.id.show_button_show, R.id.show_relativeLayout_progressBar_view, R.id.show_relativeLayout_sub_view})
    public void onViewClicked(View view) {

        disappearKeypad(this, view);

        switch (view.getId()) {
            case R.id.show_button_show:

                getAllValues();

                break;
            case R.id.show_relativeLayout_progressBar_view:

                //to DisEnabled Ui When Request Done

                break;
        }
    }

    private void getAllValues() {
        if (!setEmailValidation(this, showEditTextEmail)) {
            Toast.makeText(ShowActivity.this,getString(R.string.Show_Email_error), Toast.LENGTH_SHORT).show();
            return;
        }


        if (isConnected(this)) {

            showRelativeLayoutProgressBarView.setVisibility(View.VISIBLE);

            String email = showEditTextEmail.getText().toString();

            apiServices.getAllValues(email).enqueue(new Callback<GetValues>() {
                public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                    try {

                        showRelativeLayoutProgressBarView.setVisibility(View.GONE);

                        if (response.body().getStatus().equals(OK)) {

                            valuesDataList.addAll(response.body().getData());
                            valuesAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(ShowActivity.this,getString(R.string.show_error_message), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        Toast.makeText(ShowActivity.this, e.getLocalizedMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetValues> call, Throwable t) {
                    Toast.makeText(ShowActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    try {
                        showRelativeLayoutProgressBarView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(ShowActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(ShowActivity.this,getString(R.string.upload_connection_message), Toast.LENGTH_SHORT).show();

        }
    }
}
