package com.blackcompany.eeos.slackEvent.application.repository;

// 멱등성 보장을 위한 repository
public interface SlackEventDedupRepository {

	boolean isProcessed(String eventId);

	boolean tryLock(String eventId);

	void markProcessed(String eventId);

	void unlock(String eventId);
}
