package com.erasm.config;

import com.erasm.util.AuditUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Configures the AuditorAware bean so that @CreatedBy and @LastModifiedBy
 * fields on JPA entities are automatically populated from the SecurityContext.
 */
@Configuration
@org.springframework.data.jpa.repository.config.EnableJpaAuditing
public class AuditConfig {

    private final AuditUtil auditUtil;

    public AuditConfig(AuditUtil auditUtil) {
        this.auditUtil = auditUtil;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(auditUtil.getCurrentUserEmail());
    }
}
