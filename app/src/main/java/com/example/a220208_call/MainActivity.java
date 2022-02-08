package com.example.a220208_call;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEditNumber;
    private EditText mEditDuration;
    private TextView mTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditNumber = (EditText) findViewById(R.id.editNumber);
        mEditDuration = (EditText) findViewById(R.id.editDuration);
        mTextResult = (TextView) findViewById(R.id.textResult);
    }

    public void mOnClick (View v) {
        ContentResolver resolver = getContentResolver();
        ContentValues values;
        String[] projection = {
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
        };
        Cursor cur;

        switch (v.getId()) {
            case R.id.btnInsert: //통화기록 삽입
                if (mEditNumber.getText().length() > 0 && mEditDuration.getText().length() > 0) {
                    values = new ContentValues();
                    values.put(CallLog.Calls.NUMBER, mEditNumber.getText().toString());
                    values.put(CallLog.Calls.DURATION, mEditDuration.getText().toString());
                    values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
                    values.put(CallLog.Calls.DATE, System.currentTimeMillis());
                    resolver.insert(CallLog.Calls.CONTENT_URI, values);
                }
                break;
            case R.id.btnSelectAll: // 전체 검색
                cur = resolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
                if (cur != null) {
                    showResult(cur);
                    cur.close();
                }
                break;
            case R.id.btnSelectNumber: // 전화번호 검색
                if (mEditNumber.getText().length() > 0) {
                    String number = mEditNumber.getText().toString();
                    cur = resolver.query(CallLog.Calls.CONTENT_URI, projection,
                            CallLog.Calls.NUMBER + "=?", new String[]{ number }, null);
                    if (cur != null) {
                        showResult(cur);
                        cur.close();
                    }
                }
                break;
            case R.id.btnUpdateDuration: // 통화시간 갱신
                if (mEditNumber.getText().length() > 0 && mEditDuration.getText().length() > 0) {
                    String number = mEditNumber.getText().toString();
                    values = new ContentValues();
                    values.put(CallLog.Calls.DURATION, mEditDuration.getText().toString());
                    resolver.update(CallLog.Calls.CONTENT_URI, values,
                            CallLog.Calls.NUMBER + "=?", new String[]{ number });
                }
                break;
            case R.id.btnDeleteNumber: // 전화번호 삭제
                if (mEditNumber.getText().length() > 0) {
                    String number = mEditNumber.getText().toString();
                    resolver.delete(CallLog.Calls.CONTENT_URI,
                            CallLog.Calls.NUMBER + "=?", new String[]{ number });
                }
                break;
            case R.id.btnDeleteAll: // 전체 삭제
                resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
                break;
        }
    }

    private void showResult(Cursor cur) {
        mTextResult.setText("");
        int number_col = cur.getColumnIndex(CallLog.Calls.NUMBER);
        int duration_col = cur.getColumnIndex(CallLog.Calls.DURATION);
        while (cur.moveToNext()) {
            String number = cur.getString(number_col);
            int duration = cur.getInt(duration_col);
            mTextResult.append(number + ", " + duration + "\n");
        }
    }
}
