package run;

import lsieun.asm.analysis.*;
import lsieun.utils.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public class HelloWorldAnalysisTree {
    public static void main(String[] args) throws Exception {
        String relative_path = "sample/HelloWorld.class";
        String filepath = FileUtils.getFilePath(relative_path);
        byte[] bytes = FileUtils.readBytes(filepath);
        if (bytes == null) {
            throw new RuntimeException("bytes is null");
        }

        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）生成ClassNode
        int api = Opcodes.ASM9;
        ClassNode cn = new ClassNode();

        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cn, parsingOptions);

        //（3）进行分析
        List<MethodNode> methods = cn.methods;
        CyclomaticComplexity cc = new CyclomaticComplexity();
        for (MethodNode mn : methods) {
            int complexity = cc.getCyclomaticComplexity(cn.name, mn);
            String line = String.format("%s:%s%n    complexity: %d", mn.name, mn.desc, complexity);
            System.out.println(line);
        }
    }
}