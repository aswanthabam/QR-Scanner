package com.abam.qrscanner;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.io.InputStream;
import android.app.*;
import android.widget.*;
import android.text.method.*;
import android.view.View.*;
import android.view.*;

public class DebugActivity extends Activity {

	String[] exceptionType = {
			"",
			"",
			"",
			"",
			""

	};

	String[] errMessage= new String[]{"","","","",""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



		Intent intent = getIntent();
		String errMsg = "";
		String madeErrMsg = "";
		if(intent != null){
			errMsg = intent.getStringExtra("error");

			String[] spilt = errMsg.split("\n");
			//errMsg = spilt[0];
			try {
				for (int j = 0; j < exceptionType.length; j++) {
					if (spilt[0].contains(exceptionType[j])) {
						madeErrMsg = errMessage[j];

						int addIndex = spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();

						madeErrMsg += spilt[0].substring(addIndex, spilt[0].length());
						break;

					}
				}

				if(madeErrMsg.isEmpty()) madeErrMsg = errMsg;
			}catch(Exception e){}

		}

        Dialog di = new Dialog(this);
		di.setContentView(R.layout.debug_layout);
		TextView title = di.findViewById(R.id.debug_layoutTextView1);
		TextView content = di.findViewById(R.id.debug_layoutTextView2);
		Button btn = di.findViewById(R.id.debug_layoutButton1);
		title.setText("Oops something went wrong");
		content.setText(R.string.debug_error);
		content.setMovementMethod(LinkMovementMethod.getInstance());
		btn.setOnClickListener(new OnClickListener(){
			@Override public void onClick(View v){
				finish();
			}
		});
		di.setOnDismissListener(new DialogInterface.OnDismissListener(){
			@Override public void onDismiss(DialogInterface i){
				finish();
			}
		});
		di.show();
    }
}
