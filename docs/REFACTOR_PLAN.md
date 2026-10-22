# Refactor Plan: YouTube Clone KMP

Goal: secure the API key, bound and paginate requests, then move the app to Clean Architecture + MVI
with Compose Navigation, fixing code health and the Shorts (reels) UI along the way.

Ship as **one PR per phase**. Every phase must leave `./gradlew :composeApp:assembleDebug`,
`:composeApp:compileKotlinJvm` and the iOS framework build green.

---

## Phase 0: API key out of source (small, do first)

1. Add to the global `~/.gradle/gradle.properties` (never committed):
   ```properties
   YOUTUBE_API_KEY=your_new_key
   ```
2. In `composeApp/build.gradle.kts` (the BuildConfig plugin is already in the catalog):
   ```kotlin
   buildConfig {
       packageName("org.company.app")
       buildConfigField("String", "YOUTUBE_API_KEY",
           "\"${providers.gradleProperty("YOUTUBE_API_KEY").getOrElse("")}\"")
   }
   ```
3. Delete `API_KEY` from `core/common/Constant.kt` and read `BuildConfig.YOUTUBE_API_KEY`.
4. Add the key once, as a Ktor `defaultRequest { url.parameters.append("key", ...) }`, and remove
   `&key=$API_KEY` from every URL in `YoutubeClientApi`.
5. Fail fast: if the key is blank, show a clear "API key missing" error state rather than empty screens.
6. **Revoke the old key in Google Cloud** (it is in git history) and restrict the new one
   (Android package + SHA-1, iOS bundle ID, HTTP referrer for web).
7. CI: inject `YOUTUBE_API_KEY` from a GitHub Actions secret via `ORG_GRADLE_PROJECT_YOUTUBE_API_KEY`.
8. README "Getting Started": document the `gradle.properties` step.

**Done when:** `git grep AIza` returns nothing, and all targets build and load videos.

---

## Phase 1: Request limits + pagination (10 at a time)

API layer (`data/remote`):
- Every list call takes `pageSize: Int = 10` and `pageToken: String?`, and sends
  `maxResults=pageSize` (hard-capped at 50) plus `pageToken`.
- URL-encode user input with Ktor `parameters { append("q", query) }`. No string-built URLs.
- Return `nextPageToken` together with the items.
- Logging: `LogLevel.HEADERS` in debug only and `NONE` in release. Never log the key.
- Quota guard: map HTTP 403 `quotaExceeded` to a typed error (see Phase 2).

Paging (shared, no Android-only Paging lib):
```kotlin
data class Page<T>(val items: List<T>, val nextToken: String?)

class Paginator<T>(
    private val load: suspend (token: String?) -> Result<Page<T>>,
) {
    // holds items, nextToken, isLoading, endReached; loadNext() appends the next chunk of 10
}
```
- UI: the grid/list triggers `LoadNextPage` when the last visible index is ≥ `items.size - 3`.
- Footer shows a loading row, an error row with retry, or nothing at end of list.
- Apply to: Home feed, Search, Channel videos, Playlists, Comments, Shorts.

**Done when:** the first screen makes one request with `maxResults=10`, scrolling loads +10 each time,
and no request anywhere has `maxResults > 50`.

---

## Phase 2: Clean Architecture + mappers

Target package layout (inside `composeApp/commonMain`, or a later split into `:core:*` / `:feature:*` modules):
```
core/        network (HttpClient, ApiResult, error mapping), database, designsystem, util
data/
  remote/dto/     ← current models move here, renamed *Dto (one shared ThumbnailsDto, no duplicates)
  remote/         YoutubeApi (Ktor)
  local/          SQLDelight DAOs
  mapper/         Dto → Domain (VideoDto.toDomain(), ChannelDto.toDomain(), …)
  repository/     VideoRepositoryImpl, ChannelRepositoryImpl, SearchRepositoryImpl, CommentRepositoryImpl
domain/
  model/          Video, Channel, Playlist, Comment, Short, Thumbnail (small, UI-ready, no nulls everywhere)
  repository/     interfaces
  usecase/        GetHomeFeed, GetVideoDetail, GetRelatedVideos, SearchVideos, GetChannel,
                  GetChannelVideos, GetPlaylists, GetComments, GetShorts
  error/          sealed DomainError { Network, QuotaExceeded, NotFound, Unauthorized, Unknown }
```
- Replace `ResultState<T>` (with its string errors) with `Result<T, DomainError>` in domain and `UiState` in presentation.
- Mappers own the formatting (view counts, durations, dates). Move `FormateViewCount`, etc. into mapper helpers and test them.
- Koin: one module per layer (`networkModule`, `dataModule`, `domainModule`, `presentationModule`).

**Done when:** no DTO type is imported anywhere under `presentation/`.

---

## Phase 3: MVI per screen (replaces the single 439-line MainViewModel)

Pattern per feature:
```kotlin
data class HomeState(val videos: List<Video> = emptyList(), val isLoading: Boolean = false,
                     val isLoadingMore: Boolean = false, val error: DomainError? = null)
sealed interface HomeIntent { data object Load; data object LoadMore; data object Refresh; data class OpenVideo(val id: String) }
sealed interface HomeEffect { data class NavigateToDetail(val id: String) }

class HomeViewModel(private val getHomeFeed: GetHomeFeed) : ViewModel() {
    val state: StateFlow<HomeState>
    val effects: Flow<HomeEffect>      // Channel-backed, one-shot
    fun onIntent(intent: HomeIntent)
}
```
- ViewModels: Home, Detail, Shorts, Search, Channel, Subscriptions, Library, Account.
- Screens are `Route` (collects the ViewModel) + stateless `Screen(state, onIntent)`. Only Routes touch Koin.
- Remove all `koinViewModel()` / `koinInject()` calls from components (currently ~10 files).
- Add a small `BaseMviViewModel<S, I, E>` only if three or more features repeat the same boilerplate.

**Done when:** `MainViewModel.kt` is deleted, and every component previews with fake state.

---

## Phase 4: Compose Navigation (replaces Voyager)

- `org.jetbrains.androidx.navigation:navigation-compose` with type-safe `@Serializable` routes:
  `Home`, `Shorts`, `Subscriptions`, `Library`, `VideoDetail(id)`, `Channel(id)`, `Search(query)`, `Playlist(id)`.
- One `NavHost` in `App.kt`. The bottom bar / rail (keep the Haze glass bar) is driven by `currentBackStackEntry`.
- Fix the rail bug: the Home slot currently renders an empty `Box` over the tab content (`App.kt`, `selectedItemIndex == 0`).
- Deep links: `youtube.com/watch?v=` and `/shorts/` open VideoDetail / Shorts.
- Remove the Voyager dependencies and the stray `NavController` import in `LibraryScreen`.

**Done when:** no `cafe.adriel.voyager` imports remain, and back navigation works on Android, iOS, desktop and web.

---

## Phase 5: Shorts (reels) UI rebuild

Audit `ShortList.kt` (1,008 lines) first, then rebuild it as:
- `VerticalPager` with full-bleed video, page snapping, and only the current page playing
  (pause on settle-away, release players two or more pages away).
- Overlay: channel row + subscribe, title/description (expandable), and a right-side action rail
  (like, dislike, comments, share, remix) with counts from the domain model.
- Comments in a `ModalBottomSheet` backed by the paginated comments use case.
- Paginates 10 Shorts at a time (Phase 1) and preloads the next item's thumbnail.
- Handles insets / edge-to-edge, and uses the glass bar over video in dark style.

**Done when:** swiping is smooth, one player is active at a time, and there are no leaks
(Android Studio profiler shows ≤ 2 players alive).

---

## Phase 6: Code health and duplicate code

- Split the giant files: `VideosLIst.kt` (1,534 → rename `VideoList.kt`), `DetailContent.kt` (1,360),
  `ShortList.kt`, `ChannelScreen.kt`, `TopAppBar.kt`. Target < 300 lines per file.
- One `AdaptiveVideoGrid` replaces the four copy-pasted `LazyVerticalGrid` branches.
- One `VideoCard`, `ChannelRow` and `ShimmerBox` replace the per-screen variants (there are 3+ shimmer files).
- Remove Material 2 (`compose.material`) and use Material 3 only. Drop unused deps (`glace-effect` → rename or remove).
- Design system: `core/designsystem` with theme tokens, spacing, typography and the glass components.
- Rename package `org.company.app` → `io.github.khubaibkhan4.youtube` (single IDE refactor, own commit).
- Static analysis: add Detekt + ktlint (or Spotless) with a baseline, run in CI.

---

## Phase 7: Tests and CI

- Unit tests (commonTest): mappers, the paginator, each use case (fake repositories), each ViewModel
  (state transitions per intent, with Turbine).
- API tests with Ktor `MockEngine`: query encoding, `maxResults` cap, pageToken passing, quota error mapping.
- GitHub Actions: build Android, desktop, wasm and iOS (macOS runner); run tests; upload a debug APK artifact.
- Release workflow builds the APK linked in the README, so it is never stale again.

---

## Order and sizing

| Phase | Depends on | Size |
|---|---|---|
| 0 API key | none | S |
| 1 Limits + pagination | 0 | M |
| 2 Clean Arch + mappers | 1 | L |
| 3 MVI ViewModels | 2 | L |
| 4 Compose Navigation | 3 | M |
| 5 Shorts rebuild | 3 (4 for deep links) | M |
| 6 Code health | runs alongside 3–5, finish last | M |
| 7 Tests + CI | starts with 1, grows each phase | M |

Suggested sessions: Phase 0 + 1 together, then 2, then 3 per feature (Home first as the template),
then 4, 5, 6/7.
