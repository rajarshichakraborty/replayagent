package org.cts.raj.replayagent;

import java.lang.instrument.Instrumentation;

public class Agent {

	public static void premain(String agentArguments, Instrumentation instrumentation) {

		System.out.println("Replay Agent loading ...");

		ClassTransformer transformer = new ClassTransformer();
		instrumentation.addTransformer(transformer);

		System.out.println("Agent loading complete!");
	}
}
