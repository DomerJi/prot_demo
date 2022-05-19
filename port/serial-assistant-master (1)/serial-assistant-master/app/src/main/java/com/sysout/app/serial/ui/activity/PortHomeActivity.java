package com.sysout.app.serial.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sysout.app.serial.R;
import com.sysout.app.serial.utils.CommandExecution;
import com.sysout.app.serial.utils.Order;
import com.sysout.app.serial.utils.SerialDataUtils;
import com.sysout.app.serial.utils.SerialPortUtil;

import java.util.Arrays;

public class PortHomeActivity extends AppCompatActivity {

    private Button mBtBack;
    private Button mBtPage01;
    private Button mBtPage02;
    private Button mEchoHost;
    private Button mEchoPeripheral;
    private Button mEchoHost01;
    private Button mEchoPeripheral01;
    private Button mEchoHost02;
    private Button mEchoPeripheral02;
    private Button mEchoHost03;
    private Button mEchoPeripheral03;
    private Button mTvTestParse;
    private TextView mTvTextParseResult;
    private Button mBtPage03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_home);
        initView();
    }

    private void initView() {
        mBtBack = (Button) findViewById(R.id.bt_back);
        mBtPage01 = (Button) findViewById(R.id.bt_page_01);
        mBtPage02 = (Button) findViewById(R.id.bt_page_02);
        mBtPage03 = (Button) findViewById(R.id.bt_page_03);
        mBtBack.setOnClickListener(v -> {
            finish();
        });
        mBtPage01.setOnClickListener(v -> {
            startActivity(new Intent(PortHomeActivity.this, MainActivity.class));
        });
        mBtPage02.setOnClickListener(v -> {
            startActivity(new Intent(PortHomeActivity.this, PortTestActivity.class));
        });
        mBtPage03.setOnClickListener(v -> {
            startActivity(new Intent(PortHomeActivity.this, CalibrationActivity.class));
        });
        /**
         * echo host > /sys/devices/platform/usb0/dwc3_mode
         * echo peripheral > /sys/devices/platform/usb0/dwc3_mode
         */
        mEchoHost = (Button) findViewById(R.id.echo_host);
        mEchoPeripheral = (Button) findViewById(R.id.echo_peripheral);
        mEchoHost.setOnClickListener(v -> {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    CommandExecution.execCommand("/vendor/app/usb_mode.sh", false);
                }
            }.start();
        });

        mEchoPeripheral.setOnClickListener(v -> {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    CommandExecution.execCommand("/vendor/app/usb_mode.sh", false);
                }
            }.start();
        });
        mEchoHost01 = (Button) findViewById(R.id.echo_host01);
        mEchoPeripheral01 = (Button) findViewById(R.id.echo_peripheral01);
        mEchoHost02 = (Button) findViewById(R.id.echo_host02);
        mEchoPeripheral02 = (Button) findViewById(R.id.echo_peripheral02);
        mEchoHost01.setOnClickListener(v -> {
            CommandExecution.execCommand("echo host > /sys/devices/platform/usb0/dwc3_mode", true);
        });
        mEchoPeripheral01.setOnClickListener(v -> {
            CommandExecution.execCommand("echo peripheral > /sys/devices/platform/usb0/dwc3_mode", true);
        });

        mEchoHost03 = (Button) findViewById(R.id.echo_host03);
        mEchoPeripheral03 = (Button) findViewById(R.id.echo_peripheral03);
        mTvTestParse = (Button) findViewById(R.id.tv_test_parse);
        mTvTextParseResult = (TextView) findViewById(R.id.tv_text_parse_result);

        mTvTestParse.setOnClickListener(v -> {
            SerialPortUtil.setParseDataListener(new SerialPortUtil.ParseDataListener() {

                @Override
                public void onHandleOrder(int order, int[] bytes) {
                    mTvTextParseResult.setText("指令：" + SerialDataUtils.Int2Hex(order) + "   参数：" + Arrays.toString(bytes));
                }
            });
            // 上报电池电量
//            mTvTextParseResult.setText(
//                    SerialPortUtil.getSendData(UP_ELECTRICITY_STATE, new int[]{7271}));
//            SerialPortUtil.parseOrder("C0 CE 81 02 00 67 1C 94");

            //  1号舵机在-20度位置
//            mTvTextParseResult.setText(
//                    SerialPortUtil.getSendData(UP_RSERVO_STATE, new int[]{1, -200}));
//            SerialPortUtil.parseOrder("C0 CE 84 05 00 01 38 FF FF FF 4D");
            // 控制1号舵机转动到-20度位置，需要0.5s

            //  1号舵机在-20度位置
//            mTvTextParseResult.setText(
//                    SerialPortUtil.getSendData(Order.UP_STATE, new int[]{2, 0, 1, 0, 1, 0}));
//            SerialPortUtil.parseOrder("C0 CE 80 16 00 02 00 00 01 00 01 00 19"); // 按键
//            SerialPortUtil.parseOrder("C0 CE 80 16 00 03 00 00 00 19"); // 红外
            // 控制1号舵机转动到-20度位置，需要0.5s


//            mTvTextParseResult.setText(
//                    SerialPortUtil.getSendData(Order.UP_STATE, new int[]{2, 0, 1, 0, 1, 0}));

        });

    }
}