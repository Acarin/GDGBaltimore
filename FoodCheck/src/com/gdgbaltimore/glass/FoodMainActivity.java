package com.gdgbaltimore.glass;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.*;


import com.gdgbaltimore.glass.R;
import com.gdgbaltimore.glass.Log;
import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import java.util.*;


// The "main" activity...
public class FoodMainActivity extends Activity
{
    // Service to handle liveCard publishing, etc...
    private boolean mIsBound = false;
//    private LiveCardDemoLocalService liveCardDemoLocalService;
    
    public ProgressDialog progress;
    
    private List<Card> mCards;
    private CardScrollView mCardScrollView;
    
   
    // For tap event
	private GestureDetector mGestureDetector;

	
	  private Handler handler = new Handler() {
          @Override
              public void handleMessage(Message msg) {
        	  displayComplete();
          }
      };
	
	private void displayComplete()
	{
		
		final ProgressDialog progressComplete = ProgressDialog.show(this, "Yes. You can.", "",true);
		progressComplete.setIcon(R.drawable.ic_done_50);
		progressComplete.show();
		
		new Thread(new Runnable() {
			  @Override
			  public void run()
			  {
					try
					{
			    		Thread.sleep(2500);
			    		progressComplete.dismiss();
			    		
			    		//Closing the app
			    		finish();
					}
					catch(Exception ex)
					{
						Log.d("displayComplete() Failed. Reason:"+ex.getMessage());
					}
			      }
			}).start();
	}

	
	private void displayProgress(String strDisplayText,int iconId )
	{
		
		final ProgressDialog progress = ProgressDialog.show(this, strDisplayText, strDisplayText,true);
		progress.setIcon(iconId);
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setMax(100);

		
		new Thread(new Runnable() {
			  @Override
			  public void run()
			  {
					try
					{
			    		Thread.sleep(3500);
				        handler.sendEmptyMessage(0);
				        Thread.sleep(500);
				        progress.dismiss();
					}
					catch(Exception ex)
					{
						Log.d("thread of status_card Failed. Reason:"+ex.getMessage());
					}
					
			      }
			}).start();
	}


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("Beta: onCreate() called.");
        

        scan_Codes();
        //takePicture();
        
        /*
        createCards();
        mCardScrollView = new CardScrollView(this);
        ExampleCardScrollAdapter adapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        
        setContentView(mCardScrollView);
        */
       
		// For gesture handling.
		mGestureDetector = createGestureDetector(this);

    }

   
    public void scan_Codes()
    {
    	try
    	{
    		
    		//startActivity(CaptureActivity.newIntent(this));
    		Intent objIntent = new Intent("com.google.zxing.client.android.SCAN");
	    	//objIntent.addCategory(Intent.CATEGORY_DEFAULT);
  	
	    	objIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    	startActivityForResult(objIntent, 0);
    	}
    	catch(Exception ex)
    	{
    		Log.d("scan_codes : Exception. Message :"+ex.getMessage());
    	}
    }


    /* Camera section : Start */
    /*
	private void takePicture() {
		
		try
		{

		    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(intent, 0);
		    setResult(0);
		    sendBroadcast(intent);
		    finish();
		    
		    Log.d("takePicture() is complete...");
		}
		catch ( Exception ex)
		{
			Log.d("takePicture() thrown an exception :"+ex.getMessage());
		}
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d("onActivityResult() is getting called...");
		
	    if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
	        String picturePath = data.getStringExtra(
	                CameraManager.EXTRA_PICTURE_FILE_PATH);
	        processPictureWhenReady(picturePath);
	    }

	    super.onActivityResult(requestCode, resultCode, data);
	} 
	
	
	private void processPictureWhenReady(final String picturePath) {
		
		Log.d("Yayyyyyyyyyy.... Camera is working...");
	
        displayProgress("Activating ONT ...",R.drawable.fios_ont);
	}
	*/
    
    public  void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
    	//A. Display the STB Code and Add a context menu to 'Activate'.
    	
    	try
    	{
    		displayProgress("Validating...",R.drawable.ic_timer_50);
    		
		    Log.d("Inside :onActivityResult");
		    if(resultCode == 0)
		    {
	    	     	//displayProgress("Scan Complete...",R.drawable.menu_icon_record);
	    	     	
	    	    	 String contents = intent.getStringExtra("SCAN_RESULT");
	    	         Log.d("contents :" + contents);
	    	    
	    	         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	    	         Log.d("format :" + format);
	    	         
	    	         
	    	         /*AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
	    	         TextView myMsg = new TextView(this);
	    	         myMsg.setText(contents);
	    	         myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
	    	         popupBuilder.setView(myMsg); */
	    	   }
	    	   else
	    	   {
	    		   Log.d("onActivityResult: ActivityCode 0 is not found. Retruned values is :"+requestCode);
	    	   }
    	}
	    	catch(Exception ex)
	    	{
	    		 Log.d("onActivityResult thrown an error :"+ex.getMessage());
	    	}
    	}


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("Beta: onResume() called.");

        //-- Menu
        // incase if we need to add context menu
        //openOptionsMenu();  --GD
    }

    // Context menus
    // ...

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("onCreateOptionsMenu() called.");

      //-- Menu
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_GDGBalitmore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("onOptionsItemSelected() called.");

      //-- Menu
        /*
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menu_item_record:
                //doStopService(); -- GD
            	displayProgress("Scheduling Basement DVR ...",R.drawable.menu_icon_record);
                return true;
            case R.id.menu_item_watch:
            	displayProgress("Tuning Basement TV...",R.drawable.menu_icon_watch);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        */
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu)
    {
        Log.d("onOptionsMenuClosed() called.");

        // Nothing else to do, closing the activity.
        //finish();
    }
    
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		// Create a base listener for generic gestures
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (Log.D)
					Log.d("gesture = " + gesture);
				/*if(gesture == Gesture.SWIPE_RIGHT)
				{
					scrollLiveCards();
					return true;
				} */
				
				 if (gesture == Gesture.TAP || gesture == Gesture.LONG_PRESS) {
					handleGestureTap();
					return true; 
				} else if (gesture == Gesture.TWO_TAP) {
					handleGestureTap();
					//handleGestureTwoTap();
					return true;
				}
				return false;
			}
		});
		return gestureDetector;
	}

	private void handleGestureTap() {
		Log.d("handleGestureTap() called.");
		//doStopService(); -- GD
		//finish(); -- GD
		openOptionsMenu();
	}
	
	private void scrollLiveCards()
	{
		
	}
	
	private void handleGestureTwoTap() {
		Log.d("handleGestureTwoTap() called.");
	}
	
	//GD: Just leaving these func as it could be used as thi app could evovle.
	  private void createCards() {
	      mCards = new ArrayList<Card>();
	      

	      Card card;

	      card = new Card(this);
	      card.setText("Vegetables");
	      mCards.add(card);
	      
	    }


private class ExampleCardScrollAdapter extends CardScrollAdapter {
		    @Override
		    public int findIdPosition(Object id) {
		        return -1;
		    }
		
		    @Override
		    public int findItemPosition(Object item) {
		        return mCards.indexOf(item);
		    }
		
		    @Override
		    public int getCount() {
		        return mCards.size();
		    }
		
		    @Override
		    public Object getItem(int position) {
		        return mCards.get(position);
		    }
		
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        return mCards.get(position).toView();
		    }
    }

}
