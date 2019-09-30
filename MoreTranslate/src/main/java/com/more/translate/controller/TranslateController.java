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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.more.translate.CommonConstants;

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
	
	@PostMapping(value="/papago",
			produces= {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<String> papagoTranslate(HttpServletRequest req, HttpServletResponse resp) {
		
		String value = req.getParameter("str");
		String result = "";
		
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
            result = response.toString().replace("{\"langCode\":\"","").replace("\"}", "");
            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
	}
	
	@PostMapping("/kakao")
	public void kakaoTranslate(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	@PostMapping("/google")
	public void googleTranslate(HttpServletRequest req, HttpServletResponse resp) {
		
	}
}
