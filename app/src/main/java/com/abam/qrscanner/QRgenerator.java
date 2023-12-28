package com.abam.qrscanner;

import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.support.v7.app.*;
import android.content.*;
import android.graphics.*;
import androidmads.library.qrgenearator.*;
import com.google.zxing.*;
import android.util.*;
import android.text.*;
import android.app.Dialog;
import java.io.*;
import android.net.*;
import android.graphics.pdf.*;
import android.support.v4.content.*;
public class QRgenerator extends Fragment
{
	private View v;
	private LinearLayout linearLayout1;
	private EditText editText1;
	private Button btn2;
	private AppCompatActivity activity;
	private Context context;
	public static String TAG = "QR_GENERATOR";
	private int pdf_width = 792;
	private int pdf_height = 1120;
	
	public QRgenerator(AppCompatActivity a){
		activity = a;
		context = a.getApplicationContext();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.qr_generator,container,false);
		initViews();
		setClickListener();
		return v;
	}
	
	
	
	public static Bitmap makeQR(String text,final ImageView view,AppCompatActivity activity){
		WindowManager manager = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE); 
		Display display = manager.getDefaultDisplay(); 
		Point point = new Point(); 
		display.getSize(point);
		int width = point.x; 
		int height = point.y; 
		int dimen = width < height ? width : height; 
		dimen = dimen * 3 / 4; 
		QRGEncoder qrEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, dimen); 
		try { 
			final Bitmap bitmap = qrEncoder.encodeAsBitmap(); 
			activity.runOnUiThread(new Runnable(){
				@Override public void run(){
					view.setImageBitmap(bitmap);
				}
			});
			return bitmap;
		} catch (WriterException e) {
			Log.e("Tag", e.toString()); 
		}
		return null;
	}
	
	public void makeSavedDialog(String title,String content){
		final Dialog dialog = new Dialog(activity);
		dialog.setContentView(R.layout.saved_layout);
		TextView con = dialog.findViewById(R.id.dialogMessageContent);
		TextView tit = dialog.findViewById(R.id.dialogMessageHeading);
		Button bt1 = dialog.findViewById(R.id.dialogMessageButton2);
		con.setText(content);
		tit.setText(title);
		bt1.setOnClickListener(new OnClickListener(){
			@Override public void onClick(View v){
				dialog.dismiss();
			}
		});
		dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.show();
	}
	
	public void showDialog(final String text){
		if(text.length() > 4295){
			Toast.makeText(context,"Too much content maximum characters alowed is 4296",Toast.LENGTH_LONG).show();
			return;
		}
		Dialog di = new Dialog(activity);
		di.setContentView(R.layout.maked_qr);
		di.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		final ImageView v = di.findViewById(R.id.maked_qrImageView1);
		Button btn_1 = di.findViewById(R.id.maked_qrButton1);
		Button btn_2 = di.findViewById(R.id.maked_qrButton2);
		Button btn_3 = di.findViewById(R.id.maked_qrButton3);
		final ProgressBar pro = di.findViewById(R.id.maked_qrProgressBar1);
		pro.setVisibility(View.VISIBLE);
		final Bitmap bb = makeQR(text,v,activity);
		btn_1.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				File f = saveBitmap(bb);
				if(f == null){
					Toast.makeText(context,"Cant save file.",12).show();
				}else{
					makeSavedDialog("File Saved!","Image file is saved. Path: "+f.getAbsolutePath());
				}
			}
		});
		btn_2.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				File f = saveBitmap(bb);
				if(f != null){
					makeSavedDialog("File Saved!","File saved to "+f.getAbsolutePath());
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("image/jpeg");
					i.putExtra(Intent.EXTRA_TITLE,"QR Code Scanned by QR Scanner (Made by Aswanth Vc).\nScan QR code Barcode etc. and make QR code using QR Scanner Android App \nDownload QR Scanner and maker :- https://abam.herokuapp.com/projects/QRScanner\nSend feedback :- https://abam.herokuapp.com/feedback");
					i.putExtra(Intent.EXTRA_TEXT,"QR Code Scanned by QR Scanner (Made by Aswanth Vc).\nScan QR code Barcode etc. and make QR code using QR Scanner Android App \nDownload QR Scanner and maker :- https://abam.herokuapp.com/projects/QRScanner\nSend feedback :- https://abam.herokuapp.com/feedback");
					i.putExtra(Intent.EXTRA_SUBJECT,"QR Scanner App by Aswanth Vc");
					i.putExtra(Intent.EXTRA_STREAM,Uri.parse(f.getAbsolutePath()));
					startActivity(i);
				}else{
					Toast.makeText(context,"Cant share file",Toast.LENGTH_LONG).show();
				}
			}
		});
		btn_3.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				File f = createPdf(bb);
				if(f == null){
					Toast.makeText(context,"Cant save pdf",12).show();
				}else{
					makeSavedDialog("File saved!","PDF file saved successfully. Path: "+f.getAbsolutePath());
				}
			}
		});
		pro.setVisibility(View.GONE);
		di.show();
	}
	
	public File createPdf(Bitmap bm){
		PdfDocument document = new PdfDocument();
		Paint paint = new Paint();
		Paint title = new Paint();
		PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(pdf_width,pdf_height,1).create();
		PdfDocument.Page page = document.startPage(info);
		Canvas canvas = page.getCanvas();
		canvas.drawBitmap(bm,(pdf_width/2)-(bm.getWidth()/2),(pdf_height/2)-(bm.getHeight()/2),paint);
		canvas.drawText("Created Using QR Scanner (Made By Aswanth Vc). Download from here - https://abam.herokuapp.com/android/QRScanner",50,pdf_height-50,title);
		title.setColor(ContextCompat.getColor(context,R.color.colorDark));
		title.setTextSize(15);
		title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
		document.finishPage(page);
		File f = new File(Environment.getExternalStorageDirectory()+"/QRScanner/");
		if(!f.exists()) f.mkdirs();
		File ff = new File(f.getAbsolutePath()+"/QR_Code_Pdf_"+System.currentTimeMillis()+".pdf");
		try{
			document.writeTo(new FileOutputStream(ff));
			return ff;
		}catch(Exception e){
			return null;
		}
	}
	
	public File saveBitmap(Bitmap bb){
		File ff = new File(Environment.getExternalStorageDirectory()+"/QRScanner/");
		if(!ff.exists()) ff.mkdirs();
		File ff2 = new File(ff.getAbsolutePath()+"/QRCode_"+System.currentTimeMillis()+".jpg");
		try{
			ff2.createNewFile();
			FileOutputStream stream = new FileOutputStream(ff2);
			bb.compress(Bitmap.CompressFormat.JPEG,100,stream);
			stream.flush();
			stream.close();
			return ff2;
		}catch(Exception e){
			Log.e(TAG,"Cant save file. Caused by("+e.toString()+")");
			return null;
		}
	}
	
	public void initViews(){
		btn2 = v.findViewById(R.id.qr_generatorButton2);
		linearLayout1 = v.findViewById(R.id.qr_generatorLinearLayout1);
		editText1 = v.findViewById(R.id.qr_generatorEditText1);
	}
	
	public void setClickListener(){
		btn2.setOnClickListener(new OnClickListener(){
			@Override public void onClick(View v){
				
				String text = editText1.getText().toString();
				if(!TextUtils.isEmpty(text)){
					showDialog(text);
				}else{
					Toast.makeText(context,"Content is empty",12).show();
				}
			}
		});
	}
}
