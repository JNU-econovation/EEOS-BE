# EEOS TDD Workflow Orchestrator (Interactive Multi-Agent Prompt)

Use Codex multi-agent mode and orchestrate the EEOS persona workflow using the configured roles:

`planner -> architect -> qa -> backend-dev -> reviewer -> doc-slave -> doc-reviewer`

## Inputs

- Task type: `<new-feature|bug-fix>`
- Requirement / bug description: `<fill here>`

## Mandatory rules

- Spawn the corresponding sub-agent role for each step.
- Each sub-agent must read `agents/<role>.md` first.
- Pass the previous step's key output to the next step.
- Stop and ask for user confirmation after `planner` and `architect`.
- If `reviewer` returns Must Fix items, loop back to `backend-dev`, then re-run `reviewer`.
- If `doc-reviewer` finds errors, loop back to `doc-slave`, then re-run `doc-reviewer`.
- Do not skip QA before implementation.

## Final output

- Completed steps
- Rework loops performed (if any)
- Changed files summary
- Remaining risks / follow-ups

