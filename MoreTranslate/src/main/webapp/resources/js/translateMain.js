/**
 * 
 */

$(document).ready(function(){
	if(isMobile()){
		
		$("body").width("600px");
		$('.inputBox').width("600px");
		$("header").width("568px");
		$("footer").width("568px");
		$("#middle").width("85px");
		$("#left").width("230px");
		$("#right").width("279px");
		$("#translateArea").width("578px");
		$("#translateArea").height("500px");
		$("#translateArea").css("font-size", "30px");
		$(".textArea_naver").css("font-size","30px").height("400px").width("572px");
		$(".textArea_kakao").css("font-size","30px").height("400px").width("572px");
		$(".textArea_google").css("font-size","30px").height("400px").width("572px");
		$("h1").css("font-size","3rem");
		$("p").css("font-size","25px");
		$(".bmc-button").width("576px");
		
	}
	eventBind();
});

//skills들 이벤트
function isMobile() {
	return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
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
		
		if(isPapago){
			$.ajax({
				type:'POST',
				url:'/api/papago',
				accept:"application/json",
				contentType:"application/json",
				dataType:'JSON',
				data:JSON.stringify(params),
				success:function(result){
					var finalResult = JSON.parse(result.resp);
					
					let resultValue = "";
					var errorCode = finalResult.errorCode;
					
					if(errorCode == "N2MT06") { // 제공안되는 번역기
						resultValue="죄송합니다. 입력하신 언어에서 선택하신 언어로는 번역기가 제공되지 않습니다.";
					}
					
					if(errorCode == "N2MT09" && isFirst) { // 언어를 감지하지 못함.
						isFirst = false;
						alert("죄송합니다. 언어를 감지하는데 실패했습니다. 정확한 문장을 입력해주세요.");
						return;
					}
					
					if(errorCode == "010"){
						resultValue = "죄송합니다. 현재 파파고 번역기는 일일 사용량을 초과하였습니다.";
						$("#textArea_naver").text(resultValue);
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
						resultValue = finalResult.message.result.translatedText;
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
				type:'POST',
				url:'/api/kakao',
				dataType:'JSON',
				accept: "application/json",
				contentType:"application/json",
				data:JSON.stringify(params),
				success:function(result){
					
					var finalResult = JSON.parse(result.resp);

					let resultValue = "";
					var errorCode = result.errorCode;
					
					if(result.result == -1 && isFirst){ //같은 언어 타겟 소스
						//로딩표시기 종료
						isFirst = false;
						alert("같은 언어끼리는 번역하실 수 없습니다.");
						return;
					}
					
					if(finalResult.resultLang == 'unk' && isFirst){
						isFirst = false;
						alert("죄송합니다. 언어를 감지하는데 실패했습니다. 정확한 문장을 입력해주세요.");
						return;
					}
					
					if(result.result == 1 || isFirst){
						for(var i =0; i<finalResult.translated_text.length; i++){
							for(var j =0; j<finalResult.translated_text[i].length; j++){
								resultValue += finalResult.translated_text[i][j]+" ";
							}
						}
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
	        	        	
	        	        	let resultValue = response.data.translations[0].translatedText+"";
	        	        	resultValue = resultValue.replace(/&#39;/g,"'");
	        	        	$("#textArea_google").text(resultValue);
	        	        }
	        	    });
	            }
	        });
		}
	});
}
