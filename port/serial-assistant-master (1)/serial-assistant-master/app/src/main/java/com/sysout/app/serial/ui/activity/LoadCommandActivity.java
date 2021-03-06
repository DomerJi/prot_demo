/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sysout.app.serial.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sysout.app.serial.R;
import com.sysout.app.serial.db.SerialCommandServiceImpl;
import com.sysout.app.serial.entity.SerialCommand;
import com.sysout.app.serial.ui.adapter.CommandAdapter;
import com.sysout.app.serial.ui.dialog.UpCommandDialog;

import java.util.ArrayList;
import java.util.List;

public class LoadCommandActivity extends AppCompatActivity {

    public static final int RESULT_CODE = 10001;

    private List<SerialCommand> commandList = new ArrayList<>();
    private List<SerialCommand> selectCommandList = new ArrayList<>();
    private CommandAdapter commandAdapter;

    private int writeMode;      // 0 - hex; 1 - txt;

    private RecyclerView serialRvCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_command);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "??????????????????????????????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("????????????");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serialRvCommand = findViewById(R.id.serial_rv_command);
        FloatingActionButton fab = findViewById(R.id.fab_commit_command);
        fab.setOnClickListener(view -> {
            UpCommandDialog dialog = new UpCommandDialog(LoadCommandActivity.this, writeMode, null);
            dialog.setOnCenterItemClickListener((serialCommand) -> {
                commandList.add(serialCommand);
                commandAdapter.notifyDataSetChanged();
            });
            dialog.show();
        });
    }

    private void initData() {
        writeMode = getIntent().getIntExtra("writeMode", 0);
        initRecyclerView();
    }

    private void initRecyclerView() {
        //LinearLayoutManager???????????????????????????????????????????????????
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        serialRvCommand.setLayoutManager(layoutManager);
        //???????????????????????????
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //??????????????????????????????item?????????????????????
        serialRvCommand.setItemAnimator(new DefaultItemAnimator());

        //?????????????????????DividerItemDecoration??????????????????????????????
        //??????????????????????????????????????????????????????????????????ItemDecoration??????
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        serialRvCommand.addItemDecoration(dividerItemDecoration);

        //???item????????????????????????item?????????
        //??????adapter??????????????????????????????????????????????????????????????????nofityDataSetChange??????????????????
        //???????????????????????????????????????true??????????????????????????????????????????????????????nofityDataSetChange
//        serialRvCommand.setHasFixedSize(true);
        commandList = SerialCommandServiceImpl.getInstance().queryByMode(writeMode);
        commandAdapter = new CommandAdapter(this, commandList);
        commandAdapter.setOnClickListener(new CommandAdapter.OnClickListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, List<SerialCommand> commandList) {
                selectCommandList = commandList;
            }

            @Override
            public void onEditClick(SerialCommand serialCommand) {
                UpCommandDialog dialog = new UpCommandDialog(LoadCommandActivity.this, writeMode, serialCommand);
                dialog.setOnCenterItemClickListener((s) -> {
                    for (SerialCommand command : commandList) {
                        if(command.getId().equals(s.getId())){
                            command.setCommand(s.getCommand());
                            command.setRemarks(s.getRemarks());
                            break;
                        }
                    }
                    commandAdapter.notifyDataSetChanged();
                });
                dialog.show();
            }

            @Override
            public void onDelClick(long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoadCommandActivity.this)
                        .setMessage(getString(R.string.serial_del_confirm)).setPositiveButton(getString(R.string.serial_commit), (dialogInterface, i) -> {
                            SerialCommandServiceImpl.getInstance().deleteSerialCommandByKey(id);
                            for (SerialCommand serialCommand : commandList) {
                                if(serialCommand.getId().equals(id)){
                                    commandList.remove(serialCommand);
                                    break;
                                }
                            }
                            commandAdapter.notifyDataSetChanged();
                        }).setNegativeButton(getString(R.string.serial_cancel), (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });
                builder.create().show();
            }
        });
        serialRvCommand.setAdapter(commandAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_command, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        } else if (id == R.id.load_command_menu_commit) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("result", (ArrayList<? extends Parcelable>) selectCommandList);
            setResult(RESULT_CODE, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
