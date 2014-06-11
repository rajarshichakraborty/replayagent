package org.cts.raj.replayagent;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.util.proxy.ProxyFactory;

public class ClassTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;
		if ("org/cts/raj/client/TestServlet".equals(className)) {
			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

				CtMethod targetM = cc.getDeclaredMethod("getContent");
				
				//sample returned value
				//targetM.insertBefore("return \"hello website!\";");
				
				targetM.addLocalVariable("elapsedTime", CtClass.longType);
				targetM.insertBefore("elapsedTime = System.currentTimeMillis();");
				targetM.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
						+ "System.out.println(\"Method Executed in ms: \" + elapsedTime);}");
				
				/*ProxyFactory pfac = new ProxyFactory();
				pfac.create(classBeingRedefined, args);*/
				

				byteCode = cc.toBytecode();
				cc.detach();
				return byteCode;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return byteCode;
	}

}
