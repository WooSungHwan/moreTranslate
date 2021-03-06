<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>모아번역, 한번에 비교하여 번역하자</title>
  </head>
  <link rel="shortcut icon" href="resources/image/language_VCk_icon.ico" type="image/x-icon">
  <link rel="icon" href="resources/image/language_VCk_icon.ico" type="image/x-icon">
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css">
  <link rel="stylesheet" href="resources/css/translateStyle.css">
  <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
  <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="resources/js/jquery.selectlist.js"></script>
  <body>
      <header>
        <h1>모아번역</h1>
        <p>파파고, 카카오, 구글 3대 번역 기술을 한곳에서 <br> 모아 번역하자</p>
      </header>
      <textarea id = "translateArea" class="translateArea shadow" maxlength="1500"></textarea>
		<script>
			var placeholder="\r\n번역할 내용을 입력하세요.(언어감지) \r\n\r\n"+
							"* 문장/문단 단위로 입력하셔야 구분이 편하십니다.\r\n"+
							"* 한번에 최대 1500자 까지만 번역이가능합니다.\r\n"+
							"* 글자수는 공백을 포함한 단위입니다.";
			$("#translateArea").attr('placeholder', placeholder);
		</script>
	

    <div class="inputBox" style="height:50px; background:#F3F3F3; vertical-align:middle; line-height: 40px; ">
      <div id="left" style="float:left; width:330px; text-align:left;">
        <label for="">번역기 선택 : </label>
        <select id="selectBox1" class="selectBox2" style="text-align:left;">
          <option value="0">All</option>
          <option value="3">Google</option>
          <option value="1">Papago</option>
          <option value="2">Kakao</option>
        </select>
      </div>
      <div id="middle" style="text-align:center; float:left; width:340px; ">
        <span id="translateBtn" class="translateBtn">
          <i class="fas fa-play-circle fa-2x"></i>
        </span>
      </div>
		
      <!-- <span style="display:inline-block; width:100px;"></span>
      <span style="display:inline-block; width:100px;"></span> -->
      <!-- 번역언어 선택 -->
      <div id="right" style="float:left; width:330px; text-align:right;">
        <label for="">번역언어 선택 : </label>
        <select id="selectBox2" class="selectBox2">
    		<option value="ko">한국어</option><!-- 카카오 : kr -->
    		<option value="en">영어</option>
    		<option value="ja">일본어</option><!-- 카카오 : jp -->
    		<option value="zh-CN">중국어(간체)</option> <!-- 카카오 cn -->
    		<option value="zh-TW">중국어(번체)</option> <!-- 카카오 cn -->
    		<option value="es">스페인어</option>
    		<option value="fr">프랑스어</option>
    		<option value="vi">배트남어</option>
    		<option value="th">태국어</option>
    		<option value="id">인도네시아어</option>
    	</select>
      </div>
      <script type="text/javascript">
  			$(function(){
  				$('#selectBox1').selectlist({ //이걸 호출해줘야함.
  					zIndex: 1,
  					width: 110,
  					height: 40
  				});
          $('#selectBox2').selectlist({ //이걸 호출해줘야함.
  					zIndex: 1,
  					width: 150,
  					height: 40
  				});
  			})
  		</script>

    </div><!-- 띄우기용 -->
	<div class="inputBox shadow">
      <textarea id="textArea_google" class="textArea_google positionadjust" readonly="readonly">
      </textarea>
    </div>	
    <div class="inputBox shadow">
      	<textarea id="textArea_naver" class="textArea_naver positionadjust" readonly="readonly">
			
      </textarea>
    </div>
    <div class="inputBox shadow">
      <textarea id="textArea_kakao" class="textArea_kakao positionadjust" readonly="readonly">
			
      </textarea>
    </div>
    <div style="background:#F3F3F3; width:1000px; margin:15px auto;">
    	<link href="https://fonts.googleapis.com/css?family=Cookie" rel="stylesheet">
    	<a class="bmc-button" target="_blank" href="https://www.buymeacoffee.com/BWVU2uF" style="text-align:center;">
    		<img src="https://cdn.buymeacoffee.com/buttons/bmc-new-btn-logo.svg" alt="Buy me a coffee"><span style="margin-left:15px;">응원의 글과 커피한잔의 후원 부탁드립니다.</span>
    	</a>
    </div>
    <jsp:include page="footer.jsp" flush="false"></jsp:include>
  </body>
</html>
<script src="resources/js/translateMain.js"></script>