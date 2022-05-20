package com.sysout.app.serial.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sysout.app.serial.R;
import com.sysout.app.serial.utils.Order;
import com.sysout.app.serial.utils.SerialDataUtils;
import com.sysout.app.serial.utils.SerialHelper;
import com.sysout.app.serial.utils.SerialPortUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class CalibrationActivity extends AppCompatActivity {

    private static final String TAG = "Calibration";

    private Button mBtBack;
    private TextView mTvShakeTitle;
    private Button mBtShakeLeft;
    private TextView mTvShakeLeftValue;
    private Button mBtShakeRight;
    private TextView mTvShakeRightValue;
    private Button mBtShakeCalibration;
    private TextView mTvShakeCalibrationValue;
    private SerialHelper serialHelper;
    private TextView mTvPortStatus;

    private HashMap<Integer, Boolean> mCheckMap = new HashMap<>();

    Handler mHandler = new Handler(Looper.getMainLooper());
    int sHakeLeft = -999;
    int sHakeRight = -999;
    int getsHakeLeftMax = -999;
    int getsHakeRightMax = -999;
    int mZeroValue = -999;

    int nodLeft = -999;
    int nodRight = -999;
    int getNodLeftMax = -999;
    int getNodRightMax = -999;
    int mNodZeroValue = -999;

    private Button mBtDownElectricity;
    private Button mBtUpElectricity;
    private Button mBtShakeZero;
    private TextView mTvShakeHint;
    private TextView mTvNodTitle;
    private Button mBtNodLeft;
    private TextView mTvNodLeftValue;
    private Button mBtNodRight;
    private TextView mTvNodRightValue;
    private Button mBtNodCalibration;
    private TextView mTvNodCalibrationValue;
    private Button mBtNodZero;
    private TextView mTvNodHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        initView();
        try {
            Log.d(TAG, "open =====================1111111111111111111=");
            int openStatus = open(SerialPortUtil.PORT_NAME, SerialPortUtil.IBAUDTATE);
            Log.d(TAG, "open =====================openStatus = " + openStatus);
        } catch (Exception e) {
            Log.d(TAG, "open =====================444444444444444444=" + e.getMessage());
        }

    }

    private void initView() {
        mBtBack = (Button) findViewById(R.id.bt_back);
        mTvShakeTitle = (TextView) findViewById(R.id.tv_shake_title);
        mBtShakeLeft = (Button) findViewById(R.id.bt_shake_left);
        mTvShakeLeftValue = (TextView) findViewById(R.id.tv_shake_left_value);
        mBtShakeRight = (Button) findViewById(R.id.bt_shake_right);
        mTvShakeRightValue = (TextView) findViewById(R.id.tv_shake_right_value);
        mBtShakeCalibration = (Button) findViewById(R.id.bt_shake_calibration);
        mTvShakeCalibrationValue = (TextView) findViewById(R.id.tv_shake_calibration_value);
        mTvPortStatus = (TextView) findViewById(R.id.tv_port_status);

        mBtDownElectricity = (Button) findViewById(R.id.bt_down_electricity);
        mBtUpElectricity = (Button) findViewById(R.id.bt_up_electricity);


        mTvShakeHint = (TextView) findViewById(R.id.tv_shake_hint);
        mTvNodTitle = (TextView) findViewById(R.id.tv_nod_title);
        mBtNodLeft = (Button) findViewById(R.id.bt_nod_left);
        mTvNodLeftValue = (TextView) findViewById(R.id.tv_nod_left_value);
        mBtNodRight = (Button) findViewById(R.id.bt_nod_right);
        mTvNodRightValue = (TextView) findViewById(R.id.tv_nod_right_value);
        mBtNodCalibration = (Button) findViewById(R.id.bt_nod_calibration);
        mTvNodCalibrationValue = (TextView) findViewById(R.id.tv_nod_calibration_value);
        mTvNodHint = (TextView) findViewById(R.id.tv_nod_hint);

        mBtNodZero = (Button) findViewById(R.id.bt_nod_zero);
        mBtShakeZero = (Button) findViewById(R.id.bt_shake_zero);

        mBtBack.setOnClickListener(v -> {
            finish();
        });

        initShake();
        initNod();
    }


    private void initShake() {

        mBtShakeLeft.setOnClickListener(v -> {
            send(0, Order.DOWN_STATE, 4);
        });

        mBtShakeRight.setOnClickListener(v -> {
            send(1, Order.DOWN_STATE, 4);
        });

        mBtShakeCalibration.setOnClickListener(v -> {
            if (getsHakeLeftMax == -999 || getsHakeRightMax == -999) {
                Toast.makeText(CalibrationActivity.this, "请先获取左右极限位", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("校准中...".equals(mBtShakeCalibration.getText().toString())) {
                Toast.makeText(CalibrationActivity.this, "校准中...", Toast.LENGTH_SHORT).show();
                return;
            }
            mBtShakeCalibration.setText("校准中...");
            mTvShakeCalibrationValue.setText("");
            send(3, Order.DOWN_SERVO_STATE, 2, getsHakeLeftMax, 5000);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    send(4, Order.DOWN_SERVO_STATE, 2, getsHakeRightMax, 5000);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }
                            mBtShakeCalibration.setText("校准");
                        }
                    }, 11500);
                }
            }, 6500);
        });
        mBtShakeZero.setOnClickListener(v -> {
            if (serialOpened()) {
                if (mZeroValue == -999) {
                    Toast.makeText(CalibrationActivity.this, "请先校准获取零位", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CalibrationActivity.this, "转到零位:" + (getPeople(mZeroValue / 10f)), Toast.LENGTH_SHORT).show();
                serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_SERVO_STATE, 2, mZeroValue, 5000));
            }
        });
    }


    private void initNod() {

        mBtNodLeft.setOnClickListener(v -> {
            send(5, Order.DOWN_STATE, 4);
        });

        mBtNodRight.setOnClickListener(v -> {
            send(6, Order.DOWN_STATE, 4);
        });

        mBtNodCalibration.setOnClickListener(v -> {
            if (getNodLeftMax == -999 || getNodRightMax == -999) {
                Toast.makeText(CalibrationActivity.this, "请先获取上下极限位", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("校准中...".equals(mBtNodCalibration.getText().toString())) {
                Toast.makeText(CalibrationActivity.this, "校准中...", Toast.LENGTH_SHORT).show();
                return;
            }
            mBtNodCalibration.setText("校准中...");
            mTvNodCalibrationValue.setText("");
            send(7, Order.DOWN_SERVO_STATE, 1, getNodLeftMax, 5000);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    send(8, Order.DOWN_SERVO_STATE, 1, getNodRightMax, 5000);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }
                            mBtNodCalibration.setText("校准");
                        }
                    }, 11500);
                }
            }, 6500);
        });
        mBtNodZero.setOnClickListener(v -> {
            if (serialOpened()) {
                if (mNodZeroValue == -999) {
                    Toast.makeText(CalibrationActivity.this, "请先校准获取零位", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CalibrationActivity.this, "转到零位:" + (getPeople(mNodZeroValue / 10f)), Toast.LENGTH_SHORT).show();
                serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_SERVO_STATE, 1, mNodZeroValue, 5000));
            }
        });
    }

    public void send(int getIndex, int order, int... params) {
        this.getIndex = getIndex;
        if (serialOpened()) {
            serialHelper.sendHex(SerialPortUtil.getSendData(order, params));
        }
    }

    public void sendCheck(int order, int... params) {
        this.getIndex = -1;
        if (serialOpened()) {
            serialHelper.sendHex(SerialPortUtil.getSendData(order, params));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String portStatus = "打开状态：" + (serialOpened() ? "打开" : "关闭");
        if (serialOpened()) {
            serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_FACTORY_STATE, 1));

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_ELECTRICITY_STATE, 0));
                    mTvPortStatus.setText(portStatus + " -|- 舵机：已断电");
                }
            }, 1000);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_ELECTRICITY_STATE, 1));
                    mTvPortStatus.setText(portStatus + " -|- 舵机：已上电");
                }
            }, 3000);
        }
        mTvPortStatus.setText(portStatus);

        mBtDownElectricity.setOnClickListener(v -> {
            if (serialOpened()) {
                serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_ELECTRICITY_STATE, 0));
                mTvPortStatus.setText(portStatus + " -|- 舵机：已断电");
            }
        });
        mBtUpElectricity.setOnClickListener(v -> {
            if (serialOpened()) {
                serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_ELECTRICITY_STATE, 1));
                mTvPortStatus.setText(portStatus + " -|- 舵机：已上电");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serialOpened()) {
            serialHelper.sendHex(SerialPortUtil.getSendData(Order.DOWN_FACTORY_STATE, 0));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serialHelper != null && serialHelper.isOpen()) {
            serialHelper.close();
        }
    }

    private int getIndex = -1;

    private int open(String selectPort, int selectBaud) {
        if (serialOpened()) {
            serialHelper.close();
        }
        serialHelper = new SerialHelper(selectPort, selectBaud) {
            @Override
            protected void onDataReceived(byte[] buff) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String hexData = SerialDataUtils.ByteArrToHex(buff);
                        Log.d(TAG, "接收到数据: hexData = " + hexData);
                        SerialPortUtil.setParseDataListener(new SerialPortUtil.ParseDataListener() {


                            @Override
                            public void onHandleOrder(int order, int[] bytes) {

                                switch (order) {
                                    case Order.UP_STATE:
                                        // 查询舵机角度 1，2，4，4，4 状态|故障位|1号舵机|2号舵机|3号舵机
                                        if (bytes.length == 5 && bytes[0] == 4) {

                                            // 摇头舵机 左右极限位
                                            if (getIndex == 0) {
                                                float angle = bytes[3] / 10f;
                                                mTvShakeLeftValue.setText(getPeople(angle) + "度");
                                                getsHakeLeftMax = bytes[3];
                                                mCheckMap.put(getIndex, true);
                                            } else if (getIndex == 1) {
                                                float angle = bytes[3] / 10f;
                                                mTvShakeRightValue.setText(getPeople(angle) + "度");
                                                getsHakeRightMax = bytes[3];
                                                mCheckMap.put(getIndex, true);
                                                // 点头舵机 上下极限位
                                            } else if (getIndex == 5) {
                                                float angle = bytes[2] / 10f;
                                                mTvNodLeftValue.setText(getPeople(angle) + "度");
                                                getNodLeftMax = bytes[2];
                                                mCheckMap.put(getIndex, true);
                                            } else if (getIndex == 6) {
                                                float angle = bytes[2] / 10f;
                                                mTvNodRightValue.setText(getPeople(angle) + "度");
                                                getNodRightMax = bytes[2];
                                                mCheckMap.put(getIndex, true);
                                            }
                                        }

                                        break;
                                    case Order.UP_RSERVO_STATE:
                                        // 摇头舵机 == 2
                                        if (bytes[0] == 2) {
                                            if (getIndex == 3) {
                                                float angle = bytes[1] / 10f;
                                                sHakeLeft = bytes[1];
                                                mTvShakeCalibrationValue.setText("right：" + getPeople(angle));
                                            } else if (getIndex == 4) {
                                                float angle = bytes[1] / 10f;
                                                sHakeRight = bytes[1];
                                                mTvShakeCalibrationValue.setText(mTvShakeCalibrationValue.getText().toString()
                                                        + "  *  left：" + getPeople(angle));
                                                mZeroValue = (sHakeLeft + sHakeRight) / 2;
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sendCheck(Order.DOWN_SERVO_STATE, 2, mZeroValue, 2000);
                                                        mTvShakeCalibrationValue.setText(mTvShakeCalibrationValue.getText().toString()
                                                                + "  *  校准：" + getPeople(mZeroValue / 10f));
                                                    }
                                                }, 5000);
                                            }
                                            // 点头舵机 == 1
                                        } else if (bytes[0] == 1) {
                                            if (getIndex == 7) {
                                                float angle = bytes[1] / 10f;
                                                nodLeft = bytes[1];
                                                mTvNodCalibrationValue.setText("bottom：" + getPeople(angle));
                                            } else if (getIndex == 8) {
                                                float angle = bytes[1] / 10f;
                                                nodRight = bytes[1];
                                                mTvNodCalibrationValue.setText(mTvNodCalibrationValue.getText().toString()
                                                        + "  *  top：" + getPeople(angle));
                                                mNodZeroValue = (nodLeft + nodRight) / 2;
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sendCheck(Order.DOWN_SERVO_STATE, 1, mNodZeroValue, 2000);
                                                        mTvNodCalibrationValue.setText(mTvNodCalibrationValue.getText().toString()
                                                                + "  *  校准：" + getPeople(mNodZeroValue / 10f));
                                                    }
                                                }, 5000);
                                            }
                                        }
                                        break;
                                }
                            }
                        });
                        SerialPortUtil.parseOrder(hexData);
                    }
                });

            }

            @Override
            protected void onSendDataReceived(byte[] buff) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String hexData = SerialDataUtils.ByteArrToHex(buff);
                        Log.d(TAG, "onSendDataReceived = " + hexData);
                    }
                });

            }
        };
        Log.d(TAG, "open =================AAAAAAAAAAAAA");
        try {
            serialHelper.open();
            Log.d(TAG, "open =================BBBBBBBBBBBBBBBBBBB");
            return 1;
        } catch (IOException e) {
            Log.d(TAG, "open =================CCCCCCCCCCCCCCCC" + e.getMessage());
            return -1;
        }

    }


    public static String getPeople(float people) {
        if (people == 0f) {
            return "0.00";
        }
        return new DecimalFormat("##0.00").format(people);
    }


    private boolean serialOpened() {
        if (serialHelper == null) return false;
        return serialHelper.isOpen();
    }

}