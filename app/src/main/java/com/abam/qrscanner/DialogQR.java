package com.abam.qrscanner;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.text.ClipboardManager;
import android.net.*;
import java.io.*;
import android.provider.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.*;
public class DialogQR extends Dialog
{
	private AppCompatActivity activity;
	private Context context;
	private EditText edittext;
	private Button btn1;
	private String content;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private boolean isLink = false;
	private boolean isPhone = false;
	private boolean isNum = false;
	
	public DialogQR(AppCompatActivity a,String c){
		super(a);
		activity = a;
		context = a.getApplicationContext();
		content = c;
		if(c.startsWith("http://") || c.startsWith("https://") || c.startsWith("file://"))
			isLink = true;
		String regx = "[0-9]+";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(c);
		if(m.matches()) isNum = true;
		if((isNum || c.startsWith("+") && (c.length() >= 10)))
			isPhone = true;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		initView();
		setClickListeners();
		edittext.setText(content);
		getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
	}
	
	public void initView(){
		edittext = findViewById(R.id.dialog_layoutEditText1);
		btn1 = findViewById(R.id.dialog_layoutButton1);
		btn2 = findViewById(R.id.dialog_layoutButton2);
		btn3 = findViewById(R.id.dialog_layoutButton3);
		btn4 = findViewById(R.id.dialog_layoutButton4);
		btn5 = findViewById(R.id.dialog_layoutButton5);
		btn6 = findViewById(R.id.dialog_layoutButton6);
		
		if(!isPhone){
			btn5.setVisibility(View.GONE);
			btn6.setVisibility(View.GONE);
		}
	}
	
	public void setClickListeners(){
		btn1.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// Copy text
				copyToClipboard(content,activity);
			}
		});
		btn2.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// share 
				shareText(content,activity);
			}
		});
		btn3.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// open link
				openLink(content,activity);
			}
		});
		btn4.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// to txt file
				saveTxt(content,activity);
			}
		});
		btn5.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// phone
				makeCall(content,activity);
			}
		});
		btn6.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// add to contact
				saveContact(content,activity);
			}
		});
	}
	
	public static void makeCall(String text,AppCompatActivity a){
		Intent i = new Intent(Intent.ACTION_DIAL);
		i.setData(Uri.parse("tel:"+text));
		a.startActivity(i);
	}
	
	public static void saveContact(String text,AppCompatActivity a){
		Intent i = new Intent(ContactsContract.Intents.Insert.ACTION);
		i.setType(ContactsContract.RawContacts.CONTENT_TYPE);
		i.putExtra(ContactsContract.Intents.Insert.PHONE,text);
		a.startActivity(i);
	}
	
	public static void saveTxt(String text,AppCompatActivity a){
		try{
			File f = new File(Environment.getExternalStorageDirectory()+"/QRScanner/");
			if(!f.exists()) f.mkdirs();
			File ff = new File(f.getAbsolutePath()+"/QR_Code_Content_"+System.currentTimeMillis()+".txt");
			if(!ff.exists()) ff.createNewFile();
			FileOutputStream s = new FileOutputStream(ff);
			s.write(text.getBytes()); 
			s.flush();
			s.close();
		}catch(Exception e){
			Toast.makeText(a,"Cant save file", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void copyToClipboard(String text,AppCompatActivity a){
		ClipboardManager m = (ClipboardManager) a.getSystemService(Context.CLIPBOARD_SERVICE);
		m.setText(text);
	}
	
	public static void shareText(String text,AppCompatActivity a){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT,text);
		a.startActivity(i);
	}
	
	public void openLink(String text,AppCompatActivity a){
		if(!isLink){
			if(text.startsWith("www.") && !text.contains(" ")){
				text = "http://"+text;
			}else{
				String[] i = text.split(" ");
				String k = "https://google.com/search?q=";
				int u = 0;
				for(String j:i){
					u++;
					if(u == i.length){
						k+=j;
					}else{
						k+=j+"+";
					}
				}
				text = k;
			}
		}
		Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(text.split(" ")[0]));
		a.startActivity(i);
	}
}
