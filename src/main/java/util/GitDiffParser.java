package util;

import evolution.ManipulationInformationContainer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses Git Diffs to extract required Information.
 */
public class GitDiffParser {

/*
Struktur:
- Erste Line ist "diff --git a/file.c b/file.c"
- Dann kommt ein index
- Dann kommt der unified diff header in zwei Zeilen "--- a/file.c" "+++ b/file.c"
- Dann kommen die hunks,die zeigen die Bereiche der Unterschiede zwischen den Dateien auf
--- Bspw. @@ -1,8 +1,9 @@
--- -1, 8 bedeutet: Ab zeile 1 (inkl) gibt es insgesamt 8 Zeilen mit keinem Vorzeichen
    oder mit einem minus (d.h. entfernte Zeilen)
--- +1, 9 bedeutet: Ab Zeile 1 (inkl) gibt es insgesamt 9 Zeilen mit keinem Vorzeichen
    oder mit einem plus (d.h. hinzugefügt)
==> Für uns relevant: +x,y - Ab Zeile x bis zeile x+y sind für uns relevante Zeilen an sich (theoretisch etwas weniger)
---> Dadurch müssen wir vielleicht keine Nachbarzeilen anschauen, weil da ja schon welche drin sind?
 */

  public List<ManipulationInformationContainer> parseDiff(Path pathToDiff) {

    return new ArrayList<ManipulationInformationContainer>();
  }
}
