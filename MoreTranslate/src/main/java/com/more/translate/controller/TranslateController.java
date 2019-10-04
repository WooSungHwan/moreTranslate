package com.more.translate.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.more.translate.CommonConstants;
import com.more.translate.model.TransVO;

@Controller
public class TranslateController {
	
	/**
	 * 특정 게시물의 댓글 목록 확인.
	 */
	/*@GetMapping(value="/pages/{bno}/{page}",
			produces= {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno){
		
		Criteria cri = new Criteria(page,10);
		log.info("get Reply List bno : "+bno);
		log.info(cri);
		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}
	*/
	
	@GetMapping("/moreTranslate")
	public String moreTranslateMain(HttpServletRequest req, HttpServletResponse resp) {
		return "moreTranslate";
	}
	
	
	@RequestMapping(value = "/papago", method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject papagoTranslate(HttpServletRequest req, HttpServletResponse resp, TransVO vo) {
		//String lang = req.getParameter("lang"); //return lang
		//String value = req.getParameter("value"); //사용자 텍스트
		
		String sensor = languageSensor(vo.getValue()); //여기서 언어감지함.
		JSONObject json = new JSONObject();
		if(sensor.equals(vo.getLang())) {
			json.put("result",-1);
			return json;
		}else {
			json = naverNMT(sensor, vo.getValue(), vo.getLang()); // 타겟언어타입, 값, 번역언어타입
			json.put("result",1);
			return json;
		}
	}
	
	@RequestMapping(value = "/kakao", method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject kakaoTranslate(HttpServletRequest req, HttpServletResponse resp, TransVO vo) {
		String sensor = languageSensor(vo.getValue()); //여기서 언어감지함.
		JSONObject json = new JSONObject();
		if(sensor.equals(vo.getLang())) {
			json.put("result",-1);
			return json;
		}else {
			json = kakao(sensor, vo.getValue(), vo.getLang()); // 타겟언어타입, 값, 번역언어타입
			json.put("result",1);
			json.put("resultLang", sensor);
			return json;
		}
	}
	
	private String languageSensor(String value) {

        try {
            String query = URLEncoder.encode(value, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", CommonConstants.PAPAGO_CLIENTID);
            con.setRequestProperty("X-Naver-Client-Secret", CommonConstants.PAPAGO_CLIENTSECRET);
            // post request
            String postParams = "query=" + query;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            return response.toString().replace("{\"langCode\":\"","").replace("\"}", "");
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
	}
	
	private JSONObject naverNMT(String sensor, String value, String lang) {
        try {
            String text = URLEncoder.encode(value, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", CommonConstants.PAPAGO_CLIENTID);
            con.setRequestProperty("X-Naver-Client-Secret", CommonConstants.PAPAGO_CLIENTSECRET);
            // post request
            String postParams = "source="+sensor+"&target="+lang+"&text=" + text; //타겟은 클라이언트가 선택
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            //System.out.println(response.toString());
            JSONParser jsonParser = new JSONParser();
            
            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
            
            return jsonObject;
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return null;
	}
	
	private JSONObject kakao(String sensor, String value, String lang) {
		try {	
			String text = URLEncoder.encode(value,"UTF-8");
			sensor = changeKakaoLang(sensor);
			lang = changeKakaoLang(lang);
			String postParams = "src_lang="+sensor.trim()+"&target_lang="+lang.trim()+"&query="+text;
			String apiURL = "https://kapi.kakao.com/v1/translation/translate?"+postParams;
			URL url = new URL(apiURL);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			String userCredentials = CommonConstants.KAKAO_APIKEY;
			String basicAuth = "KakaoAK "+userCredentials;
			con.setRequestProperty("Authorization", basicAuth);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("charset", "utf-8");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
			
			System.out.println("responseCode >> "+responseCode);
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else { //에러발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			JSONParser jsonParser = new JSONParser();
            System.out.println(res.toString());
            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject = (JSONObject) jsonParser.parse(res.toString());
            return jsonObject;
		} catch (Exception e) {
			System.out.println("--확인용 -- 오류 발생");
			e.printStackTrace();
		}
		return null;
	}
	
	private String changeKakaoLang(String lang) {
		String result = "";
		
		if(lang.equals("ko")) result = "kr";
		else if(lang.equals("ja")) result = "jp";
		else if(lang.equals("zh-CN")) result = "cn";
		else if(lang.equals("zh-TW")) result = "cn";
		else result = lang;
		return result;
	}
}
