package com.more.translate.model;

import com.google.api.client.http.HttpResponse;

import lombok.Data;

@Data
public class ResultVO {
	private int result;
	private String resultLang;
	private String resp; 
}
