package com.jeremy.tripcord.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.database.domain.TripInfo;
import com.jeremy.tripcord.main.model.TripInfoModel;
import com.jeremy.tripcord.main.view.TripInfoListAdaptor;
import com.jeremy.tripcord.record.RecordActivity;
import com.jeremy.tripcord.record.RecordDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asura1983 on 2014. 6. 22..
 */
public class TripInfoFragment extends Fragment {

    List<TripInfo> tripInfoList;
    ListView listViewTripInfos;

    /*
     * Fragment Setting / Life Cycle
     */
    public static TripInfoFragment newInstance() {

        TripInfoFragment fragment = new TripInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TripInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initModules();
        initViews(rootView);

        loadTripInfoList();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadTripInfoList();
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    /*
     * Custom Methods
     */
    private void initViews(View rootView) {

        listViewTripInfos = (ListView) rootView.findViewById(R.id.listView_trip_infos);

        final TripInfoListAdaptor tripInfoListAdaptor = new TripInfoListAdaptor(getActivity(), R.layout.adapter_trip_info_list, tripInfoList, handler);
        listViewTripInfos.setAdapter(tripInfoListAdaptor);

        Button buttonNewTrip = (Button) rootView.findViewById(R.id.button_new_trip);
        buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRecord = new Intent(getActivity().getApplicationContext(), RecordActivity.class);
                getActivity().startActivity(intentRecord);
            }
        });
    }

    private void initModules() {

        tripInfoList = new ArrayList<TripInfo>();
    }

    private void loadTripInfoList() {

        tripInfoList = TripInfoModel.loadTripInfoList(getActivity().getApplicationContext());

        TripInfoListAdaptor tripInfoListAdaptor = (TripInfoListAdaptor) listViewTripInfos.getAdapter();
        tripInfoListAdaptor.clear();
        tripInfoListAdaptor.addAll(tripInfoList);
        tripInfoListAdaptor.notifyDataSetChanged();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case CommonContants.WHAT_TRIP_LIST_CLICKED :
                    TripInfo tripInfo = (TripInfo) msg.obj;
                    Intent intent = new Intent(getActivity(), RecordDetailActivity.class);
                    intent.putExtra(CommonContants.EXTRA_KEY_TRIPSEQ, tripInfo.getTripSeq());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

}
