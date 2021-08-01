package util;

import evolution.ManipulationInformationContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public List<ManipulationInformationContainer> parseDiff(Path pathToDiff) throws IOException {
    List<ManipulationInformationContainer> result = new ArrayList<ManipulationInformationContainer>();
    List<String> diffContent = Files.readAllLines(pathToDiff);

    // Enthält alle einzelnen diffs (Also jede innere Liste ist ein file)
    // Subliste erstellen mit den ganzen Lines bis wir eine neue Zeil encountern die mit "diff --git" anfängt"
    List<List<String>> diffs = extractSubLists(diffContent, "diff --git");
    // Diese subliste dann in extractSingleDiff geben, sodass
    for (List<String> diff : diffs) {
      ManipulationInformationContainer mic;
      // Hier den Path extrahieren zunächst
      Path path = Paths.get(diff.get(0).split(" ")[3]); // 4. Element in dem Array
      mic = new ManipulationInformationContainer(path, extractLinesFromSingleDiff(diff));
      result.add(mic);
    }

    return result;
  }

  private Map<Integer, String> extractLinesFromSingleDiff(List<String> diff) {
    // Jeweils die einzelnen Hunks durchgehen
    // Die Zeilen erhalten wir aus dem Hunk header, müssen dann nur durchiterieren und
    List<List<String>> hunks = extractSubLists(diff, "@@");
    Map<Integer, String> lineContentMap = new HashMap<Integer, String>();
    for (List<String> hunk : hunks) {
      int startLine = Integer.parseInt(hunk.get(0)
                                           .split(" ")[2] // Retrieves the +x,y part
                                           .split(",")[1] // Retrieve +x of +x,y part
                                           .substring(1)); // Cut off the +, so we only have x
      for (int i = 1; i < hunk.size(); i++) {
        lineContentMap.put(startLine, hunk.get(i));
        startLine++;
      }
    }
    return lineContentMap;
  }

  private List<List<String>> extractSubLists(List<String> diffContent, String searchString) {
    List<Integer> indices = new ArrayList<>();
    List<List<String>> result = new ArrayList<List<String>>();
    for (int i = 0; i < diffContent.size(); i++) {
      if (diffContent.get(i).startsWith(searchString)) {
        indices.add(i);
      }
    }

    for (int i = 1; i < indices.size(); i++) {
      result.add(diffContent.subList(indices.get(i - 1), indices.get(i)));
    }
    result.add(diffContent.subList(indices.get(indices.size() - 1), diffContent.size()));
    return result;
  }

}
