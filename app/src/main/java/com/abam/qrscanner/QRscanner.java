package com.abam.qrscanner;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.content.*;
import android.view.*;
import android.os.*;
import com.google.android.gms.vision.barcode.*;
import com.google.android.gms.vision.*;
import java.io.*;
import android.*;
import android.content.pm.*;
import android.widget.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;
import com.google.zxing.*;
import com.google.zxing.Reader;

public class QRscanner extends Fragment
{
	private AppCompatActivity activity;
	private Context context;
	private View v;
	private SurfaceView surface;
	private BarcodeDetector detector;
	public static String TAG = "QR_SCANNER";
	private CameraSource cameraSource;
	private RelativeLayout linearLayout1;
	private ProgressBar progress;
	private ImageView img;
	private AnimationDrawable anim;
	private boolean recived = false;
	public QRscanner(AppCompatActivity a){
		activity = a;
		context = a.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.qr_scanner,container,false);
		initView();
		hideCam();
		new Thread(new Runnable(){
			@Override public void run(){
				initBarcodeDetector();
			}
		}).start();
		return v;
	}
	
	public void fromBitmap(Bitmap bm){
		
		Reader reader = new MultiFormatReader();
		
	}
	public void setAnimation(){
		img.setBackgroundResource(R.anim.scan_anim);
		anim = (AnimationDrawable) img.getBackground();
		anim.start();
	}
	
	public void showCam(){
		linearLayout1.setVisibility(View.VISIBLE);
		progress.setVisibility(View.GONE);
	}
	
	public void hideCam(){
		linearLayout1.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}
	
	public void initView(){
		surface = v.findViewById(R.id.qr_scannerSurfaceView1);
		linearLayout1 = v.findViewById(R.id.qr_scannerLinearLayout1);
		progress = v.findViewById(R.id.qr_scannerProgressBar1);
		img = v.findViewById(R.id.qr_scannerImageView1);
	}
	
	public void initBarcodeDetector(){
		detector = new BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.ALL_FORMATS).build();
		cameraSource = new CameraSource.Builder(activity,detector).setRequestedPreviewSize(310,310).setAutoFocusEnabled(true).build();
		surface.getHolder().addCallback(new SurfaceHolder.Callback(){
			@Override public void surfaceCreated(SurfaceHolder holder){
				try{
					if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {  
						cameraSource.start(surface.getHolder());  
					} else {  
						ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 6099);  
					} 
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}
			@Override public void surfaceChanged(SurfaceHolder holder,int p1,int p2,int p3){
				
			}
			@Override public void surfaceDestroyed(SurfaceHolder holder){
				cameraSource.stop();
			}
		});
		detector.setProcessor(new Detector.Processor<Barcode>(){
				@Override  
				public void release() {
					activity.runOnUiThread(new Runnable(){
						@Override public void run(){
							Toast.makeText(activity.getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
						}
					});
				}  

				@Override  
				public void receiveDetections(Detector.Detections<Barcode> detections) {  
					final SparseArray<Barcode> barcodes = detections.getDetectedItems();  
					if (barcodes.size() != 0 && !recived) {  
						activity.runOnUiThread(new Runnable(){
							@Override public void run(){
								//hideCam();
								String data = barcodes.valueAt(0).displayValue;
								DialogQR dialog = new DialogQR(activity,data);
								dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
									@Override public void onDismiss(DialogInterface in){
										try{
											cameraSource.start();
											recived = false;
										}catch(IOException e){
											Log.e(TAG,"Error in restatring camera source. Caused by("+e.toString()+")");
										}
									}
								});
								dialog.show();
							}
						});
						recived = true;
					}
				}
		});
		activity.runOnUiThread(new Runnable(){
			@Override public void run(){
				showCam();
				setAnimation();
			}
		});
	}
	
	public void removeCamera(){
		if(detector != null) detector = null;
		if(cameraSource != null) cameraSource.stop();
	}
	
	@Override
	public void onPause()
	{
		hideCam();
		removeCamera();
		super.onPause();
	}

	@Override
	public void onResume()
	{
		showCam();
		if(detector == null)
		initBarcodeDetector();
		super.onResume();
	}
	
}
