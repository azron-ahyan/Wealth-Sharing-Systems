package com.wealthsharingsystems.app.a7x67;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    WebView mWebView;
    SwipeRefreshLayout swipe;

    ActionBarDrawerToggle toggle;
    AHBottomNavigation bottomNavigation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //isWriteStoragePermissionGranted();


//
//        if (ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)){ ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }else{
//                ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            }
//        }
//        statusCheck();

         bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_2, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Play", R.drawable.play, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Live Updates", R.drawable.update, R.color.colorPrimary);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setCurrentItem(0);


        bottomNavigation.setAccentColor(Color.parseColor("#016DCD"));
        bottomNavigation.setInactiveColor(Color.parseColor("#000000"));



        //bottomNavigation.enableItemAtPosition(0);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Wealth Sharing Systems" + "</font>")));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();







        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(Color.parseColor("#2e2e2e"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch(position) {
                    case 0:
                        //Play voicefile
                        mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=dashboard");
                        break;
                    case 1:
                        //Stop MediaPlayer

                        mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=play");

                        break;
                    case 2:
                        //Stop MediaPlayer

                        mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=tvfeed");


                        break;
                }

                return true;
            }
        });


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    //open drawer
                    drawerLayout.openDrawer(GravityCompat.START);

                }
            }
        });

        mWebView= (WebView) findViewById(R.id.myWebView);





        mWebView.clearCache(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //improve webView performance
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

       // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(110);
        mWebView.loadUrl("https://wealthsharingsystems.com/");
        //force links open in webview only
        //swipe.setRefreshing(true);

//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setUserAgentString(getString(R.string.app_name));
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setAllowContentAccess(true);
//        mWebView.getSettings().supportZoom();
//        mWebView.loadUrl("https://www.malaichaap.com/");

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:


                Log.e("weburl===",url.toString());
                if (url.startsWith("mailto:")) {
                    final Activity activity = Main3Activity.this;
                    if (activity != null) {
                        MailTo mt = MailTo.parse(url);
                        Intent i = newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                        activity.startActivity(i);
                        view.reload();
                        return true;
                    }
                }




                PackageManager pm = getApplicationContext().getPackageManager();

//                if (url.startsWith("whatsapp://")) {
//
//
//                    if(isPackageInstalled("com.whatsapp",pm)){
//
//                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//
//
//                    }else{
//                        // create an intent for Play Store
//
//                        final Uri uri = Uri.parse("market://details?id=com.whatsapp");
//                        final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                        @SuppressWarnings({"NewApi", "deprecation"})
//                        final int newDocumentFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? Intent.FLAG_ACTIVITY_NEW_DOCUMENT : Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
//                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | newDocumentFlag | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//
//                        try {
//                            // open Whatsapp listing in Play Store app
//                            startActivity(goToMarket);
//                        } catch (ActivityNotFoundException ex) {
//                            // open Whatsapp listing in browser
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
//                        }
//
//
//                    }
//                    return true;
//
//                }else


                if (url != null && (url.startsWith("https://www.instagram.com/") || url.startsWith("http://instagram.com/") || url.startsWith("https://instagram.com/"))) {

                    //mWebView.goBack();
                    view.stopLoading();
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    //view.loadUrl("https://www.google.com");

                    Log.e("insta","lassan");
                    return false;

                }else


                if(url.startsWith("https://www.facebook.com/") || url.startsWith("https://m.facebook.com/")){


                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else




                if(url.startsWith("https://www.facebook.com") || url.startsWith("https://m.facebook.com")){

                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else

                if (url != null && (url.startsWith("https://www.facebook.com/") || url.startsWith("https://m.facebook.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://www.pinterest.com/") || url.startsWith("https://pinterest.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://www.youtube.com/") || url.startsWith("https://m.youtube.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://www.linkedin.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://t.me/") || url.startsWith("https://t.me/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if (url != null && (url.startsWith("https://twitter.com/") || url.startsWith("https://www.twitter.com/") || url.startsWith("https://mobile.twitter.com/"))) {
                    view.stopLoading();

                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return false;
                }else
                if((url.contains("com.facebook.orca") || url.startsWith("https://www.messenger.com/t/ParnellApparel") || url.startsWith("http://m.me/ParnellApparel"))){



                    //mWebView.loadUrl("https://m.me/107353697685688");

                    if(isPackageInstalled("com.facebook.orca",pm)){

                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/ParnellApparel")));

////                        Intent intent= new Intent();
////                        intent.setAction(Intent.ACTION_SEND);
////                        intent.putExtra(Intent.EXTRA_TEXT, "Hello");
////                        intent.setType("text/plain");
////                        intent.setPackage("com.facebook.orca");
////                        try
////                        {
////                            startActivity(intent);
////                        }
////                        catch (ActivityNotFoundException ex)
////                        {
////
////                        }
                    }else{


                        // create an intent for Play Store

                        final Uri uri = Uri.parse("market://details?id=com.facebook.orca");
                        final Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        @SuppressWarnings({"NewApi", "deprecation"})
                        final int newDocumentFlag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? Intent.FLAG_ACTIVITY_NEW_DOCUMENT : Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | newDocumentFlag | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                        try {
                            // open Whatsapp listing in Play Store app
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException ex) {
                            // open Whatsapp listing in browser
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.facebook.orca")));
                        }


                    }


                    return true;


                }else
                {
                    view.loadUrl(url);

                    return true;
                }


            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Log.e("error",description);
            }



            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            pd=new ProgressDialog(MainActivity.this);
//            pd.setTitle("Please Wait..");
//            pd.setMessage("Website is Loading..");
//            pd.show();
                swipe.setRefreshing(true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //pd.dismiss();
                swipe.setRefreshing(false);
                super.onPageFinished(view, url);
            }


        });
        mWebView.setWebChromeClient(new ChromeClient());

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                LoadWebPull();
                //LoadWeb();
            }
        });

    }





    private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void LoadWebPull(){




        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(110);
        //mwebView.loadUrl("https://minitask.fun");
        //force links open in webview only
        //swipe.setRefreshing(true);



        String url = mWebView.getUrl();
        mWebView.loadUrl(url);




    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted2");\
                isReadStoragePermissionGranted();
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(Main3Activity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if(mWebView!=null){


                            mWebView.loadUrl("https://wealthsharingsystems.com/");
                            isWriteStoragePermissionGranted();

                        }
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        isReadStoragePermissionGranted();

                    }
                    //startLocationUpdates();
                } else {
                    // Permission Denied
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                    //startLocationUpdates();
                } else {
                    // Permission Denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
    public class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("lllll", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }


        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }


            private View mCustomView;
            private CustomViewCallback mCustomViewCallback;
            protected FrameLayout mFullscreenContainer;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;

            ChromeClient() {}

            public Bitmap getDefaultVideoPoster()
            {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView()
            {
                ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback)
            {
                if (this.mCustomView != null)
                {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }


    }

    //flipscreen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        swipe.setRefreshing(true);
        if (id == R.id.nav_1) {

            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=login");
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.setCurrentItem(0);
        } else if (id == R.id.nav_2) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=dashboard");
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.setCurrentItem(0);

        } else if (id == R.id.nav_3) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=play");
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.setCurrentItem(1);
        }else if (id == R.id.nav_4) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=deposit");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_5) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=accounts");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_6) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=games_won");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_7) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=winners_all");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_9) {
            mWebView.loadUrl("https://wealthsharingsystems.com/affiliates-earn-bitcoins");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_10) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=top_referrals");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_11) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=videos");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_12) {
            mWebView.loadUrl("https://cryptonews.wealthsharingsystems.com/");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_13) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=blog");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_14) {
            mWebView.loadUrl("https://forum.wealthsharingsystems.com/");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_15) {
            mWebView.loadUrl("https://ico.wealthsharingsystems.com/");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_16) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=support");
            bottomNavigation.setVisibility(View.GONE);

        }else if (id == R.id.nav_17) {
            mWebView.loadUrl("https://wealthsharingsystems.com/index.php?page=logout");
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.setCurrentItem(0);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:


                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Close App")
                            .setMessage("Are you sure you want to exit from app?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();

                }
                return true;
        }
    }
        return super.onKeyDown(keyCode, event);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id ==R.id.share){

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wealth Sharing Systems");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.rate) {
//
//
//            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
//            }
//
//            return true;
//        }




        return super.onOptionsItemSelected(item);
    }


}