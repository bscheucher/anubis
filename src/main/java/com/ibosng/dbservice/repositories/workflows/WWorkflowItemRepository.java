package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface WWorkflowItemRepository extends JpaRepository<WWorkflowItem, Integer>, WWorkflowItemRepositoryExtended {

    String INCOMPLETE_WWI = """
            SELECT wwi.id, wwi.workflow_item, swi.name, wwi.status, wwi.changed_on, wwi.changed_by
            FROM w_workflow_items wwi
                     join s_workflow_items swi on swi.id = wwi.workflow_item
                     LEFT JOIN w_workflow_items predec ON wwi.predecessor = predec.id AND predec.status = 2
                     left join w_workflows ww on ww.id = wwi.workflow
                     left join w_workflow_groups wwg on ww.workflow_group = wwg.id
                     left join s_workflow_groups swg on wwg.workflow_group = swg.id and swg.name = :wwgName
            WHERE ww.data = :data
              AND wwi.status IN (0, 1, 3)
            ORDER BY wwi.status DESC, wwi.workflow_item
            LIMIT 1""";

    List<WWorkflowItem> findAllByWorkflow(WWorkflow wWorkflow);

    @Query("select ww from WWorkflowItem ww where ww.workflow.id = :workflowIp")
    List<WWorkflowItem> findAllByWorkflowId(Integer workflowIp);

    List<WWorkflowItem> findAllByWorkflowAndStatus(WWorkflow wWorkflow, WWorkflowStatus status);

    @Query("select ww from WWorkflowItem ww where ww.workflow.id = :wWorkflow and ww.status = :status")
    List<WWorkflowItem> findAllByWorkflowIdAndStatus(Integer wWorkflow, WWorkflowStatus status);

    WWorkflowItem findBySuccessor(WWorkflowItem workflowItem);

    @Query("select ww from WWorkflowItem ww where ww.successor.id = :workflowItemId")
    WWorkflowItem findBySuccessorId(Integer workflowItemId);

    List<WWorkflowItem> findAllByPredecessor(WWorkflowItem workflowItem);

    @Query("select ww from WWorkflowItem ww where ww.predecessor.id = :workflowItemId")
    List<WWorkflowItem> findAllByPredecessorId(Integer workflowItemId);

    @Query("select wwi from WWorkflowItem wwi where wwi.workflow = :wWorkflow and wwi.workflowItem.name = :name")
    WWorkflowItem findByWorkflowAndWorkflowItemName(WWorkflow wWorkflow, String name);

    @Query("select wwi from WWorkflowItem wwi join fetch wwi.predecessor p where wwi.workflowItem.name = :sWorkflowItem and wwi.status = :status")
    List<WWorkflowItem> findAllByNameAndStatus(String sWorkflowItem, WWorkflowStatus status);

    @Query(value = INCOMPLETE_WWI, nativeQuery = true)
    List<Object[]> findFirstIncompleteItemWithCompletedPredecessor(String data, String wwgName);

    @Query(value = "select wwi from WWorkflowItem wwi where wwi.id = :id")
    WWorkflowItem findFreshWorkflowItemById(Integer id);
}
