## AndroidManifest 추가 
``` xml
<uses-permission android:name="android.permission.READ_CALL_LOG"/>
<uses-permission android:name="android.permission.WRITE_CALL_LOG"/>
```
## java 파일 수정
### 통화기록 삽입
``` java
if (mEditNumber.getText().length() > 0 && mEditDuration.getText().length() > 0) { 
  values = new ContentValues(); //레코드 정의하는 객체 (insert, updata) 
  values.put(CallLog.Calls.NUMBER, mEditNumber.getText().toString()); 
  values.put(CallLog.Calls.DURATION, mEditDuration.getText().toString());
  values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE); // 방근 걸려온 통화인 것처럼 설정 
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
통화기록 삽의 코드에 의해서 모든 기록은 `CallLog.Calls.CONTENT_URI` 가 가지게 되니까 `resolver.query` 코드의 첫 번째 인자로 들어가게 된다.   
```
ContentResolver : query(uri, projection, selection, selectionArgs, sortOrder)   
uri : content://scheme 방식의 원하는 데이터를 가져오기 위한 정해진 주소
projection : 가져올 컬럼 이름 목록, null이면 모든 컬럼
selection : where 절에 해당하는 내용, 초기값       
selectionArgs : selection에서 ?로 표시한 곳에 들어갈 데이터     
sortDorder : 정렬을 위한 값 
-> 01011111111 ==>  "number=01011111111"로 검색 조건이 된다는 뜻임, 결국에는 차례차례 연결 되어서 들어감.
```
`query()` 에 URI와 필드 이름을 담은 배열을 전달, 나머지 인자에 기본값인 null을 전달한다.   
리턴값은 Cursor 타입의 객체인데 showResult()에 전달하면 결과를 화면에 전달한다.   
`Cursor`을 사용하면 반드시 제일 아래에 close()를 선언해줘야 된다.

### 전화번호 검색
``` java
if (mEditNumber.getText().length() > 0) {
  String number = mEditNumber.getText().toString();
  cur = resolver.query(CallLog.Calls.CONTENT_URI, projection, 
                       CallLog.Calls.NUMBER + "=?", new String[]{ number }, null);
  if (cur != null) {
    showResult(cur);
    cur.close();
  }
} break;
```
특정 전화번호를 가진 레코드를 검색한다.   
전체 검색하고 전체적으로 비슷한데, 여기에 세 번째와 네 번째 인자에 조건을 전달하면 된다.   
세 번째 인자의 물음표 자리에 네 번째 인자로 전달한 문자열이 차례로 채워져서 조건이 완성된다.   

### 통화시간 갱신
``` java
if (mEditNumber.getText().length() > 0 && mEditDuration.getText().length() > 0) {
  String number = mEditNumber.getText().toString();
  values = new ContentValues();
  values.put(CallLog.Calls.DURATION, mEditDuration.getText().toString());
  resolver.update(CallLog.Calls.CONTENT_URI, values,
                 CallLog.Calls.NUMBER + "=?", new String[]{ number });
} break;
```
전화버호와 통화시간을 입력 받아 레코드를 갱신.   
ContentValues 객체에 갱신할 필드명과 데이터를 넣은 후 `updata()`를 호출하면 된다. 
`updata`의 세 번째와 네 번째 인자는 query()의 세 번째와 네 번째 인자처럼 조건을 의미한다. 

### 전화번호 삭제
``` java
if (mEditNumber.getText().length() > 0) {
  String number = mEditNumber.getText().toString();
  resolver.delete(CallLog.Calls.CONTENT_URI,
                 CallLog.Calls.NUMBER + "=?", new String[]{ number });
} break;
```
특정 전화번호를 가진 레코드를 삭제한다.
```
delete 문법   
ContentResolver : delete(uri, where, selectionArgs)
```


### 전체 삭제 
```java
resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
break;
```

전체 코드는 깃허브 코드 속에서 참고하자 
