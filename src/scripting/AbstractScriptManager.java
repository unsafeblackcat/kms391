package scripting;

import client.MapleClient;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractScriptManager {

	private static final ScriptEngineManager sem = new ScriptEngineManager();

	protected Invocable getInvocable(String path, MapleClient c) {
		return getInvocable(path, c, false);
	}

	protected Invocable getInvocable(String path, MapleClient c, boolean npc) {
		path = "scripts/" + path;
		ScriptEngine engine = null;
		if (c != null) {
			engine = c.getScriptEngine(path);
		}

		if (engine == null) {
			File scriptFile = new File(path);
			if (!scriptFile.exists()) {
				return null;
			}

			engine = sem.getEngineByName("nashorn");
			if (c != null) {
				c.setScriptEngine(path, engine);
			}

			try (Stream<String> stream = Files.lines(scriptFile.toPath(), Charset.forName("UTF-8"))) {
				engine.eval("load('nashorn:mozilla_compat.js');" + System.lineSeparator());
				engine.eval(stream.collect(Collectors.joining(System.lineSeparator())));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return (Invocable) engine;
	}
}
