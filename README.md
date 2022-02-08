## AndroidManifest 추가 
``` xml
<uses-permission android:name="android.permission.READ_CALL_LOG"/>
<uses-permission android:name="android.permission.WRITE_CALL_LOG"/>
```

### 통화기록 삽입
``` java
if (mEditNumber.getText().length() > 0 && mEditDuration.getText().length() > 0) { 
  values = new ContentValues(); //레코드 정의하는 객체 (insert, updata) 
  values.put(CallLog.Calls.NUMBER, mEditNumber.getText().toString()); 
  values.put(CallLog.Calls.DURATION, mEditDuration.getText().toString());
  values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
  values.put(CallLog.Calls.DATE, System.currentTimeMillis());
  resolver.insert(CallLog.Calls.CONTENT_URI, values);
  } break;
```
제일 첫 번째 줄은 전화번호의 길이가 0보다는 커야 된다는 의미이다 이 조건문이 맞으면 데이터를 삽입해야 된다.   
전화번호와 통화시간을 입력 받아 삽입 연산을 처리. `ContentValues` 객체에 필드명과 데이터를 차례로 넣은 뒤, `insert()` 를 호출하면 된다.   
전화를 하다 보면 # 같은 것이 붙을 수 있기 때문에 처음부터 `문자열`로 받는다.


### 전체 검색
``` java
cur = resolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
  if (cur != null) {
    showResult(cur);
    cur.close();
 } break;
```
`query()` 에 URI와 필드 이름을 담은 배열을 전달, 나머지 인자에 기본값인 null을 전달한다.   
리턴값은 Cursor 타입의 객체인데 showResult()에 전달하면 결과를 화면에 전달한다.   
`Cursor`을 사용하면 반드시 제일 아래에 close()를 선언해줘야 된다.

### 전화번호 검색

### 통화시간 갱신

### 전화번호 삭제

### 전체 삭제 
