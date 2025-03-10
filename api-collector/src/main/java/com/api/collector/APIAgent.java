//package com.api.collector;
//
//public class APIAgent {
//}
package com.api.collector;

import java.lang.instrument.Instrumentation;

public class APIAgent {
    public static void premain(String args, Instrumentation inst) {
        initAgent(inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        initAgent(inst);
    }

    private static void initAgent(Instrumentation inst) {
        inst.addTransformer(new ServletTransformer(), true);
    }
}