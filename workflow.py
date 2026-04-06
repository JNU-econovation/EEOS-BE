"""
EEOS TDD 개발 워크플로우 오케스트레이터

페르소나 기반 TDD 개발 프로세스를 Claude Agent SDK로 실행한다.

사용법:
    python workflow.py "새 기능 요구사항 설명"
    python workflow.py --bug "버그 설명"
    python workflow.py --from-step architect "기존 플랜 파일.md"

요구사항:
    pip install claude-agent-sdk
    ANTHROPIC_API_KEY 환경변수 설정
"""

import asyncio
import argparse
import os
import sys
from pathlib import Path

# Claude Code 내부에서 실행 시 중첩 세션 감지를 우회한다.
# Agent SDK는 CLAUDECODE 환경변수가 있으면 중첩 실행을 거부하므로 제거한다.
os.environ.pop("CLAUDECODE", None)

from claude_agent_sdk import query, ClaudeAgentOptions, CLINotFoundError, CLIConnectionError

# 프로젝트 루트 (workflow.py 위치 기준)
PROJECT_ROOT = Path(__file__).parent
AGENTS_DIR = PROJECT_ROOT / "agents"

# 각 페르소나의 시스템 프롬프트 파일
PERSONAS = {
    "planner": AGENTS_DIR / "planner.md",
    "architect": AGENTS_DIR / "architect.md",
    "qa": AGENTS_DIR / "qa.md",
    "backend-dev": AGENTS_DIR / "backend-dev.md",
    "reviewer": AGENTS_DIR / "reviewer.md",
    "doc-slave": AGENTS_DIR / "doc-slave.md",
    "doc-reviewer": AGENTS_DIR / "doc-reviewer.md",
}

# 워크플로우 단계 정의
NEW_FEATURE_WORKFLOW = [
    "planner",
    "architect",
    "qa",
    "backend-dev",
    "reviewer",
    "doc-slave",
    "doc-reviewer",
]

BUG_FIX_WORKFLOW = [
    "planner",
    "architect",  # 설계 변경 필요 여부 판단 (필요 없으면 PASS)
    "qa",
    "backend-dev",
    "reviewer",
    "doc-slave",
    "doc-reviewer",
]

# 사용자 컨펌이 필요한 단계
CONFIRM_REQUIRED = {"planner", "architect"}

# 각 단계별 허용 도구
STEP_TOOLS = {
    "planner": ["Read", "Glob", "Grep"],
    "architect": ["Read", "Glob", "Grep"],
    "qa": ["Read", "Glob", "Grep", "Write"],
    "backend-dev": ["Read", "Glob", "Grep", "Write", "Edit", "Bash"],
    "reviewer": ["Read", "Glob", "Grep"],
    "doc-slave": ["Read", "Glob", "Grep", "Write", "Edit"],
    "doc-reviewer": ["Read", "Glob", "Grep"],
}

# 각 단계의 한국어 이름
STEP_NAMES = {
    "planner": "플래너",
    "architect": "아키텍트",
    "qa": "테스트 설계자",
    "backend-dev": "개발자",
    "reviewer": "코드 리뷰어",
    "doc-slave": "문서화 노예",
    "doc-reviewer": "문서 리뷰어",
}


def load_persona(step: str) -> str:
    """페르소나 파일을 읽어 시스템 프롬프트로 반환한다."""
    persona_file = PERSONAS[step]
    if not persona_file.exists():
        raise FileNotFoundError(f"페르소나 파일을 찾을 수 없습니다: {persona_file}")
    return persona_file.read_text(encoding="utf-8")


def print_step_header(step: str, step_num: int, total: int):
    """단계 시작 헤더를 출력한다."""
    name = STEP_NAMES[step]
    print(f"\n{'='*60}")
    print(f"  단계 {step_num}/{total}: {name} ({step})")
    print(f"{'='*60}\n")


def ask_user_confirmation(step: str) -> bool:
    """사용자 컨펌을 요청하고 결과를 반환한다."""
    name = STEP_NAMES[step]
    print(f"\n{'─'*60}")
    print(f"⏸  {name}의 산출물을 검토해주세요.")
    print(f"{'─'*60}")
    while True:
        answer = input("다음 단계로 진행하시겠습니까? [y/n/q(종료)]: ").strip().lower()
        if answer in ("y", "yes"):
            return True
        elif answer in ("n", "no"):
            print("워크플로우를 중단합니다.")
            return False
        elif answer in ("q", "quit"):
            print("워크플로우를 종료합니다.")
            sys.exit(0)
        else:
            print("y(진행), n(중단), q(종료) 중 하나를 입력하세요.")


def ask_skip_step(step: str) -> bool:
    """단계 스킵 여부를 묻는다 (아키텍트 단계 등)."""
    name = STEP_NAMES[step]
    answer = input(f"\n⏩ {name} 단계를 스킵하시겠습니까? [y/n]: ").strip().lower()
    return answer in ("y", "yes")


async def run_step(
    step: str,
    prompt: str,
    context: str = "",
) -> str:
    """단일 페르소나 에이전트를 실행하고 결과를 반환한다."""
    system_prompt = load_persona(step)
    full_prompt = f"{context}\n\n---\n\n{prompt}" if context else prompt

    result_text = ""

    try:
        async for message in query(
            prompt=full_prompt,
            options=ClaudeAgentOptions(
                cwd=str(PROJECT_ROOT),
                allowed_tools=STEP_TOOLS[step],
                system_prompt=system_prompt,
                permission_mode="acceptEdits" if step in ("backend-dev", "doc-slave") else "default",
                model="claude-opus-4-6",
            ),
        ):
            if message.type == "result":
                result_text = message.result
            elif message.type == "assistant":
                # 스트리밍 출력
                for block in getattr(message, "content", []):
                    if hasattr(block, "text"):
                        print(block.text, end="", flush=True)

    except CLINotFoundError:
        print("\n❌ Claude Code CLI가 설치되어 있지 않습니다.")
        print("   설치: pip install claude-agent-sdk")
        sys.exit(1)
    except CLIConnectionError as e:
        print(f"\n❌ 연결 오류: {e}")
        sys.exit(1)

    return result_text


async def run_workflow(
    requirement: str,
    workflow: list[str],
    is_bug_fix: bool = False,
    start_from: str = "planner",
):
    """전체 워크플로우를 순서대로 실행한다."""
    print("\n🚀 EEOS 개발 워크플로우 시작")
    print(f"   유형: {'버그 수정' if is_bug_fix else '새 기능 개발'}")
    print(f"   요구사항: {requirement[:80]}{'...' if len(requirement) > 80 else ''}")

    # start_from 단계 이전은 스킵
    start_idx = workflow.index(start_from) if start_from in workflow else 0
    active_workflow = workflow[start_idx:]

    context = ""  # 이전 단계 결과를 다음 단계로 전달

    for i, step in enumerate(active_workflow, start=start_idx + 1):
        total = len(workflow)

        print_step_header(step, i, total)

        # 버그 수정 시 아키텍트 단계 스킵 여부 확인
        if is_bug_fix and step == "architect":
            if ask_skip_step(step):
                print("   → 아키텍트 단계 스킵")
                continue

        # 에이전트 실행
        step_prompt = build_step_prompt(step, requirement, context, is_bug_fix)
        result = await run_step(step, step_prompt)

        if result:
            print(f"\n\n📄 {STEP_NAMES[step]} 결과:\n{result}")
            context = result  # 다음 단계로 전달

        # 컨펌 필요 단계에서 사용자 승인 요청
        if step in CONFIRM_REQUIRED:
            if not ask_user_confirmation(step):
                return

    print(f"\n\n{'='*60}")
    print("  ✅ 워크플로우 완료!")
    print(f"{'='*60}\n")


def build_step_prompt(
    step: str,
    requirement: str,
    previous_context: str,
    is_bug_fix: bool,
) -> str:
    """각 단계에 맞는 프롬프트를 생성한다."""
    base = f"{'버그 수정' if is_bug_fix else '새 기능 개발'} 요청:\n\n{requirement}"

    if not previous_context:
        return base

    prompts = {
        "planner": base,
        "architect": (
            f"{base}\n\n"
            f"## 플래너의 To-do 리스트\n\n{previous_context}\n\n"
            "위 To-do 리스트를 바탕으로 API 설계서와 DB 스키마를 작성하세요."
        ),
        "qa": (
            f"{base}\n\n"
            f"## 설계서\n\n{previous_context}\n\n"
            "위 설계서를 바탕으로 실패하는 테스트 코드를 작성하세요. "
            "구현 코드는 작성하지 마세요."
        ),
        "backend-dev": (
            f"{base}\n\n"
            f"## 테스트 코드 및 설계 컨텍스트\n\n{previous_context}\n\n"
            "위 테스트를 모두 통과시키는 구현 코드를 작성하세요. "
            "구현 후 `./gradlew test integrationTest`로 전체 테스트 통과를 확인하세요."
        ),
        "reviewer": (
            f"{base}\n\n"
            f"## 구현 완료된 코드 컨텍스트\n\n{previous_context}\n\n"
            "구현된 코드 전체를 리뷰하고 피드백을 작성하세요."
        ),
        "doc-slave": (
            f"{base}\n\n"
            f"## 리뷰 완료된 구현 컨텍스트\n\n{previous_context}\n\n"
            "구현된 API와 코드를 바탕으로 문서를 작성/업데이트하세요."
        ),
        "doc-reviewer": (
            f"{base}\n\n"
            f"## 작성된 문서 컨텍스트\n\n{previous_context}\n\n"
            "작성된 문서를 리뷰하고 피드백을 작성하세요."
        ),
    }

    return prompts.get(step, base)


def main():
    parser = argparse.ArgumentParser(
        description="EEOS TDD 개발 워크플로우 오케스트레이터",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
예시:
  # 새 기능 개발
  python workflow.py "팀 빌딩 기능: 팀원 신청/수락/거절 API 개발"

  # 버그 수정
  python workflow.py --bug "프로그램 조회 시 삭제된 멤버가 포함되는 버그"

  # 특정 단계부터 시작 (플래너 단계 완료 후 아키텍트부터)
  python workflow.py --from-step architect "기능 요구사항"

  # 단일 페르소나만 실행
  python workflow.py --step reviewer "코드 리뷰 요청: [코드 내용]"
        """,
    )

    parser.add_argument("requirement", help="기능 요구사항 또는 버그 설명")
    parser.add_argument("--bug", action="store_true", help="버그 수정 워크플로우 사용")
    parser.add_argument(
        "--from-step",
        choices=list(PERSONAS.keys()),
        default="planner",
        help="특정 단계부터 시작 (기본값: planner)",
    )
    parser.add_argument(
        "--step",
        choices=list(PERSONAS.keys()),
        help="단일 페르소나만 실행",
    )

    args = parser.parse_args()

    if args.step:
        # 단일 페르소나 실행
        print(f"\n🤖 {STEP_NAMES[args.step]} 단일 실행\n")
        asyncio.run(run_step(args.step, args.requirement))
    else:
        # 전체 워크플로우 실행
        workflow = BUG_FIX_WORKFLOW if args.bug else NEW_FEATURE_WORKFLOW
        asyncio.run(
            run_workflow(
                requirement=args.requirement,
                workflow=workflow,
                is_bug_fix=args.bug,
                start_from=args.from_step,
            )
        )


if __name__ == "__main__":
    main()
