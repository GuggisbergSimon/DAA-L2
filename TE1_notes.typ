#import "@preview/cram-snap:0.2.1": cram-snap, theader

#set page(
  paper: "a4",
  flipped: true,
  margin: 0.1cm,
)
#set text(font: "Arial", size: 8pt)

#show: cram-snap.with(
  title: [DAA - Furrer - Guggisberg - Troeltsch],
  column-number: 2,
)

#table(
  theader[Introduction Kotlin],
  [`val unmutable`],[`var mutable`],
  [`val p : Person?; p?.name`], [Null safety: Pour chaque type T, il existe T? qui prend T ou null],
  [`c?.name ?: "default"`], [Elvis Operator: Prendre valeur en cas de null],
  [`fun String.doSomething():String {...}`], [Extension functions: ajouter fonctions à classes sans sources],
  [`class Person(var i:Int) {init {...}}`], [init: constructeur par défaut],
  [`constructor(i:Int,j:Int):this(i) {...}`], [constructor: constructeurs supplémentaire],
  [`fun add(x: Int, y: Int):Int {...}`],[Arguments nommés possible: `add(y = 1, x = 2)`],
  [`class Student(...): Person(...) {...}`],[Héritage: appel super-constructeur. `open class Person() {...}`],
  [`interface Flying {
  val w:Wings 
  fun fly(){...}
}`],
  [`class Bird: Flying {
  override val wings:Wings=...
}`],
  [`data class Person(var name:String)`], [Génère: getter - setters - toString()],
  [`val(name, surname) = p`], [Déstructuration pour data class],
  [`var p2 = p1.copy(surname="Bob")`],[Copie pour data class, arguments optionnels],
  [`val s = "Person[name:${p.name},id:$id"]`],[String templates],
  [Collections],[List, MutableList, Set, MutableSet, Map, MutableMap],
  [`val l = listOf(1, 2, 3, 4)`],[
`l.any{...} l.count{...} l.max() l.filter{...} l.map{...} l.partition{...} l+l` etc],
  [*Operators overload*: `            ` Array:],[`get(i) set(i)        a..b b.rangeTo(b) a in b b.contains(a)`],
  [Unary: `unaryPlus() not() inc() dec()`],[Binary: `plus(b) minus(b) times(b) div(b) mod(b) plusAssign()`],
  [`val res = when(x) {1->"a" else->{"b"}}`],[*When* - Pattern Matching],
  [`p?.let {it.name="Bob" println(it)}`],[Scope functions - *let* - retourne dernière ligne du bloc],
  [`val p2 = p.apply {this.name="Bob" println(this)}`],[Scope functions - *apply* - retourne l'objet lui-même],
  [],[Scope functions - *use*],
  [`companion object {var a  fun  getA():A}`],[Companion objects contient variables/fonctions *statiques*],

)

#table(
  theader[Storing changes],
  [`git stash [push] [path]`], [put current changes in the *working tree* into *stash* for later use],
  [`git stash pop`], [apply stored *stash* content into *working tree*, and clear *stash*],
  [`git stash drop`], [delete a specific *stash* from all the previous *stashes*],
)

#table(
  theader[Inspecting diffs],
  [`git diff [path]`], [show changes between *working tree* and *staging area*],
  [`git diff --cached/--staged [path]`], [show any changes between the *staging area* and the *repository*],
  [`git diff > file.patch`], [generate a patch file for current changes],
)

#table(
  theader[Reverting changes],
  [`git rebase`], [rebase the current branch on top of another specified branch],
  [`git rebase -i [commit sha]`], [start an interactive rebase],
  [`git revert [commit sha]`], [Create a new commit, reverting changes from the specified commit. It generates an *inversion* of changes.],
  [`git checkout <path>`], [discard changes in the *working tree*],
  [`git restore [-W/--worktree] <path>`], [discard changes in the *working tree*],
  [`git restore -S/--staged <path>`], [remove a file from a *staging area*],
  [`git restore -SW <path>`], [discard changes in the *working tree* and to the *staged* files],
  [`git reset <path>`], [remove a file from the *staging area*],
  [`git reset [mode] HEAD^`], [remove the latest *commit* from the current branch and:
    - `--soft` - keep file changes in the
      *working tree* and *stage* them;
    - `--mixed` - keep file changes;
    - `--keep` - reset only files which are
      different between current `HEAD` and the
      last commit
    - `--hard` - do *not* keep file changes],
)

#table(
  theader[Tagging commits],
  [`git tag`], [list all tags],
  [`git tag <name> [commit sha]`], [create a tag reference named `name` for the current or specific commit],
  [`git tag -a <name> -m <message>`], [create an annotated tag with the given message],
  [`git tag -d <name>`], [delete the tag with the given name],
)

#table(
  theader[Synchronizing repositories],
  [`git fetch [remote]`], [fetch changes from the *remote*, but not update tracking branches],
  [`git fetch --prune [remote]`], [delete remote refs that were removed from the *remote* repository],
  [`git pull [remote]`], [fetch changes from the *remote* and *merge* current branch with its upstream],
  [`git pull -r/--rebase [remote]`], [fetch changes from the *remote* and *rebase* current branch on top of the upstream],
  [`git push -u [remote] [branch]`], [push local branch to a *remote* repository and set its copy as an upstream],
)
