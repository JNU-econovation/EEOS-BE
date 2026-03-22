#!/bin/bash
# Web/App 인증 분리 API 통합 테스트 스크립트
# 사용법: ./test-auth-api.sh [BASE_URL]
# 예: ./test-auth-api.sh http://localhost:8080

set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"
PASS=0
FAIL=0
TOTAL=0

# 색상
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

assert_status() {
	local test_name="$1"
	local expected="$2"
	local actual="$3"
	local body="${4:-}"
	TOTAL=$((TOTAL + 1))

	if [ "$actual" -eq "$expected" ]; then
		echo -e "${GREEN}[PASS]${NC} $test_name (HTTP $actual)"
		PASS=$((PASS + 1))
	else
		echo -e "${RED}[FAIL]${NC} $test_name (expected $expected, got $actual)"
		if [ -n "$body" ]; then
			echo "       Response: $(echo "$body" | head -3)"
		fi
		FAIL=$((FAIL + 1))
	fi
}

assert_contains() {
	local test_name="$1"
	local expected="$2"
	local body="$3"
	TOTAL=$((TOTAL + 1))

	if echo "$body" | grep -q "$expected"; then
		echo -e "${GREEN}[PASS]${NC} $test_name (contains '$expected')"
		PASS=$((PASS + 1))
	else
		echo -e "${RED}[FAIL]${NC} $test_name (missing '$expected')"
		echo "       Response: $(echo "$body" | head -3)"
		FAIL=$((FAIL + 1))
	fi
}

assert_redirect() {
	local test_name="$1"
	local expected_status="$2"
	local expected_location_contains="$3"
	local actual_status="$4"
	local actual_location="$5"
	TOTAL=$((TOTAL + 1))

	if [ "$actual_status" -eq "$expected_status" ] && echo "$actual_location" | grep -q "$expected_location_contains"; then
		echo -e "${GREEN}[PASS]${NC} $test_name (HTTP $actual_status → $expected_location_contains)"
		PASS=$((PASS + 1))
	else
		echo -e "${RED}[FAIL]${NC} $test_name (status=$actual_status, location=$actual_location)"
		FAIL=$((FAIL + 1))
	fi
}

echo ""
echo "======================================"
echo " Web/App 인증 분리 API 테스트"
echo " Base URL: $BASE_URL"
echo "======================================"
echo ""

# ============================================
# 1. Client 등록 API
# ============================================
echo -e "${YELLOW}--- 1. Client 등록 API ---${NC}"

# 1-1. 성공: WEB 클라이언트 등록
# NOTE: 관리자 인증이 필요하므로, 테스트 전 관리자 토큰을 ADMIN_TOKEN에 설정
ADMIN_TOKEN="${ADMIN_TOKEN:-}"

if [ -n "$ADMIN_TOKEN" ]; then
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/clients" \
		-H "Content-Type: application/json" \
		-H "Authorization: Bearer $ADMIN_TOKEN" \
		-d '{
			"clientName": "EEOS-Web-Test",
			"clientType": "WEB",
			"redirectUris": ["http://localhost:3000/callback"]
		}')
	BODY=$(echo "$RESPONSE" | head -n -1)
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "1-1. WEB 클라이언트 등록" 201 "$STATUS" "$BODY"

	if [ "$STATUS" -eq 201 ]; then
		WEB_CLIENT_ID=$(echo "$BODY" | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['clientId'])" 2>/dev/null || echo "")
		WEB_CLIENT_SECRET=$(echo "$BODY" | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['clientSecret'])" 2>/dev/null || echo "")
		assert_contains "1-1a. WEB clientSecret 존재" "clientSecret" "$BODY"
	fi

	# 1-2. 성공: APP 클라이언트 등록
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/clients" \
		-H "Content-Type: application/json" \
		-H "Authorization: Bearer $ADMIN_TOKEN" \
		-d '{
			"clientName": "EEOS-App-Test",
			"clientType": "APP",
			"redirectUris": ["kr.econovation.eeos://callback"]
		}')
	BODY=$(echo "$RESPONSE" | head -n -1)
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "1-2. APP 클라이언트 등록" 201 "$STATUS" "$BODY"

	if [ "$STATUS" -eq 201 ]; then
		APP_CLIENT_ID=$(echo "$BODY" | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['clientId'])" 2>/dev/null || echo "")
	fi

	# 1-3. 실패: redirectUris 비어있음
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/clients" \
		-H "Content-Type: application/json" \
		-H "Authorization: Bearer $ADMIN_TOKEN" \
		-d '{
			"clientName": "Bad-Client",
			"clientType": "WEB",
			"redirectUris": []
		}')
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "1-3. redirectUris 비어있음 → 400" 400 "$STATUS"

	# 1-4. 실패: redirectUri 512자 초과
	LONG_URI="https://example.com/$(python3 -c "print('a'*500)")"
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/clients" \
		-H "Content-Type: application/json" \
		-H "Authorization: Bearer $ADMIN_TOKEN" \
		-d "{
			\"clientName\": \"Bad-Client\",
			\"clientType\": \"WEB\",
			\"redirectUris\": [\"$LONG_URI\"]
		}")
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "1-4. redirectUri 512자 초과 → 400" 400 "$STATUS"

else
	echo -e "${YELLOW}[SKIP]${NC} Client 등록 테스트: ADMIN_TOKEN 미설정"
	echo "       export ADMIN_TOKEN=<관리자토큰> 후 재실행"

	# 수동 테스트용 기본값 설정
	WEB_CLIENT_ID="${WEB_CLIENT_ID:-}"
	APP_CLIENT_ID="${APP_CLIENT_ID:-}"
fi

# ============================================
# 2. /authorize 엔드포인트
# ============================================
echo ""
echo -e "${YELLOW}--- 2. /authorize 엔드포인트 ---${NC}"

if [ -n "${WEB_CLIENT_ID:-}" ]; then
	# 2-1. 성공: 로그인 페이지로 redirect
	RESPONSE=$(curl -s -o /dev/null -w "%{http_code}\n%{redirect_url}" \
		-X GET "$BASE_URL/api/auth/authorize?client_id=$WEB_CLIENT_ID&redirect_uri=http://localhost:3000/callback&response_type=code&state=test123")
	STATUS=$(echo "$RESPONSE" | head -1)
	LOCATION=$(echo "$RESPONSE" | tail -1)
	assert_redirect "2-1. WEB authorize → 로그인 페이지 redirect" 302 "client_id" "$STATUS" "$LOCATION"
fi

# 2-2. 실패: 존재하지 않는 client_id
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X GET "$BASE_URL/api/auth/authorize?client_id=nonexistent&redirect_uri=http://fake.com&response_type=code&state=test123")
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "2-2. 잘못된 client_id → 400" 400 "$STATUS"

# 2-3. 실패: response_type이 code가 아님
if [ -n "${WEB_CLIENT_ID:-}" ]; then
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X GET "$BASE_URL/api/auth/authorize?client_id=$WEB_CLIENT_ID&redirect_uri=http://localhost:3000/callback&response_type=token&state=test123")
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "2-3. response_type=token → 400" 400 "$STATUS"
fi

# 2-4. 실패: APP인데 code_challenge 누락
if [ -n "${APP_CLIENT_ID:-}" ]; then
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X GET "$BASE_URL/api/auth/authorize?client_id=$APP_CLIENT_ID&redirect_uri=kr.econovation.eeos://callback&response_type=code&state=test123")
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "2-4. APP code_challenge 누락 → 400" 400 "$STATUS"
fi

# ============================================
# 3. /login/oauth2 엔드포인트
# ============================================
echo ""
echo -e "${YELLOW}--- 3. /login/oauth2 엔드포인트 ---${NC}"

# 테스트 계정 (환경변수로 설정 가능)
TEST_EMAIL="${TEST_EMAIL:-}"
TEST_PASSWORD="${TEST_PASSWORD:-}"

if [ -n "${WEB_CLIENT_ID:-}" ] && [ -n "$TEST_EMAIL" ]; then
	# 3-1. 성공: WEB 로그인 → 303 + 쿠키
	RESPONSE=$(curl -s -D - -o /dev/null -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=$WEB_CLIENT_ID&redirect_uri=http://localhost:3000/callback&state=test123&email=$TEST_EMAIL&password=$TEST_PASSWORD")
	STATUS=$(echo "$RESPONSE" | tail -1)
	HEADERS=$(echo "$RESPONSE" | head -n -1)
	assert_status "3-1. WEB 로그인 → 303" 303 "$STATUS"

	if echo "$HEADERS" | grep -qi "eeos_access_token"; then
		assert_contains "3-1a. AT 쿠키 존재" "eeos_access_token" "$HEADERS"
	fi

	# 3-2. 실패: 잘못된 credentials → 303 (로그인 페이지로)
	RESPONSE=$(curl -s -o /dev/null -w "%{http_code}\n%{redirect_url}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=$WEB_CLIENT_ID&redirect_uri=http://localhost:3000/callback&state=test123&email=$TEST_EMAIL&password=wrong_password")
	STATUS=$(echo "$RESPONSE" | head -1)
	LOCATION=$(echo "$RESPONSE" | tail -1)
	assert_redirect "3-2. 잘못된 비밀번호 → 303 (로그인 페이지)" 303 "error=invalid_credentials" "$STATUS" "$LOCATION"

	# 3-3. 실패: 잘못된 client_id → 400
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=nonexistent&redirect_uri=http://fake.com&state=test123&email=$TEST_EMAIL&password=$TEST_PASSWORD")
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "3-3. 잘못된 client_id → 400" 400 "$STATUS"

	# 3-4. 실패: redirect_uri 불일치 → 400
	RESPONSE=$(curl -s -w "\n%{http_code}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=$WEB_CLIENT_ID&redirect_uri=http://evil.com/steal&state=test123&email=$TEST_EMAIL&password=$TEST_PASSWORD")
	STATUS=$(echo "$RESPONSE" | tail -1)
	assert_status "3-4. redirect_uri 불일치 → 400" 400 "$STATUS"
else
	echo -e "${YELLOW}[SKIP]${NC} /login/oauth2 테스트: WEB_CLIENT_ID 또는 TEST_EMAIL 미설정"
	echo "       export TEST_EMAIL=<이메일> TEST_PASSWORD=<비밀번호> 후 재실행"
fi

# ============================================
# 4. /token 엔드포인트 (App code 교환)
# ============================================
echo ""
echo -e "${YELLOW}--- 4. /token 엔드포인트 ---${NC}"

# 4-1. 실패: grant_type 미지원
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X POST "$BASE_URL/api/auth/token" \
	-H "Content-Type: application/x-www-form-urlencoded" \
	-d "grant_type=password&code=fake&code_verifier=fake&redirect_uri=fake&client_id=fake")
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "4-1. grant_type=password → 400" 400 "$STATUS"

# 4-2. 실패: 만료/존재하지 않는 code
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X POST "$BASE_URL/api/auth/token" \
	-H "Content-Type: application/x-www-form-urlencoded" \
	-d "grant_type=authorization_code&code=expired_code&code_verifier=fake&redirect_uri=fake&client_id=fake")
BODY=$(echo "$RESPONSE" | head -n -1)
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "4-2. 만료된 code → 400" 400 "$STATUS"

# 4-3. APP PKCE 전체 플로우 (login → code → token)
if [ -n "${APP_CLIENT_ID:-}" ] && [ -n "$TEST_EMAIL" ]; then
	# PKCE: code_verifier → code_challenge 생성
	CODE_VERIFIER="dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
	CODE_CHALLENGE=$(echo -n "$CODE_VERIFIER" | openssl dgst -sha256 -binary | openssl base64 -A | tr '+/' '-_' | tr -d '=')

	# App 로그인 → authorization_code 받기
	RESPONSE=$(curl -s -o /dev/null -w "%{http_code}\n%{redirect_url}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=$APP_CLIENT_ID&redirect_uri=kr.econovation.eeos://callback&state=test456&email=$TEST_EMAIL&password=$TEST_PASSWORD&code_challenge=$CODE_CHALLENGE&code_challenge_method=S256")
	STATUS=$(echo "$RESPONSE" | head -1)
	REDIRECT_URL=$(echo "$RESPONSE" | tail -1)

	if [ "$STATUS" -eq 303 ]; then
		# redirect URL에서 code 추출
		AUTH_CODE=$(echo "$REDIRECT_URL" | grep -oP 'code=\K[^&]+' || echo "")

		if [ -n "$AUTH_CODE" ]; then
			# 4-3a. 성공: code + code_verifier → AT/RT
			RESPONSE=$(curl -s -w "\n%{http_code}" \
				-X POST "$BASE_URL/api/auth/token" \
				-H "Content-Type: application/x-www-form-urlencoded" \
				-d "grant_type=authorization_code&code=$AUTH_CODE&code_verifier=$CODE_VERIFIER&redirect_uri=kr.econovation.eeos://callback&client_id=$APP_CLIENT_ID")
			BODY=$(echo "$RESPONSE" | head -n -1)
			STATUS=$(echo "$RESPONSE" | tail -1)
			assert_status "4-3a. APP PKCE 토큰 교환 → 200" 200 "$STATUS" "$BODY"
			assert_contains "4-3b. access_token 존재" "access_token" "$BODY"
			assert_contains "4-3c. refresh_token 존재" "refresh_token" "$BODY"
			assert_contains "4-3d. token_type Bearer" "Bearer" "$BODY"

			# 토큰 저장 (reissue/logout 테스트용)
			APP_ACCESS_TOKEN=$(echo "$BODY" | python3 -c "import sys,json; print(json.load(sys.stdin)['access_token'])" 2>/dev/null || echo "")
			APP_REFRESH_TOKEN=$(echo "$BODY" | python3 -c "import sys,json; print(json.load(sys.stdin)['refresh_token'])" 2>/dev/null || echo "")

			# 4-4. 실패: 같은 code 재사용 → 400
			RESPONSE=$(curl -s -w "\n%{http_code}" \
				-X POST "$BASE_URL/api/auth/token" \
				-H "Content-Type: application/x-www-form-urlencoded" \
				-d "grant_type=authorization_code&code=$AUTH_CODE&code_verifier=$CODE_VERIFIER&redirect_uri=kr.econovation.eeos://callback&client_id=$APP_CLIENT_ID")
			STATUS=$(echo "$RESPONSE" | tail -1)
			assert_status "4-4. code 재사용 → 400 (일회성)" 400 "$STATUS"

		else
			echo -e "${RED}[FAIL]${NC} 4-3. redirect URL에서 code 추출 실패: $REDIRECT_URL"
			FAIL=$((FAIL + 1))
			TOTAL=$((TOTAL + 1))
		fi
	else
		echo -e "${RED}[FAIL]${NC} 4-3. APP 로그인 실패 (status=$STATUS)"
		FAIL=$((FAIL + 1))
		TOTAL=$((TOTAL + 1))
	fi

	# 4-5. 실패: PKCE code_verifier 불일치
	# 새 code 발급
	RESPONSE=$(curl -s -o /dev/null -w "%{http_code}\n%{redirect_url}" \
		-X POST "$BASE_URL/api/auth/login/oauth2" \
		-d "client_id=$APP_CLIENT_ID&redirect_uri=kr.econovation.eeos://callback&state=test789&email=$TEST_EMAIL&password=$TEST_PASSWORD&code_challenge=$CODE_CHALLENGE&code_challenge_method=S256")
	STATUS=$(echo "$RESPONSE" | head -1)
	REDIRECT_URL=$(echo "$RESPONSE" | tail -1)

	if [ "$STATUS" -eq 303 ]; then
		AUTH_CODE=$(echo "$REDIRECT_URL" | grep -oP 'code=\K[^&]+' || echo "")
		if [ -n "$AUTH_CODE" ]; then
			RESPONSE=$(curl -s -w "\n%{http_code}" \
				-X POST "$BASE_URL/api/auth/token" \
				-H "Content-Type: application/x-www-form-urlencoded" \
				-d "grant_type=authorization_code&code=$AUTH_CODE&code_verifier=wrong_verifier_value&redirect_uri=kr.econovation.eeos://callback&client_id=$APP_CLIENT_ID")
			STATUS=$(echo "$RESPONSE" | tail -1)
			assert_status "4-5. PKCE verifier 불일치 → 400" 400 "$STATUS"
		fi
	fi
else
	echo -e "${YELLOW}[SKIP]${NC} APP PKCE 플로우: APP_CLIENT_ID 또는 TEST_EMAIL 미설정"
fi

# ============================================
# 5. /reissue 엔드포인트
# ============================================
echo ""
echo -e "${YELLOW}--- 5. /reissue 엔드포인트 ---${NC}"

# 5-1. 실패: RT 없음 → 401
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X POST "$BASE_URL/api/auth/reissue")
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "5-1. RT 없음 → 401" 401 "$STATUS"

# 5-2. 실패: 잘못된 RT → 401
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X POST "$BASE_URL/api/auth/reissue" \
	-H "Cookie: eeos_refresh_token=invalid_token_value")
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "5-2. 잘못된 RT 쿠키 → 401" 401 "$STATUS"

# ============================================
# 6. /logout 엔드포인트
# ============================================
echo ""
echo -e "${YELLOW}--- 6. /logout 엔드포인트 ---${NC}"

# 6-1. 실패: 인증 없이 → 401
RESPONSE=$(curl -s -w "\n%{http_code}" \
	-X POST "$BASE_URL/api/auth/logout")
STATUS=$(echo "$RESPONSE" | tail -1)
assert_status "6-1. 인증 없이 logout → 401" 401 "$STATUS"

# ============================================
# 결과 요약
# ============================================
echo ""
echo "======================================"
echo -e " 결과: ${GREEN}PASS=$PASS${NC} / ${RED}FAIL=$FAIL${NC} / TOTAL=$TOTAL"
echo "======================================"

if [ "$FAIL" -gt 0 ]; then
	exit 1
fi
