package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ExpenseEditActivity extends AppCompatActivity {
    private static final String UUID_EXTRA = "com.kopyl.financeogranizer.uuid_extra";
    private static final int REQUEST_PHOTO = 1;
    private static final String URI_STR = "com.kopyl.financeorganaizer.fileprovider";
    private Expense mExpense;

    private EditText mEditName;
    private EditText mEditCost;
    private Button mApplyButton;
    private CheckBox mComingBox;
    private boolean isEdit;
    private ImageButton mEditPhotoButton;
    private File mPhotoFile;
    private ImageView mEditImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_edit);

        String uuid = getIntent().getStringExtra(UUID_EXTRA);

        if(!uuid.equals(""))
        {
            isEdit = true;
            mExpense = ExpenseLab.get(this).getExpense(UUID.fromString(uuid));
        }
        else
        {
            isEdit = false;
            mExpense = new Expense();
        }

        mPhotoFile = ExpenseLab.get(this).getFileDir(mExpense);

        mEditCost = findViewById(R.id.editCost);
        mEditName = findViewById(R.id.editName);
        mApplyButton = findViewById(R.id.editApplyButton);
        mComingBox = findViewById(R.id.coming);
        mEditPhotoButton = findViewById(R.id.editImagePhotoButton);
        mEditImageView = findViewById(R.id.editImage);

        mComingBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) mComingBox.setTextColor(getResources().getColor(R.color.colorGreen, getTheme()));
                else mComingBox.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
            }
        });


        if(isEdit) bind();

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mExpense.setCost(Double.valueOf(mEditCost.getText().toString()));
                    mExpense.setName(mEditName.getText().toString());
                    mExpense.setComing(mComingBox.isChecked());
                }
                catch (Exception ex) {
                    Toast.makeText(ExpenseEditActivity.this, "Enter the correct values", Toast.LENGTH_SHORT).show();
                }
                if(isEdit)
                {
                    ExpenseLab.get(ExpenseEditActivity.this).updateExpense(mExpense);
                }
                else
                {
                    ExpenseLab.get(ExpenseEditActivity.this).addExpense(mExpense);
                }
                finish();
            }
        });

        mEditPhotoButton.setOnClickListener(p -> {
            Context context = ExpenseEditActivity.this;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(intent.resolveActivity(context.getPackageManager()) != null) {

                Uri uri = FileProvider.getUriForFile(context, URI_STR, mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = ExpenseEditActivity.this
                        .getPackageManager().queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity: cameraActivities) {
                    context.grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
    }
    public static Intent newIntent(Context context, UUID uuid)
    {
        Intent intent = new Intent(context, ExpenseEditActivity.class);
        if(uuid == null)
            intent.putExtra(UUID_EXTRA, "");
        else
            intent.putExtra(UUID_EXTRA, uuid.toString());
        return intent;
    }

    private void bind()
    {
        mEditName.setText( mExpense.getName() );
        mEditCost.setText( Double.toString(mExpense.getCost()) );
        mComingBox.setChecked(mExpense.isComing());

        loadImage();


    }
    public void loadImage(){
        Picasso.with(this)
                .load(mPhotoFile)
                .fit()
                .centerCrop()
                .into(mEditImageView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case REQUEST_PHOTO:
                    Uri uri = FileProvider.getUriForFile(this, URI_STR, mPhotoFile);
                    this.revokeUriPermission(uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    loadImage();
                    break;
            }

        }
    }
}
