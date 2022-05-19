package com.thfw.robotheart.activitys;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thfw.base.base.IPresenter;
import com.thfw.base.utils.LogUtil;
import com.thfw.base.utils.ToastUtil;
import com.thfw.robotheart.R;
import com.thfw.robotheart.util.SerialPortUtil;
import com.vi.vioserial.COMSerial;
import com.vi.vioserial.NormalSerial;
import com.vi.vioserial.listener.OnComDataListener;
import com.vi.vioserial.listener.OnSerialDataListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortTestActivity extends RobotBaseActivity implements OnComDataListener, OnSerialDataListener {

    private android.widget.Button mBtBack;
    private android.widget.EditText mEtName;
    private android.widget.EditText mEtCode;
    private android.widget.Button mBtOpen;
    private android.widget.Button mBtColse;
    private android.widget.TextView mTvOpenStatusHint;
    private android.widget.EditText mEtCode01;
    private android.widget.EditText mEtCode02;
    private android.widget.EditText mEtCode03;
    private android.widget.EditText mEtCode04;
    private android.widget.EditText mEtCode05;
    private android.widget.EditText mEtCode06;
    private android.widget.Button mBtSend;
    private android.widget.TextView mTvSendStatusHint;
    private android.widget.TextView mTvRiceverStatusHint;
    private EditText mEtOrder;

    @Override
    public int getContentView() {
        return R.layout.activity_port_test;
    }

    @Override
    public IPresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void initView() {

        mBtBack = (Button) findViewById(R.id.bt_back);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtOrder = (EditText) findViewById(R.id.et_order);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mBtOpen = (Button) findViewById(R.id.bt_open);
        mBtColse = (Button) findViewById(R.id.bt_colse);
        mTvOpenStatusHint = (TextView) findViewById(R.id.tv_open_status_hint);
        mEtCode01 = (EditText) findViewById(R.id.et_code01);
        mEtCode02 = (EditText) findViewById(R.id.et_code02);
        mEtCode03 = (EditText) findViewById(R.id.et_code03);
        mEtCode04 = (EditText) findViewById(R.id.et_code04);
        mEtCode05 = (EditText) findViewById(R.id.et_code05);
        mEtCode06 = (EditText) findViewById(R.id.et_code06);
        mBtSend = (Button) findViewById(R.id.bt_send);
        mTvSendStatusHint = (TextView) findViewById(R.id.tv_send_status_hint);
        mTvRiceverStatusHint = (TextView) findViewById(R.id.tv_ricever_status_hint);

        mEtName.setText(SerialPortUtil.PORT_NAME);
        mEtCode.setText(String.valueOf(SerialPortUtil.IBAUDTATE));

        mBtOpen.setOnClickListener(v -> {
            String name = mEtName.getText().toString();
            String code = mEtCode.getText().toString();
            if (TextUtils.isEmpty(name)) {
                ToastUtil.show("【串口名称】 不能是空的");
                return;
            }

            if (TextUtils.isEmpty(code)) {
                ToastUtil.show("【串口号】 不能是空的");
                return;
            }
            try {
                int ibaudRate = Integer.parseInt(code);
                int openStatus = SerialPortUtil.open(name, ibaudRate);
                if (openStatus == 1) {
                    COMSerial.instance().setSerialDataListener(name, this);
                }
                mTvOpenStatusHint.setText("【串口名称】:" + name + "    【串口号】:" + code + "   【打开状态】:" + openStatus);
            } catch (Exception e) {
                mTvOpenStatusHint.setText(e.getMessage());
            }

        });

        mBtColse.setOnClickListener(v -> {
            NormalSerial.instance().close();
            mTvOpenStatusHint.setText("已关闭");
        });

        EditText[] editTexts = new EditText[]{mEtCode01, mEtCode02, mEtCode03, mEtCode04, mEtCode05, mEtCode06};

        mBtSend.setOnClickListener(v -> {

            String order = mEtOrder.getText().toString().trim();
            int orderInt = 0;
            try {
                orderInt = Integer.parseInt(order);
            } catch (Exception e) {
                mTvSendStatusHint.setText("指令参数异常：" + e.getMessage());
                return;
            }

            int len = editTexts.length;
            List<Byte> bytes = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                String content = editTexts[i].getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    try {
                        Byte mB = Byte.parseByte(content);
                        bytes.add(mB.byteValue());
                    } catch (Exception e) {
                        mTvSendStatusHint.setText("第" + (i + 1) + "个参数异常：" + e.getMessage());
                        return;
                    }
                }
            }

            byte[] params = new byte[bytes.size()];

            int pLen = params.length;
            for (int i = 0; i < pLen; i++) {
                params[i] = bytes.get(i).byteValue();
            }
            String senData = SerialPortUtil.sendOrder(orderInt, params);
            LogUtil.d(TAG, "指令：" + orderInt + "   原始参数：" + Arrays.toString(params));
            mTvSendStatusHint.setText("指令：" + orderInt + "   原始参数：" + Arrays.toString(params)
                    + "  final发送数据：" + senData);


        });

        COMSerial.instance().addDataListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        COMSerial.instance().removeDataListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void comDataBack(String com, String hexData) {
        LogUtil.d("接收到数据: com = " + com + "  hexData = " + hexData);
    }

    @Override
    public void onSend(String hexData) {
        LogUtil.d("发送数据: hexData = " + hexData);
    }

    @Override
    public void onReceive(String hexData) {
        LogUtil.d("接收到数据: hexData = " + hexData);
        mTvRiceverStatusHint.setText("接收到数据：" + hexData);
        SerialPortUtil.setParseDataListener(new SerialPortUtil.ParseDataListener() {
            @Override
            public void onHandleOrder(int order, int[] bytes) {
                mTvRiceverStatusHint.setText("接收到数据：" + hexData + "  \n  "
                        + "指令：" + order + "   参数：" + Arrays.toString(bytes));
            }
        });
        SerialPortUtil.parseOrder(hexData);

    }

    @Override
    public void onReceiveFullData(String hexData) {

    }
}