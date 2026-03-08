# PR 6-Point Parallel Review (Multi-Agent)

Use Codex multi-agent mode.

Review the current branch against `main`.
Spawn **one sub-agent per review point in parallel**, wait for all results, then return a consolidated summary.

## Required agent usage

- Use `reviewer` role for points 1, 2, 3, 6
- Use `explorer` role for points 4, 5 (read-heavy investigation)

## Review points (one agent each)

1. Security issue
2. Code quality
3. Bugs / regressions
4. Race / concurrency risk
5. Test flakiness risk
6. Maintainability

## Execution rules

- Each sub-agent must inspect the diff (`main...HEAD`) and relevant surrounding code.
- Ask sub-agents to cite file paths and concrete evidence.
- Wait for all agents before summarizing.
- If a sub-agent finds no issue, it must explicitly say so.

## Final output format

1. One section per point (`1` to `6`)
2. For each point:
   - `Status`: `Issue Found` or `No Major Issue Found`
   - `Findings`: bullet list
   - `Files`: file paths
3. Final short section:
   - `Top risks to fix first`
   - `Suggested next actions`

