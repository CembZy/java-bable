package cap3.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class JamesImportSelector implements ImportSelector {
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //要加载的bean的路径
        return new String[]{"cap3.bean.Fish", "cap3.bean.Tiger"};
    }
}
