#!/usr/bin/env python3
"""
EEOS Codex Multi-Agent Workflow Orchestrator

This script orchestrates the EEOS persona workflow by running `codex exec`
stage-by-stage and instructing Codex to spawn the configured sub-agent role
for each stage.

Usage:
  python scripts/codex_workflow.py new-feature "요구사항"
  python scripts/codex_workflow.py fix-bug "버그 설명"
  python scripts/codex_workflow.py new-feature "요구사항" --from-step qa
  python scripts/codex_workflow.py new-feature "요구사항" --dry-run
"""

from __future__ import annotations

import argparse
import json
import os
import re
import subprocess
import sys
import textwrap
from pathlib import Path
from typing import Iterable


PROJECT_ROOT = Path(__file__).resolve().parents[1]

WORKFLOWS = {
    "new-feature": [
        "planner",
        "architect",
        "qa",
        "backend-dev",
        "reviewer",
        "doc-slave",
        "doc-reviewer",
    ],
    "fix-bug": [
        "planner",
        "architect",
        "qa",
        "backend-dev",
        "reviewer",
        "doc-slave",
        "doc-reviewer",
    ],
}

STEP_NAMES = {
    "planner": "플래너",
    "architect": "아키텍트",
    "qa": "테스트 설계자",
    "backend-dev": "개발자",
    "reviewer": "코드 리뷰어",
    "doc-slave": "문서화 담당자",
    "doc-reviewer": "문서 리뷰어",
}

CONFIRM_REQUIRED = {"planner", "architect"}


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def load_role_guide(step: str) -> str:
    return read_text(PROJECT_ROOT / "agents" / f"{step}.md")


def collect_git_status() -> str:
    try:
        out = subprocess.run(
            ["git", "status", "--short"],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True,
            check=False,
        )
        return out.stdout.strip() or "(no changes)"
    except Exception:
        return "(git status unavailable)"


def ask_yes_no(prompt: str, default: bool | None = None) -> bool:
    suffix = " [y/n]: "
    if default is True:
        suffix = " [Y/n]: "
    elif default is False:
        suffix = " [y/N]: "

    while True:
        value = input(prompt + suffix).strip().lower()
        if not value and default is not None:
            return default
        if value in {"y", "yes"}:
            return True
        if value in {"n", "no"}:
            return False
        print("y 또는 n을 입력하세요.")


def truncate(text: str, limit: int = 12000) -> str:
    if len(text) <= limit:
        return text
    return text[:limit] + "\n\n[...truncated...]"


def build_stage_prompt(
    task_type: str,
    step: str,
    requirement: str,
    previous_output: str,
    review_feedback: str | None = None,
    doc_feedback: str | None = None,
) -> str:
    role_guide = load_role_guide(step)
    base = textwrap.dedent(
        f"""
        You are the parent orchestrator for one EEOS workflow stage.
        Use Codex multi-agent features for this stage.

        Mandatory:
        - Spawn exactly one sub-agent with agent_type `{step}`.
        - In the sub-agent task, explicitly instruct it to read and follow `agents/{step}.md`.
        - Wait for the sub-agent result.
        - Return only the consolidated stage result (do not include internal orchestration chatter).

        Task type: {task_type}
        Current stage: {step} ({STEP_NAMES[step]})

        User requirement / bug description:
        {requirement}
        """
    ).strip()

    context_chunks: list[str] = []

    if previous_output:
        context_chunks.append(
            "Previous stage output (summarized context to use as input):\n"
            + truncate(previous_output)
        )

    if review_feedback:
        context_chunks.append(
            "Latest reviewer feedback (must address before proceeding):\n"
            + truncate(review_feedback)
        )

    if doc_feedback:
        context_chunks.append(
            "Latest doc-reviewer feedback (must address before proceeding):\n"
            + truncate(doc_feedback)
        )

    step_specific = {
        "planner": """
        Ask the `planner` sub-agent to analyze the request and produce an actionable To-do list.
        The result must end with:
        CONFIRMATION_REQUIRED: YES
        STAGE_STATUS: DONE
        """,
        "architect": """
        Ask the `architect` sub-agent to produce API/DB/domain design based on the planner output.
        If this is a bug-fix workflow, the result must also include:
        DESIGN_CHANGE_REQUIRED: YES|NO
        The result must end with:
        CONFIRMATION_REQUIRED: YES
        STAGE_STATUS: DONE
        """,
        "qa": """
        Ask the `qa` sub-agent to write failing tests only (TDD).
        For bug fixes, require a failing reproduction test first.
        The result must include changed test file paths and expected failure summary.
        End with:
        STAGE_STATUS: DONE
        """,
        "backend-dev": """
        Ask the `backend-dev` sub-agent to implement code that makes tests pass.
        It may edit files and run tests as needed.
        The result must include changed file paths and test command summary.
        End with:
        STAGE_STATUS: DONE
        """,
        "reviewer": """
        Ask the `reviewer` sub-agent to review the implementation and classify findings.
        The result must clearly include:
        REVIEW_STATUS: PASS|MUST_FIX
        MUST_FIX_COUNT: <number>
        and file-path-based findings.
        End with:
        STAGE_STATUS: DONE
        """,
        "doc-slave": """
        Ask the `doc-slave` sub-agent to update docs based on the implementation and review context.
        The result must include changed doc file paths.
        End with:
        STAGE_STATUS: DONE
        """,
        "doc-reviewer": """
        Ask the `doc-reviewer` sub-agent to review documentation correctness/completeness.
        The result must clearly include:
        DOC_REVIEW_STATUS: PASS|MUST_FIX
        DOC_MUST_FIX_COUNT: <number>
        End with:
        STAGE_STATUS: DONE
        """,
    }[step].strip()

    # Provide the role guide inline to reduce ambiguity for spawned agent prompts.
    # Keep truncated to avoid runaway context if guides grow.
    role_guide_chunk = "Role guide excerpt (`agents/{step}.md`):\n".format(step=step) + truncate(
        role_guide, limit=8000
    )

    sections = [base, role_guide_chunk]
    sections.extend(context_chunks)
    sections.append("Stage-specific requirements:\n" + step_specific)
    sections.append(
        "Also include a short 'FILES_CHANGED' section based on actual edits (or 'none')."
    )
    return "\n\n".join(sections)


def run_codex_exec(prompt: str, dry_run: bool = False, extra_args: Iterable[str] = ()) -> str:
    cmd = [
        "codex",
        "exec",
        "--json",
        "--color",
        "never",
        "--cd",
        str(PROJECT_ROOT),
        "--full-auto",
    ]
    cmd.extend(extra_args)
    cmd.append(prompt)

    if dry_run:
        print("[DRY-RUN] codex command:")
        print(" ".join(cmd[:-1] + ["<PROMPT>"]))
        print("[DRY-RUN] prompt preview:")
        print(truncate(prompt, 2000))
        return ""

    process = subprocess.Popen(
        cmd,
        cwd=PROJECT_ROOT,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True,
        bufsize=1,
    )

    last_agent_messages: list[str] = []
    stderr_lines: list[str] = []

    assert process.stdout is not None
    for line in process.stdout:
        line = line.rstrip("\n")
        if not line.strip():
            continue
        try:
            event = json.loads(line)
        except json.JSONDecodeError:
            continue

        if event.get("type") == "item.completed":
            item = event.get("item", {})
            if item.get("type") == "agent_message":
                text = str(item.get("text", "")).strip()
                if text:
                    last_agent_messages.append(text)
                    print(text)
            elif item.get("type") == "reasoning":
                # Keep console clean; parent stage headers are enough.
                pass
        elif event.get("type") == "turn.completed":
            usage = event.get("usage", {})
            if usage:
                print(
                    f"[codex usage] in={usage.get('input_tokens')} out={usage.get('output_tokens')}"
                )

    assert process.stderr is not None
    stderr_text = process.stderr.read().strip()
    if stderr_text:
        stderr_lines.append(stderr_text)

    code = process.wait()
    if code != 0:
        detail = "\n".join(stderr_lines).strip()
        raise RuntimeError(f"`codex exec` failed (exit={code})\n{detail}")

    if not last_agent_messages:
        return ""
    return "\n\n".join(last_agent_messages).strip()


def parse_flag(text: str, key: str, allowed: set[str]) -> str | None:
    pattern = re.compile(rf"(?im)^\s*{re.escape(key)}\s*:\s*([A-Z_-]+)\s*$")
    match = pattern.search(text)
    if not match:
        return None
    value = match.group(1).strip().upper()
    return value if value in allowed else None


def parse_int_flag(text: str, key: str) -> int | None:
    pattern = re.compile(rf"(?im)^\s*{re.escape(key)}\s*:\s*(\d+)\s*$")
    match = pattern.search(text)
    if not match:
        return None
    return int(match.group(1))


def print_header(step: str, idx: int, total: int) -> None:
    print("\n" + "=" * 72)
    print(f"Step {idx}/{total} - {STEP_NAMES[step]} ({step})")
    print("=" * 72)


def run_workflow(
    task_type: str,
    requirement: str,
    start_from: str,
    dry_run: bool,
    extra_codex_args: list[str],
) -> None:
    workflow = WORKFLOWS[task_type]
    if start_from not in workflow:
        raise ValueError(f"Unknown start step: {start_from}")

    start_idx = workflow.index(start_from)
    previous_output = ""
    latest_review_feedback: str | None = None
    latest_doc_feedback: str | None = None
    current_idx = start_idx
    total = len(workflow)

    print(f"EEOS Codex Multi-Agent Workflow ({task_type})")
    print(f"Requirement: {requirement}")
    print(f"Git status before start:\n{collect_git_status()}\n")

    while current_idx < len(workflow):
        step = workflow[current_idx]
        print_header(step, current_idx + 1, total)

        prompt = build_stage_prompt(
            task_type=task_type,
            step=step,
            requirement=requirement,
            previous_output=previous_output,
            review_feedback=latest_review_feedback if step == "backend-dev" else None,
            doc_feedback=latest_doc_feedback if step == "doc-slave" else None,
        )

        result = run_codex_exec(prompt, dry_run=dry_run, extra_args=extra_codex_args)
        if dry_run:
            current_idx += 1
            continue

        if not result:
            raise RuntimeError(f"No result returned for step: {step}")

        previous_output = result
        print("\n[Stage result captured]")
        print(f"Git status after {step}:\n{collect_git_status()}")

        if step in CONFIRM_REQUIRED:
            if not ask_yes_no(f"{STEP_NAMES[step]} 결과를 승인하고 다음 단계로 진행할까요?", default=True):
                print("Workflow stopped by user.")
                return

        if task_type == "fix-bug" and step == "architect":
            required = parse_flag(result, "DESIGN_CHANGE_REQUIRED", {"YES", "NO"})
            if required == "NO":
                if ask_yes_no(
                    "아키텍트가 설계 변경 불필요로 판단했습니다. 설계 상세를 확정하고 QA로 진행할까요?",
                    default=True,
                ):
                    pass

        if step == "reviewer":
            review_status = parse_flag(result, "REVIEW_STATUS", {"PASS", "MUST_FIX"})
            must_fix_count = parse_int_flag(result, "MUST_FIX_COUNT") or 0
            if review_status == "MUST_FIX" or must_fix_count > 0:
                latest_review_feedback = result
                print("\n[Review requires fixes] Looping back to backend-dev.")
                current_idx = workflow.index("backend-dev")
                continue
            latest_review_feedback = None

        if step == "doc-reviewer":
            doc_status = parse_flag(result, "DOC_REVIEW_STATUS", {"PASS", "MUST_FIX"})
            doc_fix_count = parse_int_flag(result, "DOC_MUST_FIX_COUNT") or 0
            if doc_status == "MUST_FIX" or doc_fix_count > 0:
                latest_doc_feedback = result
                print("\n[Doc review requires fixes] Looping back to doc-slave.")
                current_idx = workflow.index("doc-slave")
                continue
            latest_doc_feedback = None

        current_idx += 1

    print("\n" + "=" * 72)
    print("Workflow completed")
    print("=" * 72)
    print("Final git status:")
    print(collect_git_status())


def main() -> None:
    parser = argparse.ArgumentParser(
        description="EEOS Codex multi-agent workflow orchestrator",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument("task_type", choices=["new-feature", "fix-bug"])
    parser.add_argument("requirement", help="Feature requirement or bug description")
    parser.add_argument(
        "--from-step",
        choices=WORKFLOWS["new-feature"],
        default="planner",
        help="Start from a specific stage",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Print generated prompts/commands without running codex",
    )
    parser.add_argument(
        "--codex-arg",
        action="append",
        default=[],
        help="Extra raw argument to pass to `codex exec` (repeatable). Example: --codex-arg=--dangerously-bypass-approvals-and-sandbox",
    )
    args = parser.parse_args()

    run_workflow(
        task_type=args.task_type,
        requirement=args.requirement,
        start_from=args.from_step,
        dry_run=args.dry_run,
        extra_codex_args=args.codex_arg,
    )


if __name__ == "__main__":
    main()
