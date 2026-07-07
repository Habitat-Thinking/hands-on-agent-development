# Branch map

The workshop is operated by checking out git branches. Each lab has a `*-before` starting point and a
`*-after` reference solution; Lab 4 starts from `lab4-broken` instead of a `-before`. There are 13
refs: 12 lab branches plus `main`. To work a lab, see [Walk the labs](../tutorials/walk-the-labs.md).

## Refs

| # | Ref | Lab | Role |
|---|---|---|---|
| 1 | `lab1-before` | 1 — DICE | Starting point + worksheet + TODO anchors. |
| 2 | `lab1-after` | 1 — DICE | Reference solution. |
| 3 | `lab2-before` | 2 — GOAP | Starting point. |
| 4 | `lab2-after` | 2 — GOAP | Reference solution. |
| 5 | `lab3-before` | 3 — Guardrails | Starting point. |
| 6 | `lab3-after` | 3 — Guardrails | Reference solution. |
| 7 | `lab4-broken` | 4 — Explainability | Deliberately broken: compiles, agent goes `STUCK` at runtime. The one ref where `./mvnw verify` is expected to fail. |
| 8 | `lab4-after` | 4 — Explainability | Reference solution (the fix). |
| 9 | `lab5-before` | 5 — Extend | Starting point. |
| 10 | `lab5-after` | 5 — Extend | Reference solution. |
| 11 | `lab6-before` | 6 — Model routing | Starting point. |
| 12 | `lab6-after` | 6 — Model routing | Reference solution. |
| 13 | `main` | — | All labs applied plus the workshop docs. |

## Walk order

```
lab1-before → lab1-after
lab2-before → lab2-after
lab3-before → lab3-after
lab4-broken → lab4-after        ← Lab 4 starts from a deliberately broken plan
lab5-before → lab5-after
lab6-before → lab6-after
main                            ← all labs applied + the workshop docs
```

## Operating a lab

```bash
git checkout lab1-before                 # starting point + worksheet + TODO anchors
./mvnw -q verify                         # check your work
git diff lab1-before lab1-after -- src   # compare against the reference solution
```

`lab4-broken` is the only branch that intentionally fails `./mvnw verify`: it compiles but the agent
stalls at runtime, which is the failure Lab 4 teaches you to diagnose.
