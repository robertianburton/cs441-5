package com.example.cs441_5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mabboud.android_tone_player.ContinuousBuzzer;

import static android.content.Context.SENSOR_SERVICE;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //tonePLayer
    ContinuousBuzzer tonePlayer = new ContinuousBuzzer();
    int pitchHerz = 440;


    // coord values stuff
    private TextView xText, yText, zText;
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

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

                    //
                    pitchHerz = Math.round(1000 * mOrientation[1]);

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
                    TextView xaxisOrientationText = (TextView)getView().findViewById(R.id.xText);
                    xaxisOrientationText.setText("X coord: ".concat(mOrientationString[0]));
                    TextView yaxisOrientationText = (TextView)getView().findViewById(R.id.yText);
                    yaxisOrientationText.setText("Y coord: ".concat(mOrientationString[1]));
                    TextView zaxisOrientationText = (TextView)getView().findViewById(R.id.zText);
                    zaxisOrientationText.setText("Z coord: ".concat(mOrientationString[2]));;
                    /*
                    TextView xaxisOldOrientationText = (TextView)getView().findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText(mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView)getView().findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText(mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView)getView().findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText(mOldOreintationString[2]);
                    */

                }else{

                    /* Make everything text to show on device even if getRotationMatrix fails*/
                    String matrixFailed = "Rotation Matrix Failed";
                    /*
                    TextView xaxisAccelerometerText = (TextView)getView().findViewById(R.id.xaxisAccelerometer);
                    xaxisAccelerometerText.setText(mAccelerometer[0]);
                    TextView yaxisAccelerometerText = (TextView)getView().findViewById(R.id.yaxisAccelerometer);
                    yaxisAccelerometerText.setText(mAccelerometer[1]);
                    TextView zaxisAccelerometerText = (TextView)getView().findViewById(R.id.zaxisAccelerometer);
                    zaxisAccelerometerText.setText(mAccelerometer[2]);
                    TextView xaxisMagneticText = (TextView)getView().findViewById(R.id.xaxisMagnetic);
                    xaxisMagneticText.setText(mMagnetic[0]);
                    TextView yaxisMagneticText = (TextView)getView().findViewById(R.id.yaxisMagnetic);
                    yaxisMagneticText.setText(mMagnetic[1]);
                    TextView zaxisMagneticText = (TextView)getView().findViewById(R.id.zaxisMagnetic);
                    zaxisMagneticText.setText(mMagnetic[2]);
                    */
                    TextView xaxisOrientationText = (TextView)getView().findViewById(R.id.xText);
                    xaxisOrientationText.setText(matrixFailed);
                    TextView yaxisOrientationText = (TextView)getView().findViewById(R.id.yText);
                    yaxisOrientationText.setText(matrixFailed);
                    TextView zaxisOrientationText = (TextView)getView().findViewById(R.id.zText);
                    zaxisOrientationText.setText(matrixFailed);
                    /*
                    TextView xaxisOldOrientationText = (TextView)getView().findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText(mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView)getView().findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText(mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView)getView().findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText(mOldOreintationString[2]);
                    */



                }
            }


        }
    };


    @Override
    public void onResume() {

        SM = (SensorManager)getActivity().getSystemService(SENSOR_SERVICE);
        xText = (TextView)getView().findViewById(R.id.xText);
        yText = (TextView)getView().findViewById(R.id.yText);
        zText = (TextView)getView().findViewById(R.id.zText);
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(sensorEventListener, SM.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();

    }


    public void onPause() {
        SM.unregisterListener(sensorEventListener);
        super.onPause();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        tonePlayer.setToneFreqInHz(pitchHerz);
        tonePlayer.play();
        pitchHerz += 110;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        tonePlayer.stop();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
