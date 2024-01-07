package com.blackcompany.eeos.program.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.presentation.description.Description;
import com.blackcompany.eeos.program.presentation.description.ProgramDescription;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class ProgramControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	private static final String TAG = "Program-Controller";
	private static final String BASE_URL = "/api/programs/";

	@Test
	void create() throws Exception {
		CreateProgramRequest request =
				CreateProgramRequest.builder()
						.title("title")
						.deadLine(Timestamp.valueOf(LocalDateTime.now()))
						.content("content")
						.build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(post(BASE_URL, 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(
						document(
								"create_program",
								resource(
										ResourceSnippetParameters.builder()
												.description("프로그램을 생성한다.")
												.tag(TAG)
												.requestSchema(Schema.schema("CreateProgramRequest"))
												.responseSchema(Schema.schema("CreateProgramResponse"))
												.responseFields(
														Description.created(ProgramDescription.browseProgramOnlyId()))
												.build())));
	}

	@Test
	void findOne() throws Exception {
		mockMvc
				.perform(get(BASE_URL + "/{id}", 1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"get_program",
								resource(
										ResourceSnippetParameters.builder()
												.description("프로그램을 조회한다.")
												.tag(TAG)
												.requestSchema(Schema.schema("GetProgramRequest"))
												.responseSchema(Schema.schema("GetProgramResponse"))
												.responseFields(Description.get(ProgramDescription.browseProgram()))
												.build())));
	}

	@Test
	void update() throws Exception {
		UpdateProgramRequest request =
				UpdateProgramRequest.builder()
						.title("title update")
						.deadLine(Timestamp.valueOf(LocalDateTime.now()))
						.content("content update")
						.build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						put(BASE_URL + "/{id}", 1).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"update_program",
								resource(
										ResourceSnippetParameters.builder()
												.description("프로그램을 업데이트한다.")
												.tag(TAG)
												.requestSchema(Schema.schema("UpdateProgramRequest"))
												.responseSchema(Schema.schema("UpdateProgramResponse"))
												.responseFields(
														Description.updated(ProgramDescription.browseProgramOnlyId()))
												.build())));
	}

	@Test
	void findAll() throws Exception {
		mockMvc
				.perform(
						get(BASE_URL, 0)
								.queryParam("programStatus", "active")
								.queryParam("size", "2")
								.queryParam("page", "0")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"get_programs",
								resource(
										ResourceSnippetParameters.builder()
												.description("프로그램을 조회한다.")
												.tag(TAG)
												.requestSchema(Schema.schema("GetProgramsRequest"))
												.responseSchema(Schema.schema("GetProgramsResponse"))
												.responseFields(Description.get(ProgramDescription.browsePrograms()))
												.build())));
	}
}
