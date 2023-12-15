//package com.example.santintcolddish.correction.view;
//
//import android.content.Context;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.santintcolddish.R;
//import com.example.santintcolddish.correction.adapter.PopCorrectAdapter;
//import com.example.santintcolddish.correction.bean.CorrectBean;
//import com.example.santintcolddish.correction.bean.CorrectMethodBean;
//import com.example.santintcolddish.correction.fragment.CorrectMethod;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import razerdp.basepopup.BasePopupWindow;
//
//
//public class CorrectMethodDialog extends BasePopupWindow implements View.OnClickListener, TextView.OnEditorActionListener,PopCorrectAdapter.OnItemClickListener {
//    private Button btnSure;
//    private EditText editPerson;
//    private String person;
//    private Context mContext;
//    private RecyclerView recyclerView;
//    private PopCorrectAdapter mAdapter;
//    private List<CorrectBean> mDatas;
//    private List<CorrectMethodBean> correctMethodBeans = new ArrayList<CorrectMethodBean>();
//
//    public CorrectMethodDialog(Context context, int width, int height) {
//        super(context, width, height);
//        this.mContext = context;
//    }
//
//    public CorrectMethodDialog(Context context,List<CorrectMethodBean> correctMethodBeans) {
//        super(context);
//        this.mContext = context;
//        this.correctMethodBeans = correctMethodBeans;
//    }
//
//    @Override
//    public View onCreateContentView() {
//        View view = createPopupById(R.layout.pop_correct_method);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
//        mDatas = new ArrayList<CorrectBean>();
//        getList();
//        bindAdapter();
//        return view;
//    }
//    private void getList() {
//
//        for (int i = 0; i < correctMethodBeans.size(); i++) {
//            CorrectMethodBean correctMethodBean = correctMethodBeans.get(i);
//            CorrectBean correctBean = new CorrectBean();
//            correctBean.setCorrectType(correctMethodBean.getMethodName());
//            mDatas.add(correctBean);
//        }
//    }
//
//    private void bindAdapter() {
//        mAdapter = new PopCorrectAdapter(getContext(), mDatas);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        mAdapter.setOnItemClickListener(this);
////        mDecoration = new ItemHeaderDecoration(getContext(), mDatas);
////        rvMain.addItemDecoration(mDecoration);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.id_tv_correct:
//                break;
//
//        }
//    }
//    @Override
//    public void onItemClick(View view, int position) {
//        if (correctMethodDialog != null) {
//            correctMethodDialog.dismiss();
//        }
//        CorrectBean correctBean = mDatas.get(position);
//        correctMethod = correctBean.getCorrectType();
//        tvMethod.setText(correctBean.getCorrectType());
//        getCurPointList();
//    }
//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        switch (actionId) {
//            case EditorInfo.IME_ACTION_DONE:
//                //next();
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//    }
//}
