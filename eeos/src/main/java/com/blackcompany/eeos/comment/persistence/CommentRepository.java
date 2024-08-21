package com.blackcompany.eeos.comment.persistence;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	@Query(
			"SELECT c FROM CommentEntity c WHERE c.programId=:programId AND c.presentingTeamId=:teamId ORDER BY c.createdDate ASC")
	List<CommentEntity> findCommentByProgramIdAndPresentingTeamId(
			@Param("programId") Long programId, @Param("teamId") Long teamId);

	@Query(
			"SELECT c FROM CommentEntity c WHERE c.superCommentId=:commentId ORDER BY c.createdDate ASC")
	List<CommentEntity> findCommentBySuperCommentId(@Param("commentId") Long commentId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE CommentEntity c SET c.content=:content WHERE c.id=:commentId")
	int updateById(@Param("commentId") Long commentId, @Param("content") String content);

	@Transactional
	@Modifying
	void deleteById(@NonNull Long commentId);
}
