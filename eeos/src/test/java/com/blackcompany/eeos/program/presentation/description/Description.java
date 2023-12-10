package com.blackcompany.eeos.program.presentation.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class Description {
	private static FieldDescriptor getCreateCodeDescriptor() {
		return fieldWithPath("code").type(JsonFieldType.STRING).description("201");
	}

	private static FieldDescriptor getCreateMessageDescriptor() {
		return fieldWithPath("message").type(JsonFieldType.STRING).description("생성 성공");
	}

	public static FieldDescriptor[] created(FieldDescriptor[] data) {
		return ArrayUtils.addAll(data, getCreateMessageDescriptor(), getCreateCodeDescriptor());
	}

	private static FieldDescriptor getUpdateCodeDescriptor() {
		return fieldWithPath("code").type(JsonFieldType.STRING).description("200");
	}

	private static FieldDescriptor getUpdateMessageDescriptor() {
		return fieldWithPath("message").type(JsonFieldType.STRING).description("수정 성공");
	}

	public static FieldDescriptor[] updated(FieldDescriptor[] data) {
		return ArrayUtils.addAll(data, getUpdateMessageDescriptor(), getUpdateCodeDescriptor());
	}

	private static FieldDescriptor getCodeDescriptor() {
		return fieldWithPath("code").type(JsonFieldType.STRING).description("200");
	}

	private static FieldDescriptor getMessageDescriptor() {
		return fieldWithPath("message").type(JsonFieldType.STRING).description("조회 성공");
	}

	public static FieldDescriptor[] get(FieldDescriptor[] data) {
		return ArrayUtils.addAll(data, getMessageDescriptor(), getCodeDescriptor());
	}
}
