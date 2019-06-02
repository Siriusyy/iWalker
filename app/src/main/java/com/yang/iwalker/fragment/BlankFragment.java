package com.yang.iwalker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.yang.iwalker.R;
import com.yang.iwalker.adapter.HomeAdapter;
import com.yang.iwalker.dialog.BottomDialog;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }



    }

    private View rootView;

    View getRootView() {
        return rootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView contentTv = rootView.findViewById(R.id.content_tv);
        contentTv.setText(mContentText);



        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recycle = getActivity().findViewById(R.id.recycle_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(layoutManager);
        List<String> datas=new ArrayList<String>();
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");
        datas.add("蔡徐坤");

        HomeAdapter adatper=new HomeAdapter(datas);
        recycle.setAdapter(adatper);
    }
}
