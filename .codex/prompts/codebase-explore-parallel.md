# Parallel Codebase Exploration Template

Use Codex multi-agent mode.

I want a fast exploration of the codebase. Spawn sub-agents in parallel, one per topic, and summarize their findings.

## Topics

1. Domain model structure (`explorer`)
2. API controllers and DTO entry points (`explorer`)
3. UseCase / application service patterns (`explorer`)
4. Persistence / repository / migration patterns (`explorer`)
5. Test structure (unit + integration) (`explorer`)
6. Shared conventions from `CLAUDE.md` and docs (`explorer`)

## Rules

- Each agent should read only the files needed for its topic.
- Prefer `rg`, `find`, and targeted file reads.
- Include file paths and short evidence snippets (paraphrased).
- Wait for all sub-agents, then merge results into one architecture map.

## Final output

- `Codebase map`
- `Key conventions`
- `Risks / inconsistencies`
- `Where to implement <feature>` (leave placeholder if feature not provided)

