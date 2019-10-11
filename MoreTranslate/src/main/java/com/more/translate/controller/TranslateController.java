package com.more.translate.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.more.translate.CommonConstants;
import com.more.translate.model.ResultVO;
import com.more.translate.model.TransVO;

@Controller
public class TranslateController {
	
	@GetMapping("/moreTranslate")
	public String moreTranslateMain(HttpServletRequest req, HttpServletResponse resp) {
		return "moreTranslate";
	}
	
	
	@RequestMapping(value = "/papago", method = { RequestMethod.POST }, produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ResultVO> papagoTranslate(HttpServletRequest req, HttpServletResponse resp, TransVO vo) {
		String sensor = languageSensor(vo.getValue()); //여기서 언어감지함.
		ResultVO result = new ResultVO();
		
		if(sensor.equals(vo.getLang())) {
			result.setResult(-1);
		}else {
			StringBuffer response = naverNMT(sensor, vo.getValue(), vo.getLang()); // 타겟언어타입, 값, 번역언어타입
			result.setResult(1);
			result.setResp(response.toString());
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/kakao", method = { RequestMethod.POST }, produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ResultVO> kakaoTranslate(HttpServletRequest req, HttpServletResponse resp, TransVO vo) {
		String sensor = languageSensor(vo.getValue()); //여기서 언어감지함.
		ResultVO result = new ResultVO();
		if(sensor.equals(vo.getLang())) {
			result.setResult(-1);
		}else {
			result.setResult(1);
			result.setResp(kakao(sensor, vo.getValue(), vo.getLang()));
			 // 타겟언어타입, 값, 번역언어타입
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
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
	
	private StringBuffer naverNMT(String sensor, String value, String lang) {
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
            return response;
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return null;
	}
	
	private String kakao(String sensor, String value, String lang) {
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
			
			return res.toString();
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
