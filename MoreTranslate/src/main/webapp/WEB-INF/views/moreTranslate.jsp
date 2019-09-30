<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>모아번역, 한번에 비교하여 번역하자</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css">
    <link rel="stylesheet" href="resources/css/translateStyle.css">
    <link rel="shortcut icon" href="language_VCk_icon.ico" type="image/x-icon">
    <link rel="icon" href="language_VCk_icon.ico" type="image/x-icon">
  </head>
  <body>
      <header>
        <h1>모아번역</h1>
        <p>파파고, 카카오, 구글 3대 번역 기술을 한데모아 번역하자</p>
      </header>
      <textarea class="translateArea shadow"
      placeholder="번역할 내용을 입력하세요.(언어감지)"
      maxlength="1000"></textarea>

    <div class="inputBox" style="height:50px; background:#F3F3F3; vertical-align:middle; line-height: 40px; ">
      <div style="float:left; width:330px; text-align:left;">
        <label for="">번역기 선택 : </label>
        <select id="selectBox1" class="selectBox2" style="text-align:left;">
          <option value="0">All</option>
          <option value="1">Papago</option>
          <option value="2">Kakao</option>
          <option value="3">Google</option>
        </select>
      </div>

  		<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
  		<script src="resources/css/jquery.selectlist.js"></script>
      <div style="text-align:center; float:left; width:340px; ">
        <span class="translateBtn">
          <i class="fas fa-exchange-alt"></i>
        </span>
      </div>

      <!-- <span style="display:inline-block; width:100px;"></span>
      <span style="display:inline-block; width:100px;"></span> -->
      <!-- 번역언어 선택 -->
      <div style="float:left; width:330px; text-align:right;">
        <label for="">번역언어 선택 : </label>
        <select id="selectBox2" class="selectBox2">
    		  <option value="ko">한국어</option>
    		  <option value="en">영어</option>
    		  <option value="jp">일본어</option>
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
  					width: 100,
  					height: 40
  				});
  			})
  		</script>

    </div><!-- 띄우기용 -->

    <div class="inputBox shadow">
      	<div class="textArea_naver">
			네이버
      </div>
    </div>
    <div class="inputBox shadow">
      <div class="textArea_kakao">
			카카오
      </div>
    </div>
    <div class="inputBox shadow">
      <div class="textArea_google">
			구글
      </div>
    </div>
  </body>
</html>
