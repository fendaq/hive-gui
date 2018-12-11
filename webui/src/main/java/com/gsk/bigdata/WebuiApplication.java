package com.gsk.bigdata;

import com.aiyi.core.AiYiApplicationContiger;
import com.webkettle.core.hdfs.LoginUtil;
import com.webkettle.core.hive.connection.ConnectionTools;
import com.webkettle.core.hive.datasource.HiveDataSource;
import com.webkettle.sql.SpringContextUtil;
import com.webkettle.webservice.client.UserDetailsService;
import com.webkettle.webservice.client.UserDetailsServiceImplService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.util.Progressable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AiYiApplicationContiger.class)
@ComponentScan(basePackages = {"com.webkettle", "com.gsk.bigdata"})
public class WebuiApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Resource
    private HiveDataSource hiveDataSource;
    @Resource
    private ConnectionTools connectionTools;

    @Value("${bigdata.hdfs.url}")
    private String hdfsUrl;
    @Value("${bigdata.hdfs.user}")
    private String hadoopUser;
    @Value("${bigdata.hdfs.prop}")
    private String hdfsConfPath;
    @Value("${bigdata.hdfs.prncipal-name}")
    private String hdfsPrncipalName;
    @Value("${bigdata.hdfs.keytab-path}")
    private String pathToKeyTab;
    @Value("${bigdata.hdfs.krb5-path}")
    private String pathToKrb5;
    @Value("${auth.auth-path}")
    private String authPath;


    protected static Logger logger = Logger.getLogger(WebuiApplication.class);

    public static void main(String[] args) {
    	try{
    		SpringApplication.run(WebuiApplication.class, args);
    	}catch(Exception e){
    	    logger.info("项目启动失败", e);
    		e.printStackTrace();
    	}
        
    }

    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(WebuiApplication.class);
    }

    private static final SpringApplicationBuilder configureApplication(final SpringApplicationBuilder builder) {
        return builder.sources(WebuiApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        // 初始化连接池
        connectionTools.init(hiveDataSource);
        TomcatServletWebServerFactory webServerFactory = (TomcatServletWebServerFactory)
                SpringContextUtil.getApplicationContext().getBean("tomcatServletWebServerFactory");
        int port = webServerFactory.getPort();
        String contextPath = webServerFactory.getContextPath();
        contextPath = ("".equals(contextPath.trim()) ? "" : "/" + contextPath);
        logger.info("---------------------------------------------------------------------");
        logger.info("-=-=-=-项目地址:http://your-host:" + port + contextPath);
        logger.info("---------------------------------------------------------------------");
    }

    @Bean
    public UserDetailsService userDetailsService(){
//        CommonAttributes.WEB_SERVICE.ADDR = authPath;
        return new UserDetailsServiceImplService().getUserDetailsServiceImplPort();
//        return new UserDetailsServiceImpl();
    }

    @Bean
    public FileSystem fileSystem(){
        try {
            Configuration configuration = new Configuration();
            configuration.addResource(new ClassPathResource(hdfsConfPath + "hdfs-site.xml").getInputStream());
            configuration.addResource(new ClassPathResource(hdfsConfPath + "core-site.xml").getInputStream());

            if (!StringUtils.isEmpty(hdfsUrl)){
                return FileSystem.get(new URI(hdfsUrl), new Configuration(), hadoopUser);
            }

            if(ResourceUtils.isJarFileURL(ResourceUtils.getURL("classpath:" + pathToKeyTab))
            		|| ResourceUtils.getURL("classpath:" + pathToKeyTab).getPath().contains("!/BOOT-INF/classes!")){
            	logger.info("检测到Jar包方式启动， 开始准备将Jar包相关配置移动到外层");
            	ApplicationHome home = new ApplicationHome(getClass());
            	String jarPath = home.getSource().getParentFile().toString();
            	ClassPathResource resource = new ClassPathResource(pathToKeyTab);
            	byte[] buffer = new byte[4096];

            	InputStream inputStream = resource.getInputStream();
            	pathToKeyTab = jarPath + "/" + pathToKeyTab;
            	logger.info("确定外层地址：" + pathToKeyTab + ", 开始复制");
            	System.out.println("确定外层地址：" + pathToKeyTab + ", 开始复制");
            	File file = new File(pathToKeyTab);
            	if(!file.exists()){
            		FileOutputStream out = new FileOutputStream(file);
                	for(int i = inputStream.read(buffer); i > 0; i = inputStream.read(buffer)){
                		out.write(buffer, 0,  i);
                	}
                	inputStream.close();
                	out.close();
            	}

            	inputStream = new ClassPathResource(pathToKrb5).getInputStream();
            	pathToKrb5 = jarPath + "/" + pathToKrb5;
            	logger.info("确定外层地址：" + pathToKrb5 + ", 开始复制");
            	System.out.println("确定外层地址：" + pathToKrb5 + ", 开始复制");
            	file = new File(pathToKrb5);
            	if(!file.exists()){
            		FileOutputStream out = new FileOutputStream(new File(pathToKrb5));
                	for(int i = inputStream.read(buffer); i > 0; i = inputStream.read(buffer)){
                		out.write(buffer, 0,  i);
                	}
                	inputStream.close();
                	out.close();
            	}

            }else{
            	logger.info("非JAR包方式启动， 直接读取Classpath目录");
            	System.out.println("------------------------------------------非JAR宝包方式启动， 直接读取Classpath目录");
            	pathToKeyTab = ResourceUtils.getFile("classpath:" + pathToKeyTab).getPath();
            	pathToKrb5 = ResourceUtils.getFile("classpath:" + pathToKrb5).getPath();
            }

            logger.info("HDFS LOGIN开始");
            System.out.println("HDFS LOGIN开始");
            LoginUtil.login(hdfsPrncipalName,pathToKeyTab,
            		pathToKrb5, configuration);
            System.out.println("HDFS初始化成功");
            logger.info("HDFS初始化成功");
            return FileSystem.get(configuration);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            logger.info("HDFS服务初始化失败", e);
            throw new RuntimeException("HDFS服务初始化失败", e);
        }
    }
}
