# Risk register

The risks that could break this workshop — in the room or in the repo — with owner-facing
mitigations and explicit review triggers. Review this page **before every delivery** and whenever
an Embabel release tags. Statuses: **Open** (live risk, mitigation in place), **Watch** (horizon
risk, no action yet), **Closed** (superseded).

Last reviewed: 2026-07-08 · Embabel pin: **0.5.0 "Darwin"** (latest release as of review date).

## Framework and dependency risks

| ID | Risk | Likelihood | Impact | Status |
|---|---|---|---|---|
| F1 | **Embabel 1.0.0-RC1 breaks the pin.** The RC1 dev cycle is underway on `main`; a deprecation purge (~1,100 deletions, PR #1750, 2026-06-30) already landed. | High (imminent) | High | Open |
| F2 | **`embabel-agent-test` API shifts.** The test module is officially *incubating*; every lab's tests use `FakeOperationContext` / `EmbabelMockitoIntegrationTest` / `whenCreateObject`. | Medium | High | Open |
| F3 | **Embabel 2.0 line invalidates the stack pins.** The 2.0 branch targets Spring Boot 4, Spring Framework 7, Spring AI 2.0 (vendor SDK facades), Jackson 3 (`tools.jackson.*`). | Certain, timing unknown | High | Watch |
| F4 | **Model-name churn.** `application.yml` and `MODEL_ROUTING.md` name concrete models; providers retire names quarterly (0.5.0 itself dropped retired Gemini 2.0 names). | High | Low | Open |
| F5 | **`java-agent-template` drift.** The upstream template this repo derives from is pinned at 0.3.5 — attendees comparing against it will see older idioms. | Medium | Low | Watch |

**F1 mitigation:** when RC1 tags, re-run all six labs plus `lab4-broken` against it on a branch
before changing any pin; the workshop stays on 0.5.0 until that pass is green.
**F2 mitigation:** the mocked-test idioms are isolated to `src/test/java` — budget a half-day
migration when F1's check runs; treat any test-API compile failure as expected, not a lab bug.
**F3 mitigation:** none needed now; the wrap deck's "where this is going" slide sets attendee
expectations. Revisit the README's "Spring Boot 3.5.x" claim when 2.0 reaches RC.
**F4 mitigation:** roles-not-names in code (Lab 6's whole point); before each delivery, run
`./mvnw spring-boot:run` once with a real key and confirm the mapped models still exist.
**F5 mitigation:** noted in the gaps review; no action — this repo is deliberately ahead.

## Repo and branch-contract risks

| ID | Risk | Likelihood | Impact | Status |
|---|---|---|---|---|
| R1 | **Branch drift.** The lesson lives in the `*-before`/`*-after` pairs (Lab 4 starts from `lab4-broken` instead of a `-before`); a fix applied to `main` but not rippled through the branch progression silently diverges the walk. | Medium | High | Open |
| R2 | **`lab4-broken` CI confusion.** It is the *only* branch allowed to fail `./mvnw verify`; a well-meaning fix (human or agent) would destroy Lab 4. | Medium | High | Open |
| R3 | **Worksheet/branch skew.** The lab-branch `labs/*.md` copies must stay identical to `main` (a learner reads the branch copy after `git checkout labN-before`). A worksheet fix or extension landing on `main` alone silently diverges the walk — and the skew can reach *core steps*, not just *Going further* sections. | Certain | Medium | Open |

**R1 mitigation:** any change to lab-path code lands branch-by-branch in walk order and is
verified with `git diff labN-before labN-after -- src`; `main`-only additions (like the content
guardrail) must sit *off* the lab diff path.
**R2 mitigation:** the expected failure is documented in README, SETUP and the Lab 4 worksheet;
never "fix" `lab4-broken` — the fix lives on `lab4-after`.
**R3 mitigation:** any `labs/*.md` (or lab-relevant `docs/**`) change lands on `main` **and** is
rippled to every `labN-{before,after}` / `lab4-broken` copy in the same batch; verify with
`git diff main labN-before -- labs/` (expect empty). Do not assume changes are confined to *Going
further* — the 2026-07 learner audit found core-step skew (worksheet stubs that failed `verify`).

## Workshop-day risks

| ID | Risk | Likelihood | Impact | Status |
|---|---|---|---|---|
| W1 | **No/slow WiFi or provider outage** during live demos. | Medium | High | Open |
| W2 | **Attendees without keys** (procurement, personal machines). | High | Medium | Open |
| W3 | **No Docker** on locked-down laptops (Zipkin path, Lab 4). | High | Low | Open |
| W4 | **Setup friction burns the first hour** (JDK versions, proxies). | Medium | High | Open |
| W5 | **Live-demo cost surprise** — a room of attendees running `best`-routed runs on the presenter's key. | Low | Medium | Open |

**W1 mitigation:** every demo has a mock-mode fallback (`--spring.profiles.active=mock`) and the
planning-log screenshots live in the decks; rehearse the offline path once per delivery.
**W2 mitigation:** the build and all tests are green with **no keys** by design; keyless
attendees run mock mode and pair for real-model moments.
**W3 mitigation:** the planning-log path (`-p -r`) shows the same world-state as the trace;
Zipkin is explicitly optional.
**W4 mitigation:** Codespaces/devcontainer route in SETUP; open the room with `./mvnw clean
verify` as the first act so failures surface while the M0 talk runs.
**W5 mitigation:** the per-run `Budget(0.50, 20, 200_000)` caps worst-case spend; per-call cost
lines (0.4.0+) make any anomaly visible immediately.

## Content and legal risks

| ID | Risk | Likelihood | Impact | Status |
|---|---|---|---|---|
| C1 | **Catalog mistaken for the real conference programme** — real speakers/abstracts implied. | Low | Medium | Open |
| C2 | **Slide claims go stale** — research citations (versions, dates, model names) age between deliveries. | Certain | Medium | Open |

**C1 mitigation:** the catalog is wholly synthetic and says so in README and the data file's
docs; keep the "entirely invented" line on the M0 deck.
**C2 mitigation:** the [gaps-and-extensions review](https://github.com/Habitat-Thinking/hands-on-agent-development/blob/main/slides/notes/gaps-and-extensions.md)
carries the research date in its header; re-run its verification (releases page + Maven Central
metadata) before each delivery and update this page's review date.

---

*Companion documents:* the slide-deck notes and the Embabel state-of-play review live in
`slides/notes/` in the repository; this register is the durable summary of their risk findings.
