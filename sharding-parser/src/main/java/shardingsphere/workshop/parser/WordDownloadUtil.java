package shardingsphere.workshop.parser;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * @author wangyujie63
 * @version v1.0
 * @ClassName WordDownloadUtil
 * @Description
 * @E-mail wangyujie63@jd.com
 * @Date 2020/8/26 16:18
 */
public class WordDownloadUtil {


    private Configuration configuration = null;

    public WordDownloadUtil() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    }
    /**
     *
     * @param dataMap 要填入模本的数据文件
     * @param path 输出文档的路径
     * @param fileName 输出文档的名称
     * @param templateName 模版文件名称
     * @return flag 0000导出成功 0001模版不存在 0002文件编码异常 0003模版异常 0004导出异常
     */
    public String createDocNew(Map<String, Object> dataMap, String path,
                               String fileName, String templateName) {
        String flag = "0000";
        try {
            // 设置模本装置方法和路径，包名
            configuration.setClassForTemplateLoading(this.getClass(), "/");
            // .ftl为要装载的模板
            Template template = configuration.getTemplate(templateName);
            // 输出文档路径及名称
            File outFile = new File(path);
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            outFile = new File(path + fileName);
            /*
             * 此处对流的编码不可或缺，使用main()单独调用时，应该可以
             * 但是如果是web请求导出时导出后word文档就会打不开，并且报XML文件错误，主要是编码格式不正确，无法解析
             */
            FileOutputStream fos = new FileOutputStream(outFile);
            OutputStreamWriter osWriter = new OutputStreamWriter(fos, "UTF-8");
            Writer writer = new BufferedWriter(osWriter);
            template.process(dataMap, writer);
            writer.close();
            fos.close();
        } catch (FileNotFoundException e) {
            flag = "0001";
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            flag = "0002";
            e.printStackTrace();
        } catch (TemplateException e) {
            flag = "0003";
            e.printStackTrace();
        } catch (IOException e) {
            flag = "0004";
            e.printStackTrace();
        }
        return flag;
    }

}
