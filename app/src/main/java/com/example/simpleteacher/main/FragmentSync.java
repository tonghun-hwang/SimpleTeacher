package com.example.simpleteacher.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSync.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSync#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSync extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG = "FragmentSync";
    private OnFragmentInteractionListener mListener;
    private FragmentManager fm;
    private Fragment fragment;

    private View view;
    private ItemActivity mParent;
    public Button btnSync;
    public TextView txtUpdate, txtConnect;

    public FragmentSync() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSync.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSync newInstance(String param1, String param2) {
        FragmentSync fragment = new FragmentSync();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fm = getFragmentManager();
        view = inflater.inflate(R.layout.fragment_fragment_sync, container, false);
        btnSync = view.findViewById(R.id.btn_sync);
        txtConnect = view.findViewById(R.id.txtIsConnected);
        txtUpdate = view.findViewById(R.id.txtUpdated);
        fragment = this;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtUpdate.setText(mParent.pref.getString("syncDate", "00-00-0000 00:00:00"));
        btnSync.setOnClickListener((new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParent.readResultURL(mParent.mID, mParent.mReadResultURLTask);
                mParent.readTrainingURL(mParent.mID, mParent.mReadTrainingUrlTask);
            }
        }
        ));
    }

    public void syncOnPressed(){
        if (txtUpdate != null) {
            txtUpdate.setText("Synchronization...");
        }
    }

    public void postResult(){
        Log.d(TAG, "postResult()");
    }
    public void postTrain(){
        Log.d(TAG, "postTrain()");
        //txtUpdate.setText(mParent.pref.getString("syncDate", "00-00-0000 00:00:00"));

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment4, fragment);
        fragmentTransaction.commit();
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
        mParent = (ItemActivity) getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
