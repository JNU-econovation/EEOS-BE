#!/bin/bash
# 로그인/회원가입 플로우 curl 스모크 테스트
#
# 사전 준비:
#   1. 로컬 서버 실행: cd eeos && ./gradlew bootRun
#   2. 테스트 클라이언트 DB seed:
#      mysql -u root -p eeos < scripts/api-test/seed-test-client.sql
#
# 사용법:
#   ./scripts/api-test/test-login-flow.sh
#   ./scripts/api-test/test-login-flow.sh http://localhost:8080

BASE_URL="${1:-http://localhost:8080}"
CLIENT_ID="test-web-client-id"
REDIRECT_URI="http://localhost:3000/callback"
STATE="test-state-$(date +%s)"

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

PASS=0
FAIL=0

pass() { echo -e "${GREEN}[PASS]${NC} $1"; PASS=$((PASS+1)); }
fail() { echo -e "${RED}[FAIL]${NC} $1"; echo "       $2"; FAIL=$((FAIL+1)); }
info() { echo -e "${YELLOW}[INFO]${NC} $1"; }

echo ""
echo "=============================="
echo " 로그인/회원가입 플로우 테스트"
echo " $BASE_URL"
echo "=============================="

# ─────────────────────────────────────
# 1. 회원가입
# ─────────────────────────────────────
echo ""
info "1. 회원가입"

SIGNUP_BODY=$(curl -s -w "\n%{http_code}" \
  -X POST "$BASE_URL/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "testuser01",
    "password": "test1234",
    "generation": 1,
    "name": "테스트유저",
    "activeStatus": "am"
  }')
SIGNUP_STATUS=$(echo "$SIGNUP_BODY" | tail -1)
SIGNUP_RESP=$(echo "$SIGNUP_BODY" | head -n -1)

if [ "$SIGNUP_STATUS" -eq 201 ]; then
  pass "1-1. 회원가입 성공 (201)"
elif [ "$SIGNUP_STATUS" -eq 409 ]; then
  info "1-1. 이미 가입된 계정 (409) - 계속 진행"
  PASS=$((PASS+1))
else
  fail "1-1. 회원가입 실패" "HTTP $SIGNUP_STATUS / $SIGNUP_RESP"
fi

# 실패: 비밀번호 규칙 위반
STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST "$BASE_URL/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{"id":"bad","password":"1234","generation":1,"name":"테스트","activeStatus":"am"}')
[ "$STATUS" -eq 400 ] && pass "1-2. 비밀번호 규칙 위반 → 400" || fail "1-2. 비밀번호 규칙 위반" "HTTP $STATUS"

# ─────────────────────────────────────
# 2. /authorize 검증
# ─────────────────────────────────────
echo ""
info "2. /authorize 검증"

# 성공: 로그인 페이지로 redirect
AUTH_RESP=$(curl -s -o /dev/null -w "%{http_code}" \
  -X GET "$BASE_URL/api/auth/authorize?client_id=$CLIENT_ID&redirect_uri=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$REDIRECT_URI'))")&response_type=code&state=$STATE")
[ "$AUTH_RESP" -eq 302 ] && pass "2-1. /authorize 로그인 페이지 redirect (302)" || fail "2-1. /authorize redirect 실패" "HTTP $AUTH_RESP"

# 실패: 잘못된 client_id
STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X GET "$BASE_URL/api/auth/authorize?client_id=invalid&redirect_uri=http://x.com&response_type=code&state=s")
[ "$STATUS" -eq 400 ] && pass "2-2. 잘못된 client_id → 400" || fail "2-2. 잘못된 client_id" "HTTP $STATUS"

# 실패: response_type != code
STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X GET "$BASE_URL/api/auth/authorize?client_id=$CLIENT_ID&redirect_uri=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$REDIRECT_URI'))")&response_type=token&state=s")
[ "$STATUS" -eq 400 ] && pass "2-3. response_type=token → 400" || fail "2-3. response_type=token" "HTTP $STATUS"

# ─────────────────────────────────────
# 3. /login (WEB 플로우)
# ─────────────────────────────────────
echo ""
info "3. /login WEB 플로우"

# 성공: 로그인 → 303 + 쿠키
LOGIN_RESP=$(curl -s -D - -o /dev/null \
  -X POST "$BASE_URL/api/auth/login" \
  -d "client_id=$CLIENT_ID&redirect_uri=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$REDIRECT_URI'))")&state=$STATE&email=testuser01&password=test1234")

LOGIN_STATUS=$(echo "$LOGIN_RESP" | grep "HTTP/" | awk '{print $2}' | tail -1)
HAS_AT=$(echo "$LOGIN_RESP" | grep -i "eeos_access_token")
HAS_RT=$(echo "$LOGIN_RESP" | grep -i "eeos_refresh_token")
LOCATION=$(echo "$LOGIN_RESP" | grep -i "location:" | head -1)

if [ "$LOGIN_STATUS" = "303" ]; then
  pass "3-1. WEB 로그인 → 303 redirect"
else
  fail "3-1. WEB 로그인" "HTTP $LOGIN_STATUS"
fi

[ -n "$HAS_AT" ] && pass "3-2. eeos_access_token 쿠키 존재" || fail "3-2. eeos_access_token 쿠키 없음" "$LOGIN_RESP"
[ -n "$HAS_RT" ] && pass "3-3. eeos_refresh_token 쿠키 존재" || fail "3-3. eeos_refresh_token 쿠키 없음" ""

if echo "$LOCATION" | grep -q "state=$STATE"; then
  pass "3-4. redirect Location에 state 포함"
else
  fail "3-4. redirect Location에 state 없음" "$LOCATION"
fi

# 실패: 잘못된 비밀번호 → 303 (로그인 페이지로 돌아감)
BAD_LOGIN=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST "$BASE_URL/api/auth/login" \
  -d "client_id=$CLIENT_ID&redirect_uri=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$REDIRECT_URI'))")&state=$STATE&email=testuser01&password=wrongpass")
[ "$BAD_LOGIN" -eq 303 ] && pass "3-5. 잘못된 비밀번호 → 303 (로그인 페이지)" || fail "3-5. 잘못된 비밀번호" "HTTP $BAD_LOGIN"

# 실패: redirect_uri 불일치 → 400
STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST "$BASE_URL/api/auth/login" \
  -d "client_id=$CLIENT_ID&redirect_uri=http://evil.com&state=$STATE&email=testuser01&password=test1234")
[ "$STATUS" -eq 400 ] && pass "3-6. redirect_uri 불일치 → 400" || fail "3-6. redirect_uri 불일치" "HTTP $STATUS"

# ─────────────────────────────────────
# 결과
# ─────────────────────────────────────
echo ""
echo "=============================="
echo -e " ${GREEN}PASS=$PASS${NC} / ${RED}FAIL=$FAIL${NC} / TOTAL=$((PASS+FAIL))"
echo "=============================="
[ "$FAIL" -gt 0 ] && exit 1 || exit 0
