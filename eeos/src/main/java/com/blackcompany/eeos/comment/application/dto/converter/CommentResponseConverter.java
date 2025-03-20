package com.blackcompany.eeos.comment.application.dto.converter;

import com.blackcompany.eeos.comment.application.dto.CommandCommentResponse;
import com.blackcompany.eeos.comment.application.dto.QueryAnswerResponse;
import com.blackcompany.eeos.comment.application.dto.QueryCommentResponse;
import com.blackcompany.eeos.comment.application.dto.QueryCommentsResponse;
import com.blackcompany.eeos.comment.application.exception.NotConvertedCommentException;
import com.blackcompany.eeos.comment.application.model.CommentModel;
import com.blackcompany.eeos.comment.application.model.CommentType;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.stream.events.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentResponseConverter {

	private final MemberRepository memberRepository;
	private final String ANONYMOUS_USER_NAME = "익명";

	public CommandCommentResponse from(CommentModel source) {
		return CommandCommentResponse.builder().commentId(source.getId()).build();
	}

	public QueryCommentsResponse from(List<QueryCommentResponse> responses) {
		responses.stream().forEach(r -> r.getAnswers().stream().forEach(a -> a.toString()));
		return QueryCommentsResponse.builder().comments(responses).build();
	}

	public QueryCommentResponse from(Long memberId, CommentModel source, List<CommentModel> answers) {

		List<QueryAnswerResponse> answersResponse =
				answers.stream().map(e -> from(e, memberId)).collect(Collectors.toList());

		return QueryCommentResponse.builder()
				.time(getCreateTimeLong(source))
				.content(source.getContent())
				.teamId(source.getPresentingTeam())
				.writer(findMemberName(source.getWriter(), source))
				.commentId(source.getId())
				.accessRight(source.getAccessRight(memberId))
				.answers(answersResponse)
				.build();
	}

	private QueryAnswerResponse from(CommentModel source, Long memberId) {
		if (source.getSuperCommentId() == -1) throw new NotConvertedCommentException();
		QueryAnswerResponse response =
				QueryAnswerResponse.builder()
						.commentId(source.getId())
						.content(source.getContent())
						.writer(findMemberName(source.getWriter(), source))
						.time(getCreateTimeLong(source))
						.accessRight(source.getAccessRight(memberId))
						.build();
		return response;
	}

	private String findMemberName(Long memberId, CommentModel source) {
		if (source.getCommentType().equals(CommentType.ANONYMOUS)) {
			return ANONYMOUS_USER_NAME;
		}
		return memberRepository.findById(memberId).getName();
	}

	private String getCreateTimeString(CommentModel model) {
		return model
				.getCreatedDate()
				.toLocalDateTime()
				.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
	}

	private Long getCreateTimeLong(CommentModel model){
		return model
				.getCreatedDate()
				.getTime();
	}
}
