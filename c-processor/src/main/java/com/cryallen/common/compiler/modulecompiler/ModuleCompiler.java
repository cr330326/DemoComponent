package com.cryallen.common.compiler.modulecompiler;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.cryallen.common.annotation.EFModuleAnnotation;
import com.cryallen.common.compiler.info.ModuleProxyInfo;

/**
 * Created by chenran3 on 2018/2/2.
 */

public class ModuleCompiler extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;

    private String mModuleProjectName;

    public ModuleCompiler() {}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mMessager = processingEnv.getMessager();
        this.mFiler = processingEnv.getFiler();

        // 在这里打印gradle文件传进来的参数
        Map<String, String> map = processingEnv.getOptions();
        if(map != null){
            mModuleProjectName = map.get("moduleName");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processModuleAnnotation(roundEnvironment);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(EFModuleAnnotation.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void processModuleAnnotation(RoundEnvironment roundEnv){
        this.note("processModuleAnnotation...", new Object[0]);
        Set elementWithModuleAnnotation = roundEnv.getElementsAnnotatedWith(EFModuleAnnotation.class);
        if(elementWithModuleAnnotation.size() > 1) {
            this.error("EFModuleAnnotation only used by one class in one Module", new Object[0]);
        } else {
            ModuleProxyInfo moduleProxyInfo = new ModuleProxyInfo();

            for(Iterator item = elementWithModuleAnnotation.iterator(); item.hasNext(); this.writeJsonToAssets(moduleProxyInfo, mModuleProjectName)) {
                Element element = (Element)item.next();
                if(!this.checkAnnotationValid(EFModuleAnnotation.class, element)) {
                    return;
                }

                EFModuleAnnotation efModuleAnnotation = (EFModuleAnnotation)element.getAnnotation(EFModuleAnnotation.class);
                if(mModuleProjectName == null || mModuleProjectName.equals("") || mModuleProjectName.length() == 0){
                    mModuleProjectName = this.getModuleProjectName();
                }
                moduleProxyInfo.moduleName = efModuleAnnotation.moduleName();
                moduleProxyInfo.delegateName = efModuleAnnotation.delegateName();
                moduleProxyInfo.packageName = this.getPackageName(mModuleProjectName);
            }

        }
    }

    private boolean checkAnnotationValid(Class<? extends Annotation> annotationClass, Element element) {
        boolean isVaild = true;
        TypeElement enclosingElement = (TypeElement)element;
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        if(enclosingElement.getKind() != ElementKind.CLASS) {
            this.error(enclosingElement, "@%s may only be used on classes. (%s)", new Object[]{annotationClass.getSimpleName(), qualifiedName});
            isVaild = false;
        }

        if(qualifiedName.startsWith("android.")) {
            this.error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", new Object[]{annotationClass.getSimpleName(), qualifiedName});
            return false;
        } else if(qualifiedName.startsWith("java.")) {
            this.error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", new Object[]{annotationClass.getSimpleName(), qualifiedName});
            return false;
        } else {
            return isVaild;
        }
    }

    private String getModuleProjectName() {
        String moduleName = "";
        FileObject resource = null;

        try {
            resource = this.mFiler.createResource(StandardLocation.CLASS_OUTPUT, "", "tmp", new Element[0]);
            String e = resource.toUri().toString();
            this.note("tmpResourceFilePath = " + e, new Object[0]);
            if(e.startsWith("file:")) {
                if(!e.startsWith("file://")) {
                    e = "file://" + e.substring("file:".length());
                }
            } else {
                e = "file://" + e;
            }

            URI cleanURI = new URI(e);
            File tmpFile = new File(cleanURI);
            File projectRoot = tmpFile;
            do {
                projectRoot = projectRoot.getParentFile();
            } while(!projectRoot.getName().equals("build") && projectRoot.getName().length() > 0);

            projectRoot = projectRoot.getParentFile();
            moduleName = projectRoot.getName();
            resource.delete();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        } catch (URISyntaxException ex2) {
            ex2.printStackTrace();
        }

        return moduleName;
    }

    private String getPackageName(String moduleName) {
        File manifest = new File(moduleName + "/src/main/AndroidManifest.xml");
        if(!manifest.exists()) {
            return null;
        } else {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser e = factory.newSAXParser();
                PackageParserHandler handler = new PackageParserHandler();
                e.parse(manifest, handler);
                return handler.getPackageName();
            } catch (ParserConfigurationException ex1) {
                ex1.printStackTrace();
            } catch (SAXException ex2) {
                ex2.printStackTrace();
            } catch (IOException ex3) {
                ex3.printStackTrace();
            }
            return null;
        }
    }

    private void writeJsonToAssets(ModuleProxyInfo moduleProxyInfo, String moduleName) {
        File dir = new File(moduleName + "/src/main/assets");
        if(!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, ModuleProxyInfo.MODULE_PREFIX + moduleProxyInfo.packageName + ".json");
        try {
            if(file.exists()) {
                file.delete();
            }

            file.createNewFile();
            this.note("file.getAbsolutePath() = " + file.getAbsolutePath(), new Object[0]);
            this.writerJsonToFile(moduleProxyInfo, file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writerJsonToFile(ModuleProxyInfo moduleProxyInfo, File file) throws Exception {
        Gson gson = new Gson();
        FileOutputStream out = new FileOutputStream(file);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        gson.toJson(moduleProxyInfo, ModuleProxyInfo.class, writer);
        writer.flush();
        writer.close();
    }

    private void error(Element e, String msg, Object... args) {
        this.mMessager.printMessage(Kind.ERROR, String.format(msg, args), e);
    }

    private void error(String msg, Object... args) {
        this.mMessager.printMessage(Kind.ERROR, String.format(msg, args));
    }

    private void note(String msg, Object... args) {
        this.mMessager.printMessage(Kind.NOTE, String.format(msg, args));
    }
}
