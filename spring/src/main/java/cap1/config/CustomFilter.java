package cap1.config;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

//自定义过滤类
public class CustomFilter implements TypeFilter {


    /**
     * @param metadataReader        可以获取当前正在扫描类的信息
     * @param metadataReaderFactory 可以获取其他类的信息
     * @return
     * @throws IOException
     */
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {

        String className = metadataReader.getClassMetadata().getClassName();
        if (className.contains("Service")) {
            return true;
        }

        //false表示默认的过滤规则
        return false;
    }
}
