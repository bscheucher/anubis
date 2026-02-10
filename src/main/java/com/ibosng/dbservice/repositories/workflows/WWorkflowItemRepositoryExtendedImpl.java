package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public class WWorkflowItemRepositoryExtendedImpl implements WWorkflowItemRepositoryExtended {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refresh(WWorkflowItem entity) {
        entityManager.refresh(entity);
    }
}
