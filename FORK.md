# About this fork

A personal fork of [orgzly-revived/org-java](https://github.com/orgzly-revived/org-java),
carrying patches that upstream has not taken yet. It exists to serve a matching fork of the
Android app, which cannot use upstream's artifact while it depends on those patches.

It is `FORK.md` rather than a `README.md` section because upstream owns the readme: editing
it would conflict on every sync.

## Branches

| Branch | What it is |
|---|---|
| `master` | mirror of upstream, fast-forward only, never rewritten |
| `feat/*`, `fork/*` | one change each, branched from `master` |
| `patched` | `master` plus every open pull request, merged |

`patched` is **derived, not authored**: it is rebuilt and force-pushed whenever upstream
releases. Never branch from it, never target a pull request at it.

**The open pull requests are the patch set.** Each carries one change and targets `master`.
It stays open for as long as the fork carries that patch, and closes on its own when upstream
merges the change — so nothing else needs tracking.

## How the app consumes this

There is no publishing step. JitPack builds any public repository on demand, so **a tag is
the release**:

    com.github.aterentic:org-java:v1.3.6-patched-1

Tags are named after the upstream version they are based on, plus a counter:
`v<upstream>-patched-<n>`. The counter restarts when the base moves. Naming them after the
base keeps them honest — a tag called `v1.3.7-…` would start lying the day upstream releases
a real 1.3.7.

To ship a change: tag it, push the tag, bump the version in the app fork. The first request
for a new tag triggers a build; after that it is cached.

Commit hashes (`com.github.aterentic:org-java:0c8a8e6`) work too, and need no tag. Branch
snapshots (`patched-SNAPSHOT`) also work but should not be used for anything installed:
`patched` is force-pushed, so the same version string would silently change meaning.

When a patch lands upstream and they tag a release, point the app back at
`com.github.orgzly-revived:org-java` and drop the local tag.

## jitpack.yml pins a JDK on purpose

JitPack builds from source, so the build must work on *their* machines. The Gradle wrapper
here is old enough that a current JDK cannot run it — which is why local builds need an older
JDK or a newer Gradle, while the published artifact keeps building fine.

## Syncing and rebasing

```bash
git fetch upstream --tags
git merge --ff-only upstream/master
git push origin master --tags
```

A refused fast-forward means `master` has been written to; reset it to upstream rather than
reconciling.

When upstream releases:

1. Fast-forward `master`.
2. Close the pull requests upstream has taken; delete their branches.
3. Rebase each remaining branch onto the new base.
4. Rebuild `patched`: reset to the base, merge each branch, force-push.
5. Tag, and bump the version in the app fork.

Step 3 is the work, and it scales with how many patches the fork carries. That is the
argument for upstreaming everything that can be upstreamed.
