package com.blackcompany.eeos.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class AcceptanceTestSteps {

	public static ExtractableResponse<Response> get(String path) {
		return RestAssured.given()
				.log()
				.all()
				.when()
				.get(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> get(String path, String token) {
		return RestAssured.given()
				.log()
				.all()
				.header("Authorization", "Bearer " + token)
				.when()
				.get(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> post(String path, Object body) {
		return RestAssured.given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(body)
				.when()
				.post(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> post(String path, Object body, String token) {
		return RestAssured.given()
				.log()
				.all()
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(body)
				.when()
				.post(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> put(String path, Object body, String token) {
		return RestAssured.given()
				.log()
				.all()
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(body)
				.when()
				.put(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> patch(String path, Object body, String token) {
		return RestAssured.given()
				.log()
				.all()
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(body)
				.when()
				.patch(path)
				.then()
				.log()
				.all()
				.extract();
	}

	public static ExtractableResponse<Response> delete(String path, String token) {
		return RestAssured.given()
				.log()
				.all()
				.header("Authorization", "Bearer " + token)
				.when()
				.delete(path)
				.then()
				.log()
				.all()
				.extract();
	}
}
