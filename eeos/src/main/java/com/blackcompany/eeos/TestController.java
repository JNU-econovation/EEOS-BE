package com.blackcompany.eeos;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class TestController {

	@Profile("local")
	@RequestMapping("/api/members/test")
	@RestController
	public static class Members {
		@GetMapping
		public ApiResponse<SuccessBody<Void>> membersGetTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}

		@PostMapping
		public ApiResponse<SuccessBody<Void>> membersPostTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@DeleteMapping
		public ApiResponse<SuccessBody<Void>> membersDeleteTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
		}
	}

	@Profile("local")
	@RequestMapping("/api/programs/test")
	@RestController
	public static class Programs {

		@PostMapping
		public ApiResponse<SuccessBody<Void>> programsPostTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@PutMapping
		public ApiResponse<SuccessBody<Void>> programsPutTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
		}

		@GetMapping
		public ApiResponse<SuccessBody<Void>> programsGetTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}
	}

	@Profile("local")
	@RequestMapping("/api/teams/test")
	@RestController
	public static class Teams {

		@GetMapping
		public ApiResponse<SuccessBody<Void>> teamsGetTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}

		@PostMapping
		public ApiResponse<SuccessBody<Void>> teamsPostTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@PutMapping
		public ApiResponse<SuccessBody<Void>> teamsPutTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
		}
	}

	@Profile("local")
	@RequestMapping("/api/comments/test")
	@RestController
	public static class Comments {
		@GetMapping
		public ApiResponse<SuccessBody<Void>> commentsGetTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}

		@PostMapping
		public ApiResponse<SuccessBody<Void>> commentsPostTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@PutMapping
		public ApiResponse<SuccessBody<Void>> commentsPutTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
		}
	}

	@Profile("local")
	@RequestMapping("/api/attend/test")
	@RestController
	public static class Attend {
		@GetMapping
		public ApiResponse<SuccessBody<Void>> getTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}

		@PostMapping
		public ApiResponse<SuccessBody<Void>> postTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@PutMapping
		public ApiResponse<SuccessBody<Void>> putTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
		}

		@DeleteMapping
		public ApiResponse<SuccessBody<Void>> deleteTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
		}
	}

	@Profile("local")
	@RequestMapping("/api/admin/test")
	@RestController
	public static class Admin {
		@GetMapping
		public ApiResponse<SuccessBody<Void>> getTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
		}

		@PostMapping
		public ApiResponse<SuccessBody<Void>> postTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
		}

		@PutMapping
		public ApiResponse<SuccessBody<Void>> putTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
		}

		@DeleteMapping
		public ApiResponse<SuccessBody<Void>> deleteTest() {
			return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
		}
	}
}
