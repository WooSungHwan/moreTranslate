/**
 * 
 */

$(document).ready(function(){
	if(isMobile()){
		
		$("body").width("600px");
		$('.inputBox').width("600px");
		$("header").width("568px");
		$("#middle").width("85px");
		$("#left").width("230px");
		$("#right").width("279px");
		$("#translateArea").width("578px");
		$("#translateArea").height("500px");
		$("#translateArea").css("font-size", "30px");
		$(".textArea_naver").css("font-size","30px").height("400px");
		$(".textArea_kakao").css("font-size","30px").height("400px");;
		$(".textArea_google").css("font-size","30px").height("400px");;
	}
	eventBind();
});

//skills들 이벤트
function isMobile() {
	return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function mobileEvent(){
	
}

function eventBind(){
	$("#translateBtn").on("click",function(){
		let form = $("form");
		let vendor = $("#selectBox1").find("input[name=selectBox1]").val()
		let lang = $("#selectBox2").find("input[name=selectBox2]").val(); //return 언어값
		let value = $(".translateArea")[0].value;
		
		let isPapago = (vendor == 0 || vendor == 1);
		let isKakao = (vendor == 0 || vendor == 2);
		let isGoogle = (vendor == 0 || vendor == 3);
		
		//ALL 선택시 한번 alert뜨면 다같이 뜨지 않도록
		let isFirst = true;
		
		//모든 칸 초기화
		$("#textArea_naver").empty();
		$("#textArea_kakao").empty();
		$("#textArea_google").empty();
		
		if(value==""){
			alert("내용을 입력해주시기 바랍니다.");
			return;
		}
		
		//vendor : 0 -> 모두다, 1 -> 파파고, 2 -> 카카오, 3 -> 구글
		//lang : 그냥 전달. -> 세개의 벤더들이 다 같은지 봐야함. -> 다르고 처리했음.
		var params = {};
		params.lang = lang;
		params.value = value;
		//로딩표시기
		
		if(isPapago){
			$.ajax({
				type:'post',
				url:'/papago',
				dataType:'json',
				data:params,
				success:function(result){
					console.log(result);
					let resultValue = "";
					var errorCode = result.errorCode;
					
					if(errorCode == "N2MT06") { // 제공안되는 번역기
						resultValue="죄송합니다. 입력하신 언어에서 선택하신 언어로는 번역기가 제공되지 않습니다.";
					}
					
					if(errorCode == "N2MT09" && isFirst) { // 언어를 감지하지 못함.
						isFirst = false;
						alert("죄송합니다. 언어를 감지하는데 실패했습니다. 정확한 문장을 입력해주세요.");
						return;
					}
					
					if(errorCode == "N2MT99") { // 제공안되는 번역기
						alert("papago : 알 수 없는 오류 발생.");
						return;
					}
					
					if(result.result == -1 && isFirst){ //같은 언어 타겟 소스
						//로딩표시기 종료
						isFirst = false;
						alert("같은 언어끼리는 번역하실 수 없습니다.");
						return;
					}
					
					if(result.result == 1 && !errorCode){
						resultValue = result.message.result.translatedText;
					}
					$("#textArea_naver").text(resultValue);
				}
				,error:function(a,b,c){
					console.log(a,b,c);
				}
				
			});
		}
		
		if(isKakao){ //웬만하면 모든 채널 되는 듯.
			$.ajax({
				type:'post',
				url:'/kakao',
				dataType:'json',
				data:params,
				success:function(result){
					console.log(result);
					let resultValue = "";
					var errorCode = result.errorCode;
					
					if(result.result == -1 && isFirst){ //같은 언어 타겟 소스
						//로딩표시기 종료
						isFirst = false;
						alert("같은 언어끼리는 번역하실 수 없습니다.");
						return;
					}
					
					if(result.resultLang == 'unk' && isFirst){
						isFirst = false;
						alert("죄송합니다. 언어를 감지하는데 실패했습니다. 정확한 문장을 입력해주세요.");
						return;
					}
					
					if(result.result == 1){
						resultValue = result.translated_text[0][0];
					}
					$("#textArea_kakao").text(resultValue);
					
				},error:function(a,b,c){
					console.log(a,b,c);
				}
			});
		
		}
		if(isGoogle){
			let source_lang = "";
			let target_lang = "";
	    	$.ajax({
	            type:"POST",
	            data:'q=' + value,
	            url:'https://translation.googleapis.com/language/translate/v2/detect?key=AIzaSyBgsl1DeUzESAeIDatvgtlUMH57jlop3DA',

	            success:function(response){
	            	source_lang = response.data.detections[0][0].language
	            	target_lang = lang;
	            	
	                console.log("google source_lang : "+response.data.detections[0][0].language);
	                console.log("google target_lang : "+lang);
	                
	                $.ajax({
	        	        type:"POST",
	        	        data:'&target='+lang+'&format=html&q=' + value,
	        	        url:'https://www.googleapis.com/language/translate/v2?key=AIzaSyBgsl1DeUzESAeIDatvgtlUMH57jlop3DA',

	        	        success:function(response){
	        	        	
	        	        	let resultValue = response.data.translations[0].translatedText;
	        	        	console.log(resultValue);
	        	        	resultValue = resultValue.replace("&#39;","'")
	        	        	$("#textArea_google").text(resultValue);
	        	        }
	        	    });
	            }
	        });
		}
	});
}
