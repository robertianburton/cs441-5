package com.example.cs441_5;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
import com.example.cs441_5.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ItemFragment.OnListFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    private static final int CONTENT_VIEW_ID = 1010;

    //for float rounding precision
    private static final String FLOAT_ROUND = "";
    private TextView mTextMessage;

    //temp, example for future implementation
    private TextView xText, yText, zText;
    private Button tarButton;
    //private Sensor mySensor;
    private SensorManager SM;
    float[] mGravs = new float[3];
    float[] mGeoMags = new float[3];
    float[] mRotationM = new float[16];
    float[] mInclinationM = new float[16];
    float[] mOrientation = new float[3];
    float[] mOldOreintation = new float[3];
    String[] mAccelerometer =  new String[3];
    String[] mMagnetic =  new String[3];
    String[] mRotation =  new String[16];
    String[] mInclination =  new String[16];
    String[] mOrientationString =  new String[3];
    String[] mOldOreintationString =  new String[3];

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //build SensorManager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Sensors
        //mySensor = SM.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        //Register sensor listener
        //SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button tar = (Button)findViewById(R.id.tarButton);

        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }


        /* Get the Sensors */
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, mGravs, 0, 3);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, mGeoMags, 0, 3);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    System.arraycopy(event.values, 0, mOldOreintation, 0, 3);
                    break;

                default:
                    return;
            }

            // If mGravs and mGeoMags have values then find rotation matrix
            if (mGravs != null && mGeoMags != null) {

                // checks that the rotation matrix is found
                boolean success = SensorManager.getRotationMatrix(mRotationM, mInclinationM, mGravs, mGeoMags);
                if (success) {
                    /* getOrientation Values */
                    SensorManager.getOrientation(mRotationM, mOrientation);

                    for(int i=0; i<=2; i++){
                        mAccelerometer[i] = Float.toString(mGravs[i]);
                        mMagnetic[i] = Float.toString(mGeoMags[i]);
                        mOrientationString[i] = String.format("%.2f",mOrientation[i]);
                        mOldOreintationString[i] = Float.toString(mOldOreintation[i]);
                    }

                    /* Make everything text to show on device */
                    /*
                    TextView xaxisAccelerometerText = (TextView)findViewById(R.id.xaxisAccelerometer);
                    xaxisAccelerometerText.setText(mAccelerometer[0]);
                    TextView yaxisAccelerometerText = (TextView)findViewById(R.id.yaxisAccelerometer);
                    yaxisAccelerometerText.setText(mAccelerometer[1]);
                    TextView zaxisAccelerometerText = (TextView)findViewById(R.id.zaxisAccelerometer);
                    zaxisAccelerometerText.setText(mAccelerometer[2]);
                    TextView xaxisMagneticText = (TextView)findViewById(R.id.xaxisMagnetic);
                    xaxisMagneticText.setText(mMagnetic[0]);
                    TextView yaxisMagneticText = (TextView)findViewById(R.id.yaxisMagnetic);
                    yaxisMagneticText.setText(mMagnetic[1]);
                    TextView zaxisMagneticText = (TextView)findViewById(R.id.zaxisMagnetic);
                    zaxisMagneticText.setText(mMagnetic[2]);
                    */
                    TextView xaxisOrientationText = (TextView)findViewById(R.id.xText);
                    xaxisOrientationText.setText("X coord: ".concat(mOrientationString[0]));
                    TextView yaxisOrientationText = (TextView)findViewById(R.id.yText);
                    yaxisOrientationText.setText("Y coord: ".concat(mOrientationString[1]));
                    TextView zaxisOrientationText = (TextView)findViewById(R.id.zText);
                    zaxisOrientationText.setText("Z coord: ".concat(mOrientationString[2]));;
                    /*
                    TextView xaxisOldOrientationText = (TextView)findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText(mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView)findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText(mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView)findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText(mOldOreintationString[2]);
                    */

                }else{

                    /* Make everything text to show on device even if getRotationMatrix fails*/
                    String matrixFailed = "Rotation Matrix Failed";
                    /*
                    TextView xaxisAccelerometerText = (TextView)findViewById(R.id.xaxisAccelerometer);
                    xaxisAccelerometerText.setText(mAccelerometer[0]);
                    TextView yaxisAccelerometerText = (TextView)findViewById(R.id.yaxisAccelerometer);
                    yaxisAccelerometerText.setText(mAccelerometer[1]);
                    TextView zaxisAccelerometerText = (TextView)findViewById(R.id.zaxisAccelerometer);
                    zaxisAccelerometerText.setText(mAccelerometer[2]);
                    TextView xaxisMagneticText = (TextView)findViewById(R.id.xaxisMagnetic);
                    xaxisMagneticText.setText(mMagnetic[0]);
                    TextView yaxisMagneticText = (TextView)findViewById(R.id.yaxisMagnetic);
                    yaxisMagneticText.setText(mMagnetic[1]);
                    TextView zaxisMagneticText = (TextView)findViewById(R.id.zaxisMagnetic);
                    zaxisMagneticText.setText(mMagnetic[2]);
                    */
                    TextView xaxisOrientationText = (TextView)findViewById(R.id.xText);
                    xaxisOrientationText.setText(matrixFailed);
                    TextView yaxisOrientationText = (TextView)findViewById(R.id.yText);
                    yaxisOrientationText.setText(matrixFailed);
                    TextView zaxisOrientationText = (TextView)findViewById(R.id.zText);
                    zaxisOrientationText.setText(matrixFailed);
                    /*
                    TextView xaxisOldOrientationText = (TextView)findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText(mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView)findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText(mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView)findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText(mOldOreintationString[2]);
                    */



                }
            }


        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onPause() {
        super.onPause();
        SM.unregisterListener(sensorEventListener);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item){

    }

}
