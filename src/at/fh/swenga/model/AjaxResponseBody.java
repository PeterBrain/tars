package at.fh.swenga.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonView;

public class AjaxResponseBody {
	@JsonView(Views.Public.class)
	String msg;

	@JsonView(Views.Public.class)
	String code;

	@JsonView(Views.Public.class)
	ArrayList<ArrayList<String>> result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<ArrayList<String>> getResult() {
		return result;
	}

	public void setResult(ArrayList<ArrayList<String>> result) {
		this.result = result;
	}
}
