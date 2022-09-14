package com.company.truonghoc.service;

import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.stereotype.Service;

@Service(ServerConfigService.NAME)
public class ServerConfigServiceBean implements ServerConfigService {
    @Override
    public String getCubaTempDir() {
        return AppContext.getProperty("cuba.tempDir");
    }
}