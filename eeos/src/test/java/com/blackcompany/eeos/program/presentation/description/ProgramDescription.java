package com.blackcompany.eeos.program.presentation.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class ProgramDescription {
	public static FieldDescriptor[] browseProgramOnlyId() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로그램"),
			fieldWithPath("data.programId").type(JsonFieldType.NUMBER).description("프로그램 id"),
		};
	}

	public static FieldDescriptor[] browseProgram() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로그램"),
			fieldWithPath("data.programId").type(JsonFieldType.NUMBER).description("프로그램 id"),
			fieldWithPath("data.title").type(JsonFieldType.STRING).description("프로그램 제목"),
			fieldWithPath("data.programDate").type(JsonFieldType.NUMBER).description("프로그램 마감기한"),
			fieldWithPath("data.content").type(JsonFieldType.STRING).description("프로그램 내용"),
			fieldWithPath("data.programStatus").type(JsonFieldType.STRING).description("프로그램 상태"),
		};
	}

	public static FieldDescriptor[] browsePrograms() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로그램 페이징 정보"),
			fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("한 페이지 당 프로그램 갯수"),
			fieldWithPath("data.page").type(JsonFieldType.NUMBER).description("현 페이지"),
			fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지 갯수"),
			fieldWithPath("data.programs[]").type(JsonFieldType.ARRAY).description("프로그램"),
			fieldWithPath("data.programs[].programId").type(JsonFieldType.NUMBER).description("프로그램 id"),
			fieldWithPath("data.programs[].title").type(JsonFieldType.STRING).description("프로그램 제목"),
			fieldWithPath("data.programs[].programDate")
					.type(JsonFieldType.NUMBER)
					.description("프로그램 마감기한"),
			fieldWithPath("data.programs[].content").type(JsonFieldType.STRING).description("프로그램 내용"),
			fieldWithPath("data.programs[].programId").type(JsonFieldType.NUMBER).description("프로그램 id"),
			fieldWithPath("data.programs[].title").type(JsonFieldType.STRING).description("프로그램 제목"),
			fieldWithPath("data.programs[].programDate")
					.type(JsonFieldType.NUMBER)
					.description("프로그램 마감기한"),
			fieldWithPath("data.programs[].content").type(JsonFieldType.STRING).description("프로그램 내용"),
		};
	}
}
