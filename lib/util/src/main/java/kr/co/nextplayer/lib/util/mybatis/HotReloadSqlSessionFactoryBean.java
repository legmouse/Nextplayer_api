package kr.co.nextplayer.lib.util.mybatis;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;

import io.methvin.watcher.DirectoryWatcher;

public class HotReloadSqlSessionFactoryBean extends SqlSessionFactoryBean implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(HotReloadSqlSessionFactoryBean.class);

    private SqlSessionFactory proxy;

    private Resource[] mapperLocations;

    private ReloaderThread reloaderThread;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    public HotReloadSqlSessionFactoryBean() {
        proxy = (SqlSessionFactory) Proxy.newProxyInstance(
                SqlSessionFactory.class.getClassLoader(),
                new Class[] { SqlSessionFactory.class },
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(getParentObject(), args);
                    }
                });
    }

    @Override
    public void setMapperLocations(Resource... mapperLocations) {
        super.setMapperLocations(mapperLocations);
        this.mapperLocations = mapperLocations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        initReloaderThread();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        // Configuration 설정 방지
        // super.setConfiguration(configuration);
    }

    private void initReloaderThread() {
        if (reloaderThread == null) {
            reloaderThread = new ReloaderThread();
            reloaderThread.setDaemon(true);
            reloaderThread.start();
        }
    }

    public void reloadSqlSessionFactory() {
        writeLock.lock();

        try {
            try {
                super.afterPropertiesSet();
            } catch (Exception e) {
                LOG.info("Unable to reload SqlSessionFactory", e);
            }
        } finally {
            writeLock.unlock();
        }

        reloaderThread.enableReload();

        LOG.info("Reloaded SqlSessionFactory.");
    }

    private Object getParentObject() throws Exception {
        readLock.lock();

        try {
            return super.getObject();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        return this.proxy;
    }

    @Override
    public void destroy() throws Exception {
        reloaderThread.exit();
    }

    private class ReloaderThread extends Thread {
        private DirectoryWatcher watcher;

        private boolean shouldReload = true;

        public ReloaderThread() {
            super("MyBatisHotReloader");
        }

        public void disableReload() {
            shouldReload = false;
        }

        public void enableReload() {
            shouldReload = true;
        }

        @Override
        public void run() {
            LOG.info("Starting MyBatisHotReloader Thread");
            try {
                Set<String> directories = new HashSet<>();

                for (Resource mapperLocation : mapperLocations) {
                    directories.add(mapperLocation.getFile().getParent());
                }

                ArrayList<Path> paths = new ArrayList<>();

                for (String directory : directories) {
                    Path path = Paths.get(directory);
                    paths.add(path);
                    LOG.info("Adding directory to watch: {}", path);
                }

                watcher = DirectoryWatcher.builder()
                    .paths(paths)
                    .listener((listener) -> {
                        LOG.info("Detected change in {}", listener.path());
                        String fileName = listener.path().getFileName().toString();

                        if (shouldReload && fileName.endsWith(".xml")) {
                            LOG.info("File change detected: {}", fileName);
                            this.disableReload();
                            reloadSqlSessionFactory();
                        }
                    })
                    .build();

                watcher.watch();

            } catch (IOException e) {
                LOG.error("File Watcher Exception:", e);
            }
        }

        public void exit() {
            try {
                watcher.close();
            } catch (IOException e) {
                //
            }
        }
    }
}